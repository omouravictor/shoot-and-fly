package socketserver.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TrataConexao {

    private final Socket connection;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public TrataConexao(Socket conn) {
        this.connection = conn;
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

    private int recebePalpite() throws IOException {
        System.out.println("Aguardando palpite do cliente");
        System.out.flush();
        return dis.readInt();
    }

    private void prepareServerForIndividualGame() throws IOException {
        int opcao = 0;
        Map<Integer, Integer> numerosAcertadosMap = new HashMap<>();
        String nickName = recebeNickName();

        while (opcao != 2) {
            int qtdRodadas = 0;
            int continuaPartida = 0;
            String listaPalpite = "";
//            String numeroAlvoString = String.valueOf(getRandomNumberBetween(100, 999));
            String numeroAlvoString = String.valueOf(100);

            while (continuaPartida != 2) {
                qtdRodadas++;
                int palpite = recebePalpite();

                int[] tirosEmoscas = getQtdTirosEmoscas(numeroAlvoString, String.valueOf(palpite));
                int qtdTiros = tirosEmoscas[0];
                int qtdMoscas = tirosEmoscas[1];
                listaPalpite += palpite + " - " + qtdTiros + "T" + qtdMoscas + "M\n";

                int resultado = mandaResultado(qtdMoscas, qtdRodadas, listaPalpite, nickName);
                if (resultado == 200) {
                    continuaPartida = 2;
                    numerosAcertadosMap.put(Integer.valueOf(numeroAlvoString), qtdRodadas);
                } else continuaPartida = getContinuaPartidaFromCliente();

                while (continuaPartida == 1) {
                    retornaNumerosAcertados(numerosAcertadosMap);
                    continuaPartida = getContinuaPartidaFromCliente();
                }

            }
            opcao = getContinuaJogoFromCliente();
            while (opcao == 1) {
                retornaNumerosAcertados(numerosAcertadosMap);
                opcao = getContinuaJogoFromCliente();
            }
        }
    }

    private void retornaNumerosAcertados(Map<Integer, Integer> numerosAcertadosMap) throws IOException {
        final String[] listaNumerosAcertados = {""};
        numerosAcertadosMap.forEach((numero, rodadas) -> {
            listaNumerosAcertados[0] += "Numero: " + numero + " | QtdRodadas: " + rodadas + "\n";
        });
        if (!listaNumerosAcertados[0].isEmpty()) {
            dos.writeUTF(listaNumerosAcertados[0]);
            System.out.println("O servidor enviou a lista de números acertados para o cliente");
        } else {
            dos.writeUTF("Nenhum número acertado até o momento :(");
            System.out.println("O servidor informou que não há números acertados até o momento para o cliente");
        }
    }

    private String recebeNickName() throws IOException {
        System.out.println("Aguardando nickname do cliente");
        System.out.flush();
        return dis.readUTF();
    }

    private int getContinuaJogoFromCliente() throws IOException {
        System.out.println("Aguardando opção de jogo do cliente");
        System.out.flush();
        return dis.readInt();
    }

    private int getContinuaPartidaFromCliente() throws IOException {
        System.out.println("Aguardando opção de continuar partida do cliente");
        System.out.flush();
        return dis.readInt();
    }

    private int mandaResultado(int qtdMoscas, int qtdRodadas, String listaPalpite, String nickname) throws IOException {
        int resultado;

        if (qtdMoscas == 3) {
            resultado = 200;
            dos.writeInt(resultado);
            System.out.println("O servidor enviou o resultado para o cliente");

            dos.writeUTF("Parabéns " + nickname + "! Número acertado!\n" + "Você levou " + qtdRodadas + " rodada(s) para acertar.");
            System.out.println("O servidor identificou o vencedor e enviou para o cliente");
        } else {
            resultado = 100;
            dos.writeInt(resultado);
            System.out.println("O servidor enviou o resultado para o cliente");

            dos.writeUTF(listaPalpite);
            System.out.println("O servidor enviou a lista de palpites para o cliente");
        }
        return resultado;
    }

    public void run() {
        try {
            System.out.print("\n");
            System.out.println("Configurando streams");
            System.out.flush();
            dos = new DataOutputStream(connection.getOutputStream());
            dis = new DataInputStream(connection.getInputStream());

            prepareServerForIndividualGame();

            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
