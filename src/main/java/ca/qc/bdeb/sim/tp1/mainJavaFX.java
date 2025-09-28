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
    private ComboBox<String> detectionCombo;
    private ToggleGroup toleranceGroup;

    @Override
    public void start(Stage stage) {
        // HBox principal
        HBox hboxPrincipal = new HBox();
        hboxPrincipal.setSpacing(20);
        hboxPrincipal.setPadding(new Insets(20));
        hboxPrincipal.setStyle("background-color:rgb(196, 196, 196);");

        // Application
        VBox sectionLogique = initSectionLogique(stage);
        
        // Gallerie
        VBox sectionGallerie = initSectionGallerie();

        

        // Ajouter les sections au HBox
        hboxPrincipal.getChildren().addAll(sectionLogique, sectionGallerie);

        // Créer la scene et afficher
        Scene scene = new Scene(hboxPrincipal, 900, 800);
        stage.setTitle("Photorama");
        stage.setScene(scene);
        stage.show();
    }

    private VBox initSectionLogique(Stage stage) {
        VBox paneauGauche = new VBox();
        paneauGauche.setSpacing(5);
        paneauGauche.setPrefWidth(300);

        // Logo et titre
        HBox logoBox = new HBox();
        logoBox.setSpacing(10);
        
        ImageView logoImage = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logoImage.setFitWidth(60);
        logoImage.setFitHeight(60);
        
        Label textLogo = new Label("Photorama");
        textLogo.setAlignment(Pos.CENTER_RIGHT);
        textLogo.setPadding(new Insets(5, 0, 0, 0));
        textLogo.setFont(Font.font("Arial", FontWeight.BOLD, 35));

        
        logoBox.getChildren().addAll(logoImage, textLogo);

        // Ouvrir dossier
        Label titreSection = new Label("Ouvrir une gallerie");
        titreSection.setPadding(new Insets(20, 0, 0, 0));
        titreSection.setFont(Font.font(22));

        // Detection de doublons
        Label detectionTitre = new Label("Détection de doublons:");
        detectionTitre.setFont(Font.font("Arial", 12));
        
        detectionCombo = new ComboBox<>();
        detectionCombo.getItems().addAll("Pixels", "Hachage (Moyenne)", "Hachage (Différences)");
        detectionCombo.setValue("Pixels");

        // Tolerance section
        Label toleranceTitre = new Label("Tolérance aux différences:");
        toleranceTitre.setFont(Font.font("Arial", 12));
        
        toleranceGroup = new ToggleGroup();
        RadioButton btnFaible = new RadioButton("Faible");
        btnFaible.setToggleGroup(toleranceGroup);
        btnFaible.setSelected(true);
        
        RadioButton btnElevee = new RadioButton("Élevée");
        btnElevee.setToggleGroup(toleranceGroup);

        HBox radioBox = new HBox(20);
        radioBox.getChildren().addAll(btnFaible, btnElevee);

        // Bouton pour ouvrir dossier
        Button btnOuvrirDossier = new Button("Ouvrir un dossier...");
        btnOuvrirDossier.setPrefWidth(200);
        btnOuvrirDossier.setOnAction(x -> {
            String cheminDossier = choisirDossier(stage);
            if (cheminDossier != null) { // null check
                traiterDossier(cheminDossier);
            }
        });

        // Ajouter tous les composants au paneau gauche
        paneauGauche.getChildren().addAll(logoBox, titreSection, detectionTitre, detectionCombo, toleranceTitre, radioBox, btnOuvrirDossier);

        return paneauGauche;
    }
    private VBox initSectionGallerie() {
        VBox paneauDroit = new VBox();
        paneauDroit.setAlignment(Pos.TOP_CENTER);
        paneauDroit.setPrefWidth(450);

        // Polaroid.png
        ImageView imgPolaroid = new ImageView(new Image(getClass().getResourceAsStream("/polaroid.png")));
        imgPolaroid.setFitWidth(400);
        imgPolaroid.setFitHeight(350);

        paneauDroit.getChildren().add(imgPolaroid);

        return paneauDroit;
    }

    private String choisirDossier(Stage stage) { // Fct provenant des instructions
        DirectoryChooser selecteur = new DirectoryChooser();
        selecteur.setTitle("Sélectionnez un dossier d'images");
        selecteur.setInitialDirectory(new File("."));

        File dossierChoisi = selecteur.showDialog(stage);
        if (dossierChoisi != null) {
            String chemin = dossierChoisi.getAbsolutePath();

            return chemin;
        }

        return null;
    }
    
    private void traiterDossier(String cheminDossier) {
        // Recup val
        String methodeDetection = detectionCombo.getValue();
        RadioButton toleranceSelectionnee = (RadioButton) toleranceGroup.getSelectedToggle();
        String tolerance = toleranceSelectionnee.getText();
        int seuil = tolerance.equals("Faible") ? 20 : 30;
        double prct_max = tolerance.equals("Faible") ? 0.1 : 0.4;
        int max_diff = tolerance.equals("Faible") ? 10 : 15;

        gallerie gallerie = new gallerie(new ArrayList<img>());

            File dossier = new File(cheminDossier);
            File[] fichiers = dossier.listFiles();
            int nbFichiers = (fichiers != null) ? fichiers.length : 0;
            for (int j = 0; j < nbFichiers; j++) {
                gallerie.ajouterImage(new img((fichiers[j].getPath().split("airbnb-petit")[1].substring(1)), fichiers[j].getPath()));
            }
        
        // selon les params
        ArrayList<ArrayList<img>> groupes = new ArrayList<ArrayList<img>>();
        switch (methodeDetection) {
            case "Pixels":
                groupes = gallerie.grouperSimilaires(seuil, prct_max, 1);
                    for (int j = 0; j < groupes.size(); j++) {
                        System.out.print("[" + (j+1) + "] ");
                    for (int k = 0; k < groupes.get(j).size(); k++) {
                            System.out.print(groupes.get(j).get(k).getNom() + " ");
                        }
                        System.out.println();
            }
                break;
            case "Hachage (Moyenne)":
                groupes = gallerie.grouperSimilaires(max_diff, prct_max, 3);
                    for (int j = 0; j < groupes.size(); j++) {
                        System.out.print("[" + (j+1) + "] ");
                    for (int k = 0; k < groupes.get(j).size(); k++) {
                            System.out.print(groupes.get(j).get(k).getNom() + " ");
                        }
                        System.out.println();
            }
                break;
            case "Hachage (Différences)":
                groupes = gallerie.grouperSimilaires(max_diff, prct_max, 2);
                    for (int j = 0; j < groupes.size(); j++) {
                        System.out.print("[" + (j+1) + "] ");
                    for (int k = 0; k < groupes.get(j).size(); k++) {
                            System.out.print(groupes.get(j).get(k).getNom() + " ");
                        }
                        System.out.println();
            }
                break;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
