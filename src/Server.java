import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server na ges hurung....");
        ServerSocket serverSocket = new ServerSocket(1234);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Asup hiji kang satria barong goreng patut" + clientSocket);
            new ClientHandler(clientSocket).start();
        }
    }

    private static class ClientHandler extends Thread {  
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;


        public ClientHandler(Socket socket ) {
            this.socket = socket; 
        }
    
    

    public void run() { 
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); 
        
            synchronized (clientWriters) {
                clientWriters.add(out);
            }

            out.println("Masukkan username : ");
            username = in.readLine();
            broadcast(username + " has joined the chat!" );

            String msg;

            while ((msg = in.readLine()) != null) {
                if (msg.equalsIgnoreCase("/exit")) {
                    break;
                }
            broadcast("[" + username + "]: " + msg);
         }
    } catch (IOException e) {
        System.out.println(" Error: " + e.getMessage());
    } finally {
        if (out != null) {
            synchronized (clientWriters) {
                clientWriters.remove(out);
            }
        }

        broadcast(username + " has left the chat.");
        try { socket.close(); } catch (IOException e) {}
      }
    }

    private void broadcast(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }
}
}