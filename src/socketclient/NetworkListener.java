/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketclient;

/**
 *
 * @author Filipe
 */
public interface NetworkListener {
    
    public static final int UDP_MESSAGE = 1;
    public static final int TCP_MESSAGE = 2;
    
    
    public void networkMessage (int messageType, String msg);
    
}
