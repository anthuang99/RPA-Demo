package org.webdriver.patatiumwebui.httprequest;

import java.security.MessageDigest;

/**
 * MD5的算法在RFC1321 中定义
 * 在RFC 1321中，给出了Test suite用来检验你的实现是否正确： 
 * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e 
 * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661 
 * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72 
 * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0 
 * MD5 ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b 
 * 
 * @author haogj
 *
 * 传入参数：一个字节数组
 * 传出参数：字节数组的 MD5 结果字符串
 */
public class MD5Util {
	private MD5Util() {}

	public static String convert(String s){
     char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
      try { 
          byte[] bytes = s.getBytes(); 
          MessageDigest md = MessageDigest.getInstance("MD5");
          md.update(bytes); 
          bytes = md.digest(); 
          int j = bytes.length; 
         char[] chars = new char[j * 2]; 
          int k = 0; 
          for (int i = 0; i < bytes.length; i++) { 
              byte b = bytes[i]; 
              chars[k++] = hexChars[b >>> 4 & 0xf]; 
              chars[k++] = hexChars[b & 0xf]; 
          } 
          return new String(chars);
      } 
      catch (Exception e){
          return null; 
      } 
 }
 
 //public static void main(String[] args) {
	 //System.out.println("123456:" + MD5Util.convert("123456"));
	 //System.out.println(MD5Util.convert("123456").length());
 //}
}


