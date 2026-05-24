package com.hackathon.backend.service;

import com.hackathon.backend.dto.NotificationRequest;
import com.hackathon.backend.dto.NotificationResponse;
import com.hackathon.backend.entity.AcademicTask;
import com.hackathon.backend.entity.Notification;
import com.hackathon.backend.entity.Student;
import com.hackathon.backend.enums.NotificationStatus;
import com.hackathon.backend.exception.AcademicTaskNotFoundException;
import com.hackathon.backend.exception.NotificationNotFoundException;
import com.hackathon.backend.repository.AcademicTaskRepository;
import com.hackathon.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final StudentService studentService;
    private final AcademicTaskRepository academicTaskRepository;
    private final WhatsappService whatsappService;

    public NotificationResponse create(NotificationRequest request) {
        Student student = studentService.getEntityById(request.studentId());

        AcademicTask task = academicTaskRepository.findById(request.taskId())
                .orElseThrow(() -> new AcademicTaskNotFoundException(request.taskId()));

        Notification notification = Notification.builder()
                .task(task)
                .student(student)
                .phoneNumber(student.getPhoneNumber())
                .message(request.message())
                .status(NotificationStatus.PENDING)
                .build();

        return toResponse(notificationRepository.save(notification));
    }

    public List<NotificationResponse> findAll() {
        return notificationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificationResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    public List<NotificationResponse> findAllByStudentId(Long studentId) {
        studentService.getEntityById(studentId);

        return notificationRepository.findAllByStudentId(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> findAllByTaskId(Long taskId) {
        if (!academicTaskRepository.existsById(taskId)) {
            throw new AcademicTaskNotFoundException(taskId);
        }

        return notificationRepository.findAllByTaskId(taskId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> findAllByStatus(NotificationStatus status) {
        return notificationRepository.findAllByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificationResponse send(Long id) {
        Notification notification = getEntityById(id);

        try {
            whatsappService.sendMessage(
                    notification.getPhoneNumber(),
                    notification.getMessage()
            );

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setErrorMessage(null);
        } catch (Exception ex) {
            notification.setStatus(NotificationStatus.ERROR);
            notification.setErrorMessage(ex.getMessage());
        }

        return toResponse(notificationRepository.save(notification));
    }

    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification notification = getEntityById(id);

        Student student = studentService.getEntityById(request.studentId());

        AcademicTask task = academicTaskRepository.findById(request.taskId())
                .orElseThrow(() -> new AcademicTaskNotFoundException(request.taskId()));

        notification.setStudent(student);
        notification.setTask(task);
        notification.setPhoneNumber(student.getPhoneNumber());
        notification.setMessage(request.message());

        return toResponse(notificationRepository.save(notification));
    }

    public void delete(Long id) {
        Notification notification = getEntityById(id);
        notificationRepository.delete(notification);
    }

    public Notification getEntityById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
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