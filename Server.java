import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Server
 */
public class Server implements Runnable {
	
	// constants for different functions
	private static final File ROOT = new File(".");
	private static final String HOME_FILE = "index.html";
	private static final String NOTFOUND= "404.html";
	private static final String NOTSUPP = "not_supported.html";

	// instance fields
	private Socket connection;
	
	// Constructor
	public Server(Socket c) {
		this.connection = c;
	}


	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null; 
		BufferedOutputStream valOut = null;
		String fileToLookFor = null;

		try {
			// read in, print out and binary data
			InputStreamReader instr = new InputStreamReader(this.connection.getInputStream());
			in = new BufferedReader(instr); 
			out = new PrintWriter(this.connection.getOutputStream()); 
			valOut = new BufferedOutputStream(this.connection.getOutputStream()); 

			// input 
			String line = in.readLine();
			String [] methodAndFile = getTokens(line); 
			
			// now we have method and the file to look for
			String method = methodAndFile[0];
			fileToLookFor = methodAndFile[1];


			// if not get then error 
			if ( method.equals("GET") ) {

			} else {
				// get the not supported file
				File retFile = new File(ROOT, NOTSUPP); 
				int len = (int) retFile.length();

				byte[] theFileData = readFileData(retFile, len);

				out.println("HTTP/1.1 400 Bad Request");
				out.println("Server: Saad : 1.0");
				out.println("Date: " + new Date());
				out.println("Content-type: text/html");
				out.println("Content-length: " + len);
				out.println(); 
				out.flush(); 
				// file
				valOut.write(theFileData, 0, len);
				valOut.flush();
			}






		} catch (Exception e) {
			//TODO: handle exception
		}

	}

	private byte[] readFileData(File retFile, int len) throws IOException {
		FileInputStream in = null;
		byte[] data = new byte[len];

		try {
			in = new FileInputStream(retFile);
			in.read(data);
		} finally {
			if (in != null)
				in.close();
		}

		return data;
	}

	private String[] getTokens(String line) {
		try {
			// tokenize the line
			StringTokenizer parse = new StringTokenizer(line);
			String method = parse.nextToken().toUpperCase(); 
			String file = parse.nextToken().toLowerCase();
			
			return new String[] {method, file}; 

		} catch (Exception e) {
			System.err.println("not enough tokens");
		}
		return null; 
	}



	public static void main(String[] args) {
		try {
			// create the server socket 
			ServerSocket theConnection = new ServerSocket(8080);
			System.out.println("The Server has started and is listening");
			
			// start listening 
			while (true) {
				Socket clientSocket = theConnection.accept(); 
				Server serve = new Server(clientSocket); 


				// create a new thread for the client connection
				Thread aNewThread = new Thread(serve);
				aNewThread.start();
			}
		} catch (Exception e) {
			System.out.println("OH NO");
		}

	}
}

		



// 		while (true) {
// 			Socket clientSocket = server.accept();
// 			// Reading lines
// 			InputStreamReader inputStr = new InputStreamReader(clientSocket.getInputStream());
// 			BufferedReader reader = new BufferedReader(inputStr);
			
// 			// the first line of response
// 			String line = reader.readLine();
// 			String [] methodAndStatus = getStatus(line); 
			
// 			//Printer
// 			PrintWriter printHelp = new PrintWriter(clientSocket.getOutputStream());
			
// 			// Reads in the lines
// 			while (!line.isEmpty()) {
// 				// check if the line contains GET
// 				if (methodAndStatus[0].equals("GET")) {
// 					System.out.println("inside get if");
// 				}


// 				System.out.println(line);
// 				printHelp.print(line + "\r\n");
// 				line = reader.readLine();

// 			}
			
// 			inputStr.close();
// 			reader.close();
// 			printHelp.close();
// 			clientSocket.close();
// 		}

// 	}

// 	public static String[] getStatus(String line) {
		
// 		// tokenize the first row
// 		StringTokenizer parse = new StringTokenizer(line);
		
// 		// the method we are dealing with
// 		String method = "";  
// 		// the file
// 		String file = "";
// 		// Http style
// 		String http = "";
// 		try {
// 			method = parse.nextToken().toUpperCase();
// 			file = parse.nextToken().toLowerCase();
// 			http = parse.nextToken().toLowerCase(); 
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 			http = null; 
// 		}

// 		// check http
// 		if ( !http.equals("HTTP/1.1") ) {
// 			return new String[] {method, "400 Bad Request"};
// 		}
		
// 		// check file
// 		File lookingFor = new File(file);
// 		if (!lookingFor.exists()) {
// 			return new String[] {method, "404 Not Found"}; 
// 		} 

// 		// everything is good
// 		return new String[] {method, "200 OK"}; 
// 	}

	


// }