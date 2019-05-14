package application.Models;

public class Account {
	private int id;
	private String accNum;
	private float money;
	private int idClient;
	
	public Account(int id, String accNum, float money, int idClient) {
		super();
		this.id = id;
		this.accNum = accNum;
		this.money = money;
		this.idClient = idClient;
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public int getIdClient() {
		return idClient;
	}
	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}
}
