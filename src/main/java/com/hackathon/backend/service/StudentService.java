package com.hackathon.backend.service;

import com.hackathon.backend.dto.StudentRequest;
import com.hackathon.backend.dto.StudentResponse;
import com.hackathon.backend.entity.Student;
import com.hackathon.backend.exception.StudentNotFoundException;
import com.hackathon.backend.exception.StudentRegistrationAlreadyExistsException;
import com.hackathon.backend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByRegistrationNumber(request.registrationNumber())) {
            throw new StudentRegistrationAlreadyExistsException(request.registrationNumber());
        }

        Student student = Student.builder()
                .name(request.name())
                .registrationNumber(request.registrationNumber())
                .phoneNumber(request.phoneNumber())
                .birthDate(request.birthDate())
                .build();

        return toResponse(studentRepository.save(student));
    }

    public List<StudentResponse> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public StudentResponse findById(Long id) {
        Student student = getEntityById(id);
        return toResponse(student);
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = getEntityById(id);

        studentRepository.findByRegistrationNumber(request.registrationNumber())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new StudentRegistrationAlreadyExistsException(request.registrationNumber());
                });

        student.setName(request.name());
        student.setRegistrationNumber(request.registrationNumber());
        student.setPhoneNumber(request.phoneNumber());
        student.setBirthDate(request.birthDate());

        return toResponse(studentRepository.save(student));
    }

    public void delete(Long id) {
        Student student = getEntityById(id);
        studentRepository.delete(student);
    }

    private Student getEntityById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getRegistrationNumber(),
                student.getPhoneNumber(),
                student.getBirthDate()
        );
    }
}