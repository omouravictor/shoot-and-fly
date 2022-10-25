/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver.udp;

import java.io.IOException;
import java.net.*;
import socketserver.NetworkListener;

/**
 *
 * @author Filipe
 */
public class MyDatagramUDPCliente extends Thread{

    private String serverIp, msg;
    private int port;
    private NetworkListener nl;
    
    public MyDatagramUDPCliente(String ip, int port, NetworkListener nl, String msg){
        this.serverIp = ip;
        this.port = port;
        this.nl = nl;
        this.msg = msg;
    }
    
    public void run(){
        try {
            DatagramSocket udpClientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(serverIp); 
            byte[] sendData = new byte[1024]; 
            sendData = msg.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IPAddress, port);
            udpClientSocket.send(sendPacket);
        } catch (UnknownHostException ex) {
            nl.networkMessage(NetworkListener.UDP_MESSAGE, "Problema no endereço do servidor");
        }
        catch(IOException se){
            nl.networkMessage(NetworkListener.UDP_MESSAGE, "Problema ao enviar a mensagem!");                       
        }        
    }
    
}
