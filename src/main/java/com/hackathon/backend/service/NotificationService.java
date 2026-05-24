package com.hackathon.backend.service;

import com.hackathon.backend.dto.NotificationResponse;
import com.hackathon.backend.entity.*;
import com.hackathon.backend.exception.NoStudentsInGroupException;
import com.hackathon.backend.exception.NotificationNotFoundException;
import com.hackathon.backend.repository.NotificationRepository;
import com.hackathon.backend.repository.StudentGroupRepository;
import com.twilio.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final WhatsappService whatsappService;

    public List<NotificationResponse> findAll() {
        return notificationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void generateForTask(AcademicTask task) {
        List<StudentGroup> links = studentGroupRepository.findByGroupId(task.getGroup().getId());

        if (links.isEmpty()) {
            throw new NoStudentsInGroupException(task.getGroup().getId());
        }

        notificationRepository.deleteByAcademicTaskId(task.getId());

        for (StudentGroup link : links) {
            Student student = link.getStudent();
            Notification notification = Notification.builder()
                    .student(student)
                    .academicTask(task)
                    .message(buildMessage(student, task))
                    .status(NotificationStatus.PENDING)
                    .build();
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void sendAllForTask(AcademicTask task) {
        List<Notification> notifications = notificationRepository.findByAcademicTaskId(task.getId());

        for (Notification notification : notifications) {
            if (notification.getStatus() != NotificationStatus.SENT) {
                dispatch(notification);
            }
        }
    }

    @Transactional
    public void send(Long id) {
        dispatch(getEntityById(id));
    }

    @Transactional
    public void resendErrors() {
        List<Notification> errors = notificationRepository.findByStatus(NotificationStatus.ERROR);
        for (Notification notification : errors) {
            dispatch(notification);
        }
    }

    private void dispatch(Notification notification) {
        try {
            whatsappService.sendMessage(
                    notification.getStudent().getPhoneNumber(),
                    notification.getMessage()
            );
            notification.setStatus(NotificationStatus.SENT);
        } catch (ApiException ex) {
            notification.setStatus(NotificationStatus.ERROR);
        }
        notificationRepository.save(notification);
    }

    private Notification getEntityById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    private String buildMessage(Student student, AcademicTask task) {
        String typeLabel = switch (task.getType()) {
            case PROVA -> "Prova";
            case TRABALHO -> "Trabalho";
            case AVISO -> "Aviso";
            case EVENTO -> "Evento";
        };

        StringBuilder msg = new StringBuilder();
        msg.append("Olá ").append(student.getName()).append("! ");
        msg.append("Lembrete de ").append(typeLabel.toLowerCase()).append(": ");
        msg.append("\"").append(task.getTitle()).append("\"");
        msg.append(" — prazo: ").append(task.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if (task.getDescription() != null && !task.getDescription().isBlank()) {
            msg.append(". ").append(task.getDescription());
        }

        return msg.toString();
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getStudent().getName(),
                notification.getStudent().getPhoneNumber(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getAcademicTask().getTitle(),
                notification.getCreatedAt().toString()
        );
    }
}
