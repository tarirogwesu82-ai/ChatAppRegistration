package com.mycompany.chatappregistration;

import java.util.Scanner;
@SuppressWarnings({"resource", "CallToPrintStackTrace"})
public class ChatAppRegistration {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        // ========== REGISTRATION ==========
        System.out.println();
        System.out.println("  ==================================================");
        System.out.println("   QUICKCHAT - REGISTRATION");
        System.out.println("  ==================================================");

        // Username
        String username = "";
        boolean usernameValid = false;
        while (!usernameValid) {
            System.out.println("\n  USERNAME: Must contain _ and be ≤ 5 characters");
            System.out.print("  Enter username: ");
            username = scan.nextLine();
            Registration tempReg = new Registration("", "", username, "Temp@1234", "+27000000000");
            if (tempReg.checkUserName()) {
                System.out.println("  [OK] Username successfully captured.");
                usernameValid = true;
            } else {
                System.out.println("  [ERROR] Username must contain _ and be ≤ 5 chars.");
            }
        }

        // Password
        String password = "";
        boolean passwordValid = false;
        while (!passwordValid) {
            System.out.println("\n  PASSWORD: 8+ chars, 1 capital, 1 number, 1 special");
            System.out.print("  Enter password: ");
            password = scan.nextLine();
            Registration tempReg = new Registration("", "", "a_1", password, "+27000000000");
            if (tempReg.checkPasswordComplexity()) {
                System.out.println("  [OK] Password successfully captured.");
                passwordValid = true;
            } else {
                System.out.println("  [ERROR] Password must have 8+ chars, capital, number, special.");
            }
        }

        // Cell phone
        String cellPhone = "";
        boolean cellValid = false;
        while (!cellValid) {
            System.out.println("\n  CELL NUMBER: Must start with +27 (e.g., +27838968976)");
            System.out.print("  Enter cell phone: ");
            cellPhone = scan.nextLine();
            Registration tempReg = new Registration("", "", "a_1", "Temp@1234", cellPhone);
            if (tempReg.checkCellPhoneNumber()) {
                System.out.println("  [OK] Cell number successfully captured.");
                cellValid = true;
            } else {
                System.out.println("  [ERROR] Cell must start with +27 and have 9 digits after.");
            }
        }

        // Name
        System.out.print("\n  Enter first name: ");
        String firstName = scan.nextLine();
        System.out.print("  Enter last name: ");
        String lastName = scan.nextLine();

        Registration reg = new Registration(firstName, lastName, username, password, cellPhone);
        Message.setCurrentUser(firstName, lastName);
        
        System.out.println("\n  --------------------------------------------------");
        System.out.println("  " + reg.registerUser());
        System.out.println("  --------------------------------------------------");

        // ========== LOGIN ==========
        System.out.println("\n  ==================================================");
        System.out.println("   QUICKCHAT - LOGIN");
        System.out.println("  ==================================================");

        boolean loginSuccess = false;
        while (!loginSuccess) {
            System.out.print("\n  Username: ");
            String loginUsername = scan.nextLine();
            System.out.print("  Password: ");
            String loginPassword = scan.nextLine();
            Login login = new Login(loginUsername, loginPassword, reg);
            System.out.println();
            if (login.loginUser()) {
                System.out.println("  [OK] " + login.returnLoginStatus());
                loginSuccess = true;
            } else {
                System.out.println("  [ERROR] Username or password incorrect, please try again.");
            }
        }

        // ========== MESSAGE COUNT SETUP ==========
        System.out.println("\n  --------------------------------------------------");
        System.out.println("        Welcome to QuickChat.");
        System.out.println("  --------------------------------------------------");

        int totalMessagesAllowed = 0;
        boolean countValid = false;
        while (!countValid) {
            System.out.print("\n  How many messages do you wish to send this session? ");
            try {
                totalMessagesAllowed = Integer.parseInt(scan.nextLine().trim());
                if (totalMessagesAllowed > 0) {
                    countValid = true;
                    System.out.println("  [OK] You may send " + totalMessagesAllowed + " message(s).");
                } else {
                    System.out.println("  [ERROR] Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("  [ERROR] Please enter a valid number.");
            }
        }

        // Load stored messages from JSON
        Message.loadStoredMessagesFromFile();

        // ========== MAIN MENU ==========
        boolean running = true;
        int sentCount = 0;
        Message lastMsg = null;

        while (running) {
            System.out.println("\n  ==================================================");
            System.out.println("   QUICKCHAT - MAIN MENU");
            System.out.println("  ==================================================");
            System.out.println("\n   [1]  Send Messages");
            System.out.println("   [2]  Coming Soon");
            System.out.println("   [3]  Show All Sent Messages");
            System.out.println("   [4]  Stored Messages");
            System.out.println("   [5]  Quit");
            System.out.print("\n   Enter your choice: ");
            String menuInput = scan.nextLine().trim();

            switch (menuInput) {
                case "1": // SEND MESSAGES
                    while (sentCount < totalMessagesAllowed) {
                        System.out.println("\n  --------------------------------------------------");
                        System.out.println("   Message " + (sentCount + 1) + " of " + totalMessagesAllowed);
                        System.out.println("  --------------------------------------------------");

                        // Recipient
                        String recipient = "";
                        boolean recipOk = false;
                        while (!recipOk) {
                            System.out.print("   Enter recipient (+27...): ");
                            recipient = scan.nextLine().trim();
                            Message tempMsg = new Message(recipient, "test");
                            if (tempMsg.checkRecipientCell().equals("Cell phone number successfully captured.")) {
                                recipOk = true;
                            } else {
                                System.out.println("  [ERROR] " + tempMsg.checkRecipientCell());
                            }
                        }

                        // Message text
                        String msgText = "";
                        boolean msgOk = false;
                        while (!msgOk) {
                            System.out.print("   Enter message (max 250 chars): ");
                            msgText = scan.nextLine();
                            if (msgText.length() <= 250) {
                                msgOk = true;
                            } else {
                                System.out.println("  [ERROR] Message exceeds 250 characters by " + (msgText.length() - 250));
                            }
                        }

                        Message msg = new Message(recipient, msgText);
                        System.out.println("\n  --------------------------------------------------");
                        System.out.println("   Message ID   : " + msg.getMessageID());
                        System.out.println("   Message Hash : " + msg.getMessageHash());
                        System.out.println("  --------------------------------------------------");
                        System.out.println("\n   What would you like to do?");
                        System.out.println("   [1] Send");
                        System.out.println("   [2] Disregard");
                        System.out.println("   [3] Store");
                        System.out.print("\n   Choice: ");

                        int actionChoice = 1;
                        try {
                            actionChoice = Integer.parseInt(scan.nextLine().trim());
                            if (actionChoice < 1 || actionChoice > 3) actionChoice = 2;
                        } catch (NumberFormatException e) {
                            actionChoice = 2;
                        }

                        String result = msg.SentMessage(actionChoice);
                        System.out.println("\n  --------------------------------------------------");
                        
                        switch (actionChoice) {
                            case 1:
                                sentCount++;
                                lastMsg = msg;
                                System.out.println("   MESSAGE SENT");
                                System.out.println("  --------------------------------------------------");
                                System.out.println("   ID: " + msg.getMessageID());
                                System.out.println("   Hash: " + msg.getMessageHash());
                                System.out.println("   To: " + msg.getRecipientCell());
                                System.out.println("   Msg: " + msg.getMessage());
                                break;
                            case 2:
                                System.out.println("   MESSAGE DISREGARDED");
                                break;
                            case 3:
                                System.out.println("   MESSAGE STORED");
                                break;
                        }
                        System.out.println("  --------------------------------------------------");
                        System.out.println("  " + result);
                    }
                    
                    // Display total messages sent after all are processed
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("   Total messages sent this session: " + sentCount);
                    System.out.println("  --------------------------------------------------");
                    break;

                case "2": // COMING SOON (as per spec)
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("   Coming Soon.");
                    System.out.println("  --------------------------------------------------");
                    break;

                case "3": // ALL SENT MESSAGES
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("   ALL SENT MESSAGES");
                    System.out.println("  --------------------------------------------------");
                    if (lastMsg != null) {
                        System.out.println("\n  " + lastMsg.printMessages());
                    } else {
                        System.out.println("\n   No messages have been sent yet.");
                    }
                    System.out.println("\n  --------------------------------------------------");
                    break;

                case "4": // STORED MESSAGES MENU (Part 3)
                    runStoredMessagesMenu(scan);
                    break;

                case "5": // QUIT
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("   Thank you for using QuickChat. Goodbye!");
                    System.out.println("  --------------------------------------------------");
                    running = false;
                    break;

                default:
                    System.out.println("\n  [ERROR] Invalid option. Choose 1-5.");
                    break;
            }
        }
        scan.close();
    }

    // ========== STORED MESSAGES SUB-MENU (PART 3) ==========
    private static void runStoredMessagesMenu(Scanner scan) {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ==================================================");
            System.out.println("   STORED MESSAGES");
            System.out.println("  ==================================================");
            System.out.println("\n   [a] Display sender and recipient of all stored messages");
            System.out.println("   [b] Display the longest stored message");
            System.out.println("   [c] Search for a message ID");
            System.out.println("   [d] Search all messages for a particular recipient");
            System.out.println("   [e] Delete a message using the message hash");
            System.out.println("   [f] Display a report of all sent messages");
            System.out.println("   [0] Back to main menu");
            System.out.print("\n   Choice: ");
            String choice = scan.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("   STORED MESSAGES - SENDER & RECIPIENT");
                    System.out.println("  --------------------------------------------------");
                    System.out.println("\n  " + Message.displayStoredMessagesWithSenderAndRecipient());
                    System.out.println("\n  --------------------------------------------------");
                    break;

                case "b":
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("   LONGEST MESSAGE");
                    System.out.println("  --------------------------------------------------");
                    System.out.println("\n  " + Message.getLongestMessage());
                    System.out.println("\n  --------------------------------------------------");
                    break;

                case "c":
                    System.out.print("\n  Enter Message ID: ");
                    String searchID = scan.nextLine().trim();
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("  " + Message.searchByMessageID(searchID));
                    System.out.println("  --------------------------------------------------");
                    break;

                case "d":
                    System.out.print("\n  Enter recipient cell number: ");
                    String recipient = scan.nextLine().trim();
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("  " + Message.searchByRecipient(recipient));
                    System.out.println("  --------------------------------------------------");
                    break;

                case "e":
                    System.out.print("\n  Enter message hash: ");
                    String hash = scan.nextLine().trim().toUpperCase();
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println("  " + Message.deleteMessageByHash(hash));
                    System.out.println("  --------------------------------------------------");
                    break;

                case "f":
                    System.out.println("\n  --------------------------------------------------");
                    System.out.println(Message.displayReport());
                    System.out.println("  --------------------------------------------------");
                    break;

                case "0":
                    back = true;
                    break;

                default:
                    System.out.println("\n  [ERROR] Invalid option. Choose a, b, c, d, e, f, or 0.");
                    break;
            }
        }
    }
}