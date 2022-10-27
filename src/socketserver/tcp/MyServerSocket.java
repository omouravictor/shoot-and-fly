package socketserver.tcp;

import socketserver.AppConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    public MyServerSocket() {
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(AppConstants.SERVER_PORT);
            System.out.println("O servidor está executando");
            System.out.println("Esperando por conexões na porta " + AppConstants.SERVER_PORT);
            boolean connected = true;
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

}
