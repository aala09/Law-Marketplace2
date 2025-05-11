package tn.esprit.cloud_in_mypocket.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class PdfGeneratorService {

    public byte[] generateCertificate(String apprenantNom, String formationTitre) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());

        try {
            // Add background image
            String imagePath = "src/main/resources/static/0.png";
            Image bg = new Image(ImageDataFactory.create(imagePath));
            bg.setFixedPosition(0, 0);
            bg.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
            document.add(bg);

            // Styled name
            Paragraph name = new Paragraph(apprenantNom)
                    .setFont(PdfFontFactory.createFont("Helvetica-Bold"))
                    .setFontSize(32)
                    .setFontColor(ColorConstants.BLACK)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(0, 300, PageSize.A4.getWidth());

            // Formation title
            Paragraph course = new Paragraph("For completing: " + formationTitre)
                    .setFont(PdfFontFactory.createFont("Helvetica"))
                    .setFontSize(18)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(0, 260, PageSize.A4.getWidth());

            // Date
            Paragraph date = new Paragraph("Issued on: " + LocalDate.now())
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(0, 200, PageSize.A4.getWidth());

            document.add(name);
            document.add(course);
            document.add(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        document.close();
        return baos.toByteArray();
    }
}
