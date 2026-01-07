/**
 * Main application entry point
 * Launches the chat application starting with the login screen
 */
public class ChatApp {
    public static void main(String[] args) {
        // Create and display login UI
        LoginUI loginUI = new LoginUI();
        loginUI.setVisible(true);
    }
}
