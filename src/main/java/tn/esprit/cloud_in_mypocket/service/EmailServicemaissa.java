package tn.esprit.cloud_in_mypocket.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Async

public class EmailServicemaissa {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmailmaissa(String toEmail, String trainingTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Reservation Confirmation - Training: " + trainingTitle);
            message.setText("Hello,\n\n" +
                    "Your reservation for the training \"" + trainingTitle + "\" has been successfully recorded.\n\n" +
                    "Thank you for your trust.\n\nBest regards,\nThe Training Team");

            mailSender.send(message);
            System.out.println("Email sent to " + toEmail);
        } catch (Exception e) {
            System.err.println("Error while sending the email: " + e.getMessage());
        }
    }



    // Method to send email with certificate attachment


    public void sendCertificateEmail(String recipientEmail, String learnerName, String trainingTitle, byte[] pdfData, String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject("🎓 Training Certificate: " + trainingTitle);

        // Prepare HTML body
        String htmlBody = getHtmlTemplate()
                .replace("{{LEARNER_NAME}}", learnerName)
                .replace("{{TRAINING_TITLE}}", trainingTitle)
                .replace("{{TODAY_DATE}}", LocalDate.now().toString());

        helper.setText(htmlBody, true); // `true` enables HTML
        helper.addAttachment(fileName, new ByteArrayDataSource(pdfData, "application/pdf"));

        mailSender.send(message);
    }

    private String getHtmlTemplate() {
        return """
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="UTF-8">
      <style>
        body {
          font-family: 'Arial', sans-serif;
          background-color: #f4f4f4;
          padding: 30px;
        }
        .container {
          background-color: #ffffff;
          padding: 40px;
          border-radius: 10px;
          max-width: 600px;
          margin: auto;
          box-shadow: 0px 4px 8px rgba(0,0,0,0.1);
          text-align: center;
        }
        .header {
          color: #4CAF50;
          font-size: 28px;
          margin-bottom: 20px;
        }
        .sub-header {
          color: #555;
          font-size: 18px;
          margin-bottom: 40px;
        }
        .name {
          font-size: 22px;
          font-weight: bold;
          color: #333;
        }
        .training {
          font-size: 20px;
          color: #4CAF50;
          margin-top: 10px;
          margin-bottom: 30px;
        }
        .footer {
          color: #777;
          font-size: 14px;
          margin-top: 30px;
        }
      </style>
    </head>
    <body>
      <div class="container">
        <div class="header">🎉 Congratulations!</div>
        <div class="sub-header">This certificate is awarded to:</div>
        <div class="name">{{LEARNER_NAME}}</div>
        <div class="sub-header">For successfully completing the training:</div>
        <div class="training">« {{TRAINING_TITLE}} »</div>
        <div class="footer">
          Thank you for your commitment to continuous learning.<br/>
          📅 Issue Date: {{TODAY_DATE}}
        </div>
      </div>
    </body>
    </html>
    """;
    }




}
