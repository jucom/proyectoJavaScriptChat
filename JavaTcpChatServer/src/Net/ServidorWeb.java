package Net;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorWeb {

    public static void main (String[] args) throws Exception {
        ServerSocket sockfd;
        Socket newsockfd;
        int portno;
        ServiceThread doService;
        
        // leemos el puerto
        portno = 8081;
        System.out.println("numero de puerto : " + portno);
        
        
        // create a new socket : el identificador del recurso
        // socket IP,TCP
        // we listen on port portno, without a timeout
        try {
			sockfd = new ServerSocket(portno);
        
			System.out.println("sockfd : " + sockfd.toString());

        	newsockfd = new Socket();
			while (true) {        	
	        	
				//cuando el cliente establece una conexion el servidor abre un socket.
	        	newsockfd = sockfd.accept();
	        
	        	//once connexion is estableached we make a new thread
	        	doService = new ServiceThread(newsockfd);
	        	doService.start();
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("---------------------------------");
			System.err.println("Error in main : ");
			e.printStackTrace();
			System.err.println("---------------------------------");
		}
    }
}
