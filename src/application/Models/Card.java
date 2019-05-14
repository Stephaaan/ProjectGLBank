package application.Models;

public class Card {
	private int id;
	private String pin;
	private boolean active;
	private int expirey;
	private int expirem;
	private int idAccount;
	public Card(int id, String pin, boolean active, int expirey, int expirem, int idAccount) {
		super();
		this.id = id;
		this.pin = pin;
		this.active = active;
		this.expirey = expirey;
		this.expirem = expirem;
		this.idAccount = idAccount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getExpirey() {
		return expirey;
	}
	public void setExpirey(int expirey) {
		this.expirey = expirey;
	}
	public int getExpirem() {
		return expirem;
	}
	public void setExpirem(int expirem) {
		this.expirem = expirem;
	}
	public int getIdAccount() {
		return idAccount;
	}
	public void setIdAccount(int idAccount) {
		this.idAccount = idAccount;
	}
	
}
