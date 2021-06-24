package thadome23.chatty.api.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	private ServerSocket server;                //this accept all the requests
	private int port;                            //the port of the server
	private int maxServers;                    //the maximum numbers of servers(connections) that can hold
	private List<SocketThread> sockets;            //the sockets
	private boolean run = false;
	private int connections_alive = 0;            //the currents connections alive


	public ChatServer(int maxservers){

		this(maxservers,8080);
	}

	public ChatServer(int maxservers, int port) {

		this.port = port;
		this.maxServers = maxservers;                  /*Initialization of fields*/
		this.sockets = new ArrayList<>(maxservers);

	}
	
	public void start() {
		
		run = true;
		
		try {  server= new ServerSocket(port);  }
		catch (IOException e)
        {
            System.out.println("cannot bind to the specified port");
            System.out.println(e.getMessage());
            close();
		}
		
		if(run)
			System.out.println("server bind to port: "+ port);
		
		while (run) {
			
			if (connections_alive < maxServers) {

				try {

					SocketThread s = new SocketThread(server.accept(), this);   //<<-------- //it passes also the current instance
					s.start();                                                           	 //for make possible to delete the connection
					sockets.add(s);            //add the current connection to the list		 //with @method release_resource
					System.out.println("connected to " + s.getStringClientIp());
				
				} catch (IOException e) {
					System.out.println("error connecting to client");
				}

				synchronized (this) {
					connections_alive++;
				}
			}
		}
	
	}

	public synchronized void release_resource(SocketThread thread) {
		thread.closeConnection();
		System.out.println("closed connection with " + thread.getStringClientIp());
		sockets.remove(thread);
		synchronized (this) {
			connections_alive--;
		}

	}


	public void close() {
		run = false;
		int a = connections_alive;
		
		for(int i = 0; i<a; i++) {
			release_resource(sockets.get(i));
		}
		
		try {
			server.close();
		} catch (IOException e) {
			
			System.out.println("error 123");
		}
		System.out.println("server closed");
	}
	
	public void destroy() {
	
		System.exit(0);
	}
	
	public boolean isOnline() {
		return run;
	}

	public int getPort() {
		return port;
	}

}
