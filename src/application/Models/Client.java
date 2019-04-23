package application.Models;

public class Client {
	  private String fname;
	  private String lname;
	  private String login;
	  private int id;
	  private String email;

	  public Client(String fname, String lname, String login, int id, String email) {
		  this.fname = fname;
		  this.lname = lname;
		  this.login = login;
		  this.id = id;
		  this.email = email;
	  }

	  public void setEmail(String email) {
		  this.email = email;
	  }

	  public String getFname() {
		  return fname;
	  }

	  public String getLname() {
		  return lname;
	  }

	  public String getLogin() {
		  return login;
	  }

	  public int getId() {
		  return id;
	  }

	  public String getEmail() {
		  return email;
	  }
}
