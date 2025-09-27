package ca.qc.bdeb.sim.tp1.algo;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ca.qc.bdeb.sim.tp1.pics.GestionnaireImages;
import ca.qc.bdeb.sim.tp1.pics.img;

// Classe pour comparer les pixels des images
// Ce qu'il faut faire:
// 1. Lire les images 
// 2. Convertir les images en matrices de pixels  (avec GestionnaireImages -> toPixels)
// 3. Comparer les pixels des images (loop, cmb different??) 
// 4. Retourner le resultat (true si similaires, false si differents -> dependant du seuil et du pourcentage max)
public class comp_pixels {

    private int seuil;
    private double prct_max;

    public comp_pixels(int seuil, double prct_max) {
        this.seuil = seuil;
        this.prct_max = prct_max;
    }

    // Comparer les pixels des images (true si similaires, false si differents
    public boolean comparaisonPixels(img a, img b) {
        try {
            // Charger les images
            BufferedImage image1 = GestionnaireImages.lireImage(a.getChemin());
            BufferedImage image2 = GestionnaireImages.lireImage(b.getChemin());

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
            System.out.println("Erreur lors de la comparaison des images");
            return false;
        }
    }
}
