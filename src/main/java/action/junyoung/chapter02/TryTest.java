package action.junyoung.chapter02;

import java.io.BufferedReader;
import java.io.IOException;

public class TryTest {
    public Integer readNumber(BufferedReader reader) throws IOException {
        try {
            final String line = reader.readLine();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return null;
        } finally {
            reader.close();
        }
    }
}
