package ca.qc.bdeb.sim.tp1;

import ca.qc.bdeb.sim.tp1.algo.comp_pixels;
import ca.qc.bdeb.sim.tp1.algo.moy_hash;
import ca.qc.bdeb.sim.tp1.algo.diff_hash;
import ca.qc.bdeb.sim.tp1.pics.img;
import ca.qc.bdeb.sim.tp1.pics.gallerie;
import java.io.File;
import java.util.ArrayList;

public class mainConsole { // mainConsole script def dans build.gradle -> .\gradlew runConsole
    private static int seuil = 5;
    private static double prct_max = 0.2;  
    private static String chemin_airbnb_petit = "./airbnb-petit";
    private static String chemin_debogage = "./debogage";
    private static comp_pixels comp_pixels = new comp_pixels(seuil, prct_max);
    private static moy_hash moy_hash = new moy_hash(seuil);
    private static diff_hash diff_hash = new diff_hash(seuil);

    public static void main(String[] args) {

        // Des informations de débogage pour vérifier les résultats des différents algorithmes

        System.out.println("========== Analyse debogage doublons algo 1 ========== \n");
        /*
         * • seuil=5, max pourcent=0.2, debogage/pixels1.png vs debogage/pixels2.png
         * • seuil=5, max pourcent=0.2, debogage/pixels1.png vs debogage/pixels3.png
         * • seuil=5, max pourcent=0.2, debogage/pixels1.png vs debogage/pixels4.png
         * • seuil=5, max pourcent=0.5, debogage/pixels1.png vs debogage/pixels4.png
         * • seuil=128, max pourcent=0.49, debogage/pixels1.png vs debogage/pixels4.png
         * • seuil=128, max pourcent=0.50, debogage/pixels1.png vs debogage/pixels4.png
         * • seuil=128, max pourcent=0.51, debogage/pixels1.png vs debogage/pixels4.png
         */
        int[] seuils = {5, 5, 5, 5, 128, 128, 128};
        double[] prct_maxs = {0.2, 0.2, 0.2, 0.5, 0.49, 0.50, 0.51};
        String[] chemin_debogages = {chemin_debogage + "/pixels2.png", chemin_debogage + "/pixels3.png", chemin_debogage + "/pixels4.png", chemin_debogage + "/pixels4.png", chemin_debogage + "/pixels4.png", chemin_debogage + "/pixels4.png", chemin_debogage + "/pixels4.png"};

        for (int i = 0; i < seuils.length; i++) {
            seuil = seuils[i];
            prct_max = prct_maxs[i];
            comp_pixels = new comp_pixels(seuil, prct_max);
            System.out.print("seuil=" + seuils[i] + ", max pourcent=" + prct_maxs[i] + ": ");
            System.out.print("debogage/pixels1.png et " + chemin_debogages[i].substring(chemin_debogage.length()-8) + " ");

            boolean similaires = comp_pixels.comparaisonPixels(new img("image: 0", chemin_debogage + "/pixels1.png"), new img("image: 1", chemin_debogages[i]));
            System.out.print(similaires ? "SIMILAIRE" : "DIFFERENT");
            System.out.println();
    
        }

        System.out.println("========== Analyse matrice 8x8 algo 2 ==========");
        /*
         * • debogage/moyenne-test1.png
         * • debogage/moyenne-test2.png
         * • debogage/moyenne-test3.png
         */
        String[] chemin_moyennes = {chemin_debogage + "/moyenne-test1.png", chemin_debogage + "/moyenne-test2.png", chemin_debogage + "/moyenne-test3.png"};
        for (int i = 0; i < chemin_moyennes.length; i++) {
                System.out.println("Post-hash: " + chemin_moyennes[i] + " :");
                String hash = moy_hash.calculerHash(new img(("image: " + i), chemin_moyennes[i]));
                for (int j = 0; j < hash.length(); j++) {
                    if (j % 8 == 0) {
                        System.out.println();
                    }
                    System.out.print(hash.charAt(j) == '0'? '1' : ' ');
                }
                System.out.println();
        }

        System.out.println("========== Analyse matrice 8x8 algo 3 ==========");
        /*
         * • debogage/diff-test1.png
         * • debogage/diff-test2.png
         * • debogage/diff-test3.png
         */
        String[] chemin_diffs = {chemin_debogage + "/diff-test1.png", chemin_debogage + "/diff-test2.png", chemin_debogage + "/diff-test3.png"};
        for (int i = 0; i < chemin_diffs.length; i++) {
                System.out.println("Post-hash: " + chemin_diffs[i] + " :");
                String hash = diff_hash.calculerHash(new img(("image: " + i), chemin_diffs[i]));
                for (int j = 0; j < hash.length(); j++) {
                    if (j % 8 == 0) {
                        System.out.println();
                    }
                    System.out.print(hash.charAt(j) == '0'? '1' : ' ');
                }
                System.out.println();
        }


        // Une liste textuelle des doublons trouvés pour la gallerie airbnb-petit seulement
        System.out.println("\n========== Analyse doublons airbnb-petit ==========\n");
        /*
         * 1. Comparateur Pixels (seuil différences=8, pourcentage différences max=0.2)
         * 2. Comparateur Pixels (seuil différences=8, pourcentage différences max=0.2)
         * 3. Comparateur Hachage Moyenne (cases différentes max=8)
         * 4. Comparateur Hachage Moyenne (cases différentes max=16)
         * 5. Comparateur Hachage Différences (cases différentes max=8)
         * 6. Comparateur Hachage Différences (cases différentes max=16)
         */

         // chaque i^ème groupe de doublons sur sa propre ligne, numéroté avec [i]. Les noms des fichiers sont ensuite listés un après l’autre, séparés par des espaces.
         
         System.out.println("================== Comparateur Pixels ==================");
         // Comparateur Pixels
         int[] seuils_pixels = {8, 8};
         double[] prct_maxs_pixels = {0.2, 0.2};

         for (int i = 0; i < seuils_pixels.length; i++) {
            System.out.println("Comparateur Pixels (seuil differences=" + seuils_pixels[i] + ", pourcentage differences max=" + prct_maxs_pixels[i] + "):");
            gallerie gallerie = new gallerie(chemin_airbnb_petit, "airbnb-petit");

            ArrayList<ArrayList<img>> groupes = gallerie.grouperSimilaires(seuils_pixels[i], prct_maxs_pixels[i], 1);
            for (int j = 0; j < groupes.size(); j++) {
                System.out.print("[" + (j+1) + "] ");
                for (int k = 0; k < groupes.get(j).size(); k++) {
                    System.out.print((k + 1) + ". " + groupes.get(j).get(k).getNom() + " ");
                    }
                     System.out.println();
            }
            System.out.println("--------------------------------------------------------");
         }
         System.out.println("========================================================\n");

         System.out.println("============== Comparateur Hachage Moyenne =============");
         // Comparateur Hachage Moyenne
         int[] seuils_hachage_moyenne = {8, 16};
         for (int i = 0; i < seuils_hachage_moyenne.length; i++) {
            System.out.println("Comparateur Hachage Moyenne (cases differentes max=" + seuils_hachage_moyenne[i] + "):");
            gallerie gallerie = new gallerie(chemin_airbnb_petit, "airbnb-petit");

            ArrayList<ArrayList<img>> groupes = gallerie.grouperSimilaires(seuils_hachage_moyenne[i], 0, 3);
            for (int j = 0; j < groupes.size(); j++) {
                System.out.print("[" + (j+1) + "] ");
                for (int k = 0; k < groupes.get(j).size(); k++) {
                    System.out.print(groupes.get(j).get(k).getNom() + " ");
                    }
                     System.out.println();
            }
            System.out.println("--------------------------------------------------------");
         }
         System.out.println("========================================================\n");

         System.out.println("=========== Comparateur Hachage Differences ============");
         // Comparateur Hachage Différences
         int[] seuils_hachage_differences = {8, 16};
         for (int i = 0; i < seuils_hachage_differences.length; i++) {
            System.out.println("Comparateur Hachage Differences (cases differentes max=" + seuils_hachage_differences[i] + "):");
            gallerie gallerie = new gallerie(chemin_airbnb_petit, "airbnb-petit");

            ArrayList<ArrayList<img>> groupes = gallerie.grouperSimilaires(seuils_hachage_differences[i], 0, 2);
            for (int j = 0; j < groupes.size(); j++) {
                System.out.print("[" + (j+1) + "] ");
                for (int k = 0; k < groupes.get(j).size(); k++) {
                    System.out.print(groupes.get(j).get(k).getNom() + " ");
                    }
                     System.out.println();
            }
            System.out.println("--------------------------------------------------------");
         }
         System.out.println("========================================================\n");
    }
}