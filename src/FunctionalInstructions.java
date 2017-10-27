import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FunctionalInstructions {

    private static String numRegEx = "([0-9])";
    private static String author;
    private static String mode;

    public static String selectAuthor() throws IOException {
        System.out.println("Select author");
        System.out.println("");
        System.out.println("1. Build upon your self");
        System.out.println("2. Shakespeare");
        System.out.println("3. Rumi");
        System.out.println("4. Whitman");
        System.out.println("5. Oscar Wilde");
        System.out.println("6. Asimov");

        //System.out.println("5. Tennyson");
        //System.out.println("5. Emerson");
        //System.out.println("5. Thoreau");

        System.out.println("0. ALL");
        System.out.println("");
        System.out.println("");
        author = readInput();
        return author;
    }

    public static String selectMode() throws IOException {

        if("1".equals(author)) {
            System.out.println("With no uploaded authors,");
            System.out.println("Choosing 2. Interactive mode");
            mode = "2";
        }else{
            System.out.println("Select mode");
            System.out.println("");
            System.out.println("1. Auto mode");
            System.out.println("2. Interactive mode");
            System.out.println("");
            System.out.println("");
            mode = readInput();
        }
        return mode;
    }

    private static String readInput() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String sInput = in.readLine();
        if (sInput.length() != 1 || !sInput.matches(numRegEx)) {
            System.out.println("");
            System.out.println("Please enter only a number from the list provided");
            System.out.println("");
            return selectAuthor();
        } else {
            return sInput;
        }
    }
}

