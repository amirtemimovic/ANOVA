package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.math3.distribution.TDistribution;
import util.Alternative;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class Contrasts {
    private LinkedList<Alternative> alternatives;
    private double totalMean;
    private double varianceSSE;
    private double totalVariance;
    private double tabulatedDistr;
    private int numOfAlt;
    private int degSSE;
    private int numOfMsrs;
    //private int[] coefficients;
    private double c;
    private double lowerBound;
    private double upperBound;
    private Scene contrastsScene;
    private GridPane contrastsGrid;
    private ScrollPane scrollPane;


    public Contrasts(LinkedList<Alternative> alternatives, double totalMean, double varianceSSE, int numOfAlt, int numOfMsrs) {
        this.alternatives = alternatives;
        this.totalMean = totalMean;
        this.varianceSSE = varianceSSE;
        this.numOfAlt = numOfAlt;
        this.numOfMsrs = numOfMsrs;
        this.degSSE = numOfAlt * (numOfMsrs - 1);
        this.totalVariance = Math.sqrt((2 * varianceSSE) / (numOfAlt * numOfMsrs));
        if (numOfMsrs < 30) {
            TDistribution tDistribution = new TDistribution(degSSE);
            tabulatedDistr = tDistribution.inverseCumulativeProbability(0.90);
        } else {
            //NormalDistribution normalDistribution = new NormalDistribution();
        }
    }

    public void calculate(Stage stage) {
        stage.setTitle("CONTRASTS");
        int counter = 1;
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        contrastsGrid = new GridPane();
        contrastsGrid.add(new Label("Alternatives"), 0, 0);
        contrastsGrid.add(new Label("Confidence intervals"), 1, 0);
        contrastsGrid.add(new Label("Result"), 2, 0);
        for (int i = 0; i < numOfAlt - 1; i++) {
            for (int j = i + 1; j < numOfAlt; j++) {
                c = (alternatives.get(i).getMean() - totalMean) - (alternatives.get(j).getMean() - totalMean);
                lowerBound = c - tabulatedDistr * totalVariance;
                upperBound = c + tabulatedDistr * totalVariance;

                contrastsGrid.add(new Label("Alt" + (i + 1) + "-Alt" + (j + 1)), 0, counter);
                contrastsGrid.add(new Label("( " + df.format(lowerBound) + ", " + df.format(upperBound) + " )"), 1, counter);
                if (0 >= lowerBound && 0 <= upperBound) {
                    contrastsGrid.add(new Label("SAME"), 2, counter++);
                } else {
                    contrastsGrid.add(new Label("DIFFERENT"), 2, counter++);
                }
            }
        }
        contrastsGrid.setGridLinesVisible(true);
        contrastsGrid.getColumnConstraints().add(new ColumnConstraints(100));
        contrastsGrid.getColumnConstraints().add(new ColumnConstraints(200));
        contrastsGrid.getColumnConstraints().add(new ColumnConstraints(200));
        contrastsGrid.setAlignment(Pos.CENTER);
        contrastsGrid.setHgap(5);
        contrastsGrid.setVgap(5);
        contrastsGrid.setPadding(new Insets(30, 30, 30, 30));
        scrollPane = new ScrollPane(contrastsGrid);
        contrastsScene = new Scene(scrollPane);
        stage.setScene(contrastsScene);
        stage.show();
    }
}
