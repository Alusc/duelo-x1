import java.util.Scanner;
import java.util.Random;
import Personagem.Personagem;


public class Jogo {
    //#region main
    public static Scanner scanner = new Scanner(System.in);
    public static void limparConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void main(String[] args) {
        
        String entrada;
        do {

            setTurno(1);
            limparConsole();
            menuPrincipal();
            loopDeJogo();
            System.out.println("Deseja jogar novamente?\n| Sim | Não |");
            scanner.nextLine();
            entrada = scanner.nextLine().toLowerCase();
            
        } while (entrada.equals("sim"));
    }
    //#region loops
    public static void menuPrincipal(){
        
        System.out.println("Bem vindo ao Duelo X1");
        System.out.println("Selecione o modo de jogo:\n| 0: Singleplayer | 1: Multiplayer |");
       
        setMultiplayer(scanner.nextInt() != 0);
        limparConsole();
        System.out.println("Selecione a classe do Jogador 1:\n| 0: Guerreiro | 1: Arqueiro | 2: Mago |");
        int classe = scanner.nextInt();
        int numeroDeClasses = Personagem.Classe.values().length;

        while (classe < 0 || classe >= numeroDeClasses) {
            System.out.println("Classe selecionada invalida, digite novemente:\n| 0: Guerreiro | 1: Arqueiro | 2: Mago |");
            classe = scanner.nextInt();
        }
        limparConsole();
        personagem1 = new Personagem(Personagem.Classe.values()[classe]);
        System.out.println("Defina um nome para o personagem do Jogador 1:");
        scanner.nextLine(); //Apaga a quebra de linha
        String nome = scanner.nextLine();
        personagem1.setNome(nome);
        if (!isMultiplayer()){
            Random r = new Random();
            //A I.A. vai escolher uma classe aleatória
            personagem2 = new Personagem(Personagem.Classe.values()[r.nextInt(numeroDeClasses)]);
        }
        if (personagem2.getAparencia() == personagem1.getAparencia()){
            //Isso evita que os personagens tenham a mesma aparência
            personagem2.setAparencia((char)(personagem2.getAparencia() + 1));
        }
        
    }

    public static void loopDeJogo(){
        
        aleatorizarPosicoes();
        int entrada;
        do {
            limparConsole();
            printarTabuleiro();
            printarTurno();
            printarPersonagens();
            
            System.out.println("Qual ação deseja fazer?\n| 0: Mover | 1: Atacar | 2: Defender | 3: Ativar poder especial | 4: Sair |");
            entrada = scanner.nextInt();
            while (entrada < 0 || entrada > 4) {
                System.out.println("Entrada inválida. Por favor, digite novemente\n| 0: Mover | 1: Atacar | 2: Defender | 3: Ativar poder especial | 4: Sair |");
                entrada = scanner.nextInt();
            }
            proximoTurno();
        }
        while (entrada != 4 && !jogoEncerrado);
    }
    //#endregion

    //#region turno
    private static int turno = 1; // Variável para controlar o turno do jogo
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
    private static Personagem personagem1;
    private static Personagem personagem2;
    public static void aleatorizarPosicoes(){
        Random a = new Random();
        personagem1.setPosicao(a.nextInt(9), a.nextInt(9));
        personagem2.setPosicao(a.nextInt(9), a.nextInt(9));
    }
    //#endregion
    //#region prints na tela
    public static void printarTabuleiro(){
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                
                if (i == 0)
                    System.out.print(j == 0 ? " " : j - 1);
                else if (j == 0)
                    System.out.print(i - 1);
                else if (personagem1.checarPosicao(j - 1, i - 1)){
                    System.out.print(personagem1.getAparencia());
                }
                else if (personagem2.checarPosicao(j - 1, i - 1)){
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
    public static void printarTurno(){
        System.out.println("Turno: " + turno);
    }
    public static void printarPersonagens(){
        personagem1.printarInformacoes();
        personagem2.printarInformacoes();
    }
    
    //#endregion

}
