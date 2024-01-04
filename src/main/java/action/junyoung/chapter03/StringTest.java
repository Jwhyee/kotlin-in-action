package action.junyoung.chapter03;

public class StringTest {
    public static void main(String[] args) {
        String filename = "D:/some folder/001.docx";
        String extensionRemoved = filename.split(".")[0];

        System.out.println(extensionRemoved);
    }
}
