import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Login UI using AWT with validation and exception handling
 */
public class LoginUI extends Frame {
    private TextField usernameField;
    private TextField passwordField;
    private Label messageLabel;
    private Button loginButton;
    private Button registerButton;
    
    /**
     * Constructor for LoginUI
     */
    public LoginUI() {
        super("Chat Application - Login");
        setupUI();
        setupEventHandlers();
        
        // Center the window on screen
        setLocationRelativeTo(null);
        
        // Window closing event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    /**
     * Setup the UI components
     */
    private void setupUI() {
        setLayout(new BorderLayout(0, 0));
        setSize(500, 600);
        setLocationRelativeTo(null);
        setBackground(new Color(245, 247, 250));
        setResizable(false);
        
        // Top panel with professional gradient header
        Panel topPanel = new Panel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(25, 118, 210)); // Professional blue
        topPanel.setPreferredSize(new Dimension(500, 140));
        
        // Logo/Icon area
        Panel logoPanel = new Panel();
        logoPanel.setBackground(new Color(25, 118, 210));
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        
        Label logoLabel = new Label("💬", Label.CENTER);
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        logoPanel.add(logoLabel);
        
        // Title container with centered text
        Panel titleContainer = new Panel();
        titleContainer.setLayout(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(25, 118, 210));
        
        Label titleLabel = new Label("Group Chat Application", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        
        Label subtitleLabel = new Label("Connect & Collaborate in Real-Time", Label.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(200, 230, 255));
        
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        topPanel.add(logoPanel, BorderLayout.NORTH);
        topPanel.add(titleContainer, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel with elegant card design
        Panel mainCenter = new Panel();
        mainCenter.setLayout(new BorderLayout());
        mainCenter.setBackground(new Color(245, 247, 250));
        
        // Create centered card container
        Panel cardContainer = new Panel();
        cardContainer.setLayout(new BorderLayout());
        cardContainer.setBackground(new Color(245, 247, 250));
        
        // Create elegant white card for form
        Panel formCard = new Panel();
        formCard.setLayout(null); // Absolute positioning for precise control
        formCard.setBackground(Color.WHITE);
        formCard.setPreferredSize(new Dimension(380, 320));
        
        // Welcome text
        Label welcomeLabel = new Label("Welcome Back!", Label.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(33, 33, 33));
        welcomeLabel.setBounds(0, 20, 380, 30);
        
        Label loginPrompt = new Label("Sign in to continue", Label.CENTER);
        loginPrompt.setFont(new Font("Arial", Font.PLAIN, 12));
        loginPrompt.setForeground(new Color(120, 120, 120));
        loginPrompt.setBounds(0, 50, 380, 20);
        
        // Username section with modern design
        Label usernameIcon = new Label("👤");
        usernameIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        usernameIcon.setBounds(50, 95, 35, 35);
        
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setForeground(new Color(80, 80, 80));
        usernameLabel.setBounds(95, 90, 240, 15);
        
        usernameField = new TextField(20);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        usernameField.setBackground(new Color(250, 251, 252));
        usernameField.setBounds(95, 110, 240, 38);
        
        // Password section with modern design
        Label passwordIcon = new Label("🔒");
        passwordIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        passwordIcon.setBounds(50, 170, 35, 35);
        
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(80, 80, 80));
        passwordLabel.setBounds(95, 165, 240, 15);
        
        passwordField = new TextField(20);
        passwordField.setEchoChar('●');
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        passwordField.setBackground(new Color(250, 251, 252));
        passwordField.setBounds(95, 185, 240, 38);
        
        // Message label for errors/success
        messageLabel = new Label("", Label.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 11));
        messageLabel.setForeground(new Color(220, 53, 69));
        messageLabel.setBounds(50, 240, 285, 25);
        
        // Add all components to form card
        formCard.add(welcomeLabel);
        formCard.add(loginPrompt);
        formCard.add(usernameIcon);
        formCard.add(usernameLabel);
        formCard.add(usernameField);
        formCard.add(passwordIcon);
        formCard.add(passwordLabel);
        formCard.add(passwordField);
        formCard.add(messageLabel);
        
        // Center the card with padding
        Panel centerWrapper = new Panel();
        centerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
        centerWrapper.setBackground(new Color(245, 247, 250));
        centerWrapper.add(formCard);
        
        cardContainer.add(centerWrapper, BorderLayout.CENTER);
        mainCenter.add(cardContainer, BorderLayout.CENTER);
        
        add(mainCenter, BorderLayout.CENTER);
        
        // Bottom panel with modern gradient buttons
        Panel bottomPanel = new Panel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        bottomPanel.setBackground(new Color(245, 247, 250));
        
        loginButton = new Button("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));
        loginButton.setBackground(new Color(76, 175, 80)); // Material green
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(160, 45));
        
        registerButton = new Button("CREATE ACCOUNT");
        registerButton.setFont(new Font("Arial", Font.BOLD, 13));
        registerButton.setBackground(new Color(33, 150, 243)); // Material blue
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(160, 45));
        
        bottomPanel.add(loginButton);
        bottomPanel.add(registerButton);
        
        // Add footer text
        Panel footerPanel = new Panel();
        footerPanel.setBackground(new Color(245, 247, 250));
        Label footerLabel = new Label("Secure • Fast • Reliable", Label.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setForeground(new Color(150, 150, 150));
        footerPanel.add(footerLabel);
        
        Panel bottomContainer = new Panel();
        bottomContainer.setLayout(new BorderLayout());
        bottomContainer.setBackground(new Color(245, 247, 250));
        bottomContainer.add(bottomPanel, BorderLayout.CENTER);
        bottomContainer.add(footerPanel, BorderLayout.SOUTH);
        
        add(bottomContainer, BorderLayout.SOUTH);
    }
    
    /**
     * Setup event handlers for buttons and Enter key
     */
    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Register button action
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
        
        // Enter key on password field triggers login
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        // Enter key on username field moves to password
        usernameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
    }
    
    /**
     * Handle login attempt with validation and exception handling
     */
    private void handleLogin() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            
            // Validation
            if (username.isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty!");
            }
            
            if (password.isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty!");
            }
            
            if (username.length() < 3) {
                throw new IllegalArgumentException("Username must be at least 3 characters!");
            }
            
            if (password.length() < 4) {
                throw new IllegalArgumentException("Password must be at least 4 characters!");
            }
            
            // Validate credentials
            if (FileManager.validateUser(username, password)) {
                messageLabel.setForeground(new Color(0, 128, 0));
                messageLabel.setText("Login successful!");
                
                // Small delay before opening chat window
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                            openChatWindow(username);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
                
            } else {
                throw new IllegalArgumentException("Invalid username or password!");
            }
            
        } catch (IllegalArgumentException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(ex.getMessage());
        } catch (IOException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error: " + ex.getMessage());
        } catch (Exception ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("An unexpected error occurred!");
            ex.printStackTrace();
        }
    }
    
    /**
     * Handle registration with validation and exception handling
     */
    private void handleRegister() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            
            // Validation
            if (username.isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty!");
            }
            
            if (password.isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty!");
            }
            
            if (username.length() < 3) {
                throw new IllegalArgumentException("Username must be at least 3 characters!");
            }
            
            if (password.length() < 4) {
                throw new IllegalArgumentException("Password must be at least 4 characters!");
            }
            
            if (username.contains("|") || password.contains("|")) {
                throw new IllegalArgumentException("Username and password cannot contain '|' character!");
            }
            
            if (username.contains(" ")) {
                throw new IllegalArgumentException("Username cannot contain spaces!");
            }
            
            // Register user
            FileManager.registerUser(username, password);
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText("Registration successful! Please login.");
            passwordField.setText("");
            
        } catch (IllegalArgumentException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(ex.getMessage());
        } catch (IOException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error: " + ex.getMessage());
        } catch (Exception ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("An unexpected error occurred!");
            ex.printStackTrace();
        }
    }
    
    /**
     * Open the chat window after successful login
     * @param username The logged-in username
     */
    private void openChatWindow(String username) {
        ChatUI chatUI = new ChatUI(username);
        chatUI.setVisible(true);
        this.dispose();
    }
}
