package duelox1.src.main.java.com.duelox1.Adversario;
import java.util.Random;
import duelox1.src.main.java.com.duelox1.Personagem.Personagem;

public class AdversarioVirtual {
    private Personagem personagem;
    private Personagem personagemDoHumano;
    Random numeroAleatorio = new Random();
    public AdversarioVirtual() {
        setPersonagem();
    }

    private boolean fugir;

    public void setPersonagem(){
        //A I.A. vai escolher uma classe aleatória para seu personagem
        fugir = false;
        int numeroDeClasses = Personagem.Classe.values().length;
        personagem = new Personagem(Personagem.Classe.values()[numeroAleatorio.nextInt(numeroDeClasses)]);
    }
    public void setPersonagemDoHumano(Personagem personagemDoHumano){
        this.personagemDoHumano = personagemDoHumano;
    }

    
    public Personagem getPersonagem() {
        return personagem;
    }

    public int acaoMaisProvavel() {
        if (personagem == null)
            return -1;

        int acao = 0;

        switch (personagem.getClasse()) {
            case Personagem.Classe.Guerreiro:
                acao = acaoMaisProvavelGuerreiro();    
            break;
            case Personagem.Classe.Arqueiro:
                acao = acaoMaisProvavelArqueiro();
            break;
            case Personagem.Classe.Mago:
               acao = acaoMaisProvavelMago();
            break;
            default:
            break;
        }

        return acao;
    }
    private int acaoMaisProvavelGuerreiro(){
        
        int distanciaEntrePersonagens = personagem.distanciaEntre(personagemDoHumano);

        if (personagemDoHumano.getPontosDeVida() <= 40){
            if (distanciaEntrePersonagens <= personagem.getAlcanceDeAtaque()){
                return 2;
            }
            return 1;
        }

        if (personagem.getPontosDeVida() <= 20 && personagem.getForcaDeDefesa() == 0)
            return 3;
        
        if (personagem.distanciaEntre(personagemDoHumano) <= personagem.getAlcanceDeAtaque())
            return 2;

        if (personagem.usouPoderEspecial())
            return 1;

        return 4;
    }
    private int acaoMaisProvavelArqueiro(){
        int distanciaEntrePersonagens = personagem.distanciaEntre(personagemDoHumano);

        if (personagemDoHumano.getPontosDeVida() <= 24){
            if (distanciaEntrePersonagens <= personagem.getAlcanceDeAtaque()){
                return 2;
            }
            fugir = false;
            return 1;
        }
        
        if (personagem.getPontosDeVida() <= 20 && personagem.getForcaDeDefesa() == 0){
            return 3;
        }
        if (distanciaEntrePersonagens <= 3){
            fugir = true;
            return 1;
        }

        if (distanciaEntrePersonagens <= personagem.getAlcanceDeAtaque()){
            return 2;
        }

        if (personagem.usouPoderEspecial()){
            fugir = false;
            return 1;
        }

        return 4;

    }
    private int acaoMaisProvavelMago(){

        
        int distanciaEntrePersonagens = personagem.distanciaEntre(personagemDoHumano);


        if (personagemDoHumano.getPontosDeVida() <= 30){
            if (distanciaEntrePersonagens <= personagem.getAlcanceDeAtaque()){
                return 2;
            }
            fugir = false;
            return 1;
        }

        if (personagem.getPontosDeVida() <= 20){
            if (!personagem.usouPoderEspecial())
                return 4;
            if (personagem.getForcaDeDefesa() == 0)
                return 3;
        }


        if (distanciaEntrePersonagens == 1){
            fugir = true;
            return 1;
        }

        if (distanciaEntrePersonagens <= personagem.getAlcanceDeAtaque()){
            return 2;
        }

        fugir = false;
        return 1;

    }
    public String direcaoMaisProvavel(){

        float direcaoHorizontal = Math.signum((float)personagemDoHumano.getX() - personagem.getX());
        float direcaoVertical = Math.signum((float)personagemDoHumano.getY() - personagem.getY());
        
        if (fugir){
            direcaoHorizontal *= -1;
            direcaoVertical *= -1;
        }

        if (direcaoHorizontal == -1 && personagem.getX() > 0)
            return "a";
        if (direcaoHorizontal == 1 && personagem.getX() < 9)
            return "d";

        if (direcaoVertical == -1 && personagem.getY() > 0)
            return "w";
        if (direcaoVertical == 1 && personagem.getY() < 9)
            return "s";

        if (personagem.getY() > 4){
            if (personagemDoHumano.getY() == personagem.getY() + 1)
                return personagem.getX() < 9 ? "d" : "a";
            return "w";
        }
        if (personagemDoHumano.getY() == personagem.getY() - 1)
            return personagem.getX() < 9 ? "d" : "a";
        return "s";
        
        
    }

    public String textoDeAcaoMaisProvavel(int acao){
        switch (acao) {
            case 1:
                return "se mover";
            case 2:
                return "atacar";
            case 3:
                return "se defender";
            case 4:
                return "usar o poder especial";
        
            default:
                return "";
        }
    }
}
