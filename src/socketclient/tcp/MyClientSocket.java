package socketclient.tcp;

import socketclient.NetworkListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class MyClientSocket implements Runnable {

    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private NetworkListener nl;
    private Socket connection;

    public MyClientSocket(String serverIp, int port, NetworkListener nl) {
        this.nl = nl;
        try {
            connection = new Socket(serverIp, port);
            nl.networkMessage(NetworkListener.TCP_MESSAGE, "Conectado ao servidor!");
            dos = new DataOutputStream(connection.getOutputStream());
            dis = new DataInputStream(connection.getInputStream());
            nl.networkMessage(NetworkListener.TCP_MESSAGE, "Streams obtidos!");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getGameOption(Scanner scan) {
        System.out.println("Bem vindo ao jogo Tiro e Mosca :)");
        System.out.println("Desenvolvido com carinho por Victor Gabriel e Yuri Ribeiro.");
        System.out.println("0 - Jogo individual");
        System.out.println("1 - Multiplayer");
        System.out.print("Digite o modo de jogo: ");
        return scan.nextInt();
    }

    public int getPalpite(Scanner scan) {
        System.out.print("\n");
        System.out.print("Informe seu palpite (numero de 3 dígitos): ");
        return scan.nextInt();
    }

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);

        try {
            int gameOption = getGameOption(scan);
            dos.writeInt(gameOption);
            dos.flush();

            int palpite = getPalpite(scan);
            dos.writeInt(palpite);
            dos.flush();

            int qtdTiros = dis.readInt();
            System.out.println("Tiros: " + qtdTiros);

            int qtdMoscas = dis.readInt();
            System.out.println("Moscas: " + qtdMoscas);

//            dos.writeUTF("Tchau!");
//            System.out.println("Tchau enviado para servidor");
//            dos.flush();

            dos.close();
            dis.close();
            connection.close();
        }//fim do try
        catch (IOException e) {
            e.printStackTrace();
        }//fim do catch
        finally {
            nl.networkMessage(NetworkListener.TCP_MESSAGE, "Fim da conexão TCP.");
        }
    }//fim do run
}