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

    public void run() {
        Scanner scan = new Scanner(System.in);
        try {
            prepareClientForIndividualGame(scan);
            dos.close();
            dis.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareClientForIndividualGame(Scanner scan) throws IOException {
        int opcao = 0;

        mandaNickName(scan);

        while (opcao != 2) {
            int continuaPartida = 0;
            while (continuaPartida != 2) {
                mandaPalpite(scan);
                int resultado = recebeResultado();
                if (resultado == 200) continuaPartida = 2;
                else continuaPartida = retornaEscolhaDeContinuaPartida(scan);
                while (continuaPartida == 1) {
                    recebeListaNumerosAcertados();
                    continuaPartida = retornaEscolhaDeContinuaPartida(scan);
                }
            }
            opcao = retornaOpcaoPrincipal(scan);
            while (opcao == 1) {
                recebeListaNumerosAcertados();
                opcao = retornaOpcaoPrincipal(scan);
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

    public void mandaPalpite(Scanner scan) throws IOException {
        System.out.print("\n");
        System.out.print("Informe seu palpite (numero de 3 dígitos diferentes): ");
        int palpite = scan.nextInt();
        dos.writeInt(palpite);
        dos.flush();
    }

    private int retornaEscolhaDeContinuaPartida(Scanner scan) throws IOException {
        System.out.println("0 - Novo palpite");
        System.out.println("1 - Ver números acertados");
        System.out.println("2 - Desistir");
        System.out.print("Digite a opcao: ");
        int continuaPartida = scan.nextInt();
        dos.writeInt(continuaPartida);
        dos.flush();
        return continuaPartida;
    }

    private int retornaOpcaoPrincipal(Scanner scan) throws IOException {
        System.out.println("0 - Nova partida");
        System.out.println("1 - Ver números acertados");
        System.out.println("2 - Encerrar jogo");
        System.out.print("Digite a opcao: ");
        int continuaJogo = scan.nextInt();
        dos.writeInt(continuaJogo);
        dos.flush();
        return continuaJogo;
    }

    private int recebeResultado() throws IOException {
        int resultado = dis.readInt();

        if (resultado == 200) {
            System.out.print("\n");
            String mensagemDeVitoria = dis.readUTF();
            System.out.println(mensagemDeVitoria);
            System.out.print("\n");
        } else {
            String listaPalpite = dis.readUTF();
            System.out.println("Lista de palpites:\n" + listaPalpite);
        }

        return resultado;
    }
}