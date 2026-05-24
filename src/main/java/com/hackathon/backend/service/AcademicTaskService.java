package com.hackathon.backend.service;

import com.hackathon.backend.dto.AcademicTaskRequest;
import com.hackathon.backend.dto.AcademicTaskResponse;
import com.hackathon.backend.entity.AcademicTask;
import com.hackathon.backend.entity.Group;
import com.hackathon.backend.exception.AcademicTaskNotFoundException;
import com.hackathon.backend.repository.AcademicTaskRepository;
import com.hackathon.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicTaskService {

    private final AcademicTaskRepository academicTaskRepository;
    private final NotificationRepository notificationRepository;
    private final GroupService groupService;
    private final NotificationService notificationService;

    public AcademicTaskResponse create(AcademicTaskRequest request) {
        Group group = groupService.getEntityById(request.groupId());

        AcademicTask task = AcademicTask.builder()
                .title(request.title())
                .description(request.description() != null ? request.description() : "")
                .type(request.type())
                .dueDate(request.dueDate())
                .group(group)
                .build();

        return toResponse(academicTaskRepository.save(task));
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

    public AcademicTaskResponse update(Long id, AcademicTaskRequest request) {
        AcademicTask task = getEntityById(id);
        Group group = groupService.getEntityById(request.groupId());

        task.setTitle(request.title());
        task.setDescription(request.description() != null ? request.description() : "");
        task.setType(request.type());
        task.setDueDate(request.dueDate());
        task.setGroup(group);

        return toResponse(academicTaskRepository.save(task));
    }

    @Transactional
    public void delete(Long id) {
        AcademicTask task = getEntityById(id);
        notificationRepository.deleteByAcademicTaskId(id);
        academicTaskRepository.delete(task);
    }

    @Transactional
    public void generateNotifications(Long id) {
        notificationService.generateForTask(getEntityById(id));
    }

    @Transactional
    public void sendWhatsappForTask(Long id) {
        notificationService.sendAllForTask(getEntityById(id));
    }

    public AcademicTask getEntityById(Long id) {
        return academicTaskRepository.findById(id)
                .orElseThrow(() -> new AcademicTaskNotFoundException(id));
    }

    private AcademicTaskResponse toResponse(AcademicTask task) {
        return new AcademicTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getType(),
                task.getDueDate(),
                task.getGroup().getId(),
                task.getGroup().getName()
        );
    }
}
