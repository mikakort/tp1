package ca.qc.bdeb.sim.tp1.algo;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ca.qc.bdeb.sim.tp1.pics.GestionnaireImages;

// Classe pour comparer les pixels des images
// Ce qu'il faut faire:
// 1. Lire les images 
// 2. Convertir les images en matrices de pixels  (avec GestionnaireImages -> toPixels)
// 3. Comparer les pixels des images (loop, cmb different??) 
// 4. Retourner le resultat (true si similaires, false si differents -> dependant du seuil et du pourcentage max)
public class CompPixels extends ComparateurImages {

    private int seuil;
    private double prct_max;

    public CompPixels(int seuil, double prct_max) {
        this.seuil = seuil;
        this.prct_max = prct_max;
    }

    // Comparer les pixels des images (true si similaires, false si differents
    public boolean imagesSimilaires(String ch1, String ch2) {
        try {
            // Charger les images
            BufferedImage image1 = GestionnaireImages.lireImage(ch1);
            BufferedImage image2 = GestionnaireImages.lireImage(ch2);

            // get taille des images
            int largeur = Math.min(image1.getWidth(), image2.getWidth());
            int hauteur = Math.min(image1.getHeight(), image2.getHeight());
            // Redimensionner les images
            image1 = GestionnaireImages.redimensionner(image1, largeur, hauteur);
            image2 = GestionnaireImages.redimensionner(image2, largeur, hauteur);

            // Convertir les images en matrices de pixels (0,255)
            int[][] pixels1 = GestionnaireImages.toPixels(image1);
            int[][] pixels2 = GestionnaireImages.toPixels(image2);

            // Compter les pixels differents
            int diff = 0;
            int totalPixels = largeur * hauteur;
            for (int y = 0; y < hauteur; y++) {
                for (int x = 0; x < largeur; x++) {
                    int spread = Math.abs(pixels1[y][x] - pixels2[y][x]);
                    if (spread > seuil) {
                        diff++;
                    }
                }
            }

            // Calculer le pourcentage de pixels differents et comparer contre le max
            double prct_diff = (double) diff / totalPixels;

            return prct_diff <= prct_max;
        } catch (IOException e) {
            // En cas d'erreur de lecture, on considère les images comme différentes
            throw new RuntimeException("Erreur lors de la lecture des images: " + e.getMessage(), e);
        }
    }
}
