package application.Models;

public class Employee {
    private String firstName;
    private String lastName;
    private String login;
    private int position;
    private int id;

    public Employee(String firstName, String lastName, String login, int position, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.position = position;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public int getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }
    @Override
    public String toString() {
    	return firstName + " " + lastName;
    }
}