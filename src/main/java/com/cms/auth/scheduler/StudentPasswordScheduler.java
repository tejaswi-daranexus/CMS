package com.cms.auth.scheduler;

import com.cms.auth.entity.User;
import com.cms.auth.repository.UserRepository;
import com.cms.auth.util.PasswordGenerator;
import com.cms.common.enums.Role;
import com.cms.common.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentPasswordScheduler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 🕕 Runs every day at 6 AM
    @Scheduled(cron = "0 0 6 * * ?")
    //@Scheduled(fixedRate = 60000)  // every 1 minute
    public void generateDailyPasswords() {

        List<User> students =
                userRepository.findByRoleAndStatus(Role.STUDENT, UserStatus.ACTIVE);

        for (User student : students) {

            // generate plain password
            String plainPassword = PasswordGenerator.generate();

            // hash it
            String hashedPassword = passwordEncoder.encode(plainPassword);

            // update user
            student.setPassword(hashedPassword);
            student.setLoginAttempts(0);
            student.setLastFailedAttemptAt(null);

            userRepository.save(student);

            // 🔥 TEMP: log password (REMOVE in production)
            System.out.println("Student: " + student.getEmail()
                    + " | New Password: " + plainPassword);
        }

        System.out.println("✅ Daily student passwords generated at " + LocalDateTime.now());
    }
}