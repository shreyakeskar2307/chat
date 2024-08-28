import java.io.*;//input output
import java.net.*;//networkinh operation creating socket
import java.util.*;//list and array list


public class Chatserver {//class define
  private static List<ClientHandler> clients = new ArrayList<>();//array set to take the get the data from client server
  //and client handler make the connection between the client and server

  public static void main(String[] args) throws IOException {//this exception deals with the input and output exception
      ServerSocket serverSocket = new ServerSocket(5000);//object created with port
      System.out.println("Server started. Waiting for clients...");

      while (true) {
          Socket clientSocket = serverSocket.accept();
          System.out.println("Client connected: " + clientSocket);
          ClientHandler clientThread = new ClientHandler(clientSocket,clients);
          clients.add(clientThread);// add the list of connected client
          new Thread(clientThread).start();//thread uses because we want to handle multiple client so
      }
  }
}

class ClientHandler implements Runnable {//runnable means it can be run on thread
  private Socket clientSocket;
  private List<ClientHandler> clients;
  private PrintWriter out;
  private BufferedReader in;

  public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {//constructor initialize client handler
      this.clientSocket = socket;//current obj
      this.clients = clients;
      this.out = new PrintWriter(clientSocket.getOutputStream(), true);//automatically flushed every time
      this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }

  public void run() {//method to execute on thread
      try {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
              for (ClientHandler aClient : clients) {// Iterates over all connected clients.
                  aClient.out.println(inputLine);
              }
          }
      } catch (IOException e) {
          System.out.println("An error occurred: " + e.getMessage());
      } finally {
          try {
              in.close();
              out.close();
              clientSocket.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
}