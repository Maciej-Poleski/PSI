import java.io.IOException;

/**
 * User: Maciej Poleski
 * Date: 27.02.13
 * Time: 18:46
 */

public class Main {
    public static void main(String... args) throws IOException {
        new Thread(new Server(9000)).start();
    }
}
