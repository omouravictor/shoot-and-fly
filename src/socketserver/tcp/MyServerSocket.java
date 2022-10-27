package socketserver.tcp;

import socketserver.AppConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    private boolean connected = false;
    private ServerSocket serverSocket = null;

    public MyServerSocket() {
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(AppConstants.SERVER_PORT);
            System.out.println("O servidor está executando");
            System.out.println("Esperando por conexões na porta " + AppConstants.SERVER_PORT);
            connected = true;
            while (connected) {
                Socket connection = serverSocket.accept();
                System.out.print("\n");
                System.out.println("Cliente conectado!");
                TrataConexao trata = new TrataConexao(connection);
                trata.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha na conexão com o servidor!");
        }
    }

    public void disconnect() {
        System.out.println("Vai encerrar a conexão");
        try {
            if (serverSocket != null) {
                serverSocket.close();
                connected = false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Falha ao fechar conexão!");
        } finally {
            System.out.println("Conexão encerrada");
        }
    }

}
