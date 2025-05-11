package tn.esprit.cloud_in_mypocket.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceAbonnement {

    @Autowired
    private JavaMailSender mailSender;

    // 🔔 Notification d'expiration proche d'abonnement
    public void sendSubscriptionExpirationReminder(String to, String username, String packName, int daysUntilExpiration) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Votre abonnement " + packName + " expire bientôt - LawConnect");
            helper.setFrom("no-reply@lawconnect.com", "LawConnect - Ne pas répondre");

            String body = "<p>Bonjour <strong>" + username + "</strong>,</p>" +
                    "<p>Votre abonnement <strong>" + packName + "</strong> expire dans <strong>" + daysUntilExpiration + " jour(s)</strong>.</p>" +
                    "<p>Pour continuer à bénéficier de tous les avantages de votre abonnement, nous vous invitons à le renouveler dès que possible.</p>" +
                    "<p>Cliquez ici pour renouveler votre abonnement : <a href='http://localhost:4200/pricing'>Renouveler mon abonnement</a></p>" +
                    "<br><p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                    "<br><p>Cordialement,<br>L'équipe <strong>LawConnect</strong></p>" +
                    "<br><p style='font-size:12px; color:#888;'>Ceci est un message automatique, merci de ne pas y répondre.</p>";

            helper.setText(body, true);
            mailSender.send(message);

            System.out.println("📧 Notification d'expiration d'abonnement envoyée à : " + to);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de la notification d'expiration : " + e.getMessage());
        }
    }


    // ⚠️ Notification d'expiration d'abonnement
    public void sendSubscriptionExpiredNotification(String to, String username, String packName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Votre abonnement " + packName + " a expiré - LawConnect");
            helper.setFrom("no-reply@lawconnect.com", "LawConnect - Ne pas répondre");

            String body = "<p>Bonjour <strong>" + username + "</strong>,</p>" +
                    "<p>Votre abonnement <strong>" + packName + "</strong> a expiré.</p>" +
                    "<p>Pour continuer à bénéficier de tous les avantages de LawConnect, nous vous invitons à renouveler votre abonnement dès que possible.</p>" +
                    "<p>Cliquez ici pour renouveler votre abonnement : <a href='http://localhost:4200/pricing'>Renouveler mon abonnement</a></p>" +
                    "<br><p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                    "<br><p>Cordialement,<br>L'équipe <strong>LawConnect</strong></p>" +
                    "<br><p style='font-size:12px; color:#888;'>Ceci est un message automatique, merci de ne pas y répondre.</p>";

            helper.setText(body, true);
            mailSender.send(message);

            System.out.println("📧 Notification d'expiration envoyée à : " + to);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de la notification d'expiration : " + e.getMessage());
        }
    }
} 