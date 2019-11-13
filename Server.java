import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Server
 */
public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8080);
		System.out.println("listening");
		while (true) {
			Socket clientSocket = server.accept();
			// Reading lines
			InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			
			// the first line of response
			String line = reader.readLine();
			String [] methodAndStatus = getStatus(line); 
			
			//Printer
			PrintWriter printHelp = new PrintWriter(clientSocket.getOutputStream());
			
			// Reads in the lines
			while (!line.isEmpty()) {
				// check if the line contains GET
				if (methodAndStatus[0].equals("GET")) {
					System.out.println("inside get if");
				}


				System.out.println(line);
				printHelp.print(line + "\r\n");
				line = reader.readLine();

			}
			
			isr.close();
			reader.close();
			printHelp.close();
			clientSocket.close();
		}

	}

	public static String[] getStatus(String line) {
		
		// tokenize the first row
		StringTokenizer parse = new StringTokenizer(line);
		
		// the method we are dealing with
		String method = "";  
		// the file
		String file = "";
		// Http style
		String http = "";
		try {
			method = parse.nextToken().toUpperCase();
			file = parse.nextToken().toLowerCase();
			http = parse.nextToken().toLowerCase(); 
		} catch (Exception e) {
			e.printStackTrace();
			http = null; 
		}

		// check http
		if ( !http.equals("HTTP/1.1") ) {
			return new String[] {method, "400 Bad Request"};
		}
		
		// check file
		File lookingFor = new File(file);
		if (!lookingFor.exists()) {
			return new String[] {method, "404 Not Found"}; 
		} 

		// everything is good
		return new String[] {method, "200 OK"}; 
	}
}