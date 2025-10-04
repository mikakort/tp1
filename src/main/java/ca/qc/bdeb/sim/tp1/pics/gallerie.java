package ca.qc.bdeb.sim.tp1.pics;

import ca.qc.bdeb.sim.tp1.algo.ComparateurImages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;

// Classe pour un groupe d'images
public class Gallerie {

    private List<String> images = new ArrayList<String>();

   public Gallerie(String cheminDossier, String regex) {
    File dossier = new File(cheminDossier);
    File[] fichiers = dossier.listFiles();
    int nbFichiers = (fichiers != null) ? fichiers.length : 0;
    for (int j = 0; j < nbFichiers; j++) {
        this.ajouterImage(fichiers[j].getPath());
    }
}

    public void ajouterImage(String image) {
        images.add(image);
    }

    // Source:
    // https://www.geeksforgeeks.org/java/java-program-to-sort-names-in-an-alphabetical-order/
    // (Un peu de modifs pour le type img)

    public void trierAlphabetique() {
        String[] noms = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            noms[i] = images.get(i);
        }
       Arrays.sort(noms);

       this.images.clear();
       for (int i = 0; i < images.size(); i++) {
        this.images.add(noms[i]);
       }

        String temp;
        for (int i = 0; i < images.size(); i++) {
            for (int j = i + 1; j < images.size(); j++) {
              
                // to compare one string with other strings
                if (images.get(i).compareTo(images.get(j)) > 0) {
                    // swapping
                    temp = images.get(i);
                    images.set(i, images.get(j));
                    images.set(j, temp);
                }
            }
        }
    }

    public ArrayList<ArrayList<String>> grouperSimilaires(ComparateurImages algo) {
        ArrayList<ArrayList<String>> groupes = new ArrayList<ArrayList<String>>();
        List<String> imgRep = new ArrayList<String>(images);
        this.trierAlphabetique();

        while (imgRep.size() > 0) {
            String currImg = imgRep.get(0);
            ArrayList<String> groupe = new ArrayList<String>();
            groupe.add(currImg);

            // Collection d'images a enlever (pas par indexe cette fois)
            List<String> prEnlever = new ArrayList<String>();
            // Commence j=1 car currImg est j=0 et est deja dans le groupe
            for (int j = 1; j < imgRep.size(); j++) {
                String comparaison = imgRep.get(j);
                if (algo.imagesSimilaires(comparaison, currImg)) {
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
    public List<String> getImages() {
        return images;
    }

    public int getTaille() {
        return images.size();
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gallerie{\n");
        for (String image : images) {
            sb.append(image.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}

