package com.hackathon.backend.repository;

import com.hackathon.backend.entity.Notification;
import com.hackathon.backend.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByAcademicTaskId(Long academicTaskId);

    List<Notification> findByStatus(NotificationStatus status);

    void deleteByAcademicTaskId(Long academicTaskId);
}
