package action.chapter02;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapTest {
    public static void main(String[] args) {
        Map<Character, String> binaryReps = new TreeMap<>();
        final char start = 'A';
        final char end = 'F';
        for (char i = start; i < end; i++) {
            binaryReps.put(i, Integer.toBinaryString(i));
        }

        for (Map.Entry<Character, String> entry : binaryReps.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
