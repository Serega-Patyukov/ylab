CREATE TABLE IF NOT EXISTS StatisticsPlayer (
                                                name VARCHAR (28) NOT NULL,
                                                won NUMBER(11) NOT NULL,
                                                lost NUMBER(11) NOT NULL,
                                                PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS historygame (
                                           history_id IDENTITY,
                                           name_player_1 VARCHAR (28) NOT NULL,
                                           value_1 VARCHAR (1) NOT NULL,
                                           id_1 VARCHAR (1) NOT NULL,
                                           name_player_2 VARCHAR (28) NOT NULL,
                                           value_2 VARCHAR (1) NOT NULL,
                                           id_2 VARCHAR (1) NOT NULL,
                                           id_pobed VARCHAR (1) NOT NULL,
                                           primary key (history_id)
);

CREATE TABLE IF NOT EXISTS step (
                                    step_id IDENTITY,
                                    key_history_id BIGINT NOT NULL,
                                    num VARCHAR (10) NOT NULL,
                                    player_id VARCHAR (10) NOT NULL,
                                    xy VARCHAR (2) NOT NULL
);

alter table step add foreign key (key_history_id) references historygame(history_id);