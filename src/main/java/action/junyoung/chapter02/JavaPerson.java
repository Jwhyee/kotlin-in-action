package action.junyoung.chapter02;

public class JavaPerson {
    private final String name;
    private boolean isMarried;

    public JavaPerson(String name, boolean isMarried) {
        this.name = name;
        this.isMarried = isMarried;
    }

    public String getName() {
        return name;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }
}
