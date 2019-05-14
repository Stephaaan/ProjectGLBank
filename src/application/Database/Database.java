package application.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import application.Globals;
import application.Tools;
import application.Models.Account;
import application.Models.Card;
import application.Models.Client;
import application.Models.Employee;

public class Database {
	private Database() {};
	
	private static Database db = null;
	
	public static Database getInstance() {
		if(db == null) {
			db = new Database();
		}
		return db;
	}
	
	private Connection getConnection(){
		Connection conn;
	    try {
	    	Class.forName(Globals.dbClassName);
	    	conn=DriverManager.getConnection(Globals.dbUrl, Globals.username, Globals.password);
	    	return conn;
	    } catch(Exception ex) {
	    	ex.printStackTrace();
	    }
	    return null;
	}
	
	public Employee EmployeeLogin(String login, String password) {
		String sql = "Select Employee.id, lname, fname, position, login from Employee inner join LoginEmployee on idemployee = Employee.id where login like ? and password like ?";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, login);
			s.setString(2, Tools.getSHA(password));
			
			 ResultSet rs = s.executeQuery();

             if (rs.next()) {
                 return new Employee(rs.getString("fname"),rs.getString("lname"), rs.getString("login"),rs.getInt("position"), rs.getInt("id"));
             }
			
			return null;
		} catch (SQLException e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public void insertNewClient(Client c, String password) {
		String sql1 = "Insert into client (fname, lname, email) values (?,?,?)";
		String sql2 = "Insert into loginclient(login, password, idclient) values (?,?,?)";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
			s.setString(1,c.getFname());
			s.setString(2,c.getLname());
			s.setString(3,c.getEmail());
			
			s.executeUpdate();
			
			ResultSet rs = s.getGeneratedKeys();
			
			if(rs.next()) {
				int id = rs.getInt(1);
				s = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
				s.setString(1, c.getLogin());
				s.setString(2, Tools.getSHA(password));
				s.setInt(3, id);
				s.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public LinkedList<Client> getAllClients(){
		String sql = "Select Client.id, lname, fname, email, login FROM Client INNER JOIN LoginClient ON idclient=Client.id ORDER BY lname, fname";
		LinkedList<Client> list = new LinkedList<Client>();
		try(Connection conn = getConnection()){
			ResultSet rs = conn.prepareStatement(sql).executeQuery();
			
			while(rs.next()) {
				list.add(new Client(rs.getString("fname"),rs.getString("lname"),rs.getString("login"),rs.getInt("id"), rs.getString("email")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public LinkedList<Account> getAllAccountsOfClient(int id){
		String sql = "Select * from account where IDClient = ?";
		LinkedList<Account> accounts = new LinkedList<Account>();
		
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setInt(1, id);
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				accounts.add(new Account(rs.getInt("ID"), rs.getString("accNum"), rs.getFloat("money"), rs.getInt("IDClient")));
			}
			return accounts;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	public float getAmountOfAccount(String accNum) {
		String sql = "Select money from account where accNum like ?";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, accNum);
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				return rs.getFloat("money");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0f;
	}
	public void widthraw(float amount, String accountNumber) {
		float a = getAmountOfAccount(accountNumber);
		String sql = "Update account set money = ? where accNum like ?";
		
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setFloat(1, a-amount);
			s.setString(2, accountNumber);
			s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deposit(float amount, String accountNumber) {
		float a = getAmountOfAccount(accountNumber);
		String sql = "Update account set money = ? where accNum like ?";
		
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setFloat(1, a+amount);
			s.setString(2, accountNumber);
			s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void updatePassword(int userID, String password) {
		String sql = "Update loginClient set password = ? where idclient = ?";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, Tools.getSHA(password));
			s.setInt(2, userID);
			s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public LinkedList<Card> getCardsOfAccount(String accountNUmber){
		LinkedList<Card> list = new LinkedList<Card>();
		
		String sql = "Select * from card inner join account on card.idaccount = account.id where account.accNum like ?";
		
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, accountNUmber);
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				list.add(new Card(rs.getInt("id"), rs.getString("PIN"), rs.getBoolean("active"), rs.getInt("expireY"), rs.getInt("expireM"), rs.getInt("IDAccount")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public Card getCardInfo(int id) {
		String sql = "Select * from card where id = ?";
		
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setInt(1, id);
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				return new Card(rs.getInt("id"), rs.getString("PIN"), rs.getBoolean("active"), rs.getInt("expireY"), rs.getInt("expireM"), rs.getInt("IDAccount"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setCardState(boolean state, int id) {
		String sql = "Update card set active = ? where id = ?";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setBoolean(1, state);
			s.setInt(2, id);
			s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getAccountIdByAccountNumber(String accountNumber) {
		String sql = "select id from account where accnum like ?";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, accountNumber);
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public void createCard(int idAccount, String pin, String expireDate) {
		String sql = "Insert into card(pin, active, expirey, expirem, idaccount) values (?, true, ?, ?, ?)";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, pin);
			s.setInt(2, Integer.parseInt(expireDate.split("/")[0]));
			s.setInt(3, Integer.parseInt(expireDate.split("/")[1]));
			s.setInt(4, idAccount);
			s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createAccount(int idUser, String accountNumber) {
		String sql = "Insert into account(accNum, money, IDClient) values(?, 0, ?)";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, accountNumber);
			s.setInt(2, idUser);
			s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean existAccountNumber(String accountNumber) {
		String sql = "Select count(id) as count from account where accNum like ?";
		try(Connection conn = getConnection()){
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, accountNumber);
			ResultSet rs = s.executeQuery();
				
			rs.next();
			if(rs.getInt("count")>0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
