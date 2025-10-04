package ca.qc.bdeb.sim.tp1;

import ca.qc.bdeb.sim.tp1.algo.CompPixels;
import ca.qc.bdeb.sim.tp1.algo.MoyHash;
import ca.qc.bdeb.sim.tp1.algo.DiffHash;
import ca.qc.bdeb.sim.tp1.pics.Gallerie;
import java.util.ArrayList;

public class mainConsole { // mainConsole script def dans build.gradle -> .\gradlew runConsole
    // Constantes pour les chemins (non modifiables)
    private static final String CHEMIN_AIRBNB_PETIT = "./airbnb-petit";
    private static final String CHEMIN_DEBOGAGE = "./debogage";

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
        String[] chemin_debogages = {CHEMIN_DEBOGAGE + "/pixels2.png", CHEMIN_DEBOGAGE + "/pixels3.png", CHEMIN_DEBOGAGE + "/pixels4.png", CHEMIN_DEBOGAGE + "/pixels4.png", CHEMIN_DEBOGAGE + "/pixels4.png", CHEMIN_DEBOGAGE + "/pixels4.png", CHEMIN_DEBOGAGE + "/pixels4.png"};

        for (int i = 0; i < seuils.length; i++) {
            // Variables locales pour chaque itération
            CompPixels comp_pixels = new CompPixels(seuils[i], prct_maxs[i]);
            System.out.print("seuil=" + seuils[i] + ", max pourcent=" + prct_maxs[i] + ": ");
            System.out.print("debogage/pixels1.png et " + chemin_debogages[i].substring(CHEMIN_DEBOGAGE.length()-8) + " ");

            boolean similaires = comp_pixels.imagesSimilaires(CHEMIN_DEBOGAGE + "/pixels1.png", chemin_debogages[i]);
            System.out.println(chemin_debogages[i] + " " + CHEMIN_DEBOGAGE + "/pixels1.png");
            System.out.print(similaires ? "SIMILAIRE" : "DIFFERENT");
            System.out.println();
    
        }

        System.out.println("========== Analyse matrice 8x8 algo 2 ==========");
        /*
         * • debogage/moyenne-test1.png
         * • debogage/moyenne-test2.png
         * • debogage/moyenne-test3.png
         */
        String[] chemin_moyennes = {CHEMIN_DEBOGAGE + "/moyenne-test1.png", CHEMIN_DEBOGAGE + "/moyenne-test2.png", CHEMIN_DEBOGAGE + "/moyenne-test3.png"};
        MoyHash moy_hash = new MoyHash(5);
        for (int i = 0; i < chemin_moyennes.length; i++) {
                System.out.println("Hasher: " + chemin_moyennes[i] + " :");
                String hash = moy_hash.calculerHash(chemin_moyennes[i]);  
                for (int j = 0; j < hash.length(); j++) {
                    if (j % 8 == 0) {
                        System.out.println();
                    }
                    System.out.print(hash.charAt(j) == '1'? '1' : ' ');
                }
                System.out.println();
        }

        System.out.println("========== Analyse matrice 8x8 algo 3 ==========");
        /*
         * • debogage/diff-test1.png
         * • debogage/diff-test2.png
         * • debogage/diff-test3.png
         */
        String[] chemin_diffs = {CHEMIN_DEBOGAGE + "/diff-test1.png", CHEMIN_DEBOGAGE + "/diff-test2.png", CHEMIN_DEBOGAGE + "/diff-test3.png"};
        DiffHash diff_hash = new DiffHash(5);
        for (int i = 0; i < chemin_diffs.length; i++) {
                System.out.println("Post-hash: " + chemin_diffs[i] + " :");
                String hash = diff_hash.calculerHash(chemin_diffs[i]);
                for (int j = 0; j < hash.length(); j++) {
                    if (j % 8 == 0) {
                        System.out.println();
                    }
                    System.out.print(hash.charAt(j) == '1'? '1' : ' ');
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
            Gallerie gallerie = new Gallerie(CHEMIN_AIRBNB_PETIT, "airbnb-petit");

            ArrayList<ArrayList<String>> groupes = gallerie.grouperSimilaires(new CompPixels(seuils_pixels[i], prct_maxs_pixels[i]));
            for (int j = 0; j < groupes.size(); j++) {
                System.out.print("[" + (j+1) + "] ");
                for (int k = 0; k < groupes.get(j).size(); k++) {
                    System.out.print((k + 1) + ". " + groupes.get(j).get(k).split("t\\\\")[1] + " ");
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
            Gallerie gallerie = new Gallerie(CHEMIN_AIRBNB_PETIT, "airbnb-petit");

            ArrayList<ArrayList<String>> groupes = gallerie.grouperSimilaires(new MoyHash(seuils_hachage_moyenne[i]));
            for (int j = 0; j < groupes.size(); j++) {
                System.out.print("[" + (j+1) + "] ");
                for (int k = 0; k < groupes.get(j).size(); k++) {
                    System.out.print(groupes.get(j).get(k).split("t\\\\")[1] + " ");
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
            Gallerie gallerie = new Gallerie(CHEMIN_AIRBNB_PETIT, "airbnb-petit");

            ArrayList<ArrayList<String>> groupes = gallerie.grouperSimilaires(new DiffHash(seuils_hachage_differences[i]));
            for (int j = 0; j < groupes.size(); j++) {
                System.out.print("[" + (j+1) + "] ");
                for (int k = 0; k < groupes.get(j).size(); k++) {
                    System.out.print(groupes.get(j).get(k).split("t\\\\")[1] + " ");
                    }
                     System.out.println();
            }
            System.out.println("--------------------------------------------------------");
         }
         System.out.println("========================================================\n");
    }
}