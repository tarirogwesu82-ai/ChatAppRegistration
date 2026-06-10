package com.mycompany.chatappregistration;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MessageTest – Unit tests for the Message class (Parts 1, 2 and 3).
 *
 * Part 3 test data (from assignment specification image 3):
 *   Msg 1 – Recipient: +27834557896, "Did you get the cake?"                           → Sent
 *   Msg 2 – Recipient: +27838884567, "Where are you? You are late! I have asked you to be on time." → Stored
 *   Msg 3 – Recipient: +27834484567, "Yohoooo, I am at your gate."                     → Disregard
 *   Msg 4 – Recipient: 0838884567 (developer/invalid), "It is dinner time !"           → Sent
 *   Msg 5 – Recipient: +27838884567, "Ok, I am leaving without you."                   → Stored
 *
 * JUnit 4 testing framework:
 * JUnit. 2024. JUnit 4 Documentation.
 * [Online]. Available at: https://junit.org/junit4/  [Accessed: 09 June 2026]
 *
 * assertEquals, assertTrue, assertFalse methods:
 * Oracle. 2024. Java SE 17 Documentation: Assert.
 * [Online]. Available at: https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
 * [Accessed: 09 June 2026]
 *
 * Author: ST10498762
 * Student Number: ST10498762
 * Date: 09 June 2026
 */
public class MessageTest {

    // ── Test fixtures ────────────────────────────────────────────────────────
    private Message msg1, msg2, msg3, msg4, msg5;

    @Before
    public void setUp() {
        // Reset all static arrays before every test
        Message.resetForTesting();

        // Create all five test-data messages exactly as specified
        msg1 = new Message("+27834557896", "Did you get the cake?");
        msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        msg4 = new Message("0838884567",   "It is dinner time !");
        msg5 = new Message("+27838884567", "Ok, I am leaving without you.");
    }

    // =========================================================================
    // EXISTING TESTS – Parts 1 & 2 (preserved exactly)
    // =========================================================================

    @Test
    public void testMessageWithin250Chars() {
        String text = "Hi Mike, can you join us for dinner tonight?";
        boolean isValid = text.length() <= 250;
        assertTrue(isValid);
    }

    @Test
    public void testMessageExceeds250Chars() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 260; i++) longText.append("A");
        boolean isTooLong = longText.length() > 250;
        assertTrue(isTooLong);
    }

    @Test
    public void testRecipientCellCorrect() {
        // msg1 has a valid cell number
        assertEquals("Cell phone number successfully captured.", msg1.checkRecipientCell());
    }

    @Test
    public void testRecipientCellIncorrect() {
        // msg4 has no international code – should fail
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an "
          + "international code. Please correct the number and try again.",
            msg4.checkRecipientCell()
        );
    }

    @Test
    public void testMessageHashFormat() {
        String hash = msg1.getMessageHash();
        assertNotNull(hash);
        assertTrue(hash.contains(":"));
    }

    @Test
    public void testMessageHashIsUpperCase() {
        String hash = msg1.getMessageHash();
        assertEquals(hash.toUpperCase(), hash);
    }

    @Test
    public void testCheckMessageID() {
        assertTrue(msg1.checkMessageID());
    }

    @Test
    public void testSendMessage() {
        assertEquals("Message successfully sent.", msg1.SentMessage(1));
    }

    @Test
    public void testDisregardMessage() {
        assertEquals("Press 0 to delete the message.", msg3.SentMessage(2));
    }

    @Test
    public void testStoreMessage() {
        assertEquals("Message successfully stored.", msg2.SentMessage(3));
    }

    @Test
    public void testReturnTotalMessages() {
        msg1.SentMessage(1);
        msg4.SentMessage(1);
        assertEquals(2, msg4.returnTotalMessages());
    }

    @Test
    public void testDisregardDoesNotIncreaseCount() {
        msg3.SentMessage(2);
        assertEquals(0, msg3.returnTotalMessages());
    }

    @Test
    public void testStoreDoesNotIncreaseCount() {
        msg2.SentMessage(3);
        assertEquals(0, msg2.returnTotalMessages());
    }

    // =========================================================================
    // PART 3 – assertEquals TESTS  (per assignment image 1)
    // =========================================================================

    /**
     * Test: Sent Messages array correctly populated.
     *
     * Developer populates using test data msgs 1-4:
     *   msg1 → Sent, msg2 → Stored, msg3 → Disregard, msg4 → Sent.
     *
     * The system returns the messages in the sentMessages array:
     *   "Did you get the cake?", "It is dinner time !"
     */
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        msg1.SentMessage(1);   // Sent
        msg2.SentMessage(3);   // Stored
        msg3.SentMessage(2);   // Disregard
        msg4.SentMessage(1);   // Sent

        // The printMessages method returns the texts of all sent messages
        String result = msg4.printMessages();

        assertTrue("Sent array must contain msg1", result.contains("Did you get the cake?"));
        assertTrue("Sent array must contain msg4", result.contains("It is dinner time !"));
        assertFalse("Sent array must NOT contain msg2 (stored)", result.contains("Where are you?"));
        assertFalse("Sent array must NOT contain msg3 (disregarded)", result.contains("Yohoooo"));
    }

    /**
     * Test: Display the longest Message.
     *
     * Test data messages 1-4 are processed (any action).
     * The system returns the longest message across ALL arrays:
     *   "Where are you? You are late! I have asked you to be on time."
     */
    @Test
    public void testDisplayLongestMessage() {
        msg1.SentMessage(1);   // Sent
        msg2.SentMessage(3);   // Stored  ← this is the longest
        msg3.SentMessage(2);   // Disregard
        msg4.SentMessage(1);   // Sent

        String longest = Message.getLongestMessage();
        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            longest
        );
    }

    /**
     * Test: Search for messageID.
     *
     * Test data: message 4 – the developer entry whose recipient is "0838884567".
     * Search using msg4's generated messageID.
     * The system returns: "It is dinner time !"
     */
    @Test
    public void testSearchByMessageID() {
        msg1.SentMessage(1);
        msg2.SentMessage(3);
        msg3.SentMessage(2);
        msg4.SentMessage(1);

        String id = msg4.getMessageID();
        String result = Message.searchByMessageID(id);

        assertEquals("It is dinner time !", result);
    }

    /**
     * Test: Search all messages sent or stored regarding a particular recipient.
     *
     * Test data: +27838884567 (recipient of msg2 stored and msg5 stored).
     * The system returns:
     *   "Where are you? You are late! I have asked you to be on time."
     *   "Ok, I am leaving without you."
     */
    @Test
    public void testSearchByRecipient() {
        msg1.SentMessage(1);   // sent  – different recipient
        msg2.SentMessage(3);   // stored – +27838884567
        msg3.SentMessage(2);   // disregarded
        msg4.SentMessage(1);   // sent  – invalid cell
        msg5.SentMessage(3);   // stored – +27838884567

        String result = Message.searchByRecipient("+27838884567");

        assertTrue("Must contain msg2 text",
            result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue("Must contain msg5 text",
            result.contains("Ok, I am leaving without you."));
    }

    /**
     * Test: Delete a message using a message hash.
     *
     * Test data: Test Message 2 – msg2 (stored).
     * The system returns:
     *   Message: "Where are you? You are late! I have asked you to be on time."
     *   successfully deleted.
     */
    @Test
    public void testDeleteMessageByHash() {
        msg2.SentMessage(3);   // store msg2

        String hash   = msg2.getMessageHash();
        String result = Message.deleteMessageByHash(hash);

        assertEquals(
            "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
            result
        );
    }

    /**
     * Test: Display Report.
     *
     * The system returns a report that shows all the sent messages, including:
     *   Message Hash, Recipient, Message.
     *
     * Send msg1 and msg4 (the two Sent messages in the test data).
     */
    @Test
    public void testDisplayReport() {
        msg1.SentMessage(1);   // Sent
        msg4.SentMessage(1);   // Sent

        String report = Message.displayReport();

        // Report must contain the required column headings
        assertTrue("Report must include 'Message Hash'",  report.contains("Message Hash"));
        assertTrue("Report must include 'Recipient'",     report.contains("Recipient"));
        assertTrue("Report must include 'Message'",       report.contains("Message"));

        // Report must contain the sent messages' content
        assertTrue("Report must contain msg1 text",       report.contains("Did you get the cake?"));
        assertTrue("Report must contain msg4 text",       report.contains("It is dinner time !"));
    }

    // =========================================================================
    // PART 3 – Array integrity tests (assertTrue / assertFalse)
    // =========================================================================

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        msg3.SentMessage(2);
        assertTrue(Message.getDisregardedMessages().contains("Yohoooo, I am at your gate."));
    }

    @Test
    public void testStoredMessagesArrayPopulated() {
        msg2.SentMessage(3);
        assertTrue(Message.getStoredMessages().contains(
            "Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testMessageHashesArrayPopulated() {
        msg1.SentMessage(1);
        assertFalse(Message.getMessageHashes().isEmpty());
        assertTrue(Message.getMessageHashes().contains(msg1.getMessageHash()));
    }

    @Test
    public void testMessageIDsArrayPopulated() {
        msg1.SentMessage(1);
        assertTrue(Message.getMessageIDs().contains(msg1.getMessageID()));
    }

    @Test
    public void testDeleteRemovesMessageFromStoredArray() {
        msg2.SentMessage(3);
        Message.deleteMessageByHash(msg2.getMessageHash());
        assertFalse(Message.getStoredMessages().contains(
            "Where are you? You are late! I have asked you to be on time."));
    }
}