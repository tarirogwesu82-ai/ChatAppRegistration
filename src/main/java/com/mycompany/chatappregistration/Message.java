package com.mycompany.chatappregistration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
@SuppressWarnings({"FieldMayBeFinal", "unused", "ConvertToTryWithResources"})
public class Message {

    private final String messageID;
    private final String recipientCell;
    private final String message;
    private String messageHash;
    private static String currentUser = "";

    // Five arrays required by Part 3
    private static final ArrayList<String> sentMessages = new ArrayList<>();
    private static final ArrayList<String> disregardedMessages = new ArrayList<>();
    private static final ArrayList<String> storedMessages = new ArrayList<>();
    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIDs = new ArrayList<>();

    // Full details for reports
    private static final ArrayList<String> sentFullDetails = new ArrayList<>();
    private static final ArrayList<String> storedFullDetails = new ArrayList<>();

    private static int numMessagesSent = 0;

    public Message(String recipientCell, String message) {
        this.recipientCell = recipientCell;
        this.message = message;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash(numMessagesSent + 1);
    }

    public static void setCurrentUser(String firstName, String lastName) {
        currentUser = firstName + " " + lastName;
    }

    private String generateMessageID() {
        Random random = new Random();
        long id = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipientCell == null || !recipientCell.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number is incorrectly formatted or does not contain an "
                 + "international code. Please correct the number and try again.";
        }
        return "Cell phone number successfully captured.";
    }

    public String createMessageHash(int messageNumber) {
        String idPrefix = messageID.substring(0, 2);
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        return (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                numMessagesSent++;
                sentMessages.add(message);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                sentFullDetails.add(buildFullEntry());
                return "Message successfully sent.";
            case 2:
                disregardedMessages.add(message);
                return "Press 0 to delete the message.";
            case 3:
                return storeMessage();
            default:
                return "Invalid option. Please choose 1, 2, or 3.";
        }
    }

    public String storeMessage() {
        String json = "{\"messageID\": \"" + messageID
                    + "\", \"messageHash\": \"" + messageHash
                    + "\", \"recipient\": \"" + recipientCell
                    + "\", \"message\": \"" + message + "\"}";

        try (FileWriter fw = new FileWriter("messages.json", true)) {
            fw.write(json + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Could not save message to file.");
        }

        storedMessages.add(message);
        messageHashes.add(messageHash);
        messageIDs.add(messageID);
        storedFullDetails.add(buildFullEntry());
        return "Message successfully stored.";
    }

    public static void loadStoredMessagesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String msgID = extractJsonValue(line, "messageID");
                String hash = extractJsonValue(line, "messageHash");
                String recip = extractJsonValue(line, "recipient");
                String msgText = extractJsonValue(line, "message");

                if (!storedMessages.contains(msgText)) {
                    storedMessages.add(msgText);
                    if (!messageHashes.contains(hash)) messageHashes.add(hash);
                    if (!messageIDs.contains(msgID)) messageIDs.add(msgID);
                    storedFullDetails.add("Sender: " + currentUser + " | Recipient: " + recip + " | Message: " + msgText);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet
        }
    }

    private static String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\": \"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private String buildFullEntry() {
        return "Sender: " + currentUser + " | Recipient: " + recipientCell + " | Message: " + message;
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) return "No messages have been sent yet.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append("\"").append(sentMessages.get(i)).append("\"");
            if (i < sentMessages.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public static String getLongestMessage() {
        String longest = "";
        for (String m : sentMessages) if (m.length() > longest.length()) longest = m;
        for (String m : storedMessages) if (m.length() > longest.length()) longest = m;
        for (String m : disregardedMessages) if (m.length() > longest.length()) longest = m;
        if (longest.isEmpty()) return "No messages have been entered yet.";
        return longest;
    }

    // FIXED: Search by Message ID
    public static String searchByMessageID(String searchID) {
        // Search in sent messages
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(searchID)) {
                if (i < sentFullDetails.size()) {
                    return sentFullDetails.get(i);
                }
            }
        }
        // Search in stored messages
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(searchID)) {
                if (i < storedFullDetails.size()) {
                    return storedFullDetails.get(i);
                }
            }
        }
        return "No message found with ID: " + searchID;
    }

    public static String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (String entry : sentFullDetails) {
            if (entry.contains("Recipient: " + recipient)) {
                sb.append(extractMessageFromEntry(entry)).append("\n");
            }
        }
        for (String entry : storedFullDetails) {
            if (entry.contains("Recipient: " + recipient)) {
                sb.append(extractMessageFromEntry(entry)).append("\n");
            }
        }
        if (sb.length() == 0) return "No messages found for recipient: " + recipient;
        return sb.toString().trim();
    }

    // FIXED: Delete by Message Hash
    public static String deleteMessageByHash(String hash) {
        // Search in stored messages
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String msgText = "";
                if (i < storedMessages.size()) {
                    msgText = storedMessages.get(i);
                    storedMessages.remove(i);
                }
                messageHashes.remove(i);
                if (i < messageIDs.size()) messageIDs.remove(i);
                if (i < storedFullDetails.size()) storedFullDetails.remove(i);
                return "Message: \"" + msgText + "\" successfully deleted.";
            }
        }
        // Search in sent messages
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String msgText = "";
                if (i < sentMessages.size()) {
                    msgText = sentMessages.get(i);
                    sentMessages.remove(i);
                }
                messageHashes.remove(i);
                if (i < messageIDs.size()) messageIDs.remove(i);
                if (i < sentFullDetails.size()) sentFullDetails.remove(i);
                return "Message: \"" + msgText + "\" successfully deleted.";
            }
        }
        return "No message found with hash: " + hash;
    }

    public static String displayReport() {
        if (sentFullDetails.isEmpty()) return "No messages have been sent yet.";
        StringBuilder sb = new StringBuilder("=== Sent Messages Report ===\n\n");
        for (int i = 0; i < sentFullDetails.size(); i++) {
            sb.append("Message ").append(i + 1).append(":\n");
            sb.append("  ").append(sentFullDetails.get(i)).append("\n\n");
        }
        return sb.toString();
    }

    public static String displayStoredMessagesWithSenderAndRecipient() {
        if (storedFullDetails.isEmpty()) return "No stored messages found.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedFullDetails.size(); i++) {
            sb.append("Message ").append(i + 1).append(": ").append(storedFullDetails.get(i)).append("\n");
        }
        return sb.toString();
    }

    private static String extractMessageFromEntry(String entry) {
        int msgIndex = entry.indexOf("Message: ");
        if (msgIndex == -1) return entry;
        return entry.substring(msgIndex + 9);
    }

    // Getters for unit tests
    public static ArrayList<String> getSentMessages() { return sentMessages; }
    public static ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<String> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getMessageHashes() { return messageHashes; }
    public static ArrayList<String> getMessageIDs() { return messageIDs; }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessage() { return message; }
    public int returnTotalMessages() { return numMessagesSent; }

    public static void resetForTesting() {
        numMessagesSent = 0;
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        sentFullDetails.clear();
        storedFullDetails.clear();
        currentUser = "";
    }
}