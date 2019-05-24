package uk.co.xsc;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import uk.co.xsc.panes.PrimaryWindowPane;
import uk.co.xsc.player.Character;
import uk.co.xsc.player.datatags.WillToLiveAttribute;
import uk.co.xsc.tutorial.TutorialManager;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class ATechnicalSuicide extends Application {

    public static int width = 1200;
    public static int height = 800;

    public static final Random RENDER_RNG = new Random();
    public static final Random GAME_RNG = new Random();
    public static final Random AI_RNG = new Random();

    private final TutorialManager tutorialManager;

    private Character character;
    private WillToLiveAttribute willToLiveAttribute;

    public ATechnicalSuicide() {
        tutorialManager = new TutorialManager();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        PrimaryWindowPane root = new PrimaryWindowPane(width, height);
        primaryStage.setTitle("A Technical Suicide");
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        GraphicsContext graphicsContext = root.getCanvas().getGraphicsContext2D();

        initCanvas(graphicsContext);

        new AnimationTimer() {
            @Override
            public void handle(long now) {

            }
        }.start();

        primaryStage.show();

        startGame(graphicsContext, root);
    }

    private void initCanvas(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, width, height);

        graphicsContext.setFill(Color.ALICEBLUE);
        graphicsContext.fillRect(0, 0, width, height);

        graphicsContext.setFill(Color.DARKSLATEGRAY);
        graphicsContext.fillPolygon(new double[]{width, width, (double) 2 * width / 3, (double) 2 * width / 3 - 25}, new double[]{0, 40, 40, 0}, 4);
        graphicsContext.fillPolygon(new double[]{0, 0, (double) width / 3 - 25, (double) width / 3}, new double[]{height, height - 40, height - 40, height}, 4);

        graphicsContext.setStroke(Color.CADETBLUE);
        graphicsContext.setLineJoin(StrokeLineJoin.ROUND);
        graphicsContext.setLineWidth(5);
        graphicsContext.strokeRect((double) width / 10, (double) height / 8, (double) 8 * width / 10, (double) 6 * height / 8);

        graphicsContext.setFill(Color.LIGHTSLATEGRAY);
        graphicsContext.setFont(Font.font("Calibri", FontWeight.BOLD, 28));
        graphicsContext.fillText("A Technical", 15, 30);

        graphicsContext.setFill(Color.CRIMSON);
        graphicsContext.fillText("Suicide", 155, 30);

        graphicsContext.setFill(Color.DIMGRAY);
        graphicsContext.fillRect(0, 40, 220, 5);

        if (character != null) {
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.setFont(Font.font("Calibri", FontWeight.BOLD, 28));
            graphicsContext.fillText(character.name, 2 * (double) width / 3 + 10, 28);

            if (willToLiveAttribute != null) {
                willToLiveAttribute.render(graphicsContext);
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private void startGame(GraphicsContext graphicsContext, PrimaryWindowPane root) {
        root.getStylesheets().add(getClass().getResource("game.css").toExternalForm());

        StackPane primary = new StackPane();

        int dialogueWidth = 450;
        int dialogueHeight = 530;

        primary.setAlignment(Pos.CENTER);
        primary.setLayoutX((double) width / 2 - (double) dialogueWidth / 2);
        primary.setLayoutY((double) height / 2 - (double) dialogueHeight / 2);

        graphicsContext.save();

        graphicsContext.applyEffect(new GaussianBlur());

        graphicsContext.setFill(Color.BLANCHEDALMOND);
        graphicsContext.fillRoundRect((double) (width / 2) - (double) dialogueWidth / 2,
                (double) (height / 2) - (double) dialogueHeight / 2, dialogueWidth, dialogueHeight, 5, 5);

        graphicsContext.setStroke(Color.BROWN);
        graphicsContext.setLineWidth(5);
        graphicsContext.strokeRoundRect((double) (width / 2) - (double) dialogueWidth / 2,
                (double) (height / 2) - (double) dialogueHeight / 2, dialogueWidth, dialogueHeight, 5, 5);

        VBox dialogueBoxPane = new VBox();

        dialogueBoxPane.setPrefWidth(dialogueWidth);
        dialogueBoxPane.setPrefHeight(dialogueHeight);
        dialogueBoxPane.setMinWidth(Control.USE_PREF_SIZE);
        dialogueBoxPane.setMaxWidth(Control.USE_PREF_SIZE);
        dialogueBoxPane.setMinHeight(Control.USE_PREF_SIZE);
        dialogueBoxPane.setMaxHeight(Control.USE_PREF_SIZE);

        HBox characterButtons = new HBox();
        characterButtons.getStyleClass().add("immutable-container-button-character-selector");
        characterButtons.setAlignment(Pos.CENTER);

        FlowPane characterStats = new FlowPane();
        characterStats.getStyleClass().add("immutable-container-stats-character-selection");

        Label instructionLabel = new Label("Please select a character from above");
        instructionLabel.getStyleClass().add("immutable-instruction-label-character-selection");

        characterStats.getChildren().add(instructionLabel);

        Label scoreSoundLabel = new Label("Sound Score:");
        scoreSoundLabel.getStyleClass().add("score-label-stat");
        Label scoreLightingLabel = new Label("Lighting Score:");
        scoreLightingLabel.getStyleClass().add("score-label-stat");
        Label scoreRiggingLabel = new Label("Rigging Score:");
        scoreRiggingLabel.getStyleClass().add("score-label-stat");
        Label scoreStageManagementLabel = new Label("Stage Management Score:");
        scoreStageManagementLabel.getStyleClass().add("score-label-stat");
        Label scoreProductionLabel = new Label("Production Score:");
        scoreProductionLabel.getStyleClass().add("score-label-stat");
        Label scoreLeadershipLabel = new Label("Leadership Score:");
        scoreLeadershipLabel.getStyleClass().add("score-label-stat");
        Label scoreEvilLabel = new Label("Evil Score:");
        scoreEvilLabel.getStyleClass().add("score-label-stat");
        Label scoreKindLabel = new Label("Kind Score:");
        scoreKindLabel.getStyleClass().add("score-label-stat");
        Label scoreHonestLabel = new Label("Honesty Score:");
        scoreHonestLabel.getStyleClass().add("score-label-stat");
        Label scoreMusicalityLabel = new Label("Musicality Score:");
        scoreMusicalityLabel.getStyleClass().add("score-label-stat");

        ProgressBar scoreSoundBar = new ProgressBar();
        scoreSoundBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreLightingBar = new ProgressBar();
        scoreLightingBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreRiggingBar = new ProgressBar();
        scoreRiggingBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreStageManagementBar = new ProgressBar();
        scoreStageManagementBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreProductionBar = new ProgressBar();
        scoreProductionBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreLeadershipBar = new ProgressBar();
        scoreLeadershipBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreEvilBar = new ProgressBar();
        scoreEvilBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreKindBar = new ProgressBar();
        scoreKindBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreHonestBar = new ProgressBar();
        scoreHonestBar.getStyleClass().add("progress-bar-stat");
        ProgressBar scoreMusicalityBar = new ProgressBar();
        scoreMusicalityBar.getStyleClass().add("progress-bar-stat");

        HBox scoreSoundBox = new HBox();
        scoreSoundBox.getChildren().addAll(scoreSoundLabel, scoreSoundBar);
        scoreSoundBox.setVisible(false);
        HBox scoreLightingBox = new HBox();
        scoreLightingBox.getChildren().addAll(scoreLightingLabel, scoreLightingBar);
        scoreLightingBox.setVisible(false);
        HBox scoreRiggingBox = new HBox();
        scoreRiggingBox.getChildren().addAll(scoreRiggingLabel, scoreRiggingBar);
        scoreRiggingBox.setVisible(false);
        HBox scoreStageManagementBox = new HBox();
        scoreStageManagementBox.getChildren().addAll(scoreStageManagementLabel, scoreStageManagementBar);
        scoreStageManagementBox.setVisible(false);
        HBox scoreProductionBox = new HBox();
        scoreProductionBox.getChildren().addAll(scoreProductionLabel, scoreProductionBar);
        scoreProductionBox.setVisible(false);
        HBox scoreLeadershipBox = new HBox();
        scoreLeadershipBox.getChildren().addAll(scoreLeadershipLabel, scoreLeadershipBar);
        scoreLeadershipBox.setVisible(false);
        HBox scoreEvilBox = new HBox();
        scoreEvilBox.getChildren().addAll(scoreEvilLabel, scoreEvilBar);
        scoreEvilBox.setVisible(false);
        HBox scoreKindBox = new HBox();
        scoreKindBox.getChildren().addAll(scoreKindLabel, scoreKindBar);
        scoreKindBox.setVisible(false);
        HBox scoreHonestBox = new HBox();
        scoreHonestBox.getChildren().addAll(scoreHonestLabel, scoreHonestBar);
        scoreHonestBox.setVisible(false);
        HBox scoreMusicalityBox = new HBox();
        scoreMusicalityBox.getChildren().addAll(scoreMusicalityLabel, scoreMusicalityBar);
        scoreMusicalityBox.setVisible(false);

        characterStats.getChildren().addAll(
                scoreSoundBox,
                scoreLightingBox,
                scoreRiggingBox,
                scoreStageManagementBox,
                scoreProductionBox,
                scoreLeadershipBox,
                scoreEvilBox,
                scoreKindBox,
                scoreHonestBox,
                scoreMusicalityBox
        );

        AtomicReference<Character> currentCharacter = new AtomicReference<>();

        for (Character character : Character.values()) {
            Button selectCharacter = new Button(character.name);
            selectCharacter.getStyleClass().add("immutable-button-character-selector");

            selectCharacter
                    .setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,
                            Insets.EMPTY)));
            selectCharacter
                    .setTextFill(Color.BROWN);

            selectCharacter.setOnMouseEntered(e -> {
                selectCharacter
                        .setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY,
                                Insets.EMPTY)));
                selectCharacter
                        .setTextFill(Color.WHITE);
            });

            selectCharacter.setOnMouseExited(e -> {
                if (character.equals(currentCharacter.get()))
                    return;
                selectCharacter
                        .setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,
                                Insets.EMPTY)));
                selectCharacter
                        .setTextFill(Color.BROWN);
            });

            selectCharacter.setOnAction(event -> {
                currentCharacter.set(character);
                if (instructionLabel.isVisible()) {
                    instructionLabel.setVisible(false);

                    scoreSoundBox.setVisible(true);
                    scoreLightingBox.setVisible(true);
                    scoreRiggingBox.setVisible(true);
                    scoreStageManagementBox.setVisible(true);
                    scoreProductionBox.setVisible(true);
                    scoreLeadershipBox.setVisible(true);
                    scoreEvilBox.setVisible(true);
                    scoreKindBox.setVisible(true);
                    scoreHonestBox.setVisible(true);
                    scoreMusicalityBox.setVisible(true);
                }

                characterButtons.getChildren().forEach(c -> {
                    ((Button) c)
                            .setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,
                                    Insets.EMPTY)));
                    ((Button) c)
                            .setTextFill(Color.BROWN);
                });

                selectCharacter.setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY,
                        Insets.EMPTY)));
                selectCharacter.setTextFill(Color.WHITE);

                scoreSoundBar.setProgress((double) character.scoreSound / 10);
                scoreLightingBar.setProgress((double) character.scoreLighting / 10);
                scoreRiggingBar.setProgress((double) character.scoreRigging / 10);
                scoreStageManagementBar.setProgress((double) character.scoreStageManagement / 10);
                scoreProductionBar.setProgress((double) character.scoreProduction / 10);
                scoreLeadershipBar.setProgress((double) character.scoreLeadership / 10);
                scoreEvilBar.setProgress((double) character.scoreEvil / 10);
                scoreKindBar.setProgress((double) character.scoreKind / 10);
                scoreHonestBar.setProgress((double) character.scoreHonest / 10);
                scoreMusicalityBar.setProgress((double) character.scoreMusicality / 10);
            });

            characterButtons.getChildren().add(selectCharacter);
        }

        StackPane continueButtonContainer = new StackPane();

        Button continueButton = new Button("Continue");
        continueButton.getStyleClass().add("button-continue-character-select");
        continueButton.setOnAction(event -> {
            if (currentCharacter.get() == null) {
                JOptionPane.showMessageDialog(null, "You must select a character!");
            } else {
                this.character = currentCharacter.get();
                root.getChildren().remove(primary);

                graphicsContext.restore();

                initCanvas(graphicsContext);

                runTutorial(root);

                beginGameplay(graphicsContext);
            }
        });

        continueButtonContainer.getChildren().add(continueButton);
        continueButtonContainer.setPadding(new Insets(20));

        dialogueBoxPane.getStyleClass().add("immutable-container-dialogue-box-pane-character-selection");

        dialogueBoxPane.getChildren().add(characterButtons);
        dialogueBoxPane.getChildren().add(characterStats);
        dialogueBoxPane.getChildren().add(continueButtonContainer);

        primary.getChildren().add(dialogueBoxPane);

        root.getChildren().add(primary);
    }

    private void runTutorial(PrimaryWindowPane windowPane) {
        StackPane pane = new StackPane();
        windowPane.getChildren().add(pane);
        try {
            this.tutorialManager.playThroughTutorial(pane);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void beginGameplay(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(Font.font("Calibri", FontWeight.BOLD, 28));
        graphicsContext.fillText(character.name, 2 * (double) width / 3 + 10, 28);

        willToLiveAttribute = new WillToLiveAttribute(graphicsContext);
    }

}
