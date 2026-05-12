package com.cms.auth.scheduler;

import com.cms.auth.entity.User;
import com.cms.auth.repository.UserRepository;
import com.cms.auth.service.MailService;
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
    private final MailService mailService;

    // 🕕 Runs every day at 6 AM
    //@Scheduled(cron = "0 0 6 * * ?")
    //@Scheduled(fixedRate = 60000)
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at 12 AM


    // 🔥 TEST MODE
    //@Scheduled(fixedRate = 60000)

    public void generateDailyPasswords() {

        List<User> students =
                userRepository.findByRoleAndStatus(
                        Role.STUDENT,
                        UserStatus.ACTIVE
                );

        for (User student : students) {

            try {

                // ✅ generate plain password
                String plainPassword = PasswordGenerator.generate();

                // ✅ hash password
                String hashedPassword =
                        passwordEncoder.encode(plainPassword);

                // ✅ update student
                student.setPassword(hashedPassword);
                student.setLoginAttempts(0);
                student.setLastFailedAttemptAt(null);

                userRepository.save(student);

                // ✅ SEND EMAIL
                mailService.sendPasswordEmail(
                        student.getEmail(),
                        student.getUsername(),
                        plainPassword
                );

                System.out.println(
                        "✅ Password generated + mailed to: "
                                + student.getEmail()
                );

            } catch (Exception e) {

                System.out.println(
                        "❌ Failed for: "
                                + student.getEmail()
                );

                e.printStackTrace();
            }
        }

        System.out.println(
                "✅ Daily student passwords generated at "
                        + LocalDateTime.now()
        );
    }
}