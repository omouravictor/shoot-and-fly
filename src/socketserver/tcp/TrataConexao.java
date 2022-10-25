package socketserver.tcp;

import socketserver.NetworkListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class TrataConexao implements Runnable {

    private Socket connection;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private NetworkListener nl;

    public TrataConexao(Socket conn, NetworkListener nl) {
        this.connection = conn;
        this.nl = nl;
    }

    public int getRandomNumberBetween(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private int[] getQtdTirosEmoscas(String numeroAlvoString, String palpiteString) {
        int qtdTiros = 0;
        int qtdMoscas = 0;

        for (int i = 0; i < 3; i++) {
            String digitoAlvoAtual = String.valueOf(numeroAlvoString.charAt(i));
            String digitoPalpiteAtual = String.valueOf(palpiteString.charAt(i));

            if (palpiteString.contains(digitoAlvoAtual)) {
                if (!digitoPalpiteAtual.equals(digitoAlvoAtual)) qtdTiros++;
                else qtdMoscas++;
            }
        }

        return new int[]{qtdTiros, qtdMoscas};
    }

    private int getPalpiteFromCliente() throws IOException {
        System.out.println("Aguardando palpite do cliente");
        System.out.flush();
        return dis.readInt();
    }

    private void sendTirosAndMoscasQtdToClient(int qtdTiros, int qtdMoscas) throws IOException {
        dos.writeInt(qtdTiros);
        dos.flush();
        System.out.println("O servidor enviou a quantidade de tiros");
        System.out.flush();

        dos.writeInt(qtdMoscas);
        dos.flush();
        System.out.println("O servidor enviou a quantidade de moscas");
        System.out.flush();
    }

    private void prepareServerForIndividualGame() throws IOException {
//        String numeroAlvoString = String.valueOf(getRandomNumberBetween(100, 999));
        String numeroAlvoString = String.valueOf(100);

        int palpite = getPalpiteFromCliente();
        int[] tirosEmoscas = getQtdTirosEmoscas(numeroAlvoString, String.valueOf(palpite));
        int qtdTiros = tirosEmoscas[0];
        int qtdMoscas = tirosEmoscas[1];
        sendTirosAndMoscasQtdToClient(qtdTiros, qtdMoscas);
    }

    private void prepareServerForMultiplayerGame() {
    }

    @Override
    public void run() {
        try {
            System.out.println("");
            System.out.println("Obtendo dos streams");
            System.out.flush();
            dos = new DataOutputStream(connection.getOutputStream());
            dis = new DataInputStream(connection.getInputStream());

            System.out.println("Aguardando opção de jogo do cliente");
            int gameOption = dis.readInt();

            if (gameOption == 0) {
                prepareServerForIndividualGame();
            } else if (gameOption == 1) {
                prepareServerForMultiplayerGame();
            }

            //Finalização dos Streams de comunicação e da conexão
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
