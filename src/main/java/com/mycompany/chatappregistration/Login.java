package com.mycompany.chatappregistration;
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class Login {

    private String enteredUsername;
    private String enteredPassword;
    private Registration registeredUser;

    public Login(String enteredUsername, String enteredPassword,
                 Registration registeredUser) {
        this.enteredUsername = enteredUsername;
        this.enteredPassword = enteredPassword;
        this.registeredUser = registeredUser;
    }

    public boolean loginUser() {
        if (enteredUsername == null || enteredPassword == null) {
            return false;
        }
        return enteredUsername.equals(registeredUser.getUsername())
            && enteredPassword.equals(registeredUser.getPassword());
    }

    public String returnLoginStatus() {
        if (loginUser()) {
            return "Welcome " + registeredUser.getFirstName()
                 + " " + registeredUser.getLastName()
                 + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}