package application;

public class Globals {
    public static final String dbUrl;
    public static final String dbClassName;
    public static final String username;
    public static final String password;
    public static final String bank_prefix;
    
    
    static {
        dbUrl = "jdbc:mysql://localhost:3306/db_gl_bank";
        dbClassName = "com.mysql.cj.jdbc.Driver";
        username = "root";
        password = "";
        bank_prefix = "4405";
    }
}