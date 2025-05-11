package tn.esprit.cloud_in_mypocket.dto;

import lombok.Data;

@Data
public class StripeResponseDTO {
    private String id;
    private String status;
    private String clientSecret;
    private Long amount;
    private String currency;
    private String paymentMethod;
    private String errorMessage;

    public static StripeResponseDTO fromPaymentIntent(com.stripe.model.PaymentIntent paymentIntent) {
        StripeResponseDTO dto = new StripeResponseDTO();
        dto.setId(paymentIntent.getId());
        dto.setStatus(paymentIntent.getStatus());
        dto.setClientSecret(paymentIntent.getClientSecret());
        dto.setAmount(paymentIntent.getAmount());
        dto.setCurrency(paymentIntent.getCurrency());
        dto.setPaymentMethod(paymentIntent.getPaymentMethod());
        return dto;
    }

    public static StripeResponseDTO error(String errorMessage) {
        StripeResponseDTO dto = new StripeResponseDTO();
        dto.setErrorMessage(errorMessage);
        return dto;
    }
}