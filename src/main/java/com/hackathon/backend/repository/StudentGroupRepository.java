package com.hackathon.backend.repository;


import com.hackathon.backend.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    boolean existsByStudentIdAndGroupId(Long studentId, Long groupId);

    List<StudentGroup> findAllByGroupId(Long groupId);

    List<StudentGroup> findAllByStudentId(Long studentId);

}
