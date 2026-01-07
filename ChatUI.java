import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Chat UI implementing MessageActions and NotificationHandler interfaces
 */
public class ChatUI extends Frame implements MessageActions, NotificationHandler {
    private String currentUsername;
    private GroupChat groupChat;
    private long userLastSeen;
    
    // UI Components
    private TextArea chatArea;
    private TextField messageField;
    private Button sendButton;
    private java.awt.List membersList;
    private Label topBarLabel;
    private Label statusLabel;
    private Button onlineButton;
    private Button offlineButton;
    private Button summarizeButton;
    private Button logoutButton;
    private Button fullscreenButton;
    private boolean isFullscreen = false;
    
    /**
     * Constructor for ChatUI
     * @param username The logged-in username
     */
    public ChatUI(String username) {
        super("Group Chat - " + username);
        this.currentUsername = username;
        this.groupChat = new GroupChat("Dev Team Chat");
        this.userLastSeen = FileManager.loadLastSeen(username);
        
        setupUI();
        setupEventHandlers();
        loadChatData();
        checkNotifications();
        
        // Window closing event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleLogout();
            }
        });
    }
    
    /**
     * Setup the UI components
     */
    private void setupUI() {
        setLayout(new BorderLayout(0, 0));
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setBackground(new Color(240, 242, 245));
        
        // Top bar panel with professional gradient-like design
        Panel topPanel = new Panel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(25, 118, 210)); // Professional blue
        topPanel.setPreferredSize(new Dimension(1000, 60));
        
        topBarLabel = new Label("  💬 Dev Team Chat", Label.LEFT);
        topBarLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topBarLabel.setForeground(Color.WHITE);
        
        // Status panel with buttons
        Panel statusPanel = new Panel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 15));
        statusPanel.setBackground(new Color(25, 118, 210));
        
        onlineButton = new Button("🟢 Online");
        onlineButton.setFont(new Font("Arial", Font.BOLD, 11));
        onlineButton.setBackground(new Color(76, 175, 80)); // Material green
        onlineButton.setForeground(Color.WHITE);
        onlineButton.setPreferredSize(new Dimension(95, 35));
        
        offlineButton = new Button("⚫ Offline");
        offlineButton.setFont(new Font("Arial", Font.BOLD, 11));
        offlineButton.setBackground(new Color(158, 158, 158)); // Material gray
        offlineButton.setForeground(Color.WHITE);
        offlineButton.setPreferredSize(new Dimension(95, 35));
        
        summarizeButton = new Button("📊 Summary");
        summarizeButton.setFont(new Font("Arial", Font.BOLD, 11));
        summarizeButton.setBackground(new Color(255, 152, 0)); // Material orange
        summarizeButton.setForeground(Color.WHITE);
        summarizeButton.setPreferredSize(new Dimension(105, 35));
        
        logoutButton = new Button("🚪 Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 11));
        logoutButton.setBackground(new Color(244, 67, 54)); // Material red
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(90, 35));
        
        fullscreenButton = new Button("⛶ Full");
        fullscreenButton.setFont(new Font("Arial", Font.BOLD, 11));
        fullscreenButton.setBackground(new Color(103, 58, 183)); // Material purple
        fullscreenButton.setForeground(Color.WHITE);
        fullscreenButton.setPreferredSize(new Dimension(80, 35));
        
        statusLabel = new Label("Status: Online  ", Label.RIGHT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        
        statusPanel.add(statusLabel);
        statusPanel.add(onlineButton);
        statusPanel.add(offlineButton);
        statusPanel.add(summarizeButton);
        statusPanel.add(fullscreenButton);
        statusPanel.add(logoutButton);
        
        topPanel.add(topBarLabel, BorderLayout.WEST);
        topPanel.add(statusPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - split into chat area and members list
        Panel centerPanel = new Panel();
        centerPanel.setLayout(new BorderLayout(5, 5));
        
        // Chat area (left side) with modern styling
        Panel chatPanel = new Panel();
        chatPanel.setLayout(new BorderLayout(0, 0));
        
        Label chatLabel = new Label(" 💬 Messages", Label.LEFT);
        chatLabel.setFont(new Font("Arial", Font.BOLD, 13));
        chatLabel.setBackground(new Color(236, 239, 241));
        chatLabel.setForeground(new Color(60, 60, 60));
        
        Panel chatLabelPanel = new Panel();
        chatLabelPanel.setLayout(new BorderLayout());
        chatLabelPanel.setBackground(new Color(236, 239, 241));
        chatLabelPanel.add(chatLabel, BorderLayout.WEST);
        
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        chatArea.setBackground(new Color(255, 255, 255)); // Clean white background
        
        chatPanel.add(chatLabelPanel, BorderLayout.NORTH);
        chatPanel.add(chatArea, BorderLayout.CENTER);
        
        // Members list (right side) with professional styling
        Panel membersPanel = new Panel();
        membersPanel.setLayout(new BorderLayout(0, 0));
        membersPanel.setPreferredSize(new Dimension(220, 0));
        membersPanel.setBackground(new Color(250, 250, 250));
        
        Label membersLabel = new Label(" 👥 Members", Label.LEFT);
        membersLabel.setFont(new Font("Arial", Font.BOLD, 13));
        membersLabel.setBackground(new Color(236, 239, 241));
        membersLabel.setForeground(new Color(60, 60, 60));
        
        Panel membersLabelPanel = new Panel();
        membersLabelPanel.setLayout(new BorderLayout());
        membersLabelPanel.setBackground(new Color(236, 239, 241));
        membersLabelPanel.add(membersLabel, BorderLayout.WEST);
        
        membersList = new java.awt.List();
        membersList.setFont(new Font("SansSerif", Font.PLAIN, 13));
        membersList.setBackground(new Color(250, 250, 250));
        
        membersPanel.add(membersLabelPanel, BorderLayout.NORTH);
        membersPanel.add(membersList, BorderLayout.CENTER);
        
        centerPanel.add(chatPanel, BorderLayout.CENTER);
        centerPanel.add(membersPanel, BorderLayout.EAST);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel - message input
        Panel bottomPanel = new Panel();
        bottomPanel.setLayout(new BorderLayout(10, 10));
        bottomPanel.setBackground(Color.WHITE);
        
        // Add some padding
        Panel paddedBottom = new Panel();
        paddedBottom.setLayout(new BorderLayout(10, 10));
        paddedBottom.setBackground(Color.WHITE);
        
        messageField = new TextField();
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageField.setBackground(Color.WHITE);
        
        sendButton = new Button("📤 Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 13));
        sendButton.setBackground(new Color(33, 150, 243)); // Material blue
        sendButton.setForeground(Color.WHITE);
        sendButton.setPreferredSize(new Dimension(100, 40));
        
        Panel inputPanel = new Panel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBackground(Color.WHITE);
        
        // Create panels for padding
        Panel leftPad = new Panel();
        leftPad.setBackground(Color.WHITE);
        leftPad.setPreferredSize(new Dimension(10, 0));
        
        Panel rightPad = new Panel();
        rightPad.setBackground(Color.WHITE);
        rightPad.setPreferredSize(new Dimension(10, 0));
        
        Panel bottomPad = new Panel();
        bottomPad.setBackground(Color.WHITE);
        bottomPad.setPreferredSize(new Dimension(0, 10));
        
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        paddedBottom.add(leftPad, BorderLayout.WEST);
        paddedBottom.add(rightPad, BorderLayout.EAST);
        paddedBottom.add(bottomPad, BorderLayout.SOUTH);
        paddedBottom.add(inputPanel, BorderLayout.CENTER);
        
        add(paddedBottom, BorderLayout.SOUTH);
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Online button action
        onlineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setUserOnline(true);
            }
        });
        
        // Offline button action
        offlineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setUserOnline(false);
            }
        });
        
        // Summarize button action
        summarizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showChatSummary();
            }
        });
        
        // Logout button action
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogoutConfirmation();
            }
        });
        
        // Fullscreen button action
        fullscreenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleFullscreen();
            }
        });
        
        // Send button action
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessageHandler();
            }
        });
        
        // Enter key on message field
        messageField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessageHandler();
                }
            }
        });
    }
    
    /**
     * Load chat data (history, members, etc.)
     */
    private void loadChatData() {
        // Load chat history
        java.util.List<Message> messages = FileManager.loadChatHistory();
        for (Message msg : messages) {
            groupChat.addMessage(msg);
            chatArea.append(msg.formatMessage() + "\n");
        }
        
        // Auto-scroll to bottom
        chatArea.setCaretPosition(chatArea.getText().length());
        
        // Load all users as members
        java.util.List<String> allUsers = FileManager.getAllUsers();
        for (String user : allUsers) {
            User member = new User(user, "");
            // Set current user as online
            if (user.equals(currentUsername)) {
                member.setOnline(true);
            }
            groupChat.addMember(member);
        }
        
        // Update members list display
        updateMembersList();
    }
    
    /**
     * Update the members list display
     */
    private void updateMembersList() {
        membersList.removeAll();
        for (User member : groupChat.getMembers()) {
            membersList.add(member.toString());
        }
    }
    
    /**
     * Check for notifications (mentions and unread messages)
     */
    private void checkNotifications() {
        // Check if user was offline and has unread messages
        if (userLastSeen > 0) {
            java.util.List<Message> recentMessages = groupChat.getMessages();
            java.util.List<Message> offlineMessages = new java.util.ArrayList<>();
            
            for (Message msg : recentMessages) {
                if (msg.getTimestamp().getTime() > userLastSeen && 
                    !msg.getSender().equals(currentUsername)) {
                    offlineMessages.add(msg);
                }
            }
            
            if (!offlineMessages.isEmpty()) {
                showOfflineMessagesSummary(offlineMessages);
            }
        }
        
        // Check for mentions
        java.util.List<String> mentions = FileManager.loadMentionsForUser(currentUsername);
        if (!mentions.isEmpty()) {
            StringBuilder mentionText = new StringBuilder();
            mentionText.append("You were mentioned ").append(mentions.size())
                       .append(" time(s) while offline:\n\n");
            
            for (int i = 0; i < Math.min(mentions.size(), 5); i++) {
                mentionText.append("- ").append(mentions.get(i)).append("\n");
            }
            
            if (mentions.size() > 5) {
                mentionText.append("\n... and ").append(mentions.size() - 5).append(" more");
            }
            
            showAlert("Mention Notification", mentionText.toString());
            FileManager.clearMentionsForUser(currentUsername);
        }
        
        // Update last seen to now
        userLastSeen = new Date().getTime();
    }
    
    /**
     * Show a comprehensive summary of offline messages
     */
    private void showOfflineMessagesSummary(java.util.List<Message> offlineMessages) {
        StringBuilder summary = new StringBuilder();
        
        // Header
        summary.append("╔═══════════════════════════════════════╗\n");
        summary.append("║   OFFLINE MESSAGES SUMMARY            ║\n");
        summary.append("╚═══════════════════════════════════════╝\n\n");
        
        summary.append("📬 You received ").append(offlineMessages.size())
               .append(" message(s) while offline!\n\n");
        
        // Count messages by sender
        java.util.HashMap<String, Integer> senderCount = new java.util.HashMap<>();
        java.util.HashMap<String, java.util.List<Message>> messagesBySender = new java.util.HashMap<>();
        
        for (Message msg : offlineMessages) {
            String sender = msg.getSender();
            senderCount.put(sender, senderCount.getOrDefault(sender, 0) + 1);
            
            if (!messagesBySender.containsKey(sender)) {
                messagesBySender.put(sender, new java.util.ArrayList<>());
            }
            messagesBySender.get(sender).add(msg);
        }
        
        // Key Point 1: Message Distribution
        summary.append("📊 MESSAGE DISTRIBUTION:\n");
        for (String sender : senderCount.keySet()) {
            int count = senderCount.get(sender);
            double percent = (count * 100.0 / offlineMessages.size());
            summary.append("   • ").append(sender).append(": ")
                   .append(count).append(" msg")
                   .append(count > 1 ? "s" : "")
                   .append(" (").append(String.format("%.0f", percent)).append("%)\n");
        }
        summary.append("\n");
        
        // Key Point 2: Important Messages (with mentions)
        summary.append("⚠️ IMPORTANT MESSAGES:\n");
        int importantCount = 0;
        for (Message msg : offlineMessages) {
            if (msg.hasMention() && msg.getMentionedUser() != null && 
                msg.getMentionedUser().equals(currentUsername)) {
                importantCount++;
                summary.append("   🔔 ").append(msg.getSender()).append(": ")
                       .append(msg.getContent()).append("\n");
            }
        }
        if (importantCount == 0) {
            summary.append("   ✓ No urgent mentions\n");
        }
        summary.append("\n");
        
        // Key Point 3: Recent Messages Preview
        summary.append("💬 RECENT MESSAGES:\n");
        int previewCount = Math.min(5, offlineMessages.size());
        int startIdx = Math.max(0, offlineMessages.size() - previewCount);
        
        for (int i = startIdx; i < offlineMessages.size(); i++) {
            Message msg = offlineMessages.get(i);
            String content = msg.getContent();
            if (content.length() > 50) {
                content = content.substring(0, 47) + "...";
            }
            summary.append("   ").append(i - startIdx + 1).append(". ")
                   .append(msg.getSender()).append(": ")
                   .append(content).append("\n");
        }
        summary.append("\n");
        
        // Key Point 4: Time Analysis
        summary.append("⏰ TIME ANALYSIS:\n");
        long firstMsgTime = offlineMessages.get(0).getTimestamp().getTime();
        long lastMsgTime = offlineMessages.get(offlineMessages.size() - 1).getTimestamp().getTime();
        long timeDiff = (lastMsgTime - firstMsgTime) / 60000; // in minutes
        
        summary.append("   • First: ").append(new java.text.SimpleDateFormat("HH:mm")
               .format(offlineMessages.get(0).getTimestamp())).append("\n");
        summary.append("   • Last: ").append(new java.text.SimpleDateFormat("HH:mm")
               .format(offlineMessages.get(offlineMessages.size() - 1).getTimestamp())).append("\n");
        summary.append("   • Duration: ").append(timeDiff).append(" minute(s)\n\n");
        
        // Action prompt
        summary.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        summary.append("💡 TIP: Scroll up in chat to see full history");
        
        showAlert("📨 Offline Messages Summary", summary.toString());
    }
    
    /**
     * Handle sending a message
     */
    private void sendMessageHandler() {
        String content = messageField.getText().trim();
        
        if (!content.isEmpty()) {
            Message message = new Message(currentUsername, content);
            sendMessage(message);
            messageField.setText("");
        }
    }
    
    /**
     * Implementation of MessageActions interface - send message
     */
    @Override
    public void sendMessage(Message message) {
        // Add to group chat
        groupChat.addMessage(message);
        
        // Display in chat area
        chatArea.append(message.formatMessage() + "\n");
        chatArea.setCaretPosition(chatArea.getText().length());
        
        // Save to file
        try {
            FileManager.saveMessage(message);
            
            // If message has mention, save it for offline users
            if (message.hasMention()) {
                String mentionedUser = message.getMentionedUser();
                
                // Save mention for users who are not the current user
                if (!mentionedUser.equals(currentUsername)) {
                    FileManager.saveMention(mentionedUser, message.formatMessage());
                }
            }
            
        } catch (IOException e) {
            showAlert("Error", "Failed to save message: " + e.getMessage());
        }
    }
    
    /**
     * Implementation of MessageActions interface - receive message
     */
    @Override
    public void receiveMessage(Message message) {
        // Display received message
        chatArea.append(message.formatMessage() + "\n");
        chatArea.setCaretPosition(chatArea.getText().length());
    }
    
    /**
     * Implementation of NotificationHandler interface - show alert
     */
    @Override
    public void showAlert(String title, String message) {
        Dialog alertDialog = new Dialog(this, title, true);
        alertDialog.setLayout(new BorderLayout(10, 10));
        alertDialog.setSize(450, 300);
        alertDialog.setLocationRelativeTo(this);
        
        // Message area
        TextArea messageArea = new TextArea(message, 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(Color.WHITE);
        
        // OK button
        Button okButton = new Button("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.setPreferredSize(new Dimension(80, 30));
        
        Panel buttonPanel = new Panel();
        buttonPanel.add(okButton);
        
        alertDialog.add(messageArea, BorderLayout.CENTER);
        alertDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Button action
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alertDialog.dispose();
            }
        });
        
        // Window closing
        alertDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                alertDialog.dispose();
            }
        });
        
        alertDialog.setVisible(true);
    }
    
    /**
     * Implementation of NotificationHandler interface - summarize messages
     */
    @Override
    public String summarizeMessages(String username) {
        java.util.List<Message> messages = groupChat.getMessages();
        int count = 0;
        StringBuilder summary = new StringBuilder();
        
        for (Message msg : messages) {
            if (msg.getTimestamp().getTime() > userLastSeen && 
                !msg.getSender().equals(username)) {
                count++;
                if (count <= 5) {
                    summary.append(msg.formatMessage()).append("\n");
                }
            }
        }
        
        if (count > 5) {
            summary.append("\n... and ").append(count - 5).append(" more messages");
        }
        
        return summary.length() > 0 ? summary.toString() : "No new messages";
    }
    
    /**
     * Toggle user online/offline status
     * @param online true for online, false for offline
     */
    private void setUserOnline(boolean online) {
        // Update status label and buttons
        if (online) {
            statusLabel.setText("Status: Online  ");
            onlineButton.setBackground(new Color(0, 150, 0)); // Dark green (active)
            offlineButton.setBackground(new Color(128, 128, 128)); // Gray (inactive)
            
            // Enable messaging
            messageField.setEnabled(true);
            sendButton.setEnabled(true);
            
            // Check for new mentions while offline
            checkOfflineMentions();
            
            // Check for notifications when coming back online
            checkNotifications();
            
        } else {
            statusLabel.setText("Status: Offline  ");
            onlineButton.setBackground(new Color(100, 180, 100)); // Light green (inactive)
            offlineButton.setBackground(new Color(200, 50, 50)); // Red (active)
            
            // Disable messaging
            messageField.setEnabled(false);
            sendButton.setEnabled(false);
            
            // Save last seen time
            userLastSeen = new Date().getTime();
            try {
                FileManager.saveLastSeen(currentUsername, userLastSeen);
            } catch (IOException e) {
                System.err.println("Error saving last seen: " + e.getMessage());
            }
        }
        
        // Update user status in group chat
        for (User member : groupChat.getMembers()) {
            if (member.getUsername().equals(currentUsername)) {
                member.setOnline(online);
                break;
            }
        }
        
        // Update members list display
        updateMembersList();
    }
    
    /**
     * Check for mentions that occurred while user was offline
     */
    private void checkOfflineMentions() {
        java.util.List<Message> messages = groupChat.getMessages();
        java.util.List<String> offlineMentionsList = new java.util.ArrayList<>();
        
        // Check messages after user went offline
        for (Message msg : messages) {
            if (msg.getTimestamp().getTime() > userLastSeen && 
                !msg.getSender().equals(currentUsername) &&
                msg.hasMention() && 
                msg.getMentionedUser() != null &&
                msg.getMentionedUser().equals(currentUsername)) {
                
                offlineMentionsList.add(msg.formatMessage());
                
                // Save to file for persistence
                try {
                    FileManager.saveMention(currentUsername, msg.formatMessage());
                } catch (IOException e) {
                    System.err.println("Error saving mention: " + e.getMessage());
                }
            }
        }
        
        // Show alert if there are new mentions
        if (!offlineMentionsList.isEmpty()) {
            StringBuilder alertMsg = new StringBuilder();
            alertMsg.append("🔔 You were mentioned ").append(offlineMentionsList.size())
                    .append(" time(s) while you were OFFLINE!\n\n");
            alertMsg.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            
            for (int i = 0; i < offlineMentionsList.size(); i++) {
                alertMsg.append("Mention ").append(i + 1).append(":\n");
                alertMsg.append("   ").append(offlineMentionsList.get(i)).append("\n\n");
            }
            
            alertMsg.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            alertMsg.append("Tip: Reply to your teammates!");
            
            showAlert("⚠️ OFFLINE MENTION ALERT", alertMsg.toString());
        }
    }
    
    /**
     * Show a summary of the chat messages
     */
    private void showChatSummary() {
        java.util.List<Message> messages = groupChat.getMessages();
        
        if (messages.isEmpty()) {
            showAlert("Chat Summary - Key Points", "No messages in chat yet!");
            return;
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("╔════════════════════════════════════════╗\n");
        summary.append("║    COMPLETE CHAT SUMMARY               ║\n");
        summary.append("║         KEY POINTS                     ║\n");
        summary.append("╚════════════════════════════════════════╝\n\n");
        
        // Key Point 1: Overall Statistics
        summary.append("📈 KEY POINT 1: OVERALL STATISTICS\n");
        summary.append("   • Total Messages: ").append(messages.size()).append("\n");
        summary.append("   • Active Members: ").append(groupChat.getMembers().size()).append("\n");
        
        // Calculate time span
        if (messages.size() > 1) {
            long firstTime = messages.get(0).getTimestamp().getTime();
            long lastTime = messages.get(messages.size() - 1).getTimestamp().getTime();
            long hours = (lastTime - firstTime) / (1000 * 60 * 60);
            long minutes = ((lastTime - firstTime) / (1000 * 60)) % 60;
            
            summary.append("   • Chat Duration: ");
            if (hours > 0) {
                summary.append(hours).append("h ");
            }
            summary.append(minutes).append("m\n");
            
            summary.append("   • Started: ").append(new java.text.SimpleDateFormat("MMM dd, HH:mm")
                   .format(messages.get(0).getTimestamp())).append("\n");
        }
        summary.append("\n");
        
        // Key Point 2: Participant Activity
        summary.append("👥 KEY POINT 2: PARTICIPANT ACTIVITY\n");
        java.util.HashMap<String, Integer> userMessageCount = new java.util.HashMap<>();
        String mostActiveUser = "";
        int maxMessages = 0;
        
        for (Message msg : messages) {
            String sender = msg.getSender();
            int count = userMessageCount.getOrDefault(sender, 0) + 1;
            userMessageCount.put(sender, count);
            
            if (count > maxMessages) {
                maxMessages = count;
                mostActiveUser = sender;
            }
        }
        
        summary.append("   • Most Active: ").append(mostActiveUser)
               .append(" (").append(maxMessages).append(" messages)\n");
        
        // Show all participants with engagement bars
        for (String user : userMessageCount.keySet()) {
            int count = userMessageCount.get(user);
            double percent = (count * 100.0 / messages.size());
            String bar = getProgressBar(percent);
            String indicator = user.equals(currentUsername) ? " (YOU)" : "";
            
            summary.append("   ").append(user).append(indicator).append(": ")
                   .append(bar).append(" ").append(count).append(" (")
                   .append(String.format("%.0f", percent)).append("%)\n");
        }
        summary.append("\n");
        
        // Key Point 3: Communication Patterns
        summary.append("💬 KEY POINT 3: COMMUNICATION PATTERNS\n");
        int totalMentions = 0;
        int questionsAsked = 0;
        int longMessages = 0;
        
        for (Message msg : messages) {
            if (msg.hasMention()) totalMentions++;
            if (msg.getContent().contains("?")) questionsAsked++;
            if (msg.getContent().length() > 100) longMessages++;
        }
        
        summary.append("   • Total Mentions: ").append(totalMentions).append("\n");
        summary.append("   • Questions Asked: ").append(questionsAsked).append("\n");
        summary.append("   • Detailed Messages: ").append(longMessages)
               .append(" (>100 chars)\n");
        
        double avgLength = 0;
        for (Message msg : messages) {
            avgLength += msg.getContent().length();
        }
        avgLength /= messages.size();
        summary.append("   • Avg Message Length: ")
               .append(String.format("%.0f", avgLength)).append(" chars\n\n");
        
        // Key Point 4: Your Involvement
        summary.append("🎯 KEY POINT 4: YOUR INVOLVEMENT\n");
        int myMessages = userMessageCount.getOrDefault(currentUsername, 0);
        int myMentions = 0;
        int mentionsByMe = 0;
        
        for (Message msg : messages) {
            if (msg.hasMention()) {
                if (msg.getMentionedUser() != null) {
                    if (msg.getMentionedUser().equals(currentUsername)) {
                        myMentions++;
                    }
                    if (msg.getSender().equals(currentUsername)) {
                        mentionsByMe++;
                    }
                }
            }
        }
        
        double participation = !messages.isEmpty() ? (myMessages * 100.0 / messages.size()) : 0;
        summary.append("   • Your Messages: ").append(myMessages)
               .append(" (").append(String.format("%.1f", participation)).append("%)\n");
        summary.append("   • Times Mentioned: ").append(myMentions).append("\n");
        summary.append("   • You Mentioned Others: ").append(mentionsByMe).append("\n");
        
        if (myMessages > 0) {
            String engagement = participation > 50 ? "Very High" : 
                              participation > 30 ? "High" : 
                              participation > 15 ? "Moderate" : "Low";
            summary.append("   • Engagement Level: ").append(engagement).append("\n");
        }
        summary.append("\n");
        
        // Key Point 5: Important Topics
        summary.append("🔑 KEY POINT 5: IMPORTANT TOPICS\n");
        
        // Find messages with mentions (important)
        java.util.List<Message> importantMsgs = new java.util.ArrayList<>();
        for (Message msg : messages) {
            if (msg.hasMention()) {
                importantMsgs.add(msg);
            }
        }
        
        if (importantMsgs.isEmpty()) {
            summary.append("   • No tagged discussions\n");
        } else {
            int showCount = Math.min(3, importantMsgs.size());
            summary.append("   Top ").append(showCount).append(" tagged discussions:\n");
            for (int i = importantMsgs.size() - showCount; i < importantMsgs.size(); i++) {
                Message msg = importantMsgs.get(i);
                String content = msg.getContent();
                if (content.length() > 40) {
                    content = content.substring(0, 37) + "...";
                }
                summary.append("   ").append(i - (importantMsgs.size() - showCount) + 1)
                       .append(". ").append(msg.getSender()).append(": ")
                       .append(content).append("\n");
            }
        }
        summary.append("\n");
        
        // Key Point 6: Recent Activity
        summary.append("⭐ KEY POINT 6: RECENT ACTIVITY\n");
        summary.append("   Latest 5 messages:\n");
        int startIdx = Math.max(0, messages.size() - 5);
        for (int i = startIdx; i < messages.size(); i++) {
            Message msg = messages.get(i);
            String content = msg.getContent();
            if (content.length() > 45) {
                content = content.substring(0, 42) + "...";
            }
            String time = new java.text.SimpleDateFormat("HH:mm")
                         .format(msg.getTimestamp());
            summary.append("   [").append(time).append("] ")
                   .append(msg.getSender()).append(": ")
                   .append(content).append("\n");
        }
        summary.append("\n");
        
        // Key Point 7: Recommendations
        summary.append("💡 KEY POINT 7: INSIGHTS & TIPS\n");
        
        if (myMessages == 0) {
            summary.append("   📌 Start participating in the conversation!\n");
        } else if (participation < 10) {
            summary.append("   📌 Consider engaging more with the team\n");
        } else if (participation > 60) {
            summary.append("   📌 Great engagement! You're very active\n");
        }
        
        if (myMentions > 0 && mentionsByMe == 0) {
            summary.append("   📌 People are mentioning you - respond back!\n");
        }
        
        if (totalMentions > messages.size() * 0.3) {
            summary.append("   📌 Highly collaborative conversation\n");
        }
        
        if (questionsAsked > messages.size() * 0.2) {
            summary.append("   📌 Active problem-solving discussion\n");
        }
        
        summary.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        summary.append("Generated: ").append(new java.text.SimpleDateFormat("MMM dd, HH:mm:ss")
               .format(new Date()));
        
        showAlert("📊 Complete Chat Summary - Key Points", summary.toString());
    }
    
    /**
     * Generate a simple progress bar
     */
    private String getProgressBar(double percent) {
        int bars = (int) (percent / 10);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < bars) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }
    
    /**
     * Toggle fullscreen mode
     */
    private void toggleFullscreen() {
        if (!isFullscreen) {
            // Enter maximized fullscreen mode
            setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
            isFullscreen = true;
            fullscreenButton.setLabel("⊗ Exit");
        } else {
            // Exit fullscreen and return to normal size
            setExtendedState(Frame.NORMAL);
            setSize(1000, 700);
            setLocationRelativeTo(null);
            isFullscreen = false;
            fullscreenButton.setLabel("⛶ Full");
        }
        validate();
        repaint();
    }
    
    /**
     * Handle logout with confirmation dialog
     */
    private void handleLogoutConfirmation() {
        Dialog confirmDialog = new Dialog(this, "Confirm Logout", true);
        confirmDialog.setLayout(new BorderLayout(10, 10));
        confirmDialog.setSize(350, 150);
        confirmDialog.setLocationRelativeTo(this);
        
        // Message
        Label messageLabel = new Label("Are you sure you want to logout?", Label.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        Panel messagePanel = new Panel();
        messagePanel.add(messageLabel);
        
        // Buttons
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        Button yesButton = new Button("Yes, Logout");
        yesButton.setFont(new Font("Arial", Font.BOLD, 12));
        yesButton.setBackground(new Color(220, 53, 69));
        yesButton.setForeground(Color.WHITE);
        yesButton.setPreferredSize(new Dimension(110, 30));
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(yesButton);
        buttonPanel.add(cancelButton);
        
        confirmDialog.add(messagePanel, BorderLayout.CENTER);
        confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Yes button action
        yesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmDialog.dispose();
                performLogout();
            }
        });
        
        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmDialog.dispose();
            }
        });
        
        // Window closing
        confirmDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                confirmDialog.dispose();
            }
        });
        
        confirmDialog.setVisible(true);
    }
    
    /**
     * Perform the actual logout
     */
    private void performLogout() {
        try {
            // Save last seen time
            FileManager.saveLastSeen(currentUsername, new Date().getTime());
        } catch (IOException e) {
            System.err.println("Error saving last seen: " + e.getMessage());
        }
        
        // Close chat window
        this.dispose();
        
        // Open login window
        LoginUI loginUI = new LoginUI();
        loginUI.setVisible(true);
    }
    
    /**
     * Handle user logout
     */
    private void handleLogout() {
        try {
            // Save last seen time
            FileManager.saveLastSeen(currentUsername, new Date().getTime());
        } catch (IOException e) {
            System.err.println("Error saving last seen: " + e.getMessage());
        }
        
        System.exit(0);
    }
}
