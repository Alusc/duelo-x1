package com.duelox1.Jogo;
import java.util.Scanner;

import com.duelox1.Personagem.Personagem;
import com.duelox1.Adversario.AdversarioVirtual;

import java.util.Random;


public class Jogo {
    private static final int larguraDoTabuleiro = 10;
    private static final int alturaDoTabuleiro = 10;

    private static final int limiteDeTurnos = 30;
    
    private static int numeroDeClasses = Personagem.Classe.values().length;
    
    private static AdversarioVirtual adversarioVirtual; 
    
    public static Scanner scanner = new Scanner(System.in);

    private static int turno = 1; // Variável para controlar o turno do jogo
    
    private static boolean jogoEncerrado = false;
    private static int vencedor = 0; 
    
    private static int entradaDeAcao;
    private static boolean entradaDeAcaoHumana = true;
    
    
    private static boolean multiplayer = false; // Variável que define se o jogo vai ter 2 players

    private static Personagem[] personagens = new Personagem[2];


    //#region main

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
            jogarNovamente = scanner.nextLine().toLowerCase();
            
        } while (!jogarNovamente.isEmpty() && jogarNovamente.charAt(0) == 's');
    }
    //#region jogo
    public static void menuPrincipal(){
        //Esse é o menu principal do jogo, onde o player ou os players vão escolher se o jogo vai ser e dois e também
        //os atributos e classes dos personagens
        System.out.println("Bem vindo ao Duelo X1");
        System.out.println("Selecione o modo de jogo:\n| 0: Singleplayer | 1: Multiplayer |");
       
        setMultiplayer(scanner.nextInt() != 0);
        limparConsole();
        
        criarPersonagem(0);
        if (isMultiplayer()){
            limparConsole();
            criarPersonagem(1);
        }
        else {
            criarPersonagemDaIa();
        }
        

        if (personagens[1].getAparencia() == personagens[0].getAparencia()){
            //Isso evita que os personagens tenham a mesma aparência
            personagens[1].setAparencia((char)(personagens[1].getAparencia() + 1));
        }
        
    }

    public static void loopDeJogo(){
        //Esse é o loop de jogo. Método no qual o jogo de fato irá rodar, até que a variável jogoEncerrado seja verdadeira
        
        aleatorizarPosicoes();
        do {
            atualizarEstadoDoJogo();
            lidarComEntradaDeAcao(0);
            //É necessário fazer essas checagens no meio do loop pois o jogo pode acabar logo após a entrada de ação
            //de cada player e para isso o loop tem que ser interrompido antes do próximo método ser lido
            if (jogoEncerrado)
                break;
            atualizarEstadoDoJogo();
            if (isMultiplayer()){
                lidarComEntradaDeAcao(1);
            }
            else {
                lidarComEntradaDeAcaoDaIa();
            }
            if (jogoEncerrado)
                break;
            proximoTurno();
        } while (!jogoEncerrado);
    }
    //#endregion

    //#region turno
    public static void proximoTurno(){
        turno++;
        checarCondicaoDeVitoriaPorLimiteDeTurnos();
    }
    //#endregion
    //#region estado de jogo
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
    public static void checarCondicaoDeVitoriaPorLimiteDeTurnos(){
        if (turno >= limiteDeTurnos + 1) {
            checarCondicaoDeVitoriaPorMaiorVida();
            encerrarJogo();
        }
    }
    public static void checarCondicaoDeVitoriaPorMaiorVida(){
        if (personagens[0].getPontosDeVida() > personagens[1].getPontosDeVida()){
            vencedor = 1;
        }
        else if (personagens[1].getPontosDeVida() > personagens[0].getPontosDeVida()){
            vencedor = 2;
        }
    }

    public static void revelarVencedor(){
        if (vencedor != 0){
            System.out.println("O Jogador " + vencedor + " venceu! Parabéns!");
        }
        else {
            System.out.println("O jogo terminou em empate!");
        }
        System.out.println();
    }
    //#endregion
    //#region ações
    
    public static void lidarComEntradaDeAcao(int n){
        //Esse método serve para lidar com entradas de ação somente de players humanos
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
        entradaDeAcaoHumana = true;
        realizarAcao(personagens[n], entradaDeAcao);
    }

    public static void lidarComEntradaDeAcaoDaIa(){
        
        if (jogoEncerrado)
            return;

        System.out.println("---------------------\nÉ a vez do Jogador 2\n---------------------");
        
        int acao = adversarioVirtual.acaoMaisProvavel();
        System.out.println("Seu adversário escolheu " + adversarioVirtual.textoDeAcaoMaisProvavel(acao) + "...");
        
        entradaDeAcaoHumana = false;
        realizarAcao(personagens[1], acao);
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
                checarCondicaoDeVitoriaPorMaiorVida();
                encerrarJogo();
                scanner.nextLine();
            return;
            default:
            break;
        }
        System.out.println("Pressione enter para continuar...");
        scanner.nextLine();
        checarCondicaoDeVitoria();
    }
    public static void realizarMovimento(Personagem personagem){
        if (entradaDeAcaoHumana) {
            System.out.println("Para onde deseja mover?\n| Cima(W) | Baixo(S) | Esquerda(A) | Direita(D) |");
            
            scanner.nextLine();
        }
        
        int destinoX;
        int destinoY;
        
        boolean caractereInvalido;

        do {
            String entrada;
            if (entradaDeAcaoHumana)
                entrada = scanner.nextLine().toLowerCase();
            else {
                entrada = adversarioVirtual.direcaoMaisProvavel();
            }
            
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
        if (entradaDeAcaoHumana)
            scanner.nextLine();
    }
    public static void realizarDefesa(Personagem personagem){
        personagem.defender();
        if (entradaDeAcaoHumana)
            scanner.nextLine();
    }
    public static void realizarAtivacaoDoPoderEspecial(Personagem personagem){
        Personagem alvo = (personagem == personagens[0]) ? personagens[1] : personagens[0];
        personagem.ativarPoderEspecial(alvo);
        if (entradaDeAcaoHumana)
            scanner.nextLine();
    }

    //#endregion
    //#region multiplayer
    public static void setMultiplayer(boolean value){
        multiplayer = value;
    }
    public static boolean isMultiplayer(){
        return multiplayer;
    }
    //#endregion 
    //#region personagens
    
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

    public static void criarPersonagemDaIa(){
        if (adversarioVirtual == null)
            adversarioVirtual = new AdversarioVirtual();
        else
            adversarioVirtual.setPersonagem();
        personagens[1] = adversarioVirtual.getPersonagem(); 
        adversarioVirtual.setPersonagemDoHumano(personagens[0]);
    }
    
    public static void aleatorizarPosicoes(){
        Random a = new Random();
        int x1 = a.nextInt(larguraDoTabuleiro - 1);
        int y1 = a.nextInt(alturaDoTabuleiro - 1);

        int x2 = a.nextInt(larguraDoTabuleiro - 1);
        int y2 = a.nextInt(alturaDoTabuleiro - 1);
    
        //Impede que os personagens nasçam na mesma posição
        if (x2 == x1 && y2 == y1){
            x2++;
            if (x2 == larguraDoTabuleiro){
                x2 = 0;
            }
        }

        personagens[0].setPosicao(x1, y1);
        personagens[1].setPosicao(x2, y2);
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
        System.out.println(turno == limiteDeTurnos ? "Turno final" : "Turno: " + turno);
    }
    public static void printarInformacoesDosPersonagens(){
        personagens[0].printarInformacoes();
        personagens[1].printarInformacoes();
    }
    
    //#endregion

}
