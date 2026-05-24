CREATE TABLE notifications (
                               id BIGSERIAL PRIMARY KEY,
                               task_id BIGINT NOT NULL,
                               student_id BIGINT NOT NULL,
                               phone_number VARCHAR(30) NOT NULL,
                               message VARCHAR(3000),
                               status VARCHAR(50) NOT NULL,
                               sent_at TIMESTAMP,
                               error_message VARCHAR(2000),

                               CONSTRAINT fk_notifications_task
                                   FOREIGN KEY (task_id)
                                       REFERENCES academic_tasks(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_notifications_student
                                   FOREIGN KEY (student_id)
                                       REFERENCES students(id)
                                       ON DELETE CASCADE
);