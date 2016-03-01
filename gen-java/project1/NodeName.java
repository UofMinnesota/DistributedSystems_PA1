package project1;

public class NodeName {
	
	private String myIP;
	private int myPort;
	private int myID;

	public void setID (int id) {
		this.myID = id;
	}
	public void setIP (String ip) {
		this.myIP = ip;
	}
	public void setPort (int port) {
		this.myPort = port;
	}
	public int getID() {
		return this.myID;
	}
	public String getIP() {
		return this.myIP;
	}
	public int getPort() {
		return this.myPort;
	}
	public NodeName(String ip, int port, int id){
		myID = id;
		myIP = ip;
		myPort = port;
	}
}