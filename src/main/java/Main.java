import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        System.out.println(engine.search("бизнес"));
        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                    String word = bufferedReader.readLine();
                    printWriter.println(engine.search(word));
                }
            }
        } catch (IOException e) {
            System.out.println("не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}




