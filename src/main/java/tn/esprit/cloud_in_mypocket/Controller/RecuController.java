package tn.esprit.cloud_in_mypocket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.cloud_in_mypocket.entity.Paiement;
import tn.esprit.cloud_in_mypocket.repository.PaiementRepository;
import tn.esprit.cloud_in_mypocket.service.ExportPdfPaiement;

@RestController
@RequestMapping("/api/recu")
public class RecuController {
    @Autowired
    private  ExportPdfPaiement exportPdfPaiement;
    @Autowired
    private  PaiementRepository paiementRepository;
    @GetMapping("/{paiementId}/pdf")
    public ResponseEntity<byte[]> getRecuPdf(@PathVariable Long paiementId) throws Exception {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        byte[] pdfBytes = exportPdfPaiement.genererRecuPDF(paiement);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("recu_" + paiementId + ".pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}