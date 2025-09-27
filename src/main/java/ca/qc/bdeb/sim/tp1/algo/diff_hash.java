package ca.qc.bdeb.sim.tp1.algo;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ca.qc.bdeb.sim.tp1.pics.GestionnaireImages;
import ca.qc.bdeb.sim.tp1.pics.img;

// Classe pour comparer les hashs des images
// Ce qu'il faut faire:
// 1. Lire les images
// 2. Hasher les images
// 3. Comparer les hashs des images
// 4. Retourner le resultat (true si similaires, false si differents)
public class diff_hash {

    private int seuil;

    public diff_hash(int seuil) {
        this.seuil = seuil;
    }

    public String calculerHash(img a) {
        try {
            BufferedImage img = GestionnaireImages.lireImage(a.getChemin());

            // Redimensionner 8x8
            img = GestionnaireImages.redimensionner(img, 8, 8);
            int[][] pixels = GestionnaireImages.toPixels(img);

            // hashing sur les diff horizontales/verticales
            StringBuilder hash = new StringBuilder();
            for (int y = 0; y < 8; y++) { // On commence en haut a gauche
                for (int x = 0; x < 8; x++) {
                    int p_i = pixels[y][x];
                    int p_i_1; // p_i vs pixel de droite (i+1)

                    if (x < 7) { // On va a droite
                        p_i_1 = pixels[y][x + 1];
                    } else if (y < 7) { // On va en bas et ensuite a droute
                        p_i_1 = pixels[y + 1][x];
                    } else {
                        p_i_1 = pixels[y][x];
                    }

                    // On compare les pixels 1 ou 0 selon la clarté
                    if (p_i > p_i_1) {
                        hash.append("1");
                    } else {
                        hash.append("0");
                    }

                }
            }

            return hash.toString();
        } catch (IOException e) {
            System.out.println("Erreur lors du calcul du hash");
            return "";
        }
    }

    public boolean comparaisonHash(img a, img b) {
        String hash1 = calculerHash(a);
        String hash2 = calculerHash(b);

        // vide? return false
        if (hash1.isEmpty() || hash2.isEmpty()) {
            return false;
        }

        // Compter les diffs
        int diff = 0;
        for (int i = 0; i < hash1.length(); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                diff++;
            }
        }

        return diff <= seuil;
    }
}
