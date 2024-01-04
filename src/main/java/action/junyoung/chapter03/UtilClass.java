package action.junyoung.chapter03;

import java.util.Collection;

public class UtilClass {
    private UtilClass(){}
    public static <T> String joinToString(Collection<T> collection, String separator, String prefix, String postfix) {
        final StringBuilder result = new StringBuilder(prefix);
        int idx = 0;
        for (T t : collection) {
            if(idx > 0) result.append(separator);
            result.append(t);
            idx++;
        }
        result.append(postfix);
        return result.toString();
    }
}
