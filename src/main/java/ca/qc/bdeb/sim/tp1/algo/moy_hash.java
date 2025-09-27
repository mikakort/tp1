package ca.qc.bdeb.sim.tp1.algo;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ca.qc.bdeb.sim.tp1.pics.GestionnaireImages;
import ca.qc.bdeb.sim.tp1.pics.img;

// Ce qu'il faut faire:
// 1. Lire les images
// 2. Hasher les images en fct de la moyenne des pixels
// 3. Comparer les hashs des images
// 4. Retourner le resultat (true si similaires, false si differents)
public class moy_hash {

    private int seuil;

    public moy_hash(int seuil) {
        this.seuil = seuil;
    }

    private String calculerHash(img a) {
        try {
            BufferedImage img = GestionnaireImages.lireImage(a.getChemin());

            // Redimensionner 8x8
            img = GestionnaireImages.redimensionner(img, 8, 8);
            int[][] pixels = GestionnaireImages.toPixels(img);

            // Calculer la moyenne des pixels
            int t = 0;
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    t += pixels[y][x];
                }
            }
            double moyenne = t / 64.0;

            // Hash -> 1 si pixel > moyenne, 0 sinon
            StringBuilder hash = new StringBuilder();
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    if (pixels[y][x] > moyenne) {
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
