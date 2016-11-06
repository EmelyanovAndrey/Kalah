CREATE TABLE avatar
(
    avatar_id BIGINT PRIMARY KEY NOT NULL,
    avatar_name VARCHAR NOT NULL,
    file_path VARCHAR NOT NULL
);

CREATE TABLE "user"
(
    user_id BIGINT PRIMARY KEY NOT NULL,
    login VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    email VARCHAR,
    avatar_id BIGINT,
    CONSTRAINT user_avatar_avatar_id_fk FOREIGN KEY (avatar_id) REFERENCES avatar (avatar_id)
);
CREATE UNIQUE INDEX user_login_uindex ON "user" (login);

CREATE TABLE game_state
(
    game_state_id BIGINT PRIMARY KEY NOT NULL,
    user_1_id BIGINT NOT NULL,
    user_2_id BIGINT NOT NULL,
    last_save_date TIMESTAMP NOT NULL,
    stone_count_of_user_1 INTEGER NOT NULL,
    stone_count_of_user_2 INTEGER NOT NULL,
    initial_hole_count INTEGER NOT NULL,
    initial_stone_count INTEGER NOT NULL,
    priority BOOLEAN NOT NULL,
    CONSTRAINT game_state_user_user_id_1_fk FOREIGN KEY (user_1_id) REFERENCES "user" (user_id),
    CONSTRAINT game_state_user_user_id_2_fk FOREIGN KEY (user_2_id) REFERENCES "user" (user_id)
);

CREATE TABLE hole
(
    hole_id BIGINT PRIMARY KEY NOT NULL,
    game_state_id BIGINT NOT NULL,
    number INTEGER NOT NULL,
    stone_count INTEGER NOT NULL,
    CONSTRAINT hole_game_state_game_state_id_fk FOREIGN KEY (game_state_id) REFERENCES game_state (game_state_id)
);

