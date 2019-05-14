package application;

import application.Models.Client;
import application.Models.Employee;

public class ApplicationState {
	public static Employee loggedEmployee = null;
	public static Client client = null;
	public static class NewCard{
		public static int account_id = -1;
	}
}
