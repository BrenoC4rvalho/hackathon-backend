package com.hackathon.backend.service;

import com.hackathon.backend.dto.NotificationResponse;
import com.hackathon.backend.entity.AcademicTask;
import com.hackathon.backend.entity.Notification;
import com.hackathon.backend.entity.Student;
import com.hackathon.backend.entity.StudentGroup;
import com.hackathon.backend.enums.NotificationStatus;
import com.hackathon.backend.exception.AcademicTaskNotFoundException;
import com.hackathon.backend.repository.AcademicTaskRepository;
import com.hackathon.backend.repository.NotificationRepository;
import com.hackathon.backend.repository.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicTaskNotificationService {

    private final AcademicTaskRepository academicTaskRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final NotificationRepository notificationRepository;
    private final WhatsappService whatsappService;

    public List<NotificationResponse> generateNotifications(Long taskId) {
        AcademicTask task = getTask(taskId);

        List<StudentGroup> studentGroups =
                studentGroupRepository.findAllByGroupId(task.getGroup().getId());

        List<Notification> notifications = studentGroups.stream()
                .map(StudentGroup::getStudent)
                .filter(student -> !notificationRepository.existsByTaskIdAndStudentId(task.getId(), student.getId()))
                .map(student -> buildNotification(task, student))
                .toList();

        return notificationRepository.saveAll(notifications)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> sendNotifications(Long taskId) {
        AcademicTask task = getTask(taskId);

        List<Notification> notifications = notificationRepository.findAllByTaskId(task.getId());

        return notifications.stream()
                .map(this::sendNotification)
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> generateAndSendNotifications(Long taskId) {
        generateNotifications(taskId);
        return sendNotifications(taskId);
    }

    private Notification sendNotification(Notification notification) {
        if (notification.getStatus() == NotificationStatus.SENT) {
            return notification;
        }

        try {
            whatsappService.sendMessage(
                    notification.getPhoneNumber(),
                    notification.getMessage()
            );

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(java.time.LocalDateTime.now());
            notification.setErrorMessage(null);
        } catch (Exception ex) {
            notification.setStatus(NotificationStatus.ERROR);
            notification.setErrorMessage(ex.getMessage());
        }

        return notificationRepository.save(notification);
    }

    private Notification buildNotification(AcademicTask task, Student student) {
        return Notification.builder()
                .task(task)
                .student(student)
                .phoneNumber(student.getPhoneNumber())
                .message(buildMessage(task, student))
                .status(NotificationStatus.PENDING)
                .build();
    }

    private String buildMessage(AcademicTask task, Student student) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        return switch (task.getType()) {
            case EXAM -> "📚 Olá, " + student.getName() + "! Você tem prova de "
                    + task.getTitle() + " em " + task.getEventDate().format(formatter) + ".";

            case ASSIGNMENT -> "⚠️ Olá, " + student.getName() + "! O trabalho "
                    + task.getTitle() + " vence em " + task.getEventDate().format(formatter) + ".";

            case EVENT -> "📅 Olá, " + student.getName() + "! Evento acadêmico: "
                    + task.getTitle() + " em " + task.getEventDate().format(formatter) + ".";

            case ROOM_CHANGE -> "🔄 Olá, " + student.getName() + "! Houve alteração: "
                    + task.getTitle() + ". " + safeDescription(task);

            case NOTICE -> "📢 Olá, " + student.getName() + "! Aviso: "
                    + task.getTitle() + ". " + safeDescription(task);
        };
    }

    private String safeDescription(AcademicTask task) {
        return task.getDescription() == null ? "" : task.getDescription();
    }

    private AcademicTask getTask(Long taskId) {
        return academicTaskRepository.findById(taskId)
                .orElseThrow(() -> new AcademicTaskNotFoundException(taskId));
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTask().getId(),
                notification.getTask().getTitle(),
                notification.getStudent().getId(),
                notification.getStudent().getName(),
                notification.getPhoneNumber(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getSentAt(),
                notification.getErrorMessage()
        );
    }
}