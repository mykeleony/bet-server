import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Servidor: " + fromServer);

                if (fromServer.toLowerCase().contains("sessão de jogo encerrada")) {
                    break;
                }

                System.out.print("Cliente: ");
                String fromUser = stdIn.readLine();
                if (fromUser != null) {
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Não foi possível conectar ao host: " + host);
            System.exit(1);
        }
    }
}