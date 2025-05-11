package tn.esprit.cloud_in_mypocket.payload;

import java.util.Map;

public class PaymentRequest {
    private Long amount;
    private String currency;
    private Map<String, Object> metadata;
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}