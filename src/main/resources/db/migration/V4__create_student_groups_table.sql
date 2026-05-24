CREATE TABLE student_groups (
                                id BIGSERIAL PRIMARY KEY,
                                student_id BIGINT NOT NULL,
                                group_id BIGINT NOT NULL,

                                CONSTRAINT fk_student_groups_student
                                    FOREIGN KEY (student_id)
                                        REFERENCES students(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_student_groups_group
                                    FOREIGN KEY (group_id)
                                        REFERENCES groups(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT uk_student_group
                                    UNIQUE (student_id, group_id)
);