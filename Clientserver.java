import java.io.*;
import java.net.*;
import java.util.function.Consumer;//implement function pragraming in java

public class Clientserver {
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    // private BufferedReader inputConsole = null;
    private Consumer<String> onMessageReceived;
    //to accept a Consumer<String> parameter. This consumer will be called with incoming messages from the server

  public Clientserver(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
    this.socket = new Socket(serverAddress, serverPort);
    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.out = new PrintWriter(socket.getOutputStream(), true);
    this.onMessageReceived = onMessageReceived;
    }
    public void sendMessage(String msg) {
        out.println(msg);
    }
  
    // try {
            // // Connecting to the server
            // socket = new Socket(address, port);
            // System.out.println("Connected to the chat server");

            // // Setting Up I/O Streams
            // inputConsole = new BufferedReader(new InputStreamReader(System.in));
            // out = new PrintWriter(socket.getOutputStream(), true);
            // in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // String line = "";//variable
    //         while (!line.equals("exit")) {
    //             line = inputConsole.readLine();
    //             out.println(line);
    //             System.out.println(in.readLine());
    //         }

    //         // Closing connections
    //         socket.close();
    //         inputConsole.close();
    //         out.close();
    //         in.close(); // Close the input stream as well
    //     } catch (UnknownHostException u) {//Catches and handles the case where the server's IP address is unknown or incorrect.
    //         System.out.println("Host unknown: " + u.getMessage());
    //     } catch (IOException i) {
    //         System.out.println("Unexpected exception: " + i.getMessage());
    //     }
     public void startClient() {
      new Thread(() -> {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                onMessageReceived.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }).start();
}

}
