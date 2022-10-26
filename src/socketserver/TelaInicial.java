/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import socketserver.tcp.MyServerSocket;

public class TelaInicial implements NetworkListener {

    public TelaInicial() {
    }

    //Referência Global para a thread que controla o servidor TCP
    public MyServerSocket mySocket;

    public static void main(String args[]) {

        TelaInicial telaInicial = new TelaInicial();

        telaInicial.initServerSocket();

    }

    private void initServerSocket() {
        mySocket = new MyServerSocket(this);
        mySocket.run();
    }

    private void disconnect() {
        mySocket.disconnect();
    }

    @Override
    public void networkMessage(int messageType, String msg) {
        System.out.println("networkMessage: " + msg);
    }
}
