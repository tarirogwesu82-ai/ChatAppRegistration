package com.mycompany.chatappregistration;

/**
 /**
 * Login - This class handles the authentication of a registered user.
 * It compares the entered username and password against the stored
 * registration details and returns the appropriate login status message.
 *
 * String comparison method used:
 * Oracle. 2024. Java SE 17 Documentation: String.equals().
 * [Online]. Available at: https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
 * [Accessed: 13 April 2026]
 */
 
 
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class Login  {

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
