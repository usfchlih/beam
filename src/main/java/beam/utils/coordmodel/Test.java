package beam.utils.coordmodel;

public class Test {
    public static void main(String[] args) {
        String test = "{\"geometry\":\"test\"}";
        System.out.println("BEFORE: " + test);
        System.out.println("AFTER: " + test.replaceAll("\\{\"geometry", "\n{\"geometry"));
    }
}
