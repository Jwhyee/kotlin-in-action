package action.junyoung.chapter05;

import java.util.Collection;
import java.util.List;

public class CollectionTestJava {
    public static void main(String[] args) {
        List<String> lists = List.of("403 Forbidden", "404 Not Found");
        String prefix = "Error :";
        lists.forEach(msg -> {
            System.out.printf("%s %s", prefix, msg);
        });
    }
}
