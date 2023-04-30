alter table users
    add name varchar;

alter table users
    add surname varchar;

alter table users
    add confirmed boolean default false not null;

update users
    set confirmed = true
    where username in ('admin', 'user');

CREATE SEQUENCE IF NOT EXISTS confirmation_token_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE confirmation_token
(
    id           BIGINT                      NOT NULL,
    token        VARCHAR(255)                NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    expires_at   TIMESTAMP NOT NULL,
    confirmed_at TIMESTAMP,
    user_id      BIGINT,
    CONSTRAINT pk_confirmationtoken PRIMARY KEY (id)
);

ALTER TABLE confirmation_token
    ADD CONSTRAINT FK_CONFIRMATIONTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

