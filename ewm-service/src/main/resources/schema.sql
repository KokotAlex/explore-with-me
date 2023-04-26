-- DROP TABLE IF EXISTS categories CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;
-- DROP TABLE IF EXISTS events CASCADE;
-- DROP TABLE IF EXISTS requests CASCADE;
-- DROP TABLE IF EXISTS compilations CASCADE;
-- DROP TABLE IF EXISTS compilations_event CASCADE;

CREATE TABLE IF NOT EXISTS categories
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY   NOT NULL,
    name         VARCHAR(100) UNIQUE                      NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY  NOT NULL,
    name  VARCHAR(100) UNIQUE                      NOT NULL,
    email VARCHAR(100) UNIQUE                      NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title       VARCHAR(120)                            NOT NULL,
    annotation  VARCHAR(2000)                           NOT NULL,
    category_id BIGINT REFERENCES categories (id) ON DELETE RESTRICT INITIALLY DEFERRED NOT NULL,
    created_on  TIMESTAMP                               NOT NULL,
    description VARCHAR(7000)                           NOT NULL,
    event_date  TIMESTAMP                               NOT NULL,
    user_id     BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    location_lat FLOAT(5)                               NOT NULL,
    location_lon FLOAT(5)                               NOT NULL,
    paid                BOOLEAN,
    participant_limit   INTEGER,
    published_on        TIMESTAMP,
    request_moderation  BOOLEAN,
    state               SMALLINT                        NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY         NOT NULL,
    created         TIMESTAMP                                       NOT NULL,
    event_id        BIGINT REFERENCES events(id) ON DELETE CASCADE  NOT NULL,
    requester_id    BIGINT REFERENCES users(id) ON DELETE CASCADE   NOT NULL,
    status          SMALLINT                                        NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned  BOOLEAN                                 NOT NULL,
    title   VARCHAR(200) UNIQUE                     NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_event
(
    compilation_id  BIGINT REFERENCES compilations(id) ON DELETE CASCADE    NOT NULL,
    event_id        BIGINT REFERENCES events(id) ON DELETE CASCADE          NOT NULL,
    CONSTRAINT pk_compilations_event PRIMARY KEY (compilation_id, event_id)
);