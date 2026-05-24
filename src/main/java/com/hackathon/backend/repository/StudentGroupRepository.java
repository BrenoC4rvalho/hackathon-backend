package com.hackathon.backend.repository;


import com.hackathon.backend.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    boolean existsByStudentIdAndGroupId(Long studentId, Long groupId);
}
