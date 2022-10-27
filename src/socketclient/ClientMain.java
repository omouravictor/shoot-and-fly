/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketclient;

import socketclient.tcp.MyClientSocket;
import socketserver.AppConstants;

public class ClientMain {

    public static void main(String[] args) {
        ClientMain clientMain = new ClientMain();

        clientMain.initConexaoServidor();
        System.exit(0);
    }

    private void initConexaoServidor() {
        try {
            System.out.println("Iniciando em localhost porta " + AppConstants.SERVER_PORT + "..");
            MyClientSocket myClientSocket = new MyClientSocket("localhost", AppConstants.SERVER_PORT);
            myClientSocket.start();
        } catch (Exception e) {
            System.out.println("Houve um problema ao tentar conectar com o servidor, tente novamente.");
            System.out.println("Certifique-se que o servidor está rodando.");
        }
    }

}
