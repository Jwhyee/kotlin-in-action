package action.junyoung.chapter02;

public class JavaInstanceOfTest {
    public static void main(String[] args) {

    }

    public static int eval(Expr e) throws IllegalAccessException {
        if (e instanceof Num) {
            final Num n = (Num) e;
            return n.getValue();
        } else if (e instanceof Sum) {
            return eval(((Sum) e).getRight()) + eval(((Sum) e).getLeft());
        }
        throw new IllegalAccessException("Unknown expression");
    }
}

