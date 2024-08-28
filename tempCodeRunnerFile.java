    public ChatClientGUI() {
        super("Chat Application");//Calls the constructor of JFrame and sets the title of the window to "Chat Application".
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);//exit =>close


      Color backgroundColor = new Color(240, 240, 240);
      Color buttonColor = new Color(75, 75, 75);
      Color textColor = new Color(50, 50, 50);
      Font textFont = new Font("Arial", Font.PLAIN, 14);
      Font buttonFont = new Font("Arial", Font.BOLD, 12);


        //Setting Up the Message Area:
        messageArea = new JTextArea();
        messageArea.setEditable(false);//they shouls not able to edit text

        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(textColor);
        messageArea.setFont(textFont);

        //scroll to get upper and lower chat
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);



        //Setting Up the Input Text Field:
        String name = JOptionPane.showInputDialog(this, "Enter your name:", "Name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat Application - " + name);

        textField = new JTextField();

        textField.setFont(textFont);
        textField.setForeground(textColor);
        textField.setBackground(backgroundColor);

        textField.addActionListener(new ActionListener() {//enter press message inthe text filed send to server
            public void actionPerformed(ActionEvent e) {

                String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + name + ": " + textField.getText();

                client.sendMessage(message);//: Sends the message to the server using the ChatClient instance
                textField.setText("");//clear the text fileld after sent
            }
        });
         exitButton = new JButton("Exit");
         exitButton.setFont(buttonFont);
         exitButton.setBackground(buttonColor);
         exitButton.setForeground(Color.WHITE);
         exitButton.addActionListener(e -> {
          String departureMessage = name + " has left the chat.";
          client.sendMessage(departureMessage);
          try {
              Thread.sleep(1000);
          } catch (InterruptedException ie) {
              Thread.currentThread().interrupt();
          }
          System.exit(0);
      });

      JPanel bottomPanel = new JPanel(new BorderLayout());
      bottomPanel.setBackground(backgroundColor);
      bottomPanel.add(textField, BorderLayout.CENTER);
      bottomPanel.add(exitButton, BorderLayout.EAST);
      add(bottomPanel, BorderLayout.SOUTH);//bottom textfiled
        
        //Initializing the ChatClient:
        try {
            this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);//The this::onMessageReceived part is a method reference to handle incoming messages.
            client.startClient();
        } catch (IOException e) {
            e.printStackTrace();//handle exception
            JOptionPane.showMessageDialog(this, "Error connecting to the server", "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);//define unsuccessfull termination
        }
    }

        //Handling Incoming Messages:
        private void onMessageReceived(String message) {//This method is called whenever a new message is received from the server.
            SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));///Ensures that updates to the messageArea happen on the Event Dispatch Thread (EDT), which is the thread responsible for managing the GUI in Swing.
            //append add the item in the area
        }
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                new ChatClientGUI().setVisible(true);
            });
            //SwingUtilities.invokeLater(...): Ensures that the GUI is created on the EDT.
            //new ChatClientGUI().setVisible(true): Creates an instance of ChatClientGUI and makes it visible, effectively launching the chat client GUI.
        }
    }
      