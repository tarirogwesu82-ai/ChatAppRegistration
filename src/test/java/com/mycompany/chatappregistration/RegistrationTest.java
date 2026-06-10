 package com.mycompany.chatappregistration;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * /**
 * RegistrationTest - Unit tests for the Registration and Login classes.
 * Tests are written using JUnit 4 to verify that all methods
 * return the correct output for both valid and invalid inputs.
 *
 * JUnit 4 testing framework used:
 * JUnit. 2024. JUnit 4 Documentation.
 * [Online]. Available at: https://junit.org/junit4/
 * [Accessed: 13 April 2026]
 *
 * assertEquals, assertTrue and assertFalse methods:
 * Oracle. 2024. Java SE 17 Documentation: Assert.
 * [Online]. Available at: https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
 * [Accessed: 13 April 2026]
 *
 * Author: student 
 * Student Number: ST10498762
 * Date: 13 April 2026
 */
 
public class RegistrationTest {

    // ─────────────────────────────────────────
    // assertEquals TESTS
    // These check that the exact right message
    // is returned by each method
    // ─────────────────────────────────────────

    @Test
    public void testUsernameCorrect_returnsWelcomeMessage() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Kyle Smith, it is great to see you.",
                reg.registerUser());
    }

    @Test
    public void testUsernameIncorrect_returnsErrorMessage() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure "
                + "that your username contains an underscore and is no "
                + "more than five characters in length.",
                reg.registerUser());
    }

    @Test
    public void testPasswordCorrect_returnsWelcomeMessage() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Kyle Smith, it is great to see you.",
                reg.registerUser());
    }

    @Test
    public void testPasswordIncorrect_returnsErrorMessage() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "password", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure "
                + "that the password contains at least eight characters, "
                + "a capital letter, a number, and a special character.",
                reg.registerUser());
    }

    @Test
    public void testCellCorrect_returnsTrue() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(reg.checkCellPhoneNumber());
    }

    @Test
    public void testCellIncorrect_returnsFalse() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(reg.checkCellPhoneNumber());
    }

    // ─────────────────────────────────────────
    // assertTrue and assertFalse TESTS
    // These check that methods return the
    // correct true or false value
    // ─────────────────────────────────────────

    @Test
    public void testLoginSuccessful_returnsTrue() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", reg);
        assertTrue(login.loginUser());
    }

    @Test
    public void testLoginFailed_returnsFalse() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        Login login = new Login("wrongUser", "wrongPass", reg);
        assertFalse(login.loginUser());
    }

    @Test
    public void testUsernameCorrect_returnsTrue() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(reg.checkUserName());
    }

    @Test
    public void testUsernameIncorrect_returnsFalse() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(reg.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexity_returnsTrue() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(reg.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity_returnsFalse() {
        Registration reg = new Registration("Kyle", "Smith",
                "kyl_1", "password", "+27838968976");
        assertFalse(reg.checkPasswordComplexity());
    }
}