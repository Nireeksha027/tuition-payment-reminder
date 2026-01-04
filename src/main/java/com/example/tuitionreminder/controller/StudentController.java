package com.example.tuitionreminder.controller;

import com.example.tuitionreminder.entity.Student;
import com.example.tuitionreminder.repository.StudentRepository;
import com.example.tuitionreminder.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class StudentController {

    private final StudentRepository repo;
    private final EmailService emailService;

    public StudentController(StudentRepository repo, EmailService emailService) {
        this.repo = repo;
        this.emailService = emailService;
    }

   
 // HOME PAGE
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // ADMIN LOGIN PAGE
    @GetMapping("/admin-login")
    public String adminLogin() {
        return "admin-login";
    }

  


    // Student List
    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("students", repo.findAll());
        return "student-list";
    }

    // Add/Edit Form
    @GetMapping("/students/form")
    public String form(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("student",
                id == null ? new Student() : repo.findById(id).orElseThrow());
        return "student-form";
    }

    // Save Student
    @PostMapping("/students/save")
    public String save(Student student, RedirectAttributes ra) {

        if (student.getJoinDate() == null) {
            student.setJoinDate(LocalDate.now());
        }

        repo.save(student);

        ra.addFlashAttribute("successMessage", "Student saved successfully");
        return "redirect:/students";
    }

    // Delete Student
    @GetMapping("/students/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("successMessage", "Student deleted successfully");
        return "redirect:/students";
    }

    // Mark Paid (Admin)
    @GetMapping("/students/mark-paid")
    public String markPaid(@RequestParam Long id, RedirectAttributes ra) {
        Student s = repo.findById(id).orElseThrow();
        s.setPaid(true);
        repo.save(s);
        ra.addFlashAttribute("successMessage", "Student marked as PAID");
        return "redirect:/students";
    }

    // Confirm Paid (Email link)
    @GetMapping("/students/confirm-paid")
    public String confirmPaid(@RequestParam Long id) {
        Student s = repo.findById(id).orElseThrow();
        s.setPaid(true);
        repo.save(s);
        return "paid-success";
    }

    // Manual Reminder
    @GetMapping("/students/send-reminder")
    public String sendReminder(@RequestParam Long id, RedirectAttributes ra) {

        Student s = repo.findById(id).orElseThrow();
        emailService.sendReminder(s.getEmail(), s.getName(), s.getFees(), s.getId());

        ra.addFlashAttribute("successMessage", "Reminder sent successfully");

        return "redirect:/students";
    }


    // Auto Reminder (30 days)
    @GetMapping("/students/auto-reminders")
    public String autoReminder() {
        LocalDate date = LocalDate.now().minusDays(30);
        List<Student> list = repo.findByPaidFalseAndJoinDateBefore(date);

        for (Student s : list) {
            emailService.sendReminder(
                    s.getEmail(), s.getName(), s.getFees(), s.getId());
        }
        return "redirect:/students";
    }
}
