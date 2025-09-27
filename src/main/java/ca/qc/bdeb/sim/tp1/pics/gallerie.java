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
        System.out.println("Triage done");
        System.out.println(this.toString());
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
            groupes.add(new ArrayList<img>());
            groupes.get(groupes.size() - 1).add(currImg);
            imgRep.remove(0);

            int[] indexToRemove = new int[imgRep.size()];
            for (int j = 0; j < imgRep.size(); j++) {
                switch (algo) {
                    case 1:
                        if (moy_hash.comparaisonHash(imgRep.get(j), currImg)) {
                            groupes.get(groupes.size() - 1).add(imgRep.get(j));
                            indexToRemove[j] = 1;
                        }
                        break;
                    case 2:
                        if (diff_hash.comparaisonHash(imgRep.get(j), currImg)) {
                            groupes.get(groupes.size() - 1).add(imgRep.get(j));
                            indexToRemove[j] = 1;
                        }
                        break;
                    case 3:
                        if (comp_pixels.comparaisonPixels(imgRep.get(j), currImg)) {
                            groupes.get(groupes.size() - 1).add(imgRep.get(j));
                            indexToRemove[j] = 1;
                        }
                        break;
                }    
            }
                for (int k = 0; k < imgRep.size(); k++) {
                    if (indexToRemove[k] == 1) {
                        imgRep.remove(k);
                    }
                }
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
