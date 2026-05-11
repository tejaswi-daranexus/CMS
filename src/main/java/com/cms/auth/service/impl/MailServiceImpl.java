package com.cms.auth.service.impl;

import com.cms.auth.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordEmail(String toEmail,
                                  String username,
                                  String password) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("CMS Daily Password");

        message.setText(
                "Hello,\n\n" +
                        "Your CMS login password has been generated.\n\n" +
                        "Username: " + username + "\n" +
                        "Password: " + password + "\n\n" +
                        "Please do not share this password.\n\n" +
                        "Regards,\nCMS Administration"
        );

        mailSender.send(message);
    }
}