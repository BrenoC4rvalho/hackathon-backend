package com.hackathon.backend.controller;

import com.hackathon.backend.dto.StudentGroupRequest;
import com.hackathon.backend.dto.StudentGroupResponse;
import com.hackathon.backend.service.StudentGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-groups")
@RequiredArgsConstructor
public class StudentGroupController {

    private final StudentGroupService studentGroupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentGroupResponse create(@Valid @RequestBody StudentGroupRequest request) {
        return studentGroupService.create(request);
    }

    @GetMapping
    public List<StudentGroupResponse> findAll() {
        return studentGroupService.findAll();
    }

    @GetMapping("/{id}")
    public StudentGroupResponse findById(@PathVariable Long id) {
        return studentGroupService.findById(id);
    }

    @PutMapping("/{id}")
    public StudentGroupResponse update(
            @PathVariable Long id,
            @Valid @RequestBody StudentGroupRequest request
    ) {
        return studentGroupService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentGroupService.delete(id);
    }
}