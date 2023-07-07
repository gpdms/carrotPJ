package com.exercise.carrotproject.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final String setFrom = "jk65333@gmail.com";


    //회원가입 : 인증 코드 보내기
    public void sendAuthCodeByEmail(String authCode, String toEmail){
        MimeMessage emailForm = makeSignupEmailForm(authCode, toEmail);
        emailSender.send(emailForm);
    }

    private MimeMessage makeSignupEmailForm(String authCode, String email) {
        try {
            return tryToMakeSignupEmailForm(authCode, email);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private MimeMessage tryToMakeSignupEmailForm(String authCode, String email) throws MessagingException {
        String toEmail = email;
        String title = "망고마켓, 회원가입 인증 번호입니다";
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        message.setFrom(setFrom);
        message.setText(setSignupContext(authCode), "utf-8", "html");
        return message;
    }

    private String setSignupContext(String authCode) {
        Context context = new Context();
        context.setVariable("code", authCode);
        return templateEngine.process("member/mail/signupMail", context);
    }


    //비밀번호 : 임시 비밀번호 보내기
    public void sendTemporaryPwdByEmail(String TempPwd, String toEmail) {
        MimeMessage emailForm = makePwdEmailForm(TempPwd, toEmail);
        emailSender.send(emailForm);
    }

    private MimeMessage makePwdEmailForm(String TempPwd, String email) {
        try {
            return tryToMakePwdEmailForm(TempPwd, email);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            log.error("Service Layer MessagingException {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private MimeMessage tryToMakePwdEmailForm(String TempPwd, String email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        String title = "망고마켓, 발급된 임시비밀번호입니다.";
        message.setSubject(title);
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setFrom(setFrom);
        message.setText(setPwdContext(TempPwd), "utf-8", "html");
        return message;
    }

    private String setPwdContext(String TempPwd) {
        Context context = new Context();
        context.setVariable("code", TempPwd);
        return templateEngine.process("member/mail/pwdMail", context);
    }
}
