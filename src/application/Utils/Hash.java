package application.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class Hash {
	public static String hash(String toHash) {
		 MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(toHash.getBytes());
		    byte[] digest = md.digest();
		    return DatatypeConverter.printHexBinary(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
