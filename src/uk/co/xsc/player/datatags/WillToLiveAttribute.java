package uk.co.xsc.player.datatags;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uk.co.xsc.ATechnicalSuicide;
import uk.co.xsc.player.datatags.base.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class WillToLiveAttribute extends Attribute {

    private static AtomicInteger renderCache;
    private final Map<Integer, Consumer<Object>> states;
    private final GraphicsContext graphicsContext;
    private int currentState = 30;

    public WillToLiveAttribute(GraphicsContext graphicsContext) {
        this.states = new HashMap<>();
        this.graphicsContext = graphicsContext;

        renderCache = new AtomicInteger();

        for (int i = 0; i <= 30; i++) {
            final int j = i;
            this.states.put(i, (c) -> {
                renderCache.set(j);
                render(this.graphicsContext);
            });
        }

        super.onChange();
    }

    @Override
    protected Map<Integer, Consumer<Object>> getStates() {
        return states;
    }

    @Override
    protected int getCurrentState() {
        return currentState;
    }

    @Override
    public String getUnlocalizedName() {
        return "ats.player.attribute.willtolive";
    }

    @Override
    public void decrement() {
        --currentState;
        super.onChange();
    }

    @Override
    public void increment() {
        ++currentState;
        super.onChange();
    }

    @SuppressWarnings({"UnnecessaryLocalVariable", "ConstantConditions"})
    @Override
    public void render(GraphicsContext graphicsContext) {
        int state = renderCache.get();
        boolean hasFraction = state % 5 != 0;
        int fractionSegmentsPresent = hasFraction ? state % 5 : 0;

        Image fullStartSegment = new Image(getClass().getClassLoader().getResource("data/img/attributes/count/willtolive/fullStartSegment.png").toExternalForm());
        Image fullMiddleSegment = new Image(getClass().getClassLoader().getResource("data/img/attributes/count/willtolive/fullMiddleSegment.png").toExternalForm());
        Image fullEndSegment = new Image(getClass().getClassLoader().getResource("data/img/attributes/count/willtolive/fullEndSegment.png").toExternalForm());
        Image emptyStartSegment = new Image(getClass().getClassLoader().getResource("data/img/attributes/count/willtolive/emptyStartSegment.png").toExternalForm());
        Image emptyMiddleSegment = new Image(getClass().getClassLoader().getResource("data/img/attributes/count/willtolive/emptyMiddleSegment.png").toExternalForm());
        Image emptyEndSegment = new Image(getClass().getClassLoader().getResource("data/img/attributes/count/willtolive/emptyEndSegment.png").toExternalForm());

        int xPos = (2 * ATechnicalSuicide.width / 3) + 130;
        int yPos = 10;

        int xCursor = xPos;
        int yCursor = yPos;
        for (int i = 0; i < state / 5; i++) {
            graphicsContext.drawImage(fullStartSegment, xCursor, yCursor);
            xCursor += 6;
            graphicsContext.drawImage(fullMiddleSegment, xCursor, yCursor);
            xCursor += 6;
            graphicsContext.drawImage(fullMiddleSegment, xCursor, yCursor);
            xCursor += 6;
            graphicsContext.drawImage(fullMiddleSegment, xCursor, yCursor);
            xCursor += 6;
            graphicsContext.drawImage(fullEndSegment, xCursor, yCursor);
            xCursor += 12;
        }

        if (fractionSegmentsPresent != 0) {
            graphicsContext.drawImage(fullStartSegment, xCursor, yCursor);
            xCursor += 6;

            --fractionSegmentsPresent;

            int midSegmentsDrawn = 0;

            while (fractionSegmentsPresent >= 1) {
                graphicsContext.drawImage(fullMiddleSegment, xCursor, yCursor);
                xCursor += 6;
                --fractionSegmentsPresent;
                ++midSegmentsDrawn;
            }

            while (midSegmentsDrawn < 4) {
                graphicsContext.drawImage(emptyMiddleSegment, xCursor, yCursor);
                ++midSegmentsDrawn;
                xCursor += 6;
            }

            graphicsContext.drawImage(fractionSegmentsPresent == 0 ? emptyEndSegment : fullEndSegment, xCursor, yCursor);

            xCursor += 12;

        }

        int stateCount = state / 5;
        if (stateCount < 5) {
            while (stateCount < 5) {
                graphicsContext.drawImage(emptyStartSegment, xCursor, yCursor);
                xCursor += 6;
                graphicsContext.drawImage(emptyMiddleSegment, xCursor, yCursor);
                xCursor += 6;
                graphicsContext.drawImage(emptyMiddleSegment, xCursor, yCursor);
                xCursor += 6;
                graphicsContext.drawImage(emptyMiddleSegment, xCursor, yCursor);
                xCursor += 6;
                graphicsContext.drawImage(emptyEndSegment, xCursor, yCursor);
                xCursor += 12;
                ++stateCount;
            }
        }

    }

}
