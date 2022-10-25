/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import socketserver.NetworkListener;

/**
 *
 * @author Filipe
 */
public class MyDatagramUDPServer extends Thread{
   
    private NetworkListener nl;
    private DatagramSocket udpSocket; // socket para conectar ao cliente
    private boolean connected;
    private int port;
    
    public MyDatagramUDPServer(int port, NetworkListener nl){
        this.nl = nl;
        connected = true;
        this.port = port;
        try {
            //Com a inicialização do DatagramSocket, a aplicação está pronta
            //para enviar e receber datagramas udp
              udpSocket= new DatagramSocket(this.port);
              nl.networkMessage(NetworkListener.UDP_MESSAGE, "Servidor UDP esperando mensagens:");
        } catch (SocketException ex) {
            nl.networkMessage(NetworkListener.UDP_MESSAGE, "Problema na inicialização do servidor UDP:");
        }
    }
    
    @Override
    public void run(){
        while(connected){
            byte data[] = new byte[200];
            try{
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                udpSocket.receive(receivePacket);
                nl.networkMessage(NetworkListener.UDP_MESSAGE, "Pacote recebido:");
                nl.networkMessage(NetworkListener.UDP_MESSAGE, "IP Fonte: " +  receivePacket.getAddress());
                nl.networkMessage(NetworkListener.UDP_MESSAGE, new String(receivePacket.getData(),0,receivePacket.getLength()));
            }
            catch(IOException ioe){
                nl.networkMessage(NetworkListener.UDP_MESSAGE, "Exceção no recebimento de mensagem UDP:\n"
                        + ioe.getMessage());
            }
        }                   
    }
    
    public void encerraServidor(){
            connected = false;
            udpSocket.close();           
    }
    
}
