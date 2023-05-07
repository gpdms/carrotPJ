package com.exercise.carrotproject.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender emailSender;
    // 타임리프를사용하기 위한 객체
    private final SpringTemplateEngine templateEngine;
    private final String setFrom = "jk65333@gmail.com";

    //랜덤 인증 코드 생성
    public String createCode() {
        String authNum;
        Random random = new Random();
        StringBuffer key = new StringBuffer();
        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
        return authNum;
    }

    //가입
    //메일 양식 작성
    public MimeMessage createSignupEmailForm(String email, String authNum) throws MessagingException {
        String toEmail = email; //받는 사람
        String title = "망고마켓, 회원가입 인증 번호입니다"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setSignupContext(authNum), "utf-8", "html");

        return message;
    }
    public String setSignupContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("member/mail/signupMail", context); //signupMail.html
    }
    //메일 전송
    public void sendSignupEmail(String toEmail,String authNum) throws MessagingException, UnsupportedEncodingException {
        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createSignupEmailForm(toEmail, authNum);
        //실제 메일 전송
        emailSender.send(emailForm);
    }

    //비밀번호 재설정 메일 생성
    public MimeMessage createPwdEmailForm(String email, String authNum) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        String title = "망고마켓, 발급된 임시비밀번호입니다."; //제목
        message.setSubject(title); //제목 설정
        message.addRecipients(MimeMessage.RecipientType.TO, email); //받을 이메일 설정
        message.setFrom(setFrom); //보내는 이메일 설정
        message.setText(setPwdContext(authNum), "utf-8", "html"); //보내는 템플릿
        return message;
    }
    //비밀번호 재설정 메일 <타임리프 템플릿> 적용
    public String setPwdContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("member/mail/pwdMail", context);
    }
    //메일 보내기
    public void sendPwdEmail(String toEmail, String authNum) throws MessagingException, UnsupportedEncodingException {
        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createPwdEmailForm(toEmail, authNum);
        //실제 메일 전송
        emailSender.send(emailForm);
    }

}
