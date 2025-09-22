import java.io.*;
import java.net.*;
// hari ini figma le asli
public class Client {
    public static void main(String[] args) throws Exception {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Server IP: ");
        String serverIP = console.readLine();

        Socket socket = new Socket(serverIP, 1234);
        System.out.println("✅ Connected to server!");

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Thread buat nerima pesan dari server
        new Thread(() -> {
            String msg;
            try {
                while ((msg = in.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (IOException e) {
                System.out.println("❌ Connection closed.");
            }
        }).start();

        // Input dari user
        String userInput;
        while ((userInput = console.readLine()) != null) {
            out.println(userInput);
            if (userInput.equalsIgnoreCase("/exit")) {
                socket.close();
                break;
            }
        }
    }
}
