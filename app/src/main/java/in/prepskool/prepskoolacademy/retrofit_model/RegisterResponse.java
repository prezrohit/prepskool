package in.prepskool.prepskoolacademy.retrofit_model;

public class RegisterResponse {

    private String status;
    private String token;
    private User user;

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
