package com.example.tuitionreminder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private static final String ADMIN_EMAIL =
            "tuition.reminder.project@gmail.com";
    private static final String ADMIN_PASSWORD = "admin123";

    // LOGIN PAGE
    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }

    // LOGIN CHECK
    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          RedirectAttributes ra) {

        if (email.equals(ADMIN_EMAIL) &&
            password.equals(ADMIN_PASSWORD)) {

            return "redirect:/students";
        }

        ra.addFlashAttribute("error", "Invalid email or password");
        return "redirect:/login";
    }
}
