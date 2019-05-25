package uk.co.xsc.tutorial;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import uk.co.xsc.ATechnicalSuicide;
import uk.co.xsc.tutorial.points.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TutorialManager {

    private final List<TutorialPoint> points;

    public TutorialManager() {
        this.points = new ArrayList<>();

        initPoints(this.points);

        this.points.sort(Comparator.comparingInt(o -> o.priority));
    }

    private void initPoints(List<TutorialPoint> pointsList) {
        pointsList.add(new ScenarioExplanationTutorialPoint());
        pointsList.add(new ChoiceActionTutorialPoint());
        pointsList.add(new ActionPurposeTutorialPoint());
        pointsList.add(new CharacterNameTutorialPoint());
        pointsList.add(new WillToLiveTutorialPoint());
    }

    public synchronized void playThroughTutorial(StackPane pane) throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Canvas canvas = new Canvas(ATechnicalSuicide.width, ATechnicalSuicide.height);
                StackPane canvasPane = new StackPane(canvas);
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

                for (Iterator<TutorialPoint> iter = points.iterator(); iter.hasNext(); ) {

                    TutorialPoint point = iter.next();

                    graphicsContext.clearRect(0, 0, ATechnicalSuicide.width, ATechnicalSuicide.height);
                    if (point.content.length() > 1028) {
                        throw new IllegalArgumentException("The content is too long! (>1028chars)");
                    }

                    int boxWidth = 350;
                    int boxHeight = (point.content.length() > 100 ? (15 * point.content.length() / 35) :
                            (15 * (point.content.length() / 20))) + 180;

                    int xPos = 0;
                    int yPos = 0;
                    boolean isXAcceptable = false;
                    boolean isYAcceptable = false;
                    while (!isXAcceptable || !isYAcceptable) {
                        int suggestedX = ATechnicalSuicide.RENDER_RNG.nextInt(ATechnicalSuicide.width - boxWidth);
                        int suggestedY = ATechnicalSuicide.RENDER_RNG.nextInt(ATechnicalSuicide.height - boxHeight);
                        if (!(Math.abs(suggestedX - point.xPos) < boxWidth + 100)) {
                            isXAcceptable = true;
                            xPos = suggestedX;
                        }
                        if (!(Math.abs(suggestedY - point.yPos) < boxHeight + 100)) {
                            isYAcceptable = true;
                            yPos = suggestedY;
                        }
                    }

                    Label label = new Label();
                    label.setText(point.content);
                    label.setFont(Font.font("Calibri", point.content.length() > 100 ? 22 : 28));
                    label.setMaxWidth(boxWidth - 20);
                    label.setMaxHeight(boxHeight - 25);
                    label.setAlignment(Pos.TOP_CENTER);
                    label.setWrapText(true);
                    label.setTextAlignment(TextAlignment.JUSTIFY);
                    label.setTranslateX(xPos - (double) ATechnicalSuicide.width / 2 + (double) boxWidth / 2);
                    label.setTranslateY(yPos - (double) ATechnicalSuicide.height / 2 + (double) boxHeight / 2 + 30);
                    label.getStyleClass().add("tutorial-point-label");

                    Button button = new Button(iter.hasNext() ? "Continue" : "Finish Tutorial");
                    button.getStyleClass().add("tutorial-point-button-continue");
                    button.setOnAction((e) ->
                    {
                        synchronized (this) {
                            this.notify();
                        }
                    });
                    button.setTranslateX(xPos - (double) ATechnicalSuicide.width / 2 + (double) boxWidth / 2);
                    button.setTranslateY(yPos - (double) ATechnicalSuicide.height / 2 + boxHeight - 35);

                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillRect(xPos, yPos, boxWidth, boxHeight);

                    graphicsContext.setLineWidth(10);
                    graphicsContext.setLineCap(StrokeLineCap.ROUND);

                    graphicsContext.setStroke(Color.BLACK);
                    if (point.xPos != -1 && point.yPos != -1)
                        graphicsContext.strokeLine(xPos + (double) boxWidth / 2, yPos + (double) boxHeight / 2, point.xPos, point.yPos);

                    graphicsContext.setFill(Color.WHITE);
                    graphicsContext.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 32));
                    graphicsContext.fillText("Tutorial", xPos + 10, yPos + 32);
                    graphicsContext.setStroke(Color.WHITE);
                    graphicsContext.setLineWidth(3);
                    graphicsContext.strokeLine(xPos + 8, yPos + 38, xPos + boxWidth - 80, yPos + 38);

                    //graphicsContext.setFont(Font.font("Calibri", 24));
                    //graphicsContext.fillText(point.content, xPos + 15, yPos + 60);

                    Platform.runLater(() -> pane.getChildren().setAll(canvasPane, label, button));

                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
                graphicsContext.clearRect(0, 0, ATechnicalSuicide.width, ATechnicalSuicide.height);
                Platform.runLater(() -> pane.getChildren().setAll());
            }
        }.start();
    }

    public void setChildren(Pane pane, Node... items) {
        pane.getChildren().setAll(items);
    }

    public static abstract class TutorialPoint {

        private final int xPos;
        private final int yPos;

        private final String content;

        private final int priority;

        protected TutorialPoint(int xPos, int yPos, String content, int priority) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.content = content;
            this.priority = priority;
        }

    }

}
