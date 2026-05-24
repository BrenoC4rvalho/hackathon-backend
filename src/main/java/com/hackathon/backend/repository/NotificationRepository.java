package com.hackathon.backend.repository;

import com.hackathon.backend.entity.Notification;
import com.hackathon.backend.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByStudentId(Long studentId);

    List<Notification> findAllByTaskId(Long taskId);

    List<Notification> findAllByStatus(NotificationStatus status);
}