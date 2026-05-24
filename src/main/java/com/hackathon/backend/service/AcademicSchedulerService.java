package com.hackathon.backend.service;

import com.hackathon.backend.entity.AcademicTask;
import com.hackathon.backend.entity.Student;
import com.hackathon.backend.entity.StudentGroup;
import com.hackathon.backend.repository.AcademicTaskRepository;
import com.hackathon.backend.repository.StudentGroupRepository;
import com.hackathon.backend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicSchedulerService {

    private final StudentRepository studentRepository;
    private final AcademicTaskRepository academicTaskRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final WhatsappService whatsappService;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    // Todo dia às 08:00
    @Scheduled(cron = "0 0 8 * * *", zone = "America/Sao_Paulo")
    public void sendBirthdayMessages() {
        LocalDate today = LocalDate.now();

        List<Student> birthdayStudents = studentRepository.findAll()
                .stream()
                .filter(student -> student.getBirthDate() != null)
                .filter(student ->
                        student.getBirthDate().getDayOfMonth() == today.getDayOfMonth()
                                && student.getBirthDate().getMonth() == today.getMonth()
                )
                .toList();

        for (Student student : birthdayStudents) {
            String message = "🎉 Feliz aniversário, " + student.getName()
                    + "! A equipe Campus Notify deseja um ótimo dia para você!";

            whatsappService.sendMessage(student.getPhoneNumber(), message);
        }
    }

    // Todo dia às 09:00
    @Scheduled(cron = "0 0 9 * * *", zone = "America/Sao_Paulo")
    public void sendUpcomingTaskReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysFromNow = now.plusDays(3);

        List<AcademicTask> tasks = academicTaskRepository.findAllByEventDateBetween(
                now,
                threeDaysFromNow
        );

        for (AcademicTask task : tasks) {
            List<StudentGroup> studentGroups =
                    studentGroupRepository.findAllByGroupId(task.getGroup().getId());

            for (StudentGroup studentGroup : studentGroups) {
                Student student = studentGroup.getStudent();

                String message = "⏰ Olá, " + student.getName()
                        + "! Lembrete: " + task.getTitle()
                        + " está chegando. Data: "
                        + task.getEventDate().format(DATE_FORMATTER) + ".";

                whatsappService.sendMessage(student.getPhoneNumber(), message);
            }
        }
    }

    // Toda segunda-feira às 08:30
    @Scheduled(cron = "0 30 8 * * MON", zone = "America/Sao_Paulo")
    public void sendWeeklyOpenTasksSummary() {
        LocalDateTime now = LocalDateTime.now();

        List<AcademicTask> openTasks =
                academicTaskRepository.findAllByEventDateAfter(now);

        List<Student> students = studentRepository.findAll();

        for (Student student : students) {
            StringBuilder message = new StringBuilder();

            message.append("📚 Resumo semanal Campus Notify\n\n");
            message.append("Olá, ").append(student.getName()).append("!\n");
            message.append("Essas são as próximas tarefas abertas:\n\n");

            if (openTasks.isEmpty()) {
                message.append("✅ Nenhuma tarefa aberta no momento.");
            } else {
                for (AcademicTask task : openTasks) {
                    message.append("- ")
                            .append(task.getTitle())
                            .append(" | ")
                            .append(task.getEventDate().format(DATE_FORMATTER))
                            .append("\n");
                }
            }

            whatsappService.sendMessage(student.getPhoneNumber(), message.toString());
        }
    }
}