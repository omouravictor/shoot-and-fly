/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketclient;

import socketclient.tcp.MyClientSocket;
import socketserver.AppConstants;

import java.util.InputMismatchException;

public class ClientMain {

    public ClientMain() {
    }

    public static void main(String args[]) {

        ClientMain clientMain = new ClientMain();

        clientMain.initConexaoServidor();
        System.exit(0);
    }

    private void initConexaoServidor() {
        try {
            System.out.println("Iniciando em localhost porta 10010..");
            MyClientSocket myClientSocket = new MyClientSocket("localhost", AppConstants.SERVER_PORT);
            myClientSocket.start();
        } catch (NumberFormatException nfe) {
            System.out.println("Problema ao conectar ao servidor");
        } catch (InputMismatchException e) {
            System.out.println("A porta informada é inválida");
        }
    }

}
