import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {

    private double saldo;

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado na porta 12345.");

            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                System.out.println("Cliente conectado.");

                // Solicita depósito inicial
                out.println("Digite o valor inicial a ser depositado:");
                saldo = Double.parseDouble(in.readLine());

                boolean continuar = true;
                while (continuar) {
                    // Solicita valor da aposta
                    out.println("Quanto deseja apostar? Saldo atual: " + saldo);
                    double aposta = Double.parseDouble(in.readLine());
                    processarAposta(aposta, out);

                    in.readLine();

                    // Pergunta se quer continuar
                    out.println("Deseja 'continuar', 'sacar' ou 'depositar'?");
                    String decisao = in.readLine();

                    switch (decisao.toLowerCase()) {
                        case "sacar" -> {
                            out.println("Você sacou: " + saldo + ". Aperte enter para finalizar a conexão.");
                            continuar = false;
                        }

                        case "depositar" -> {
                            out.println("Quanto deseja depositar?");
                            double deposito = Double.parseDouble(in.readLine());
                            saldo += deposito;
                        }

                        case "continuar" -> {
                        }

                        default -> {
                            out.println("Opção inválida.");
                            continuar = false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no Servidor: " + e.getMessage());
        }
    }

    private void processarAposta(double valor, PrintWriter out) {
        if (new Random().nextBoolean()) {
            saldo += valor;
            out.println("Você ganhou! Saldo atualizado: " + saldo + "Aperte enter para prosseguir.");
        } else {
            saldo -= valor;
            out.println("Você perdeu! Saldo atualizado: " + saldo + "Aperta enter para prosseguir.");
        }
    }
}