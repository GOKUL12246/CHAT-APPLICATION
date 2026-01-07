import java.util.Date;

/**
 * Represents a user in the chat application
 */
public class User {
    private String username;
    private String password;
    private boolean isOnline;
    private Date lastSeen;
    
    /**
     * Constructor for User
     * @param username The username
     * @param password The password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isOnline = false;
        this.lastSeen = new Date();
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isOnline() {
        return isOnline;
    }
    
    public void setOnline(boolean online) {
        this.isOnline = online;
        if (!online) {
            this.lastSeen = new Date();
        }
    }
    
    public Date getLastSeen() {
        return lastSeen;
    }
    
    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }
    
    @Override
    public String toString() {
        return username + (isOnline ? " (Online)" : " (Offline)");
    }
}
