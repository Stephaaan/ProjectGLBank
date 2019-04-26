package application.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import application.Globals;
import application.Models.Client;
import application.Models.Employee;
import application.Utils.Hash;

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
			s.setString(2, password);
			
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
				s.setString(2, Hash.hash(password));
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
}
