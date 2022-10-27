/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import socketserver.tcp.MyServerSocket;

public class ServerMain {

    public MyServerSocket mySocket;

    public static void main(String[] args) {
        ServerMain serverMain = new ServerMain();
        serverMain.initServerSocket();
    }

    private void initServerSocket() {
        mySocket = new MyServerSocket();
        mySocket.start();
    }

}
