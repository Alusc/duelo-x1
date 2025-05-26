import java.util.Scanner;
import java.util.Random;
import Personagem.Personagem;


public class Jogo {
    //#region main
    public static void main(String[] args) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    //#region loops
    public static void loopDeConfiguracoes(){
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seja bem vindo ao Duelo X1");
        System.out.println("Selecione o modo de jogo:\n | Singleplayer | Multiplayer");
        String entrada = scanner.nextLine().toLowerCase();
        if (entrada.equals("multiplayer")){
            setMultiplayer(true);
        }
        else if (entrada.equals("singleplayer")){
            setMultiplayer(false);
        }
        
    }

    public static void loopDeJogo(){
        
        Scanner scanner = new Scanner(System.in);
        String entrada = scanner.nextLine().toLowerCase();
        while (!entrada.equals("sair") && !jogoEncerrado) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            printarTabuleiro();
            printarPersonagens();
            
            System.out.println("Qual ação deseja fazer?\n| Mover | Atacar | Defender |");
            entrada = scanner.nextLine();
            proximoTurno();
        }
        scanner.close();
    }
    //#endregion

    //#endregion
    //#region turno
    private static int turno; // Variável para controlar o turno do jogo
    private static void setTurno(int novoTurno) {
        turno = novoTurno;
    }
    public static int getTurno() {
        return turno;
    }
    public static void proximoTurno(){
        setTurno(turno + 1);
    }
    //#endregion
    //#region estado de jogo
    private static boolean jogoEncerrado = false;
    public static void encerrarJogo(){
        jogoEncerrado = true;
    }
    //#endregion
    //#region multiplayer
    private static boolean multiplayer = false; // Variável que define a quantidade de players
    public static void setMultiplayer(boolean value){
        multiplayer = value;
    }
    public static boolean isMultiplayer(){
        return multiplayer;
    }
    //#endregion 
    //#region personagens
    private static Personagem personagem1 = new Personagem(Personagem.Classe.Guerreiro);
    private static Personagem personagem2 = new Personagem(Personagem.Classe.Mago);
    public static void aleatorizarPosicoes(){
        Random a = new Random();
        personagem1.setPosicao(a.nextInt(9), a.nextInt(9));
        personagem2.setPosicao(a.nextInt(9), a.nextInt(9));
    }
    //#endregion
    //#region prints na tela
    public static void printarTabuleiro(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (personagem1.checarPosicao(j, i)){
                    System.out.print(personagem1.getAparencia());
                }
                else if (personagem2.checarPosicao(j, i)){
                    System.out.print(personagem2.getAparencia());
                }
                else {
                    System.out.print('.');
                }
                System.out.print("  ");
            }
            System.out.println();
        }
    }
    public static void printarPersonagens(){
        personagem1.printarInformacoes();
        personagem2.printarInformacoes();
    }
    //#endregion


}
