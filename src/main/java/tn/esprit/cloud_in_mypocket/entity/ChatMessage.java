package tn.esprit.cloud_in_mypocket.entity;

public class ChatMessage {
    private String type;          // "IDENTIFICATION" or "CHAT"
    private String senderId;      // ID of the sender
    private String senderName;    // Full name of the sender (prenom + nom)
    private String senderEmail;   // Email of the sender
    private String receiverId;    // ID of the receiver
    private String content;       // Message content
    private String timestamp;     // ISO timestamp

    // Default constructor
    public ChatMessage() {
    }

    // Constructor with all fields
    public ChatMessage(String type, String senderId, String senderName, String senderEmail,
                       String receiverId, String content, String timestamp) {
        this.type = type;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type='" + type + '\'' +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}