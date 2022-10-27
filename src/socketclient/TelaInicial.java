/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketclient;

import socketclient.tcp.MyClientSocket;

import java.util.InputMismatchException;

public class TelaInicial {

    public TelaInicial() {
    }

    public static void main(String args[]) {

        TelaInicial telaInicial = new TelaInicial();

        telaInicial.initConexaoServidor();
        System.exit(0);
    }

    private void initConexaoServidor() {
        try {
            System.out.println("Iniciando em localhost porta 10010..");
            MyClientSocket myClientSocket = new MyClientSocket("localhost", 10010);
            myClientSocket.start();
        } catch (NumberFormatException nfe) {
            System.out.println("Problema ao conectar ao servidor");
        } catch (InputMismatchException e) {
            System.out.println("A porta informada é inválida");
        }
    }

}
