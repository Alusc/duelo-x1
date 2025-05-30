package duelox1.src.main.java.com.duelox1;
import java.util.Scanner;

import duelox1.src.main.java.com.duelox1.Personagem.Personagem;

import java.util.Random;


public class Jogo {
    
    private static final int larguraDoTabuleiro = 10;
    private static final int alturaDoTabuleiro = 10;
    private static int numeroDeClasses = Personagem.Classe.values().length;
    //#region main

    public static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        String jogarNovamente;
        do {
            reiniciarJogo();
            limparConsole();
            menuPrincipal();
            loopDeJogo();
            System.out.println("Jogo finalizado");
            revelarVencedor();
            System.out.println("Deseja jogar novamente? (Digite \"s\" para confirmar)");
            //scanner.nextLine();
            jogarNovamente = scanner.nextLine().toLowerCase();
            
        } while (!jogarNovamente.isEmpty() && jogarNovamente.charAt(0) == 's');
    }
    //#region jogo
    public static void menuPrincipal(){
        
        System.out.println("Bem vindo ao Duelo X1");
        System.out.println("Selecione o modo de jogo:\n| 0: Singleplayer | 1: Multiplayer |");
       
        setMultiplayer(scanner.nextInt() != 0);
        limparConsole();
        
        criarPersonagem(0);
        if (!isMultiplayer()){
            Random r = new Random();
            //A I.A. vai escolher uma classe aleatória
            personagens[1] = new Personagem(Personagem.Classe.values()[r.nextInt(numeroDeClasses)]);
        }
        else {
            limparConsole();
            criarPersonagem(1);
        }
        

        if (personagens[1].getAparencia() == personagens[0].getAparencia()){
            //Isso evita que os personagens tenham a mesma aparência
            personagens[1].setAparencia((char)(personagens[1].getAparencia() + 1));
        }
        
    }

    public static void loopDeJogo(){
        
        aleatorizarPosicoes();
        do {
            atualizarEstadoDoJogo();
            lidarComEntradaDeAcao(0);
            if (jogoEncerrado)
                break;
            if (isMultiplayer()){
                atualizarEstadoDoJogo();
                lidarComEntradaDeAcao(1);
            }
            if (jogoEncerrado)
                break;
            proximoTurno();
        } while (entradaDeAcao != 5 && !jogoEncerrado);
    }
    //#endregion

    //#region turno
    private static int turno = 1; // Variável para controlar o turno do jogo
    public static void proximoTurno(){
        turno++;
    }
    //#endregion
    //#region estado de jogo
    private static boolean jogoEncerrado = false;
    private static int vencedor = 0; 
    public static void encerrarJogo(){
        if (!jogoEncerrado)
            jogoEncerrado = true;
    }
    public static void reiniciarJogo(){
        if (jogoEncerrado) {
            jogoEncerrado = false;
            turno = 1;
            vencedor = 0;
        }
    }
    public static void checarCondicaoDeVitoria(){
        if (personagens[0].isMorto()){
            vencedor = 2;
            encerrarJogo();
        }
        else if (personagens[1].isMorto()){
            vencedor = 1;
            encerrarJogo();
        }
    }
    public static void revelarVencedor(){
        if (vencedor != 0){
            System.out.println("O Jogador " + vencedor + " venceu! Parabéns!");
        }
    }
    //#endregion
    //#region ações
    private static int entradaDeAcao;
    public static void lidarComEntradaDeAcao(int n){
        //Esse método ser para lidar com entradas de ação somente de players humanos

        if (jogoEncerrado)
            return;

        System.out.println("---------------------\nÉ a vez do Jogador " + (n + 1) + "\n---------------------");
        System.out.println("Qual ação deseja fazer?\n| 1: Mover | 2: Atacar | 3: Defender | 4: Ativar poder especial | 5: Sair |");
        entradaDeAcao = scanner.hasNextInt() ? scanner.nextInt() : 0;
        while (entradaDeAcao < 1 || entradaDeAcao > 5) {
            System.out.println("Entrada inválida. Por favor, digite novemente\n| 1: Mover | 2: Atacar | 3: Defender | 4: Ativar poder especial | 5: Sair |");
            scanner.nextLine();
            entradaDeAcao = scanner.hasNextInt() ? scanner.nextInt() : 0;
        }
        realizarAcao(personagens[n], entradaDeAcao);
    }

    public static void realizarAcao(Personagem personagem, int acao){
        switch (acao) {
            case 1:
                realizarMovimento(personagem);
            break;
            case 2:
                realizarAtaque(personagem);
            break;
            case 3:
                realizarDefesa(personagem);
            break;
            case 4:
                realizarAtivacaoDoPoderEspecial(personagem);
            break;
            case 5:
                encerrarJogo();
            return;
            default:
            break;
        }
        System.out.println("Pressione enter para continuar...");
        String entrada = scanner.nextLine();
        checarCondicaoDeVitoria();
    }
    public static void realizarMovimento(Personagem personagem){
        System.out.println("Para onde deseja mover?\n| Cima(W) | Baixo(S) | Esquerda(A) | Direita(D) |");
        
        scanner.nextLine();
        
        int destinoX;
        int destinoY;
        
        boolean caractereInvalido;

        do {
            String entrada = scanner.nextLine().toLowerCase();
            char where = entrada.isEmpty() ? '\0': entrada.charAt(0);
            destinoX = personagem.getX();
            destinoY = personagem.getY();
            caractereInvalido = false;

            switch (where) {
                case 'w':
                    destinoY--;
                break;
                case 's':
                    destinoY++;
                break;
                case 'a':
                    destinoX--;
                break;
                case 'd':
                    destinoX++;
                break;
                default:
                    System.out.println("Caractere inválido");
                    caractereInvalido = true;
                break;
            }
        } while (caractereInvalido || !validarMovimento(destinoX, destinoY, personagem));

        personagem.mover(destinoX, destinoY);
    }
    public static boolean validarMovimento(int x, int y, Personagem personagem){
        if (x < 0 || x >= larguraDoTabuleiro || y < 0 || y >= alturaDoTabuleiro) {
            System.out.println("Destino fora do tabuleiro");
            return false;
        }
        if (personagens[0].estaNaPosicao(x, y) || personagens[1].estaNaPosicao(x, y)) {
            System.out.println("Posições dos personagens coincidem");
            return false;
        }
        return true; 
    }
    public static void realizarAtaque(Personagem personagem){
        Personagem alvo = (personagem == personagens[0]) ? personagens[1] : personagens[0];
        personagem.atacar(alvo);
        scanner.nextLine();
    }
    public static void realizarDefesa(Personagem personagem){
        personagem.defender();
        scanner.nextLine();
    }

    public static void realizarAtivacaoDoPoderEspecial(Personagem personagem){
        Personagem alvo = (personagem == personagens[0]) ? personagens[1] : personagens[0];
        personagem.ativarPoderEspecial(alvo);
        scanner.nextLine();
    }


    //#endregion
    //#region multiplayer
    private static boolean multiplayer = false; // Variável que define se o jogo vai ter 2 players
    public static void setMultiplayer(boolean value){
        multiplayer = value;
    }
    public static boolean isMultiplayer(){
        return multiplayer;
    }
    //#endregion 
    //#region personagens
    private static Personagem[] personagens = new Personagem[2];

    // public static void anularPersonagens(){
    //     for (Personagem personagem: personagens){
    //         if (personagem != null)
    //             personagem = null;
    //     }
    // }

    public static void criarPersonagem(int n) {
        //Esse método serve para quando um player humano vai criar um personagem

        System.out.println("Selecione a classe do Jogador " + (n + 1) + ":\n| 1: Guerreiro | 2: Arqueiro | 3: Mago |");
        int classe;

        //Validar a entrada do usuário
        if (scanner.hasNextInt())
            classe = scanner.nextInt();
        else
            classe = -1;
        
        
        while (classe < 1 || classe > numeroDeClasses) {
            //Validar a classe selecionada
            System.out.println("Classe selecionada para o Jogador " + (n + 1) + " inválida, digite novemente:\n| 1: Guerreiro | 2: Arqueiro | 3: Mago |");
            if (scanner.hasNextInt())
                classe = scanner.nextInt();
            else {
                scanner.nextLine(); // Limpa a entrada inválida
                classe = -1;
            }
        }

        limparConsole();
        
        System.out.println("Defina um nome para o personagem do Jogador " + (n + 1) + ":");
        scanner.nextLine(); //Passa pela quebra de linha
        String nome = scanner.nextLine().trim();
        personagens[n] = new Personagem(Personagem.Classe.values()[classe - 1]);
        personagens[n].adicionarNome(nome);
       
    }
    
    public static void aleatorizarPosicoes(){
        Random a = new Random();
        personagens[0].setPosicao(a.nextInt(larguraDoTabuleiro - 1), a.nextInt(alturaDoTabuleiro - 1));
        personagens[1].setPosicao(a.nextInt(larguraDoTabuleiro - 1), a.nextInt(alturaDoTabuleiro - 1));
    }
    //#endregion
    //#region prints na tela
    public static void limparConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void atualizarEstadoDoJogo(){
        limparConsole();
        printarTabuleiro();
        printarTurno();
        printarInformacoesDosPersonagens();
    }

    public static void printarTabuleiro(){
        for (int i = 0; i < alturaDoTabuleiro + 1; i++) {
            for (int j = 0; j < larguraDoTabuleiro + 1; j++) {
                
                if (i == 0)
                    System.out.print(j == 0 ? " " : j - 1);
                else if (j == 0)
                    System.out.print(i - 1);
                else if (personagens[0].estaNaPosicao(j - 1, i - 1)){
                    System.out.print(personagens[0].getAparencia());
                }
                else if (personagens[1].estaNaPosicao(j - 1, i - 1)){
                    System.out.print(personagens[1].getAparencia());
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
    public static void printarInformacoesDosPersonagens(){
        personagens[0].printarInformacoes();
        personagens[1].printarInformacoes();
    }
    
    //#endregion

}
