package pl.akademiakursu.week10.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Aspect
public class MovieAspect {

    private JavaMailSender javaMailSender;

    @Autowired
    public MovieAspect(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }

    @After("@annotation(AddMovie)")
    public void addMovie() {
        System.out.println("After aspekt");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo("testjb777@gmail.com");
            helper.setText("Add movie by aspect");
            helper.setSubject("Add movie");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }
}
