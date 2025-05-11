package tn.esprit.cloud_in_mypocket.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.PackAbonnement;
import tn.esprit.cloud_in_mypocket.entity.Paiement;
import tn.esprit.cloud_in_mypocket.entity.User;

import java.awt.*;
import java.io.ByteArrayOutputStream;

@Service
public class ExportPdfPaiement {

    public byte[] genererRecuPDF(Paiement paiement) throws DocumentException {

        User user = paiement.getUtilisateur();
        PackAbonnement pack = paiement.getPackAbonnement();

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, baos);
        document.open();
        // Fonts personnalisés
        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(0, 102, 204));
        Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font bodyFont = new Font(Font.HELVETICA, 12);
        Paragraph title = new Paragraph("REÇU DE PAIEMENT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Ligne de séparation
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(Color.GRAY);
        document.add(separator);
        document.add(new Paragraph(" ")); // Espace

        // Contenu du reçu
        document.add(new Paragraph("Informations Client", headerFont));
        document.add(new Paragraph("Nom : " + user.getNom() + " " + user.getPrenom(), bodyFont));
        document.add(new Paragraph("Email : " + user.getEmail(), bodyFont));
        document.add(new Paragraph("Téléphone : " + user.getNumeroDeTelephone(), bodyFont));
        document.add(new Paragraph(" ")); // Espace

        document.add(new Paragraph("Détails du Pack", headerFont));
        document.add(new Paragraph("Nom du pack : " + pack.getNom(), bodyFont));
        document.add(new Paragraph("Type du pack : " + pack.getType(), bodyFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Détails de Paiement", headerFont));
        document.add(new Paragraph("Méthode : " + paiement.getMethode(), bodyFont));
        document.add(new Paragraph("Montant : " + paiement.getMontant() + " TND", bodyFont));
        document.add(new Paragraph("Date : " + paiement.getDatePaiement(), bodyFont));

        document.add(new Paragraph(" "));
        document.add(separator);
        document.add(new Paragraph("Merci pour votre confiance !", bodyFont));

        document.close();

        return baos.toByteArray();
    }
}