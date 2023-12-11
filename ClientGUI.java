import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class ClientGUI {

    private JFrame frame;
    private JTextField inputField;
    private JTextArea textArea;
    private JButton sendButton;
    private JButton enterButton; // Botão Enter modificado
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientGUI() {
        initialize();
        connectToServer();
    }

    private void initialize() {
        frame = new JFrame("Cliente - Jogo de Apostas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Estilo da Área de Texto
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(0, 102, 0)); // Verde escuro
        textArea.setForeground(Color.YELLOW); // Texto dourado
        textArea.setFont(new Font("Serif", Font.BOLD, 18));
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Painel de Entrada com Estilo
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(new Color(32, 32, 32)); // Fundo escuro

        inputField = new JTextField(20);
        inputField.setFont(new Font("Serif", Font.PLAIN, 16));
        inputPanel.add(inputField);

        sendButton = new JButton("Enviar");
        sendButton.setFont(new Font("Serif", Font.BOLD, 16));
        sendButton.setBackground(Color.BLACK);
        sendButton.setForeground(Color.YELLOW); // Texto dourado
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton);

        // Botão Enter modificado para enviar uma resposta vazia
        enterButton = new JButton();
        enterButton.setFont(new Font("Serif", Font.BOLD, 16));
        enterButton.addActionListener(e -> sendEmptyMessage());
        enterButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(enterButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new ServerListener()).start();
        } catch (IOException e) {
            textArea.append("Erro ao conectar ao servidor: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            inputField.setText("");
        }
    }

    private void sendEmptyMessage() {
        out.println("");
    }

    private class ServerListener implements Runnable {
        public void run() {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    String finalFromServer = fromServer;
                    SwingUtilities.invokeLater(() -> {
                        textArea.append("Servidor: " + finalFromServer + "\n");
                        if (finalFromServer.contains("Você ganhou")) {
                            enterButton.setText("uhuu!");
                            enterButton.setBackground(Color.GREEN);
                            toggleComponents(false);
                        } else if (finalFromServer.contains("Você perdeu")) {
                            enterButton.setText("vou ter que vender o corsakkkk");
                            enterButton.setBackground(Color.RED);
                            toggleComponents(false);
                        } else {
                            toggleComponents(true);
                        }
                    });
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> textArea.append("Erro ao receber mensagem do servidor: " + e.getMessage() + "\n"));
            }
        }
    }

    private void toggleComponents(boolean showSend) {
        sendButton.setVisible(showSend);
        inputField.setVisible(showSend);
        enterButton.setVisible(!showSend);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI());
    }
}