package project1;

import org.apache.thrift.TException;
import java.util.ArrayList;

public class SuperNodeServiceHandler implements SuperNodeService.Iface {

  public class  NodeInfo{
    String address = "";
    int port = 0;

  }

  ArrayList<NodeInfo> ListOfNodes = new ArrayList<NodeInfo>();
  boolean isBusy = false;

 @Override
 public String Join(String IP, int Port) throws TException {
  if(isBusy)
  {
    return "NACK";
  }
  String NodeList = " ";

  for(int x = 0; x < ListOfNodes.size(); x++)
  {
    NodeList += ListOfNodes.get(x).address + ":" + String.valueOf(ListOfNodes.get(x).port) + ",";
  }

  isBusy = true;

  return NodeList;
 }

 @Override
 public String GetNode() throws TException {
  String NodeList = " ";

  return NodeList;
 }

 @Override
 public boolean PostJoin(String IP, int Port) throws TException {
   isBusy = true;

   return isBusy;
 }


}
