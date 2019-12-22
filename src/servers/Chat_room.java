package servers;

import java.util.ArrayList;
import java.util.List;

public class Chat_room{
	
private String 		   		room_name;
private	List<SocketThread> 	users;
private int 				max;

private static List<Chat_room> 	roomsRegistry = new ArrayList<>();

//private String[] cronology;
		
public Chat_room(String name, int max) {
	this.max=max;
	this.setName(name);			//assign the name to the room
	Chat_room.addRoom(this);		//add the current room to the registry
	this.users = new ArrayList<>(max);	//initialize a list for roomsRegistry' components
}

public String getName() {
	return room_name;
}

public void setName(String room_name) {
	this.room_name = room_name;
}

public synchronized boolean joinRoom(SocketThread c) {
	if(this.activeUsers()<max){
		serverMessage(""+c.getUsername()+" joined the chat") ;
		users.add(c);

		return true;
	}else{
		return false;
	}
}
private void serverMessage(String text){
	users.forEach( (SocketThread s)-> { s.sendMessage(text); });

}
public void textMessage(String msg, String username) {
	users.forEach( (SocketThread s)-> {s.sendMessage("["+username+"]: "+msg);});
}

private static void addRoom(Chat_room c) {
	roomsRegistry.add(c);
}

public static ArrayList<Chat_room> getAvaibleRooms() {
	return (ArrayList<Chat_room>) roomsRegistry;
}


public static Chat_room getRoomByName(String c) {
	Chat_room[] d = new Chat_room[roomsRegistry.size()];
	roomsRegistry.toArray(d);
	for(int i = 0 ;i<roomsRegistry.size();i++){
		
		if( d[i].getName().equals(c) ){
			return d[i];
		}
	}
	
	return null;
}

public void deleteUser(SocketThread s) {
	users.remove(s);
	serverMessage(""+s.getUsername()+" left the chat");
}

public int maxUsers(){
	return max;
}
public int activeUsers(){
	return users.size();
}
}
