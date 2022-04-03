package ru.patyukov.ylab.zadanie6.repository.statistics;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.patyukov.ylab.zadanie6.model.game.ModelStatisticsPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class XoStatRep implements XoStatRepInterf {

    private final JdbcTemplate jdbcTemplate;

    // Получить статистику всех игроков.
    @Override
    public List<ModelStatisticsPlayer> finaAllStat() {
        return jdbcTemplate.query("select name, won, lost from statisticsplayer", this::rowMap);
    }

    // Получить статистику одного игрока.
    @Override
    public Optional<ModelStatisticsPlayer> findByNameStat(String name) {
        List<ModelStatisticsPlayer> results = jdbcTemplate.query("select name, won, lost from statisticsplayer where name=?", this::rowMap, name);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    // Сохранить статистику одного игрока.
    @Override
    public ModelStatisticsPlayer saveStat(ModelStatisticsPlayer modelStatisticsPlayer) {
        jdbcTemplate.update("insert into statisticsplayer (name, won, lost) values (?, ?, ?)",
                modelStatisticsPlayer.getName(),
                modelStatisticsPlayer.getWon(),
                modelStatisticsPlayer.getLost());
        return modelStatisticsPlayer;
    }

    // Обновить в статистике победы одного игрока.
    @Override
    public ModelStatisticsPlayer updateWonStat(ModelStatisticsPlayer modelStatisticsPlayer) {
        jdbcTemplate.update("update statisticsplayer set won = ? where name = ?",
                modelStatisticsPlayer.getWon(),
                modelStatisticsPlayer.getName());
        return modelStatisticsPlayer;
    }

    // Обновить в статистике поражения одного игрока.
    @Override
    public ModelStatisticsPlayer updateLostStat(ModelStatisticsPlayer modelStatisticsPlayer) {
        jdbcTemplate.update("update statisticsplayer set lost = ? where name = ?",
                modelStatisticsPlayer.getLost(),
                modelStatisticsPlayer.getName());
        return modelStatisticsPlayer;
    }

    private ModelStatisticsPlayer rowMap(ResultSet row, int rowNum) throws SQLException {
        return new ModelStatisticsPlayer(
                row.getString("name"),
                row.getInt("won"),
                row.getInt("lost"));
    }
}
