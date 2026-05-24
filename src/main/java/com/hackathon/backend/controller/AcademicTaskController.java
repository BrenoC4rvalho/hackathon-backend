package com.hackathon.backend.controller;

import com.hackathon.backend.dto.AcademicTaskRequest;
import com.hackathon.backend.dto.AcademicTaskResponse;
import com.hackathon.backend.enums.TaskType;
import com.hackathon.backend.service.AcademicTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-tasks")
@RequiredArgsConstructor
public class AcademicTaskController {

    private final AcademicTaskService academicTaskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicTaskResponse create(@Valid @RequestBody AcademicTaskRequest request) {
        return academicTaskService.create(request);
    }

    @GetMapping
    public List<AcademicTaskResponse> findAll() {
        return academicTaskService.findAll();
    }

    @GetMapping("/{id}")
    public AcademicTaskResponse findById(@PathVariable Long id) {
        return academicTaskService.findById(id);
    }

    @GetMapping("/group/{groupId}")
    public List<AcademicTaskResponse> findAllByGroupId(@PathVariable Long groupId) {
        return academicTaskService.findAllByGroupId(groupId);
    }

    @GetMapping("/type/{type}")
    public List<AcademicTaskResponse> findAllByType(@PathVariable TaskType type) {
        return academicTaskService.findAllByType(type);
    }

    @PutMapping("/{id}")
    public AcademicTaskResponse update(
            @PathVariable Long id,
            @Valid @RequestBody AcademicTaskRequest request
    ) {
        return academicTaskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        academicTaskService.delete(id);
    }
}
