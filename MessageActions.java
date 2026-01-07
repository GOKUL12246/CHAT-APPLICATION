/**
 * Interface for handling message operations
 */
public interface MessageActions {
    /**
     * Send a message to the group chat
     * @param message The message object to send
     */
    void sendMessage(Message message);
    
    /**
     * Receive a message from the group chat
     * @param message The message object received
     */
    void receiveMessage(Message message);
}
