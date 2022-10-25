package socketserver.tcp;

import socketserver.AppConstants;
import socketserver.NetworkListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket implements Runnable {

    private boolean connected = false;
    private ServerSocket serverSocket = null;
    private Socket connection = null;
    private TrataConexao trata = null;
    private NetworkListener nl;

    public MyServerSocket(NetworkListener nl) {
        this.nl = nl;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(AppConstants.SERVER_PORT);
            System.out.println("O servidor está executando");
            System.out.println("Esperando por conexões na porta " + AppConstants.SERVER_PORT);
            connected = true;
            while (connected) {
                connection = serverSocket.accept();
                trata = new TrataConexao(connection, nl);
                Thread trataConexao = new Thread(trata);
                trataConexao.start();
                System.out.println("Thread para tratar conexão iniciada");
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
