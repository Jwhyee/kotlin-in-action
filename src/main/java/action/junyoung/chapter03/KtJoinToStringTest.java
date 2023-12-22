package action.junyoung.chapter03;

import action.chapter03.part1.StringFunctions;

import java.util.List;

public class KtJoinToStringTest {
    public static void main(String[] args) {
        final List<Integer> list = List.of(1, 7, 53);
        System.out.println(StringFunctions.joinToString(list));
    }
}
