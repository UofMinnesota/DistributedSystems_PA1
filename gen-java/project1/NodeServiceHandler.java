package project1;

import org.apache.thrift.TException;

public class NodeServiceHandler implements NodeService.Iface {

 @Override
 public boolean Write(String Filename, String Contents) throws TException {
  //String NodeList = " ";

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
