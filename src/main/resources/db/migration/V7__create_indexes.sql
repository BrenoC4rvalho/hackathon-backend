CREATE INDEX idx_student_groups_student_id ON student_groups(student_id);
CREATE INDEX idx_student_groups_group_id ON student_groups(group_id);

CREATE INDEX idx_academic_tasks_group_id ON academic_tasks(group_id);
CREATE INDEX idx_academic_tasks_type ON academic_tasks(type);
CREATE INDEX idx_academic_tasks_event_date ON academic_tasks(event_date);

CREATE INDEX idx_notifications_task_id ON notifications(task_id);
CREATE INDEX idx_notifications_student_id ON notifications(student_id);
CREATE INDEX idx_notifications_status ON notifications(status);