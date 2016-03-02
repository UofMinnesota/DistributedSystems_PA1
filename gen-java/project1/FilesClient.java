package project1;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.TException;
import java.util.ArrayList;
import java.util.Random;

public class FilesClient {
  static boolean USE_LOCAL = true;

  public static class  NodeInfo implements Comparable<NodeInfo>{
    String address = "";
    int port = 0;
    int hash = 0;


    public int compareTo(NodeInfo N) {
        return Integer.compare(hash, N.hash);
    }


  }

  private static ArrayList<NodeName> ListOfNodes = new ArrayList<NodeName>();

  public static NodeName strToNodeName(String input)
  {
    String data[] = input.split(":");
    NodeName newNo = new NodeName(data[0].trim(),Integer.parseInt(data[1]),Integer.parseInt(data[2]));
    //newNo.setIP(data[0].trim());
    //newNo.setPort(Integer.parseInt(data[1]));
    //newNo.setID(Integer.parseInt(data[2]));

    return newNo;
  }

  public static ArrayList<NodeName> strToNodeNameArray(String input)
  {
    ArrayList<NodeName> arrN = new ArrayList<NodeName>();
    String data[] = input.split("\\,");
    for(int c = 0; c < data.length; c++)
    {
      arrN.add(strToNodeName(data[c]));
    }
    return arrN;
  }

 public static void main(String[] args) {

  try {
   TTransport SuperNodeTransport;
   TTransport NodeTransport;


   String supernodeAddr = "csel-x29-10";
   if(USE_LOCAL) supernodeAddr = "localhost";
   SuperNodeTransport = new TSocket(supernodeAddr, 9090); // csel-x29-10
   SuperNodeTransport.open();

   TProtocol SuperNodeProtocol = new TBinaryProtocol(SuperNodeTransport);
   SuperNodeService.Client supernodeclient = new SuperNodeService.Client(SuperNodeProtocol);
   NodeName ndi = strToNodeName(supernodeclient.GetNode());
   System.out.println("Connecting to: " + ndi.getIP() + ":" + ndi.getPort());
   NodeTransport = new TSocket(ndi.getIP(), ndi.getPort());
   NodeTransport.open();

   TProtocol NodeProtocol = new TBinaryProtocol(NodeTransport);
   NodeService.Client nodeclient = new NodeService.Client(NodeProtocol);

   nodeclient.Write("ABC", "def test sss");

    System.out.println(nodeclient.Read("ABC"));

   SuperNodeTransport.close();
   NodeTransport.close();

  }// catch (TTransportException e) {
   //e.printStackTrace();
  //}
  catch (TException x) {
   x.printStackTrace();
  }
 }

 private static String getHostAddress(){
	 try {
		   InetAddress addr = InetAddress.getLocalHost();
		   	return (addr.getHostAddress());
		 } catch (UnknownHostException e) {
			 return null;
		 }
 }


}
