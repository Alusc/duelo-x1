package Personagem;

public class Personagem {
    private String nome = "Ninguém";
    private char aparencia = 'A';
    private int forcaDeAtaque = 1;
    private int forcaDeDefesaPadrao = 1;
    private int forcaDeDefesa = 1;
    private int alcanceDeAtaque = 1;
    private int pontosDeVida = 100;

    public static enum Classe {Guerreiro, Arqueiro, Mago};

    private int x = 0;
    private int y = 0;
    
    //#region construtor
    public Personagem(Classe classe){
        switch (classe) {
            case Guerreiro:
                setNome("Guerreiro");
                setAparencia('G');
                setAtributos(15, 10, 1);
            break;
            case Arqueiro:
                setNome("Arqueiro");
                setAparencia('A');
                setAtributos(8, 5, 5);
            break;
            case Mago:
                setNome("Mago");
                setAparencia('M');
                setAtributos(10, 7, 3);
            break;
            default:
            break;
        }
    }
    //#endregion
    //#region setters

    public void setAtributos(int forcaDeAtaque, int forcaDeDefesaPadrao, int alcanceDeAtaque){
        setForcaDeAtaque(forcaDeAtaque);
        setForcaDeDefesaPadrao(forcaDeDefesa);
        setAlcanceDeAtaque(alcanceDeAtaque);
    }

    public void setNome(String nome){
        if (!nome.isEmpty())
            this.nome = nome;
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
        if (forcaDeDefesa > 0)
            this.forcaDeDefesa = forcaDeDefesa;
    }
    public void setAlcanceDeAtaque(int alcanceDeAtaque){
        if (alcanceDeAtaque > 0)
            this.alcanceDeAtaque = alcanceDeAtaque;
    }
    public void setPosicao(int x, int y){
        this.x = x;
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
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    //#endregion
    //#region ações do personagem
    public void defender(){
        setForcaDeDefesa(getForcaDeDefesaPadrao());
    }
    public void mover(int x, int y){
        setPosicao(x, y);
    }
    public boolean checarPosicao(int x, int y){
        return this.x == x && this.y == y;
    }
    //#endregion
    public int distanciaEntre(Personagem personagem){
        return Math.max(Math.abs(y - personagem.y), Math.abs(x - personagem.x));
    }
    public void printarInformacoes(){
        System.out.println(nome + " (" + aparencia + "): " + pontosDeVida + "/100 PV");
        
    }
}