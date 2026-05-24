package com.hackathon.backend.repository;

import com.hackathon.backend.entity.AcademicTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicTaskRepository extends JpaRepository<AcademicTask, Long> {
}
