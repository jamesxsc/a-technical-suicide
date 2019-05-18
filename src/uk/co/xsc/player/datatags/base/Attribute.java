package uk.co.xsc.player.datatags.base;

import javafx.scene.canvas.GraphicsContext;

import java.util.Map;
import java.util.function.Consumer;

public abstract class Attribute {

    protected abstract Map<Integer, Consumer<Object>> getStates();

    protected abstract int getCurrentState();

    public abstract String getUnlocalizedName();

    public int getStateCount() {
        return getStates().values().size();
    }

    public abstract void decrement();

    public abstract void increment();

    protected final void onChange() {
        System.out.println(getStates());
        System.out.println(getCurrentState());
        System.out.println(getStates().get(getCurrentState()));
        getStates().get(getCurrentState()).accept(new Object[0]);
    }

    public abstract void render(GraphicsContext graphicsContext);

}
