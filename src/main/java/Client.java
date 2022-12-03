import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        int PORT = 8989;
        String HOST = "127.0.0.1";
        try (
                Socket socket = new Socket(HOST, PORT);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            Scanner in = new Scanner(System.in);
            System.out.println(bufferedReader.readLine());
            String word = in.nextLine();
            printWriter.println(word);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
