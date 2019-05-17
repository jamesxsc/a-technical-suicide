package uk.co.xsc.panes;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class PrimaryWindowPane extends Pane {

    private final Canvas canvas;

    public PrimaryWindowPane(int width, int height) {
        this.canvas = new Canvas(width, height);
        this.getChildren().add(canvas);
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
