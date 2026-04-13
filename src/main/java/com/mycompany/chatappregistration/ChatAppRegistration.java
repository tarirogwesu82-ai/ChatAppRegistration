package com.mycompany.chatappregistration;

import java.util.Scanner;

/**
 * ChatAppRegistration - Main entry point.
 * Author: student 
 * Date: 2026
 * /**
 * ChatAppRegistration - Main entry point for the Chat App Registration system.
 * This class handles all user interaction via the console.
 * It makes use of the Registration and Login classes to validate
 * user input and authenticate users.
 *
 * Scanner class used for console input:
 * Oracle. 2024. Java SE 17 Documentation: Scanner.
 * [Online]. Available at: https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
 * [Accessed: 13 April 2026]
 *
 * Author: student
 * Student Number: ST10498762
 * Date: 13 April 2026
 */
 
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class ChatAppRegistration {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Chat App Registration");
        

        // USERNAME
        String username = "";
        while (true) {
            System.out.print("Enter username (must contain _ and be max 5 characters): ");
            username = scan.nextLine();

            Registration tempReg = new Registration("", "", username, "Temp@1234", "+27000000000");
            if (tempReg.checkUserName()) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted; please ensure "
                        + "that your username contains an underscore and is no "
                        + "more than five characters in length.");
                System.out.println("Please try again.");
            }
        }

        // PASSWORD
        String password = "";
        while (true) {
            System.out.println("Password must be at least 8 characters, contain a capital letter, a number, and a special character.");
            System.out.print("Enter password: ");
            password = scan.nextLine();

            Registration tempReg = new Registration("", "", "a_1", password, "+27000000000");
            if (tempReg.checkPasswordComplexity()) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password is not correctly formatted; please ensure "
                        + "that the password contains at least eight characters, "
                        + "a capital letter, a number, and a special character.");
                System.out.println("Please try again.");
            }
        }

        // CELL PHONE
        String cellPhone = "";
        while (true) {
            System.out.print("Enter cell phone number (must include international code e.g. +27838968976): ");
            cellPhone = scan.nextLine();

            Registration tempReg = new Registration("", "", "a_1", "Temp@1234", cellPhone);
            if (tempReg.checkCellPhoneNumber()) {
                System.out.println("Cell number successfully captured.");
                break;
            } else {
                System.out.println("Cell number is incorrectly formatted or does not contain "
                        + "an international code; please correct the number and try again.");
                System.out.println("Please try again.");
            }
        }

        // FIRST AND LAST NAME
        System.out.print("Enter first name: ");
        String firstName = scan.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scan.nextLine();

        // FINAL REGISTRATION
        Registration reg = new Registration(firstName, lastName, username, password, cellPhone);
        System.out.println(reg.registerUser());

        // LOGIN
        System.out.println("Login");
        System.out.print("Enter username to login: ");
        String loginUsername = scan.nextLine();

        System.out.print("Enter password to login: ");
        String loginPassword = scan.nextLine();

        Login login = new Login(loginUsername, loginPassword, reg);
        System.out.println(login.returnLoginStatus());

        
    }
}