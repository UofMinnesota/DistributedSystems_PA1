package project1;

import org.apache.thrift.TException;
import java.security.*;

public class NodeServiceHandler implements NodeService.Iface {


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
