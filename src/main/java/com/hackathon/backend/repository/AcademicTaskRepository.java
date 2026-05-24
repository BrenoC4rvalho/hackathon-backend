package com.hackathon.backend.repository;

import com.hackathon.backend.entity.AcademicTask;
import com.hackathon.backend.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AcademicTaskRepository extends JpaRepository<AcademicTask, Long> {

    List<AcademicTask> findAllByGroupId(Long groupId);

    List<AcademicTask> findAllByType(TaskType type);

    List<AcademicTask> findAllByEventDateBetween(LocalDateTime start, LocalDateTime end);

    List<AcademicTask> findAllByEventDateAfter(LocalDateTime now);
}