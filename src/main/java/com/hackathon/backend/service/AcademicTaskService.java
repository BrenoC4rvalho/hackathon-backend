package com.hackathon.backend.service;

import com.hackathon.backend.dto.AcademicTaskRequest;
import com.hackathon.backend.dto.AcademicTaskResponse;
import com.hackathon.backend.entity.AcademicTask;
import com.hackathon.backend.entity.Group;
import com.hackathon.backend.enums.TaskType;
import com.hackathon.backend.exception.AcademicTaskNotFoundException;
import com.hackathon.backend.exception.GroupNotFoundException;
import com.hackathon.backend.repository.AcademicTaskRepository;
import com.hackathon.backend.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicTaskService {

    private final AcademicTaskRepository academicTaskRepository;
    private final GroupRepository groupRepository;

    public AcademicTaskResponse create(AcademicTaskRequest request) {
        Group group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new GroupNotFoundException(request.groupId()));

        AcademicTask academicTask = AcademicTask.builder()
                .title(request.title())
                .description(request.description())
                .type(request.type())
                .eventDate(request.eventDate())
                .notificationDate(request.notificationDate())
                .group(group)
                .build();

        return toResponse(academicTaskRepository.save(academicTask));
    }

    public List<AcademicTaskResponse> findAll() {
        return academicTaskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AcademicTaskResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    public List<AcademicTaskResponse> findAllByGroupId(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException(groupId);
        }

        return academicTaskRepository.findAllByGroupId(groupId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AcademicTaskResponse> findAllByType(TaskType type) {
        return academicTaskRepository.findAllByType(type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AcademicTaskResponse update(Long id, AcademicTaskRequest request) {
        AcademicTask academicTask = getEntityById(id);

        Group group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new GroupNotFoundException(request.groupId()));

        academicTask.setTitle(request.title());
        academicTask.setDescription(request.description());
        academicTask.setType(request.type());
        academicTask.setEventDate(request.eventDate());
        academicTask.setNotificationDate(request.notificationDate());
        academicTask.setGroup(group);

        return toResponse(academicTaskRepository.save(academicTask));
    }

    public void delete(Long id) {
        AcademicTask academicTask = getEntityById(id);
        academicTaskRepository.delete(academicTask);
    }

    public AcademicTask getEntityById(Long id) {
        return academicTaskRepository.findById(id)
                .orElseThrow(() -> new AcademicTaskNotFoundException(id));
    }

    private AcademicTaskResponse toResponse(AcademicTask academicTask) {
        return new AcademicTaskResponse(
                academicTask.getId(),
                academicTask.getTitle(),
                academicTask.getDescription(),
                academicTask.getType(),
                academicTask.getEventDate(),
                academicTask.getNotificationDate(),
                academicTask.getGroup().getId(),
                academicTask.getGroup().getName()
        );
    }
}