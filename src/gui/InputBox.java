package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import util.Alternative;

import java.util.LinkedList;

public class InputBox {
    private int numOfMsr;
    private int numOfAlt;
    private Stage inputStage;
    private Scene inputScene;
    private GridPane inputGrid;
    private Button calcButton;
    private ScrollPane inputScroll;
    //private model.Anova anova;
    private LinkedList<Alternative> alternatives;

    public InputBox() {
    }

    public InputBox(int numOfMsr, int numOfAlt) {
        this.numOfMsr = numOfMsr;
        this.numOfAlt = numOfAlt;
    }

    public void display() {
        inputStage = new Stage();
        inputGrid = new GridPane();
        inputStage.setTitle("ENTRY OF MEASUREMENTS");
        inputStage.initModality(Modality.APPLICATION_MODAL);
        inputGrid.add(new Label("Enter measurement results for each alternative" + " (" + numOfMsr + " for each)"), 0, 0);
        inputGrid.add(new Label(), 10, 0);
        for (int i = 1; i <= numOfAlt; i++) {
            inputGrid.add(new Label("model.Alternative " + i), 0, i);
            inputGrid.add(new TextField(), 1, i);
        }

        inputGrid.add(new Label("*NOTE: Enter results like this -> 7.2 5 4.3 etc :)"), (numOfAlt + 5), 0);
        calcButton = new Button("CALCULATE!");
        calcButton.setOnAction(e -> {
            boolean res = extractInfo();
            if (res == true) {
                /*for (model.Alternative tmp : alternatives) {
                    for (int k = 0; k < numOfMsr; k++) {
                        System.out.println(tmp.msrs[k] + " ");
                    }
                    System.out.println("\n");
                }*/
                Anova anova = new Anova(alternatives, numOfAlt, numOfMsr);
                anova.display(inputStage);
            }

        });
        inputGrid.add(calcButton, (numOfAlt + 2), 5);
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(5);
        inputGrid.setVgap(5);
        inputGrid.setPadding(new Insets(20, 20, 20, 20));
        inputScroll = new ScrollPane(inputGrid);
        inputScene = new Scene(inputScroll);
        inputStage.setScene(inputScene);
        inputStage.show();

    }

    private boolean extractInfo() {
        boolean check = true;
        alternatives = new LinkedList<>();
        for (int i = 1; i <= numOfAlt; i++) {
            Alternative alt;
            for (Node node : inputGrid.getChildren()) {
                if (GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == i) {
                    double[] temp;
                    if ((temp = split((TextField) node)) != null) {
                        alt = new Alternative(i, temp, numOfMsr);
                        alternatives.add(alt);

                    } else {
                        check = false;
                        return false;
                    }
                }
            }
            if (!check) {
                return false;
            }
        }
        if (check)
            return true;
        else
            return false;
    }

    public static boolean isInt(TextField input) {
        try {
            int x = Integer.parseInt(input.getText());
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(TextField input) {
        try {
            double x = Double.parseDouble(input.getText());
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public double[] split(TextField input) {
        String[] temp = input.getText().split(" ");
        if (!(temp.length == numOfMsr)) {
            AlertBox.displayError("YOU NEED TO ENTER " + numOfMsr + " VALUES.");
            return null;
        }
        double[] result = new double[numOfMsr];
        for (int i = 0; i < temp.length; i++) {
            result[i] = Double.parseDouble(temp[i]);
        }

        return result;
    }
}
