package com.cms.auth.service;

public interface MailService {

    void sendPasswordEmail(String toEmail,
                           String username,
                           String password);
}