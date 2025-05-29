package duelox1.src.main.java.com.duelox1;
import java.util.Scanner;

import duelox1.src.main.java.com.duelox1.Personagem.Personagem;

import java.util.Random;


public class Jogo {
    private static final int larguraDoTabuleiro = 10;
    private static final int alturaDoTabuleiro = 10;
    //#region main

    public static Scanner scanner = new Scanner(System.in);
    public static void limparConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void main(String[] args) {
        
        String jogarNovamente;
        do {
            setTurno(1);
            limparConsole();
            menuPrincipal();
            loopDeJogo();
            System.out.println("Deseja jogar novamente? (Digite \"s\" para confirmar)");
            scanner.nextLine();
            jogarNovamente = scanner.nextLine().toLowerCase();
            
        } while (!jogarNovamente.isEmpty() && jogarNovamente.charAt(0) == 's');
    }
    //#region jogo
    public static void menuPrincipal(){
        
        System.out.println("Bem vindo ao Duelo X1");
        System.out.println("Selecione o modo de jogo:\n| 0: Singleplayer | 1: Multiplayer |");
       
        setMultiplayer(scanner.nextInt() != 0);
        limparConsole();
        System.out.println("Selecione a classe do Jogador 1:\n| 1: Guerreiro | 2: Arqueiro | 3: Mago |");
        int classe;

        //Validar a entrada do usuário
        if (scanner.hasNextInt())
            classe = scanner.nextInt();
        else
            classe = -1;
        
        
        int numeroDeClasses = Personagem.Classe.values().length;

        while (classe < 1 || classe > numeroDeClasses) {
            //Validar a classe selecionada
            System.out.println("Classe selecionada inválida, digite novemente:\n| 1: Guerreiro | 2: Arqueiro | 3: Mago |");
            if (scanner.hasNextInt())
                classe = scanner.nextInt();
            else {
                scanner.nextLine(); // Limpa a entrada inválida
                classe = -1;
            }
        }

        limparConsole();
        personagem1 = new Personagem(Personagem.Classe.values()[classe - 1]);
        System.out.println("Defina um nome para o personagem do Jogador 1:");
        scanner.nextLine(); //Passa pela quebra de linha
        String nome = scanner.nextLine().trim();
        personagem1.setNome(personagem1.getNome() + " " + nome);
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
            
            System.out.println("Qual ação deseja fazer?\n| 1: Mover | 2: Atacar | 3: Defender | 4: Ativar poder especial | 5: Sair |");
            entrada = scanner.nextInt();
            while (entrada < 1 || entrada > 5) {
                System.out.println("Entrada inválida. Por favor, digite novemente\n| 1: Mover | 2: Atacar | 3: Defender | 4: Ativar poder especial | 5: Sair |");
                if (scanner.hasNextInt())
                    entrada = scanner.nextInt();
                else 
                    entrada = 0;
            }
            realizarAcao(personagem1, entrada);
            if (isMultiplayer()){
                realizarAcao(personagem2, entrada);
            }
            proximoTurno();
        } while (entrada != 5 && !jogoEncerrado);
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
    //#region ações
    public static void realizarAcao(Personagem personagem, int acao){
        switch (acao) {
            case 1:
                realizarMovimento(personagem);
            break;
        
            default:
            break;
        }
    }
    public static void realizarMovimento(Personagem personagem){
        System.out.println("Para onde deseja mover?\n| Cima(C) | Baixo(B) | Esquerda(E) | Direita(D) |");
        scanner.nextLine();
        
        int destinoX;
        int destinoY;
        
        boolean charactereInvalido;

        do {
            char where = scanner.nextLine().toLowerCase().charAt(0);    
            destinoX = personagem.getX();
            destinoY = personagem.getY();
            charactereInvalido = false;

            switch (where) {
                case 'c':
                    destinoY--;
                break;
                case 'b':
                    destinoY++;
                break;
                case 'e':
                    destinoX--;
                break;
                case 'd':
                    destinoX++;
                break;
                default:
                    System.out.println("Caractere inválido");
                    charactereInvalido = true;
                break;
            }
        } while (charactereInvalido || !validarMovimento(destinoX, destinoY, personagem));

        personagem.mover(destinoX, destinoY);
    }
    public static boolean validarMovimento(int x, int y, Personagem personagem){
        if ((x < 0 || x >= larguraDoTabuleiro) || (y < 0 || y >= alturaDoTabuleiro)) {
            System.out.println("Destino fora do tabuleiro");
            return false;
        }
        if (personagem == personagem1) {
            System.out.println("Posições dos personagens coincidem");
            return !personagem2.estaNaPosicao(x, y);
        }
        if (personagem == personagem2) {
            System.out.println("Posições dos personagens coincidem");
            return !personagem1.estaNaPosicao(x, y);
        }
        return true; 
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
        personagem1.setPosicao(a.nextInt(larguraDoTabuleiro - 1), a.nextInt(alturaDoTabuleiro - 1));
        personagem2.setPosicao(a.nextInt(larguraDoTabuleiro - 1), a.nextInt(alturaDoTabuleiro - 1));
    }
    //#endregion
    //#region prints na tela
    public static void printarTabuleiro(){
        for (int i = 0; i < alturaDoTabuleiro + 1; i++) {
            for (int j = 0; j < larguraDoTabuleiro + 1; j++) {
                
                if (i == 0)
                    System.out.print(j == 0 ? " " : j - 1);
                else if (j == 0)
                    System.out.print(i - 1);
                else if (personagem1.estaNaPosicao(j - 1, i - 1)){
                    System.out.print(personagem1.getAparencia());
                }
                else if (personagem2.estaNaPosicao(j - 1, i - 1)){
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
