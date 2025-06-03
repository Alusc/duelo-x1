package duelox1.src.main.java.com.duelox1.Personagem;

public class Personagem {
    private String nome = "Ninguém";
    private Classe classe;
    private char aparencia = 'A';
    private int forcaDeAtaque = 1;
    private int forcaDeDefesaPadrao = 1;
    private int forcaDeDefesa = 1;
    private int alcanceDeAtaque = 1;
    private int pontosDeVida = 100;
    private boolean usouPoderEspecial = false;
    private boolean morto = false;

    public static enum Classe {Guerreiro, Arqueiro, Mago};

    private int x = 0;
    private int y = 0;
    
    //#region construtor
    public Personagem(Classe classe){
        this.classe = classe;
        switch (classe) {
            case Guerreiro:
                setNome("Guerreiro");
                setAparencia('G');
                setAtributos(16, 10, 1);
            break;
            case Arqueiro:
                setNome("Arqueiro");
                setAparencia('A');
                setAtributos(12, 5, 5);
            break;
            case Mago:
                setNome("Mago");
                setAparencia('M');
                setAtributos(14, 7, 3);
            break;
            default:
            break;
        }
    }
    //#endregion
    //#region setters

    public void setAtributos(int forcaDeAtaque, int forcaDeDefesaPadrao, int alcanceDeAtaque){
        setForcaDeAtaque(forcaDeAtaque);
        setForcaDeDefesaPadrao(forcaDeDefesaPadrao);
        setAlcanceDeAtaque(alcanceDeAtaque);
    }

    public void setNome(String nome){
        if (!nome.isEmpty())
            this.nome = nome;
    }
    public void adicionarNome(String nome){
        if (!nome.isEmpty())
            this.nome += " " + nome;
    }
    public void setAparencia(char aparencia) {
        if (aparencia != '\0' && aparencia != ' ')
            this.aparencia = aparencia;
        
    }
    public void setForcaDeAtaque(int forcaDeAtaque){
        if (forcaDeAtaque > 0)
            this.forcaDeAtaque = forcaDeAtaque;
    }
    public void setForcaDeDefesaPadrao(int forcaDeDefesaPadrao) {
        if (forcaDeDefesaPadrao > 0) {
            this.forcaDeDefesaPadrao = forcaDeDefesaPadrao;
            setForcaDeDefesa(forcaDeDefesaPadrao);
        }
    }
    public void setForcaDeDefesa(int forcaDeDefesa){
            this.forcaDeDefesa = Math.max(0, forcaDeDefesa);
    }
    public void setAlcanceDeAtaque(int alcanceDeAtaque){
        if (alcanceDeAtaque > 0)
            this.alcanceDeAtaque = alcanceDeAtaque;
    }
    public void setPontosDeVida(int pontosDeVida){
        this.pontosDeVida = Math.max(0, pontosDeVida);
        if (this.pontosDeVida == 0){
            morto = true;
        }
    }
    public void setPosicao(int x, int y){
        if (this.x != x)
            this.x = x;
        if (this.y != y)
            this.y = y;
    }
    //#endregion
    //#region getters
    public String getNome(){
        return nome;
    }
    public char getAparencia() {
        return aparencia;
    }
    public int getForcaDeAtaque(){
        return forcaDeAtaque;
    }
    public int getForcaDeDefesaPadrao(){
        return forcaDeDefesaPadrao;
    }
    public int getForcaDeDefesa(){
        return forcaDeDefesa;
    }
    public int getAlcanceDeAtaque(){
        return alcanceDeAtaque;
    }
    public int getPontosDeVida(){
        return pontosDeVida;
    }
    public Classe getClasse(){
        return classe;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isMorto(){
        return morto;
    }
    public boolean usouPoderEspecial(){
        return usouPoderEspecial;
    }
    //#endregion
    //#region ações do personagem
    public void atacar(Personagem personagem){
        if (distanciaEntre(personagem) <= alcanceDeAtaque) {
            personagem.tomarDano(forcaDeAtaque);
        }
        else
            System.out.println("O ataque falhou devido à distância do inimigo");
    }
    public void defender(){
        System.out.println("A defesa do " + nome + " foi restaurada para " + forcaDeDefesaPadrao);
        setForcaDeDefesa(forcaDeDefesaPadrao);
    }
    public void mover(int x, int y){
        System.out.println("O " + nome + " se moverá para (" + x + ", " + y + ")");
        setPosicao(x, y);
    }
    public void tomarDano(int ataque){
        int danoResultante = Math.max(0, ataque - forcaDeDefesa);
        System.out.println("O ataque causou " + danoResultante + " de dano no " + nome);
        setForcaDeDefesa(forcaDeDefesa - ataque);
        setPontosDeVida(pontosDeVida - danoResultante);
    }
    public void ativarPoderEspecial(Personagem alvo){
        if (usouPoderEspecial){
            System.out.println("O " + nome + " já usou seu poder especial na partida");
            return;
        }
        usouPoderEspecial = true;
        switch (classe) {
            case Guerreiro:
                System.out.println("A força de ataque do " + nome + " dobrou para 32");
                setForcaDeAtaque(32);    
            break;
            case Arqueiro:
                System.out.println("O alcance de ataque do " + nome + " subiu para 8");
                setAlcanceDeAtaque(8);
            break;
            case Mago:
                System.out.println("Os pontos de vida dos personagens foram trocados");
                int temp = alvo.pontosDeVida;
                alvo.setPontosDeVida(pontosDeVida);
                setPontosDeVida(temp);
            break;
            default:
            break;
        }

    }
    //#endregion
    //#region métodos úteis
    public boolean estaNaPosicao(int x, int y){
        return this.x == x && this.y == y;
    }
    public int distanciaEntre(Personagem personagem){
        return Math.max(Math.abs(y - personagem.y), Math.abs(x - personagem.x));
    }
    public void printarInformacoes(){
        System.out.println(nome + " (" + aparencia + "): " + pontosDeVida + "/100 PV | " + forcaDeDefesa + "/" + forcaDeDefesaPadrao + " FD");
        
    }
    //#endregion
}