package ca.qc.bdeb.sim.tp1.pics;

// Classe pour detatiller les images
public class img {

    private String nom;
    private String chemin;

    public img(String nom, String chemin) {
        this.nom = nom;
        this.chemin = chemin;
    }

    public String getNom() {
        return nom;
    }

    public String getChemin() {
        return chemin;
    }

    @Override
    public String toString() {
        return "img{" + "nom='" + nom + '\'' + ", chemin='" + chemin + '\'' + '}';
    }
}
