CREATE TABLE academic_tasks (
                                id BIGSERIAL PRIMARY KEY,
                                title VARCHAR(255) NOT NULL,
                                description VARCHAR(3000),
                                type VARCHAR(50) NOT NULL,
                                event_date TIMESTAMP NOT NULL,
                                notification_date TIMESTAMP,
                                group_id BIGINT NOT NULL,

                                CONSTRAINT fk_academic_tasks_group
                                    FOREIGN KEY (group_id)
                                        REFERENCES groups(id)
                                        ON DELETE CASCADE
);