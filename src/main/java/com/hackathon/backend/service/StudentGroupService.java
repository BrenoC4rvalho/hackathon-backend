package com.hackathon.backend.service;

import com.hackathon.backend.dto.StudentGroupRequest;
import com.hackathon.backend.dto.StudentGroupResponse;
import com.hackathon.backend.entity.Group;
import com.hackathon.backend.entity.Student;
import com.hackathon.backend.entity.StudentGroup;
import com.hackathon.backend.exception.StudentAlreadyInGroupException;
import com.hackathon.backend.exception.StudentGroupNotFoundException;
import com.hackathon.backend.repository.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentGroupService {

    private final StudentGroupRepository studentGroupRepository;
    private final StudentService studentService;
    private final GroupService groupService;

    public StudentGroupResponse create(StudentGroupRequest request) {
        if (studentGroupRepository.existsByStudentIdAndGroupId(request.studentId(), request.groupId())) {
            throw new StudentAlreadyInGroupException(request.studentId(), request.groupId());
        }

        Student student = studentService.getEntityById(request.studentId());
        Group group = groupService.getEntityById(request.groupId());

        StudentGroup studentGroup = StudentGroup.builder()
                .student(student)
                .group(group)
                .build();

        return toResponse(studentGroupRepository.save(studentGroup));
    }

    public List<StudentGroupResponse> findAll() {
        return studentGroupRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public StudentGroupResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    public StudentGroupResponse update(Long id, StudentGroupRequest request) {
        StudentGroup studentGroup = getEntityById(id);

        Student student = studentService.getEntityById(request.studentId());
        Group group = groupService.getEntityById(request.groupId());

        studentGroup.setStudent(student);
        studentGroup.setGroup(group);

        return toResponse(studentGroupRepository.save(studentGroup));
    }

    public void delete(Long id) {
        StudentGroup studentGroup = getEntityById(id);
        studentGroupRepository.delete(studentGroup);
    }

    private StudentGroup getEntityById(Long id) {
        return studentGroupRepository.findById(id)
                .orElseThrow(() -> new StudentGroupNotFoundException(id));
    }

    private StudentGroupResponse toResponse(StudentGroup studentGroup) {
        return new StudentGroupResponse(
                studentGroup.getId(),
                studentGroup.getStudent().getId(),
                studentGroup.getStudent().getName(),
                studentGroup.getGroup().getId(),
                studentGroup.getGroup().getName()
        );
    }
}