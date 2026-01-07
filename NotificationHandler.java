/**
 * Interface for handling notifications and alerts
 */
public interface NotificationHandler {
    /**
     * Show an alert dialog to the user
     * @param title The title of the alert
     * @param message The message content
     */
    void showAlert(String title, String message);
    
    /**
     * Summarize unread messages for the user
     * @param username The username to summarize messages for
     * @return Summary of unread messages
     */
    String summarizeMessages(String username);
}
