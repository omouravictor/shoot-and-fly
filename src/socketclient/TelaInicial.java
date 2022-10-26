/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketclient;

import socketclient.tcp.MyClientSocket;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaInicial implements NetworkListener {

    public TelaInicial() {
    }

    public static void main(String args[]) {

        Scanner scan = new Scanner(System.in);
        TelaInicial telaInicial = new TelaInicial();

        telaInicial.initConexaoServidor(scan);
        System.exit(0);
    }

    private void initConexaoServidor(Scanner scan) {
        try {
            System.out.println("Iniciando em localhost porta 10010..");
            MyClientSocket myClientSocket = new MyClientSocket("localhost", 10010);
            myClientSocket.run();
        } catch (NumberFormatException nfe) {
            System.out.println("Problema ao conectar ao servidor");
        } catch (InputMismatchException e) {
            System.out.println("A porta informada é inválida");
        }
    }

    @Override
    public void networkMessage(int messageType, String msg) {
        System.out.println("networkMessage: " + msg);
    }
}
