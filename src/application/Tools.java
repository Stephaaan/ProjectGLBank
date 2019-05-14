package application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import application.Database.Database;

public class Tools {
	 public static String getSHA(String input) 
	    { 
	  
	        try { 
	  
	            // Static getInstance method is called with hashing SHA 
	            MessageDigest md = MessageDigest.getInstance("SHA-256"); 
	  
	            // digest() method called 
	            // to calculate message digest of an input 
	            // and return array of byte 
	            byte[] messageDigest = md.digest(input.getBytes()); 
	  
	            // Convert byte array into signum representation 
	            BigInteger no = new BigInteger(1, messageDigest); 
	  
	            // Convert message digest into hex value 
	            String hashtext = no.toString(16); 
	  
	            while (hashtext.length() < 32) { 
	                hashtext = "0" + hashtext; 
	            } 
	  
	            return hashtext; 
	        } 
	  
	        // For specifying wrong message digest algorithms 
	        catch (NoSuchAlgorithmException e) { 
	            System.out.println("Exception thrown"
	                               + " for incorrect algorithm: " + e); 
	  
	            return null; 
	        } 
	    } 
	 public static String generatePIN() {
		 Random rnd = new Random();
		 int first = rnd.nextInt(10);
		 while(first == 0) {
			 first = rnd.nextInt(10);
		 }
		 int second = rnd.nextInt(10);
		 int third = rnd.nextInt(10);
		 int fourth = rnd.nextInt(10);
		 
		 return "" + first + "" + second + "" + third + "" + fourth;
	 }
	 public static String generateAccountNumber() {
		 boolean exists = true;
		 String newAccNum = "";
		 while(exists) {
			 newAccNum = Globals.bank_prefix + " " + getQuadrant() + " " + getQuadrant() + " " + getQuadrant();
			 //System.out.println(newAccNum);
			 exists = Database.getInstance().existAccountNumber(newAccNum);
		 }
		 return newAccNum;
	 }
	 private static String getQuadrant() {
		 String s = "";
		 Random rnd = new Random();
		 for(int i = 0; i < 4; i++) {
			 s+=rnd.nextInt(10) +"";
		 }
		 return s;
	 }
}
