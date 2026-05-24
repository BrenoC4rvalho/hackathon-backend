package com.hackathon.backend.controller;

import com.hackathon.backend.dto.GroupRequest;
import com.hackathon.backend.dto.GroupResponse;
import com.hackathon.backend.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse create(@Valid @RequestBody GroupRequest request) {
        return groupService.create(request);
    }

    @GetMapping
    public List<GroupResponse> findAll() {
        return groupService.findAll();
    }

    @GetMapping("/{id}")
    public GroupResponse findById(@PathVariable Long id) {
        return groupService.findById(id);
    }

    @PutMapping("/{id}")
    public GroupResponse update(
            @PathVariable Long id,
            @Valid @RequestBody GroupRequest request
    ) {
        return groupService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }
}