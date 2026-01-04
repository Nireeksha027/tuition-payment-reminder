package com.example.tuitionreminder.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReminder(String to, String name, double fees, Long id) {

        String confirmLink =
                "http://localhost:8083/students/confirm-paid?id=" + id;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Tuition Fee Reminder");
        message.setText(
                "Hello " + name + ",\n\n" +
                "This is a reminder to pay your tuition fees.\n" +
                "Amount: ₹" + fees + "\n\n" +
                "If you have already paid, click below:\n" +
                confirmLink + "\n\nThank you."
        );

        mailSender.send(message);
    }
}
