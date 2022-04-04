package ru.patyukov.ylab.zadanie6.repository.history;

import lombok.AllArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.patyukov.ylab.zadanie6.model.NameHistory;
import ru.patyukov.ylab.zadanie6.model.game.GameResult;
import ru.patyukov.ylab.zadanie6.model.game.Player;
import ru.patyukov.ylab.zadanie6.model.game.Step;
import ru.patyukov.ylab.zadanie6.model.Gameplay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class XoHistRep implements XoHistRepInterf {

    private JdbcOperations jdbcOperations;
    private JdbcTemplate jdbcTemplate;

    // Сохраняем историю игры.
    @Override
    @Transactional
    public Gameplay saveHistory(Gameplay gameplay) {

        // Описываем запрос на вставку вместе с типами полей ввода запроса.
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into historygame (name_player_1, value_1, id_1, name_player_2, value_2, id_2, id_pobed) " +
                        "values ( ?, ?, ?, ?, ?, ?, ? )",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR
        );

        pscf.setReturnGeneratedKeys(true);   // Это для того чтобы потом получить идентификатор сохраненной истории.

        // Заполняем данные для запроса.
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        gameplay.getPlayer1().getName(),
                        gameplay.getPlayer1().getValue(),
                        gameplay.getPlayer1().getId(),
                        gameplay.getPlayer2().getName(),
                        gameplay.getPlayer2().getValue(),
                        gameplay.getPlayer2().getId(),
                        gameplay.getGameResult().getPlayer() != null ? gameplay.getGameResult().getPlayer().getId() : "-"
                ));

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();   // Получаем идентификатор истории.

        jdbcOperations.update(psc,keyHolder);   // Сохраняем данные в таблицу historygame.

        long historyID = keyHolder.getKey().longValue();   // Получаем идентификатор истории, приведенный к типу long.
        gameplay.setHistoryID(historyID);   // Сохраняем идентификатор истории в объект.

        List<Step> stepList = gameplay.getGame();   // Получаем список ходов.

        // Сохраняем каждый шаг в связанную таблицу step.
        for (Step step : stepList) {
            saveStep(historyID, step);
        }

        return gameplay;
    }

    // Метод сохраняет шаги.
    private long saveStep(Long historyID, Step step) {

        // Описываем запрос на вставку вместе с типами полей ввода запроса.
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into step (key_history_id, num, player_id, xy) " +
                        "values (?, ?, ?, ?)",
                Types.LONGNVARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR
        );

        pscf.setReturnGeneratedKeys(true);   // Это для того чтобы потом получить идентификатор сохраненной истории.

        // Заполняем данные для запроса.
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        historyID,
                        step.getNum(),
                        step.getPlayerId(),
                        step.returnXY()));

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();   // Получаем идентификатор истории.

        jdbcOperations.update(psc, keyHolder);   // Сохраняем данные в таблицу step.

        long stepID = keyHolder.getKey().longValue();   // Получаем идентификатор истории, приведенный к типу long.
        step.setStepID(stepID);   // Сохраняем идентификатор истории в объект.

        return stepID;
    }

    // Получаем историю игры по historyID.
    @Override
    public Optional<Gameplay> findByHistoryId(Long historyID) {
        try {
            Gameplay gameplay = jdbcOperations.queryForObject(
                    "select history_id, name_player_1, value_1, id_1, " +
                            "name_player_2, value_2, id_2, id_pobed from historygame where history_id=?",
                    (rs, rowNum) -> {
                        Gameplay gp = new Gameplay(new Player(), new Player());
                        gp.setHistoryID(rs.getLong("history_id"));
                        gp.getPlayer1().setName(rs.getString("name_player_1"));
                        gp.getPlayer1().setValue(rs.getString("value_1"));
                        gp.getPlayer1().setId(rs.getString("id_1"));
                        gp.getPlayer2().setName(rs.getString("name_player_2"));
                        gp.getPlayer2().setValue(rs.getString("value_2"));
                        gp.getPlayer2().setId(rs.getString("id_2"));

                        String id_pobed = rs.getString("id_pobed");
                        gp.setGameResult(new GameResult(new Player()));
                        if (id_pobed.equals("1")){
                            gp.getGameResult().getPlayer().setName(rs.getString("name_player_1"));
                            gp.getGameResult().getPlayer().setValue(rs.getString("value_1"));
                            gp.getGameResult().getPlayer().setId(rs.getString("id_1"));
                        } else if(id_pobed.equals("2")) {
                            gp.getGameResult().getPlayer().setName(rs.getString("name_player_2"));
                            gp.getGameResult().getPlayer().setValue(rs.getString("value_2"));
                            gp.getGameResult().getPlayer().setId(rs.getString("id_2"));
                        } else {

                        }

                        gp.setGame((ArrayList<Step>) findByIdStep(rs.getLong("history_id")));
                        return gp;
                    }, historyID);
            return Optional.of(gameplay);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    // Получаем шаг.
    private List<Step> findByIdStep(long history_id) {
        return jdbcOperations.query(
                "select step_id, key_history_id, num, player_id, xy from step where key_history_id=? order by step_id",
                (rs, rowNum) -> {
                    Step step = new Step();
                    step.setStepID(rs.getLong("step_id"));
                    step.setNum(rs.getString("num"));
                    step.setPlayerId(rs.getString("player_id"));
                    String xy = rs.getString("xy");
                    step.setX(Integer.parseInt(String.valueOf(xy.charAt(0))));
                    step.setY(Integer.parseInt(String.valueOf(xy.charAt(1))));
                    return step;
                }, history_id);
    }

    // Получаем идентификатор истории игры и имена игроков.
    @Override
    public List<NameHistory> findByHistory() {
        return jdbcTemplate.query("select history_id, name_player_1, name_player_2 from historygame", this::rowMap);
    }

    private NameHistory rowMap(ResultSet row, int rowNum) throws SQLException {
        return new NameHistory(
                row.getLong("history_id"),
                row.getString("name_player_1"),
                row.getString("name_player_2"));
    }
}
