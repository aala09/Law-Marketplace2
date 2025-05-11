package tn.esprit.cloud_in_mypocket.dto;

public class VerificationRequest {
    private String email;
    private String code;

    // Getters / Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
