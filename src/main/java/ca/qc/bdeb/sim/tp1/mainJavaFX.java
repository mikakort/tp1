package ca.qc.bdeb.sim.tp1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import ca.qc.bdeb.sim.tp1.pics.gallerie;
import ca.qc.bdeb.sim.tp1.pics.img;

public class mainJavaFX extends Application {
    
    // Variables pour le traitement du dossier
    private ComboBox<String> comboDetection;
    private ToggleGroup groupeTolerance;
    
    // Variables pour la gallerie
    private ArrayList<ArrayList<img>> groupesDoublons;
    private ImageView vueImagePrincipale;
    private HBox boiteCarrousel;
    private ScrollPane defileCarrousel;
    private VBox listeDoublons;
    private int groupeSelectionne_i = 0;

    @Override
    public void start(Stage fenetre) {
        HBox boitePrincipale = new HBox(20);
        boitePrincipale.setPadding(new Insets(20));
        boitePrincipale.setStyle("background-color:rgb(196, 196, 196);");

        VBox sectionLogique = initSectionLogique(fenetre);
        VBox sectionGallerie = initSectionGallerie();

        boitePrincipale.getChildren().addAll(sectionLogique, sectionGallerie);

        Scene scene = new Scene(boitePrincipale, 900, 800);
        fenetre.setTitle("Photorama");
        fenetre.setScene(scene);
        fenetre.show();
    }

    private VBox initSectionLogique(Stage fenetre) {
        VBox panneauGauche = new VBox(5);
        panneauGauche.setPrefWidth(300);

        HBox boiteLogo = new HBox(10);
        ImageView imgLogo = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        imgLogo.setFitWidth(60);
        imgLogo.setFitHeight(60);
        Label labelLogo = new Label("Photorama");
        labelLogo.setAlignment(Pos.CENTER_RIGHT);
        labelLogo.setPadding(new Insets(5, 0, 0, 0));
        labelLogo.setFont(Font.font("Arial", FontWeight.BOLD, 35));
        boiteLogo.getChildren().addAll(imgLogo, labelLogo);

        Label labelTitreSection = new Label("Ouvrir une gallerie");
        labelTitreSection.setPadding(new Insets(20, 0, 0, 0));
        labelTitreSection.setFont(Font.font(22));

        Label labelDetection = new Label("Détection de doublons:");
        labelDetection.setFont(Font.font("Arial", 12));
        
        comboDetection = new ComboBox<>();
        comboDetection.getItems().addAll("Pixels", "Hachage (Moyenne)", "Hachage (Différences)");
        comboDetection.setValue("Pixels");

        Label labelTolerance = new Label("Tolérance aux différences:");
        labelTolerance.setFont(Font.font("Arial", 12));
        
        groupeTolerance = new ToggleGroup();
        RadioButton btnFaible = new RadioButton("Faible");
        btnFaible.setToggleGroup(groupeTolerance);
        btnFaible.setSelected(true);
        RadioButton btnElevee = new RadioButton("Élevée");
        btnElevee.setToggleGroup(groupeTolerance);

        HBox boiteRadio = new HBox(40, btnFaible, btnElevee);

        Button btnOuvrir = new Button("Ouvrir un dossier...");
        btnOuvrir.setPrefWidth(200);
        btnOuvrir.setOnAction(x -> {
            String chemin = choisirDossier(fenetre);
            if (chemin != null) {
                traiterDossier(chemin);
            }
        });

        panneauGauche.getChildren().addAll(boiteLogo, labelTitreSection, labelDetection, comboDetection, labelTolerance, boiteRadio, btnOuvrir);

        return panneauGauche;
    }
    private VBox initSectionGallerie() {
        VBox panneauDroit = new VBox();
        panneauDroit.setAlignment(Pos.TOP_LEFT);
        panneauDroit.setPrefWidth(450);
        panneauDroit.setSpacing(10);

        vueImagePrincipale = new ImageView(new Image(getClass().getResourceAsStream("/polaroid.png")));
        vueImagePrincipale.setFitWidth(400);
        vueImagePrincipale.setFitHeight(350);
        vueImagePrincipale.setPreserveRatio(true);

        Label labelCarrousel = new Label("Mes Photos (0)");
        labelCarrousel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelCarrousel.setAlignment(Pos.CENTER_LEFT);

        defileCarrousel = new ScrollPane();
        defileCarrousel.setPrefWidth(400);
        defileCarrousel.setPrefHeight(90);
        defileCarrousel.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        defileCarrousel.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        defileCarrousel.setStyle("-fx-background-color: transparent;");
        
        boiteCarrousel = new HBox(5);
        boiteCarrousel.setAlignment(Pos.CENTER_LEFT);
        boiteCarrousel.setPrefHeight(80);
        boiteCarrousel.setPadding(new Insets(5));
        
        defileCarrousel.setContent(boiteCarrousel);

        listeDoublons = new VBox(5);
        listeDoublons.setPrefHeight(150);
        listeDoublons.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");

        panneauDroit.getChildren().addAll(vueImagePrincipale, labelCarrousel, defileCarrousel, listeDoublons);

        return panneauDroit;
    }

    private String choisirDossier(Stage fenetre) {
        DirectoryChooser selecteur = new DirectoryChooser();
        selecteur.setTitle("Sélectionnez un dossier d'images");
        selecteur.setInitialDirectory(new File("."));

        File dossier = selecteur.showDialog(fenetre);
        if (dossier != null) {
            return dossier.getAbsolutePath();
        }
        return null;
    }
    
    private void traiterDossier(String chemin) {
        String methode = comboDetection.getValue();
        RadioButton tolChoisi = (RadioButton) groupeTolerance.getSelectedToggle();
        String t = tolChoisi.getText();
        int seuil = t.equals("Faible") ? 20 : 30;
        double prctMax = t.equals("Faible") ? 0.1 : 0.4;
        int maxDiff = t.equals("Faible") ? 10 : 15;

        File dossier = new File(chemin);
        gallerie gallerie = new gallerie(chemin, dossier.getName());

        groupesDoublons = new ArrayList<>();
        switch (methode) {
            case "Pixels":
                groupesDoublons = gallerie.grouperSimilaires(seuil, prctMax, 1);
                break;
            case "Hachage (Moyenne)":
                groupesDoublons = gallerie.grouperSimilaires(maxDiff, prctMax, 3);
                break;
            case "Hachage (Différences)":
                groupesDoublons = gallerie.grouperSimilaires(maxDiff, prctMax, 2);
                break;
        }
        mettreAJourGallerie();
    }
    
    private void mettreAJourGallerie() {
        if (groupesDoublons == null || groupesDoublons.isEmpty()) return;
        boiteCarrousel.getChildren().clear();
        listeDoublons.getChildren().clear();
        groupeSelectionne_i = 0;
        mettreAJourImagePrincipale();

        for (int i = 0; i < groupesDoublons.size(); i++) {
            ArrayList<img> groupe = groupesDoublons.get(i);
            if (!groupe.isEmpty()) {
                img imgPremiere = groupe.get(0);
                ImageView vueCarrousel = new ImageView();
                try {
                    Image image = new Image(new File(imgPremiere.getChemin()).toURI().toString());
                    vueCarrousel.setImage(image);
                    vueCarrousel.setFitWidth(60);
                    vueCarrousel.setFitHeight(60);
                    vueCarrousel.setPreserveRatio(true);
                    vueCarrousel.setStyle("-fx-border-color: #ccc; -fx-border-width: 2;");
                    final int x = i;
                    vueCarrousel.setOnMouseClicked(e -> {
                        groupeSelectionne_i = x;
                        mettreAJourImagePrincipale();
                        mettreAJourListeDoublons();
                    });
                    boiteCarrousel.getChildren().add(vueCarrousel);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image: " + imgPremiere.getChemin());
                }
            }
        }
        int nbrTotal = groupesDoublons.stream().mapToInt(ArrayList::size).sum();
        Label carrouselLabel = (Label) ((VBox) defileCarrousel.getParent()).getChildren().get(1);
        carrouselLabel.setText("Mes Photos (" + nbrTotal + ")");
        mettreAJourListeDoublons();
    }
    
    private void mettreAJourImagePrincipale() {
        if (groupesDoublons != null && groupeSelectionne_i < groupesDoublons.size() && !groupesDoublons.get(groupeSelectionne_i).isEmpty()) {
            img imgSelect = groupesDoublons.get(groupeSelectionne_i).get(0);
            try {
                Image image = new Image(new File(imgSelect.getChemin()).toURI().toString());
                vueImagePrincipale.setImage(image);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image principale: " + imgSelect.getChemin());
            }
        }
    }
    
    private void mettreAJourListeDoublons() {
        listeDoublons.getChildren().clear();
        if (groupesDoublons != null && groupeSelectionne_i < groupesDoublons.size()) {
            ArrayList<img> groupeSelect = groupesDoublons.get(groupeSelectionne_i);
            Label doublonsLabel = new Label("Doublons dans ce groupe:");
            doublonsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            listeDoublons.getChildren().add(doublonsLabel);

            ScrollPane defileDoublons = new ScrollPane();
            defileDoublons.setPrefHeight(120);
            defileDoublons.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            defileDoublons.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            defileDoublons.setStyle("-fx-background-color: white;");

            HBox boiteImages = new HBox(10);
            boiteImages.setPadding(new Insets(5));

            for (img image : groupeSelect) {
                VBox conteneur = new VBox(5);
                conteneur.setAlignment(Pos.CENTER);
                ImageView vueDoublon = new ImageView();
                try {
                    Image imgJavaFX = new Image(new File(image.getChemin()).toURI().toString());
                    vueDoublon.setImage(imgJavaFX);
                    vueDoublon.setFitWidth(80);
                    vueDoublon.setFitHeight(80);
                    vueDoublon.setPreserveRatio(true);
                    vueDoublon.setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image dupliquée: " + image.getChemin());
                }
                Label nom = new Label(image.getNom());
                nom.setFont(Font.font("Arial", 8));
                nom.setMaxWidth(80);
                nom.setWrapText(true);
                nom.setAlignment(Pos.CENTER);
                conteneur.getChildren().addAll(vueDoublon, nom);
                boiteImages.getChildren().add(conteneur);
            }
            defileDoublons.setContent(boiteImages);
            listeDoublons.getChildren().add(defileDoublons);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
