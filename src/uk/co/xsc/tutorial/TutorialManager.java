package uk.co.xsc.tutorial;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
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

    public void playThroughTutorial(StackPane pane) {
        
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
