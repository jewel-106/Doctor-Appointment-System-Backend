package com.appointment.demo.Controller;
import com.appointment.demo.model.User;
import com.appointment.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/api/test/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
        return "Password updated successfully for " + email + ". New password: " + password;
    }
}