package gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.math3.distribution.FDistribution;
import util.Alternative;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class Anova {
    private Scene resultsScene;
    private GridPane resultsGrid;
    private Button contrastButton;
    private LinkedList<Alternative> alternatives = new LinkedList<>();
    public int numberOfAlt;
    public int numberOfMsrs;
    private double totalMean = 0;
    public double ssa = 0;
    public double sse = 0;
    public double sst = 0;
    public int degSSA;
    public int degSSE;
    public int degSST;
    private double varianceSSA;
    private double varianceSSE;
    private double computedF;
    private double tabulatedF;
    private boolean areDifferent;

    public Anova(LinkedList<Alternative> alternatives, int numberOfAlt, int numberOfMsrs) {
        this.alternatives = alternatives;
        this.numberOfAlt = numberOfAlt;
        this.numberOfMsrs = numberOfMsrs;
        calculation();
    }

    private void calculation() {
        for (int i = 0; i < numberOfAlt; i++) {
            totalMean += alternatives.get(i).getSumOfMsr();
        }
        totalMean /= (numberOfAlt * numberOfMsrs);
        //System.out.println(totalMean);
        calculateSSE();
        calculateSSA();
        calculateSST();
        degSSA = numberOfAlt - 1;
        degSSE = numberOfAlt * (numberOfMsrs - 1);
        degSST = numberOfAlt * numberOfMsrs - 1;
        varianceSSA = ssa / degSSA;
        varianceSSE = sse / degSSE;
        computedF = varianceSSA / varianceSSE;
        FDistribution fDistr = new FDistribution(degSSA, degSSE);
        tabulatedF = fDistr.inverseCumulativeProbability(1.0 - 0.1);
        if (computedF > tabulatedF) {
            areDifferent = true;
        } else {
            areDifferent = false;
        }
    }

    private void calculateSSA() {
        for (int i = 0; i < numberOfAlt; i++) {
            ssa += Math.pow(alternatives.get(i).getMean() - totalMean, 2);
        }
        ssa *= numberOfMsrs;
    }

    private void calculateSSE() {
        for (int i = 0; i < numberOfAlt; i++) {
            for (int j = 0; j < numberOfMsrs; j++) {
                sse += (Math.pow(alternatives.get(i).msrs[j] - alternatives.get(i).getMean(), 2));
            }
        }
    }

    private void calculateSST() {
        for (int i = 0; i < numberOfAlt; i++) {
            for (int j = 0; j < numberOfMsrs; j++) {
                sst += Math.pow(alternatives.get(i).msrs[j] - totalMean, 2);
            }
        }
    }

    public void display(Stage stage) {
        resultsGrid = new GridPane();
        resultsGrid.setMinSize(200, 200);
        resultsGrid.getColumnConstraints().add(new ColumnConstraints(100));
        resultsGrid.getColumnConstraints().add(new ColumnConstraints(200));
        resultsGrid.getColumnConstraints().add(new ColumnConstraints(200));
        resultsGrid.getColumnConstraints().add(new ColumnConstraints(200));
        stage.setTitle("RESULTS");
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        contrastButton = new Button("CONTRAST");
        contrastButton.setOnAction(e -> {
            Contrasts contrasts = new Contrasts(alternatives, totalMean, varianceSSE, numberOfAlt, numberOfMsrs);
            contrasts.calculate(stage);

        });
        resultsGrid.gridLinesVisibleProperty().set(true);
        Label variationLabel = new Label("Variation");
        resultsGrid.add(new Label("Variation"), 0, 0);
        resultsGrid.add(new Label("Alternatives"), 1, 0);
        resultsGrid.add(new Label("Error"), 2, 0);
        resultsGrid.add(new Label("Total"), 3, 0);
        resultsGrid.add(new Label("Sum of squares"), 0, 1);
        resultsGrid.add(new Label("Deg freedom"), 0, 2);
        resultsGrid.add(new Label("Mean square"), 0, 3);
        resultsGrid.add(new Label("Computed F"), 0, 4);
        resultsGrid.add(new Label("Tabulated F"), 0, 5);
        resultsGrid.add(new Label("SSA = " + df.format(ssa)), 1, 1);
        resultsGrid.add(new Label("SSE = " + df.format(sse)), 2, 1);
        resultsGrid.add(new Label("SST = " + df.format(sst)), 3, 1);
        resultsGrid.add(new Label("k-1= " + degSSA), 1, 2);
        resultsGrid.add(new Label("k(n-1)= " + degSSE), 2, 2);
        resultsGrid.add(new Label("kn-1= " + degSST), 3, 2);
        resultsGrid.add(new Label("sa^2 = " + df.format(varianceSSA)), 1, 3);
        resultsGrid.add(new Label("se^2 = " + df.format(varianceSSE)), 2, 3);
        resultsGrid.add(new Label("Fc[sa^2/se^2] = " + df.format(getComputedF())), 1, 4);
        resultsGrid.add(new Label("Ft[0.90, " + degSSA + ", " + degSSE + "] = " + df.format(getTabularF())), 1, 5);
        if (areDifferent) {
            resultsGrid.add(new Label("Alternatives differ"), 3, 7);
        } else {
            resultsGrid.add(new Label("Alternatives do not differ"), 3, 7);
        }
        resultsGrid.add(contrastButton, 3, 9);
        GridPane.setHalignment(contrastButton, HPos.CENTER);
        resultsGrid.setAlignment(Pos.CENTER);
        resultsGrid.setHgap(5);
        resultsGrid.setVgap(5);
        resultsGrid.setLayoutX(30);
        resultsGrid.setLayoutY(30);
        resultsGrid.setPadding(new Insets(30, 30, 30, 30));
        resultsScene = new Scene(resultsGrid);
        stage.setScene(resultsScene);
        stage.show();
    }

    public double getVarianceSSA() {
        return varianceSSA;
    }

    public double getVarianceSSE() {
        return varianceSSE;
    }

    public double getTabularF() {
        return tabulatedF;
    }

    public double getTotalMean() {
        return totalMean;
    }

    public double getComputedF() {
        return computedF;
    }
}

