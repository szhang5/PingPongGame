import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PingPongServer {

	public static void main(String[] args) throws Exception {
		ServerSocket listener = new ServerSocket(6666);
		System.out.println("Ping-Pong Server is Running");
		
		Socket socket = listener.accept();
		
	}

}
