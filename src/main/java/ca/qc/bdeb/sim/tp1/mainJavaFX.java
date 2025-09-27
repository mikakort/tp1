package ca.qc.bdeb.sim.tp1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class mainJavaFX extends Application {

    private Label welcomeText;

    @Override
    public void start(Stage stage) {
        // Create VBox layout
        VBox vbox = new VBox();
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        // Create label
        welcomeText = new Label("Welcome to JavaFX!");

        // Create button
        Button button = new Button("Click me!");
        button.setOnAction(e -> onHelloButtonClick());

        // Add components to VBox
        vbox.getChildren().addAll(welcomeText, button);

        // Create scene and show stage
        Scene scene = new Scene(vbox, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private void onHelloButtonClick() {
        welcomeText.setText("Hello World!");
    }

    public static void main(String[] args) {
        launch();
    }
}
