import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group chat
 */
public class GroupChat {
    private String groupName;
    private List<User> members;
    private List<Message> messages;
    private List<String> offlineMentions; // Format: "username|message"
    
    /**
     * Constructor for GroupChat
     * @param groupName The name of the group
     */
    public GroupChat(String groupName) {
        this.groupName = groupName;
        this.members = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.offlineMentions = new ArrayList<>();
    }
    
    /**
     * Add a member to the group
     * @param user The user to add
     */
    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }
    
    /**
     * Add a message to the chat
     * @param message The message to add
     */
    public void addMessage(Message message) {
        messages.add(message);
        
        // Check if message has mention and store if user is offline
        if (message.hasMention()) {
            String mentionedUser = message.getMentionedUser();
            boolean userOnline = false;
            
            for (User member : members) {
                if (member.getUsername().equals(mentionedUser) && member.isOnline()) {
                    userOnline = true;
                    break;
                }
            }
            
            if (!userOnline) {
                offlineMentions.add(mentionedUser + "|" + message.formatMessage());
            }
        }
    }
    
    /**
     * Get mentions for a specific user
     * @param username The username to check
     * @return List of messages where user was mentioned
     */
    public List<String> getMentionsForUser(String username) {
        List<String> userMentions = new ArrayList<>();
        for (String mention : offlineMentions) {
            String[] parts = mention.split("\\|", 2);
            if (parts.length == 2 && parts[0].equals(username)) {
                userMentions.add(parts[1]);
            }
        }
        return userMentions;
    }
    
    /**
     * Clear mentions for a user (after they've been notified)
     * @param username The username to clear mentions for
     */
    public void clearMentionsForUser(String username) {
        offlineMentions.removeIf(mention -> mention.startsWith(username + "|"));
    }
    
    // Getters
    public String getGroupName() {
        return groupName;
    }
    
    public List<User> getMembers() {
        return members;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public List<String> getOfflineMentions() {
        return offlineMentions;
    }
    
    /**
     * Get recent messages (last N messages)
     * @param count Number of messages to retrieve
     * @return List of recent messages
     */
    public List<Message> getRecentMessages(int count) {
        int size = messages.size();
        if (size <= count) {
            return new ArrayList<>(messages);
        }
        return new ArrayList<>(messages.subList(size - count, size));
    }
}
