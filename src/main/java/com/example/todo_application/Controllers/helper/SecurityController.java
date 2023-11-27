package com.example.todo_application.Controllers.helper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "loginPage";
    }
}
