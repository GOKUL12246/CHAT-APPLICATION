import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages file operations for users, chat history, and mentions
 */
public class FileManager {
    private static final String USERS_FILE = "users.txt";
    private static final String CHAT_HISTORY_FILE = "chat_history.txt";
    private static final String MENTIONS_FILE = "mentions.txt";
    private static final String LAST_SEEN_FILE = "last_seen.txt";
    
    /**
     * Register a new user
     * @param username The username
     * @param password The password
     * @throws IOException If file operation fails
     * @throws IllegalArgumentException If user already exists
     */
    public static void registerUser(String username, String password) throws IOException {
        // Check if user already exists
        if (userExists(username)) {
            throw new IllegalArgumentException("User already exists!");
        }
        
        // Append user to file
        try (FileWriter fw = new FileWriter(USERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "|" + password);
        }
    }
    
    /**
     * Validate user credentials
     * @param username The username
     * @param password The password
     * @return true if credentials are valid
     * @throws IOException If file operation fails
     */
    public static boolean validateUser(String username, String password) throws IOException {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return false;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Check if a user exists
     * @param username The username to check
     * @return true if user exists
     */
    public static boolean userExists(String username) {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return false;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
    
    /**
     * Save a message to chat history
     * @param message The message to save
     * @throws IOException If file operation fails
     */
    public static void saveMessage(Message message) throws IOException {
        try (FileWriter fw = new FileWriter(CHAT_HISTORY_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(message.toFileFormat());
        }
    }
    
    /**
     * Load all chat messages from history
     * @return List of messages
     */
    public static List<Message> loadChatHistory() {
        List<Message> messages = new ArrayList<>();
        File file = new File(CHAT_HISTORY_FILE);
        
        if (!file.exists()) {
            return messages;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(CHAT_HISTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Message msg = Message.fromFileFormat(line);
                if (msg != null) {
                    messages.add(msg);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading chat history: " + e.getMessage());
        }
        
        return messages;
    }
    
    /**
     * Save a mention for offline user
     * @param username The mentioned username
     * @param messageText The message containing the mention
     * @throws IOException If file operation fails
     */
    public static void saveMention(String username, String messageText) throws IOException {
        try (FileWriter fw = new FileWriter(MENTIONS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "|" + messageText);
        }
    }
    
    /**
     * Load mentions for a specific user
     * @param username The username to load mentions for
     * @return List of mentions
     */
    public static List<String> loadMentionsForUser(String username) {
        List<String> mentions = new ArrayList<>();
        File file = new File(MENTIONS_FILE);
        
        if (!file.exists()) {
            return mentions;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(MENTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2 && parts[0].equals(username)) {
                    mentions.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading mentions: " + e.getMessage());
        }
        
        return mentions;
    }
    
    /**
     * Clear mentions for a user after they've been notified
     * @param username The username to clear mentions for
     */
    public static void clearMentionsForUser(String username) {
        File file = new File(MENTIONS_FILE);
        if (!file.exists()) {
            return;
        }
        
        List<String> remainingMentions = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(MENTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length >= 1 && !parts[0].equals(username)) {
                    remainingMentions.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading mentions: " + e.getMessage());
            return;
        }
        
        // Rewrite file without the cleared mentions
        try (PrintWriter out = new PrintWriter(new FileWriter(MENTIONS_FILE))) {
            for (String mention : remainingMentions) {
                out.println(mention);
            }
        } catch (IOException e) {
            System.err.println("Error writing mentions: " + e.getMessage());
        }
    }
    
    /**
     * Save user's last seen timestamp
     * @param username The username
     * @param timestamp The timestamp in milliseconds
     * @throws IOException If file operation fails
     */
    public static void saveLastSeen(String username, long timestamp) throws IOException {
        File file = new File(LAST_SEEN_FILE);
        List<String> lines = new ArrayList<>();
        boolean userFound = false;
        
        // Read existing data
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(LAST_SEEN_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 1 && parts[0].equals(username)) {
                        lines.add(username + "|" + timestamp);
                        userFound = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
        }
        
        // Add new entry if user not found
        if (!userFound) {
            lines.add(username + "|" + timestamp);
        }
        
        // Write back to file
        try (PrintWriter out = new PrintWriter(new FileWriter(LAST_SEEN_FILE))) {
            for (String line : lines) {
                out.println(line);
            }
        }
    }
    
    /**
     * Load user's last seen timestamp
     * @param username The username
     * @return Last seen timestamp in milliseconds, or 0 if not found
     */
    public static long loadLastSeen(String username) {
        File file = new File(LAST_SEEN_FILE);
        if (!file.exists()) {
            return 0;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(LAST_SEEN_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2 && parts[0].equals(username)) {
                    return Long.parseLong(parts[1]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading last seen: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Get list of all registered users
     * @return List of usernames
     */
    public static List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        
        if (!file.exists()) {
            return users;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 1) {
                    users.add(parts[0]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        
        return users;
    }
}
