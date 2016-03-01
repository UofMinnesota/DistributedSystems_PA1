package project1;

import org.apache.thrift.TException;
import java.security.*;
import java.util.ArrayList;
import java.util.Random;


public class NodeServiceHandler implements NodeService.Iface {

  public class  NodeInfo{
    String address = "";
    int port = 0;
    int hash = 0;
  }

  ArrayList<NodeInfo> ListOfNodes = new ArrayList<NodeInfo>();

  public NodeInfo strToNodeInfo(String input)
  {
    String data[] = input.split(":");
    NodeInfo newNo = new NodeInfo();
    newNo.address = data[0];
    newNo.port = Integer.parseInt(data[1]);
    newNo.hash = Integer.parseInt(data[2]);

    return newNo;
  }

  public ArrayList<NodeInfo> strToNodeInfoArray(String input)
  {
    ArrayList<NodeInfo> arrN = new ArrayList<NodeInfo>();
    String data[] = input.split(",");
    for(int c = 0; c < data.length; c++)
    {
      arrN.add(strToNodeInfo(data[c]));
    }
    return arrN;
  }

  public String MD5(String md5) {
     try {
          java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
          byte[] array = md.digest(md5.getBytes());
          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
         }
          return sb.toString();
      } catch (java.security.NoSuchAlgorithmException e) {

      return null;}
  }

 @Override
 public boolean Write(String Filename, String Contents) throws TException {
  //String NodeList = " ";
  String md5Hash = MD5(Filename);
  System.out.println("Filename is"+Filename);


  return false;
 }

 @Override
 public String Read(String Filename) throws TException {
  String NodeList = " ";

  return NodeList;
 }

 @Override
 public boolean UpdateDHT(String NodeList) throws TException {


  return false;
 }


}
