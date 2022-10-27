package socketserver.tcp;

import socketserver.AppConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrataConexao {

    private final Socket connection;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public TrataConexao(Socket conn) {
        this.connection = conn;
    }

    public void start() {
        try {
            System.out.println("Configurando streams");
            dos = new DataOutputStream(connection.getOutputStream());
            dis = new DataInputStream(connection.getInputStream());
            int opcaoTipoDeJogo;

            opcaoTipoDeJogo = recebeOpcaoTipoDeJogo();
            if (opcaoTipoDeJogo == 0) {
                preparaServidorParaJogoIndividual();
            } else if (opcaoTipoDeJogo == 1) {
                System.out.println("O servidor ainda não está preparado para jogo com 2 jogadores");
            }

            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            connection.close();
        } catch (IOException e) {
            System.out.println("Houve um problema entre a comunicação do cliente com o servidor");
            System.out.println("Esperando por conexões na porta " + AppConstants.SERVER_PORT);
        }
    }

    public int getNumberTarget() {
        Random rand = new Random();
        int digito1 = rand.nextInt((9 - 1) + 1) + 1;
        int digito2 = digito1;
        while (digito2 == digito1) {
            digito2 = rand.nextInt((9 - 1) + 1) + 1;
        }
        int digito3 = digito2;
        while (digito3 == digito2 || digito3 == digito1) {
            digito3 = rand.nextInt((9 - 1) + 1) + 1;
        }
        return Integer.parseInt(digito1 + "" + digito2 + "" + digito3);
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
        return dis.readInt();
    }

    private void preparaServidorParaJogoIndividual() throws IOException {
        int opcaoDePartida = 0;
        List<int[]> numerosAcertadosList = new ArrayList<>();

        String nickName = recebeNickName();

        while (opcaoDePartida != 2) {
            int qtdRodadas = 0;
            int opcaoDeRodada = 0;
            String listaPalpite = "";
            String numeroAlvoString = String.valueOf(getNumberTarget());

            while (opcaoDeRodada != 2) {
                qtdRodadas++;
                int palpite = recebePalpite();
                int[] tirosEmoscas = getQtdTirosEmoscas(numeroAlvoString, String.valueOf(palpite));
                int qtdTiros = tirosEmoscas[0];
                int qtdMoscas = tirosEmoscas[1];
                listaPalpite += palpite + " - " + qtdTiros + "T" + qtdMoscas + "M\n";
                int resultado = mandaResultado(qtdMoscas, qtdRodadas, listaPalpite, nickName);
                if (resultado == 200) {
                    opcaoDeRodada = 2;
                    numerosAcertadosList.add(new int[]{Integer.parseInt(numeroAlvoString), qtdRodadas});
                } else opcaoDeRodada = recebeOpcaoDeRodada();
                while (opcaoDeRodada == 1) {
                    mandaListaNumerosAcertados(numerosAcertadosList);
                    opcaoDeRodada = recebeOpcaoDeRodada();
                }
            }

            opcaoDePartida = recebeOpcaoDePartida();
            while (opcaoDePartida == 1) {
                mandaListaNumerosAcertados(numerosAcertadosList);
                opcaoDePartida = recebeOpcaoDePartida();
            }
        }

    }

    private void mandaListaNumerosAcertados(List<int[]> numerosAcertadosList) throws IOException {
        StringBuilder listaNumerosAcertados = new StringBuilder();

        for (int i = 0; i < numerosAcertadosList.size(); i++) {
            int[] numeroErodada = numerosAcertadosList.get(i);
            if (i != numerosAcertadosList.size() - 1)
                listaNumerosAcertados.append("Numero: ").append(numeroErodada[0]).
                        append(" | QtdRodadas: ").append(numeroErodada[1]).append("\n");
            else listaNumerosAcertados.append("Numero: ").append(numeroErodada[0])
                    .append(" | QtdRodadas: ").append(numeroErodada[1]);
        }
        if (listaNumerosAcertados.length() > 0) {
            dos.writeUTF(listaNumerosAcertados.toString());
            dos.flush();
            System.out.println("O servidor enviou a lista de números acertados para o cliente");
        } else {
            dos.writeUTF("Nenhum número acertado até o momento :(");
            dos.flush();
            System.out.println("O servidor informou que não há números acertados até o momento para o cliente");
        }
    }

    private String recebeNickName() throws IOException {
        System.out.println("Aguardando nickname do cliente");
        return dis.readUTF();
    }

    private int recebeOpcaoDePartida() throws IOException {
        System.out.println("Aguardando opção de partida do cliente");
        return dis.readInt();
    }

    private int recebeOpcaoTipoDeJogo() throws IOException {
        System.out.println("Aguardando opção de tipo de jogo do cliente");
        return dis.readInt();
    }

    private int recebeOpcaoDeRodada() throws IOException {
        System.out.println("Aguardando opção de rodada do cliente");
        return dis.readInt();
    }

    private int mandaResultado(int qtdMoscas, int qtdRodadas, String listaPalpite, String nickname) throws IOException {
        int resultado;

        if (qtdMoscas == 3) {
            resultado = 200;
            dos.writeInt(resultado);
            dos.flush();
            System.out.println("O servidor enviou o resultado para o cliente");

            dos.writeUTF("Parabéns " + nickname + "! Número acertado!\n" + "Você levou " + qtdRodadas + " rodada(s) para acertar.");
            dos.flush();
            System.out.println("O servidor identificou o vencedor e enviou para o cliente");
        } else {
            resultado = 100;
            dos.writeInt(resultado);
            dos.flush();
            System.out.println("O servidor enviou o resultado para o cliente");

            dos.writeUTF(listaPalpite);
            dos.flush();
            System.out.println("O servidor enviou a lista de palpites para o cliente");
        }
        return resultado;
    }
}
