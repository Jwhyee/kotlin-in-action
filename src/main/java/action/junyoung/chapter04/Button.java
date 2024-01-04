package action.junyoung.chapter04;

import action.junyoung.chapter04.part1.State;
import action.junyoung.chapter04.part1.View;
import org.jetbrains.annotations.NotNull;

public class Button implements View {

    @NotNull
    @Override
    public State getCurrentState() {
        return new ButtonState();
    }

    @Override
    public void restoreState(@NotNull State state) {}

    public class ButtonState implements State { }
}
