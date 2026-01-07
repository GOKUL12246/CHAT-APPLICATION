import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Represents a chat message
 */
public class Message {
    private String sender;
    private String content;
    private Date timestamp;
    private boolean hasMention;
    private String mentionedUser;
    
    /**
     * Constructor for Message
     * @param sender The username of the sender
     * @param content The message content
     */
    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = new Date();
        this.hasMention = false;
        checkForMention();
    }
    
    /**
     * Check if the message contains a mention (@username)
     */
    private void checkForMention() {
        if (content.contains("@")) {
            hasMention = true;
            // Extract mentioned username
            String[] words = content.split("\\s+");
            for (String word : words) {
                if (word.startsWith("@") && word.length() > 1) {
                    mentionedUser = word.substring(1);
                    break;
                }
            }
        }
    }
    
    // Getters
    public String getSender() {
        return sender;
    }
    
    public String getContent() {
        return content;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public boolean hasMention() {
        return hasMention;
    }
    
    public String getMentionedUser() {
        return mentionedUser;
    }
    
    /**
     * Format the message for display
     * @return Formatted message string
     */
    public String formatMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return "[" + sdf.format(timestamp) + "] " + sender + ": " + content;
    }
    
    /**
     * Format the message for file storage
     * @return Pipe-delimited message string
     */
    public String toFileFormat() {
        return timestamp.getTime() + "|" + sender + "|" + content;
    }
    
    /**
     * Create a Message from file format
     * @param fileString The pipe-delimited string
     * @return Message object
     */
    public static Message fromFileFormat(String fileString) {
        String[] parts = fileString.split("\\|", 3);
        if (parts.length == 3) {
            Message msg = new Message(parts[1], parts[2]);
            msg.timestamp = new Date(Long.parseLong(parts[0]));
            return msg;
        }
        return null;
    }
}
