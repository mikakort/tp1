package ca.qc.bdeb.sim.tp1.pics;

import ca.qc.bdeb.sim.tp1.algo.moy_hash;
import ca.qc.bdeb.sim.tp1.algo.diff_hash;
import ca.qc.bdeb.sim.tp1.algo.comp_pixels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Classe pour un groupe d'images
public class gallerie {

    private List<img> images;

    public gallerie(List<img> images) {
        this.images = images;
    }

    public void ajouterImage(img image) {
        images.add(image);
    }

    // Source:
    // https://www.geeksforgeeks.org/java/java-program-to-sort-names-in-an-alphabetical-order/
    // (Un peu de modifs pour le type img)

    public void trierAlphabetique() {
        String[] noms = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            noms[i] = images.get(i).getNom();
        }
       Arrays.sort(noms);

       this.images.clear();
       for (int i = 0; i < images.size(); i++) {
        this.images.add(new img(noms[i], images.get(i).getChemin()));
       }

        img temp;
        for (int i = 0; i < images.size(); i++) {
            for (int j = i + 1; j < images.size(); j++) {
              
                // to compare one string with other strings
                if (images.get(i).getNom().compareTo(images.get(j).getNom()) > 0) {
                    // swapping
                    temp = images.get(i);
                    images.set(i, images.get(j));
                    images.set(j, temp);
                }
            }
        }
        System.out.println("\nTriage fini\n");
    }

    public ArrayList<ArrayList<img>> grouperSimilaires(int diff, double prct_max, int algo) {
        algo = (algo < 4 && algo > 0) ? algo : 1;
        ArrayList<ArrayList<img>> groupes = new ArrayList<ArrayList<img>>();
        List<img> imgRep = new ArrayList<img>(images);
        this.trierAlphabetique();

        moy_hash moy_hash = new moy_hash(diff);
        diff_hash diff_hash = new diff_hash(diff);
        comp_pixels comp_pixels = new comp_pixels(diff, prct_max);


        while (imgRep.size() > 0) {
            img currImg = imgRep.get(0);
            ArrayList<img> groupe = new ArrayList<img>();
            groupe.add(currImg);

            // Collection d'images a enlever (pas par indexe cette fois)
            List<img> prEnlever = new ArrayList<img>();
            // Commence j=1 car currImg est j=0 et est deja dans le groupe
            for (int j = 1; j < imgRep.size(); j++) {
                img comparaison = imgRep.get(j);
                boolean similaire = false;
                switch (algo) {
                    case 1:
                        if (moy_hash.comparaisonHash(comparaison, currImg)) {
                            similaire = true;
                        }
                        break;
                    case 2:
                        if (diff_hash.comparaisonHash(comparaison, currImg)) {
                            similaire = true;
                        }
                        break;
                    case 3:
                        if (comp_pixels.comparaisonPixels(comparaison, currImg)) {
                            similaire = true;
                        }
                        break;
                }
                if (similaire) {
                    groupe.add(comparaison);
                    prEnlever.add(comparaison);
                }
            }
            // On enleve tt les images du groupe
            imgRep.removeAll(prEnlever);
            imgRep.remove(currImg);

            // et on ajoute le groupe a la liste
            groupes.add(groupe);
        }
        return groupes;
    }
    
    

    

    // get/set
    public List<img> getImages() {
        return images;
    }

    public int getTaille() {
        return images.size();
    }

    public void setImages(List<img> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("gallerie{\n");
        for (img image : images) {
            sb.append(image.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
