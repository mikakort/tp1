package ca.qc.bdeb.sim.tp1.pics;

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
