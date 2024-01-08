package net.salesianos.shared.models;

public class Message {
    private String date;
    private String username;
    private String content;

    public Message(String date, String username, String content) {
        this.date = date;
        this.username = username;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
