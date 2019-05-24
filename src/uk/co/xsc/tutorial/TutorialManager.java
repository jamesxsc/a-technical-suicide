package uk.co.xsc.tutorial;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import uk.co.xsc.ATechnicalSuicide;
import uk.co.xsc.tutorial.points.CharacterNameTutorialPoint;
import uk.co.xsc.tutorial.points.WillToLiveTutorialPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TutorialManager {

    private final List<TutorialPoint> points;

    public TutorialManager() {
        this.points = new ArrayList<>();

        initPoints(this.points);

        this.points.sort(Comparator.comparingInt(o -> o.priority));
    }

    private void initPoints(List<TutorialPoint> pointsList) {
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
                Button button = new Button("Continue");
                button.setOnAction((e) ->
                {
                    System.out.println("clicked");
                    synchronized (this) {
                        this.notify();
                    }
                });

                for (TutorialPoint point : points) {

                    graphicsContext.clearRect(0, 0, ATechnicalSuicide.width, ATechnicalSuicide.height);
                    if (point.content.length() > 1028) {
                        throw new IllegalArgumentException("The content is too long! (>1028chars)");
                    }

                    int boxWidth = 350;
                    int boxHeight = 15 * (point.content.length() / 20) + 100;

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

                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillRect(xPos, yPos, boxWidth, boxHeight);

                    graphicsContext.setLineWidth(10);
                    graphicsContext.setLineCap(StrokeLineCap.ROUND);

                    graphicsContext.setStroke(Color.BLACK);
                    graphicsContext.strokeLine(xPos + (double) boxWidth / 2, yPos + (double) boxHeight / 2, point.xPos, point.yPos);

                    graphicsContext.setFill(Color.WHITE);
                    graphicsContext.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 32));
                    graphicsContext.fillText("Tutorial", xPos + 10, yPos + 32);
//break at 30 create util class that taks font size and pixel width and text and returns array
                    graphicsContext.setStroke(Color.WHITE);
                    graphicsContext.setLineWidth(3);
                    System.out.println(xPos);
                    System.out.println(yPos);
                    graphicsContext.strokeLine(xPos + 8, yPos + 38, xPos + boxWidth - 80, yPos + 38);

                    graphicsContext.setFont(Font.font("Calibri", 24));
                    graphicsContext.fillText(point.content, xPos + 15, yPos + 60);

                    Platform.runLater(() -> pane.getChildren().setAll(canvasPane, button));

                    System.out.println("wait placeholder (pre)");
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    System.out.println("wait placeholder");

                }
                graphicsContext.clearRect(0, 0, ATechnicalSuicide.width, ATechnicalSuicide.height);
                Platform.runLater(()->pane.getChildren().setAll());
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
