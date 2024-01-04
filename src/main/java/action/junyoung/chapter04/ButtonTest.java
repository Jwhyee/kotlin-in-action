package action.junyoung.chapter04;

import action.junyoung.chapter04.part1.State;

public class ButtonTest {
    public static void main(String[] args) {
        Button btn = new Button();
        State state = btn.getCurrentState();
        btn.restoreState(state);
    }
}
