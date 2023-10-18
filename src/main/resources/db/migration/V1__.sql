CREATE TABLE app_initializer
(
    id            BIGINT                      NOT NULL,
    last_updated  TIMESTAMP WITHOUT TIME ZONE,
    uuid          UUID                        NOT NULL,
    last_activity TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_appinitializer PRIMARY KEY (id)
);

CREATE TABLE provider
(
    id           BIGINT       NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE,
    name         VARCHAR(255) NOT NULL,
    type         VARCHAR(255) NOT NULL,
    CONSTRAINT pk_provider PRIMARY KEY (id)
);

CREATE TABLE task
(
    id            BIGINT                      NOT NULL,
    last_updated  TIMESTAMP WITHOUT TIME ZONE,
    provider_id   BIGINT                      NOT NULL,
    delay         JSONB                       NOT NULL,
    path_response JSONB                       NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_run      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    type          VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_task PRIMARY KEY (id)
);

ALTER TABLE app_initializer
    ADD CONSTRAINT uc_appinitializer_uuid UNIQUE (uuid);

ALTER TABLE provider
    ADD CONSTRAINT uc_provider_name UNIQUE (name);

ALTER TABLE task
    ADD CONSTRAINT FK_TASK_ON_PROVIDERID FOREIGN KEY (provider_id) REFERENCES provider (id);