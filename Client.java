import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        new Client().startClient();
    }

    public void startClient() {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String fromServer, fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Servidor: " + fromServer);

                if ("Você sacou: ".startsWith(fromServer)) {
                    break;
                }

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Não foi possível estabelecer conexão: " + e.getMessage());
        }
    }
}
