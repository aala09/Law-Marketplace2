package tn.esprit.cloud_in_mypocket.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ✅ Envoi du code de vérification 2FA (inscription)
    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Vérification de votre compte LawConnect");
            helper.setFrom("no-reply@lawconnect.com", "LawConnect - Ne pas répondre");

            String body = "<p>Bonjour,</p>" +
                    "<p>Merci de vous être inscrit(e) sur <strong>LawConnect</strong>.</p>" +
                    "<p>Pour finaliser votre inscription, veuillez entrer le code de vérification suivant :</p>" +
                    "<h2 style='color:#2d3748;'>" + code + "</h2>" +
                    "<p>Ce code est valable pendant <strong>10 minutes</strong>.</p>" +
                    "<p>Si vous n’êtes pas à l’origine de cette demande, vous pouvez ignorer ce message.</p>" +
                    "<br><p style='font-size:12px; color:#888;'>Ceci est un message automatique, merci de ne pas y répondre.</p>";

            helper.setText(body, true);
            mailSender.send(message);

            System.out.println("📧 Code de vérification envoyé à : " + to);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l’envoi de l’email de vérification : " + e.getMessage());
        }
    }

    // 🔄 Envoi du code de réinitialisation de mot de passe
    public void sendResetCode(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Réinitialisation de votre mot de passe - LawConnect");
            helper.setFrom("no-reply@lawconnect.com", "LawConnect - Ne pas répondre");

            String body = "<p>Bonjour,</p>" +
                    "<p>Vous avez demandé la réinitialisation de votre mot de passe.</p>" +
                    "<p>Voici votre code de réinitialisation :</p>" +
                    "<h2 style='color:#e53e3e;'>" + code + "</h2>" +
                    "<p>Ce code est valable pendant <strong>10 minutes</strong>.</p>" +
                    "<p>Si vous n’êtes pas à l’origine de cette demande, veuillez ignorer cet email.</p>" +
                    "<br><p style='font-size:12px; color:#888;'>Ceci est un message automatique, merci de ne pas y répondre.</p>";

            helper.setText(body, true);
            mailSender.send(message);

            System.out.println("📧 Email de réinitialisation envoyé à : " + to);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l’envoi du mail de réinitialisation : " + e.getMessage());
        }
    }

    // ⏳ Envoi du rappel d'inactivité
    public void sendInactivityReminder(String to, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Rappel d'inactivité - LawConnect");
            helper.setFrom("no-reply@lawconnect.com", "LawConnect - Ne pas répondre");

            String body = "<p>Bonjour <strong>" + username + "</strong>,</p>" +
                    "<p>Nous avons remarqué que vous n'avez pas utilisé votre compte depuis un certain temps.</p>" +
                    "<p>Pour éviter une désactivation automatique, merci de vous reconnecter dès que possible.</p>" +
                    "<br><p>Nous restons à votre disposition si vous avez besoin d’assistance.</p>" +
                    "<br><p>Cordialement,<br>L’équipe <strong>LawConnect</strong></p>" +
                    "<br><p style='font-size:12px; color:#888;'>Ceci est un message automatique, merci de ne pas y répondre.</p>";

            helper.setText(body, true);
            mailSender.send(message);

            System.out.println("📧 Rappel d’inactivité envoyé à : " + to);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l’envoi du mail d’inactivité : " + e.getMessage());
        }
    }
}
