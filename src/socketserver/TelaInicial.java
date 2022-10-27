/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import socketserver.tcp.MyServerSocket;

public class TelaInicial {

    public TelaInicial() {
    }

    public MyServerSocket mySocket;

    public static void main(String args[]) {
        TelaInicial telaInicial = new TelaInicial();
        telaInicial.initServerSocket();
    }

    private void initServerSocket() {
        mySocket = new MyServerSocket();
        mySocket.run();
    }

    private void disconnect() {
        mySocket.disconnect();
    }

}
