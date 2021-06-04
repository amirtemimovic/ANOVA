package main;

import gui.AlertBox;
import gui.InputBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene;
        Button nextButton = new Button("NEXT");

        Label altLabel = new Label("Number of alternatives");
        Label msrLabel = new Label("Number of measurements");
        TextField altInput = new TextField();
        TextField msrInput = new TextField();

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(5);
        layout.setVgap(5);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.add(altLabel, 0, 1);
        layout.add(altInput, 1, 1);
        layout.add(msrLabel, 0, 2);
        layout.add(msrInput, 1, 2);
        layout.add(nextButton, 0, 3);

        nextButton.setOnAction(e -> {
            if (InputBox.isInt(altInput) && InputBox.isInt(msrInput)) {
                InputBox inputBox = new InputBox(Integer.parseInt(msrInput.getText()), Integer.parseInt(altInput.getText()));
                inputBox.display();
            } else {
                AlertBox.displayError("WRONG INPUT! YOU NEED TO ENTER NUMBER!");
            }
        });

        scene = new Scene(layout, 400, 300);
        stage.setTitle("ANOVA calculator");
        stage.setScene(scene);
        stage.show();

    }


}
