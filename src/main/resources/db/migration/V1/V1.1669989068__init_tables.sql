CREATE TABLE IF NOT EXISTS roles
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS language
(
    id          BIGSERIAL PRIMARY KEY,
    code        VARCHAR(100) NOT NULL,
    title       VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(255)        NOT NULL UNIQUE,
    password        VARCHAR(255),
    gender          VARCHAR(10)         NOT NULL,
    language_id     BIGINT REFERENCES language (id) NOT NULL,
    role_id         BIGINT REFERENCES roles (id) NOT NULL,
    language_code   VARCHAR(3)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    id              uuid PRIMARY KEY,
    user_id         BIGINT REFERENCES users (id) NOT NULL,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    created_by      BIGINT REFERENCES users(id),
    updated_by      BIGINT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS hatim
(
    id              uuid PRIMARY KEY,
    hatim_type      VARCHAR(100)         NOT NULL,
    status          VARCHAR(100)         NOT NULL,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    created_by      BIGINT REFERENCES users(id),
    updated_by      BIGINT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS juz
(
    id              uuid PRIMARY KEY,
    number          INTEGER,
    status          VARCHAR(100)         NOT NULL,
    hatim_id        uuid REFERENCES hatim (id) NOT NULL,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    created_by      BIGINT REFERENCES users(id),
    updated_by      BIGINT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS page
(
    id              uuid PRIMARY KEY,
    number          INTEGER,
    status          VARCHAR(100)         NOT NULL,
    juz_id          uuid REFERENCES juz (id) NOT NULL,
    user_id         BIGINT REFERENCES users (id),
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    created_by      BIGINT REFERENCES users(id),
    updated_by      BIGINT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS quran_page
(
    id              BIGSERIAL PRIMARY KEY,
    juz             INTEGER,
    page            INTEGER
);

CREATE TABLE IF NOT EXISTS surah
(
    id                          BIGSERIAL PRIMARY KEY,
    number                      INTEGER,
    name                        VARCHAR,
    english_name                VARCHAR,
    english_name_translation    VARCHAR,
    number_of_ayahs             INTEGER,
    revelation_type             VARCHAR
);

CREATE TABLE IF NOT EXISTS ayah
(
    id              BIGSERIAL PRIMARY KEY,
    number          INTEGER,
    ayah            TEXT,
    quran_page_id   BIGINT REFERENCES quran_page (id) NOT NULL,
    surah_id        BIGINT REFERENCES surah (id) NOT NULL,
    sajda           BOOLEAN,
    ruku            INTEGER,
    hizb_quarter    INTEGER
);

INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO language(code, title) VALUES ('eng', 'English');

INSERT INTO users (username, password, gender, language_id, role_id)
VALUES ('admin', '$2a$12$CpizkbsE.PjNu/B8QINvIe5SIY5H0ICvGeQrIbf3InBcunua1gIv.', 'MALE', 1, 1);
INSERT INTO users (username, password, gender, language_id, role_id)
VALUES ('user', '$2a$12$CpizkbsE.PjNu/B8QINvIe5SIY5H0ICvGeQrIbf3InBcunua1gIv.', 'MALE', 1, 2);