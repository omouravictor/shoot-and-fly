import java.util.Random;
import java.util.Scanner;

public class Main {
    public static int numeroAleatorioEntre(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static void imprimeListaDePalpites(String listaPalpite) {
        System.out.println("Lista de palpites:");
        System.out.println(listaPalpite);
    }

    public static int[] getQtdTirosEmoscas(String numeroAlvoString, String palpiteString) {
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

    public static int retornaEscolhaDeContinuaPartida(Scanner scan) {
        System.out.println("0 - Novo palpite");
        System.out.println("1 - Desistir");
        System.out.print("Digite a opcao: ");
        return scan.nextInt();
    }

    public static int retornaEscolhaDeContinuaJogo(Scanner scan) {
        System.out.print("\n");
        System.out.println("0 - Nova partida");
        System.out.println("1 - Encerrar jogo");
        System.out.print("Digite a opcao: ");
        return scan.nextInt();
    }

    public static void iniciarJogoIndividual(Scanner scan) {
        int continuaJogo = 0;

        while (continuaJogo != 1) {
            int qtdRodadas = 0;
            int continuaPartida = 0;
            String listaPalpite = "";
            String numeroAlvoString = String.valueOf(numeroAleatorioEntre(100, 999));

            while (continuaPartida != 1) {
                qtdRodadas++;
                System.out.print("\n");
                System.out.print("Informe seu palpite: ");
                String palpiteString = String.valueOf(scan.nextInt());

                int[] tirosEmoscas = getQtdTirosEmoscas(numeroAlvoString, palpiteString);
                int qtdTiros = tirosEmoscas[0];
                int qtdMoscas = tirosEmoscas[1];

                listaPalpite += palpiteString + " - " + qtdTiros + "T" + qtdMoscas + "M\n";
                imprimeListaDePalpites(listaPalpite);

                if (qtdMoscas == 3) {
                    System.out.println("Numero acertado!");
                    System.out.println("Você levou " + qtdRodadas + " rodada(s) para acertar");
                    break;
                }
                continuaPartida = retornaEscolhaDeContinuaPartida(scan);
            }
            continuaJogo = retornaEscolhaDeContinuaJogo(scan);
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("0 - Jogo individual");
        System.out.println("1 - 2 jogadores");
        System.out.print("Digite o modo de jogo: ");
        int modoDeJogo = scan.nextInt();

        switch (modoDeJogo) {
            case 0:
                iniciarJogoIndividual(scan);
            case 1:
                // TODO
        }

    }
}