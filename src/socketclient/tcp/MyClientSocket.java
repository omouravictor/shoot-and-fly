package socketclient.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private Socket connection;

    public MyClientSocket(String serverIp, int port) {
        try {
            connection = new Socket(serverIp, port);
            dos = new DataOutputStream(connection.getOutputStream());
            dis = new DataInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Scanner scan = new Scanner(System.in);
        try {
            int opcaoTipoDeJogo = 0;

            opcaoTipoDeJogo = mandaOpcaoTipoDeJogo();
            if (opcaoTipoDeJogo == 0) {
                preparaClienteParaJogoIndividual(scan);
            } else if (opcaoTipoDeJogo == 1) {
                System.out.print("\n");
                System.out.println("Não conseguimos implementar a tempo");
            }

            dos.close();
            dis.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int mandaOpcaoTipoDeJogo() throws IOException {
        int opcao = 10;
        while (opcao == 10) {
            Scanner scan = new Scanner(System.in);
            showMenuTipoDeJogo();
            opcao = getOpcaoTipoDeJogo(opcao, scan);
        }
        dos.writeInt(opcao);
        dos.flush();
        return opcao;
    }

    private void preparaClienteParaJogoIndividual(Scanner scan) throws IOException {
        int opcaoDeJogo = 0;

        mandaNickName(scan);

        while (opcaoDeJogo != 2) {
            int opcaoDePartida = 0;
            while (opcaoDePartida != 2) {
                mandaPalpite();
                int resultado = recebeResultado();
                if (resultado == 200) opcaoDePartida = 2;
                else opcaoDePartida = mandaOpcaoDeRodada();
                while (opcaoDePartida == 1) {
                    recebeListaNumerosAcertados();
                    opcaoDePartida = mandaOpcaoDeRodada();
                }
            }
            System.out.print("\n");
            opcaoDeJogo = mandaOpcaoDePartida();
            while (opcaoDeJogo == 1) {
                recebeListaNumerosAcertados();
                opcaoDeJogo = mandaOpcaoDePartida();
            }
        }
    }

    private void recebeListaNumerosAcertados() throws IOException {
        String listaNumerosAcertados = dis.readUTF();
        System.out.print("\n");
        System.out.println(listaNumerosAcertados);
        System.out.print("\n");
    }

    private void mandaNickName(Scanner scan) throws IOException {
        System.out.print("\n");
        System.out.print("Informe seu nickname: ");
        String nickname = scan.nextLine();
        dos.writeUTF(nickname);
        dos.flush();
    }

    public void mandaPalpite() throws IOException {
        int palpite = 0;
        while (palpite == 0) {
            Scanner scan = new Scanner(System.in);
            System.out.print("\n");
            System.out.print("Informe seu palpite (numero de 3 dígitos diferentes): ");
            try {
                palpite = scan.nextInt();
                String palpiteString = String.valueOf(palpite);
                if (palpiteString.length() != 3) {
                    System.out.println("O numero deve ter 3 dígitos (todos diferentes)");
                    palpite = 0;
                } else {
                    char digito1 = palpiteString.charAt(0);
                    char digito2 = palpiteString.charAt(1);
                    char digito3 = palpiteString.charAt(2);
                    if (!(digito1 != digito2 && digito1 != digito3 && digito3 != digito2)) {
                        System.out.println("O numero deve ter 3 dígitos (todos diferentes)");
                        palpite = 0;
                    }
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida!");
            }
        }
        dos.writeInt(palpite);
        dos.flush();
    }

    private int mandaOpcaoDeRodada() throws IOException {
        int opcao = 10;
        while (opcao == 10) {
            Scanner scan = new Scanner(System.in);
            showMenuDeRodada();
            opcao = getOpcao(opcao, scan);
        }
        dos.writeInt(opcao);
        dos.flush();
        return opcao;
    }

    private int mandaOpcaoDePartida() throws IOException {
        int opcao = 10;
        while (opcao == 10) {
            Scanner scan = new Scanner(System.in);
            showMenuDePartida();
            opcao = getOpcao(opcao, scan);
        }
        dos.writeInt(opcao);
        dos.flush();
        return opcao;
    }

    private int getOpcaoTipoDeJogo(int opcao, Scanner scan) {
        try {
            opcao = scan.nextInt();
            if (opcao < 0 || opcao > 1) {
                System.out.print("\n");
                System.out.println("Opção inválida!");
                System.out.print("\n");
                opcao = 10;
            }
        } catch (Exception e) {
            System.out.print("\n");
            System.out.println("Entrada inválida!");
            System.out.print("\n");
        }
        return opcao;
    }

    private int getOpcao(int opcao, Scanner scan) {
        try {
            opcao = scan.nextInt();
            if (opcao < 0 || opcao > 2) {
                System.out.print("\n");
                System.out.println("Opção inválida!");
                System.out.print("\n");
                opcao = 10;
            }
        } catch (Exception e) {
            System.out.print("\n");
            System.out.println("Entrada inválida!");
            System.out.print("\n");
        }
        return opcao;
    }

    private void showMenuDePartida() {
        System.out.println("0 - Nova rodada");
        System.out.println("1 - Ver números acertados");
        System.out.println("2 - Encerrar jogo");
        System.out.print("Digite a opcao: ");
    }

    private void showMenuDeRodada() {
        System.out.println("0 - Novo palpite");
        System.out.println("1 - Ver números acertados");
        System.out.println("2 - Desistir");
        System.out.print("Digite a opcao: ");
    }

    private void showMenuTipoDeJogo() {
        System.out.println("0 - Jogo individual");
        System.out.println("1 - 2 jogadores");
        System.out.print("Digite a opcao: ");
    }

    private int recebeResultado() throws IOException {
        int resultado = dis.readInt();

        if (resultado == 200) {
            System.out.print("\n");
            String mensagemDeVitoria = dis.readUTF();
            System.out.println(mensagemDeVitoria);
        } else {
            String listaPalpite = dis.readUTF();
            System.out.println("Lista de palpites:\n" + listaPalpite);
        }

        return resultado;
    }
}