package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void displayError(String message) {
        Stage window = new Stage();
        Button button = new Button("OK");
        button.setOnAction(e -> window.close());
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ERROR!");
        window.setMinWidth(250);
        Label label = new Label(message);
        VBox layout = new VBox();
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
