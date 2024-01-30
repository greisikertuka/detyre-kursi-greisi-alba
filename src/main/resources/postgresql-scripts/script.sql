CREATE DATABASE java_ee_database;
CREATE TABLE USERS
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(50)  NOT NULL,
    email    VARCHAR(255) NOT NULL,
    role     VARCHAR(50)
);
CREATE TABLE QUIZZES
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    image_url   VARCHAR(255)
);

CREATE TABLE QUESTIONS
(
    id      SERIAL PRIMARY KEY,
    quiz_id INTEGER REFERENCES QUIZZES (id) NOT NULL,
    index   INT                             NOT NULL,
    name    VARCHAR(255)                    NOT NULL
);

CREATE TABLE OPTIONS
(
    id          SERIAL PRIMARY KEY,
    question_id INTEGER REFERENCES QUESTIONS (id) NOT NULL,
    index       INTEGER                           NOT NULL,
    value       VARCHAR(255)                      NOT NULL,
    is_answer   BOOLEAN                           NOT NULL
);

CREATE TABLE RESULTS
(
    id       SERIAL PRIMARY KEY,
    quiz_id  INTEGER REFERENCES QUIZZES (id) NOT NULL,
    username VARCHAR(50)                     NOT NULL,
    timestamp    DATE NOT NULL
);

CREATE TABLE USER_ANSWERS
(
    id          SERIAL PRIMARY KEY,
    option_id   INTEGER REFERENCES OPTIONS (id)   NOT NULL,
    question_id INTEGER REFERENCES QUESTIONS (id) NOT NULL,
    result_id   INTEGER REFERENCES RESULTS (id)   NOT NULL
);
