package action.junyoung.chapter02;

public class PersonTest {
    public static void main(String[] args) {
        JavaPerson person = new JavaPerson("Bob", true);
        System.out.println(person.getName());
        System.out.println(person.isMarried());
    }
}
