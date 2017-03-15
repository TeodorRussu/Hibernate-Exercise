package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cache.spi.entry.StructuredCacheEntry;
import org.hibernate.cfg.Configuration;

import entity_classes.Employee;

public class HibernateExerciseApp {

	public static void main(String[] args) throws IOException {
		// create console input handler
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// create the session factory
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Employee.class)
				.buildSessionFactory();
		// create the session

		try {
			// app logic
			
			// begin the transaction
			while (true) {

				// select the action: add, edit, or delete employee
				String selectedAction = selectAction(reader);

				// if action = adding employee to the DB, t.c.w.e.
				if (selectedAction.equals("1")) {
					saveEmployeeToDB(reader, factory);
					continue;
				}
				
				// if action = view a employee from the DB by id, t.c.w.e.
				else if (selectedAction.equals("2")) {
					viewEmployeebyIdFromDB(reader, factory);
					continue;
				}
				
				// if action = see all emplyees from a certain company, t.c.w.e.
				else if (selectedAction.equals("3")) {
					displayEmployeeFromCompanyDB(reader, factory);
					continue;
				}

				// if action = delete a user, by selected id, t.c.w.e.
				else if (selectedAction.equals("4")) {
					deleteUserFromDb(reader, factory);
					continue;
				}

				// app exit command
				else if (selectedAction.equals("exit")) {
					break;
				}

				// other invalid input
				else {
					System.out.println("invalid command");
					continue;
				}
			}

		} finally {
			// close the factory
			factory.close();
		}
	}

	// if user want to see all emplyees from a certain company, t.c.w.e.
	private static void displayEmployeeFromCompanyDB(BufferedReader reader, SessionFactory factory)
			throws HibernateException, IOException {
		// create session
		Session session = factory.getCurrentSession();
		// begin transaction
		session.beginTransaction();
		System.out.println("Available companies:");
		List<Employee> listOfAvailableCompanies = session.createQuery("from Employee").list();
		Set<String> companiesInOneExemplar = new HashSet();

		for (Employee e : listOfAvailableCompanies) {
			if (!(companiesInOneExemplar.contains(e.getCompany()))) {
				System.out.println(e.getCompany());
			}
			companiesInOneExemplar.add(e.getCompany());
		}
		System.out.println("type a company name.");
		String companyName = reader.readLine();

		if (companiesInOneExemplar.contains(companyName)) {
			List<Employee> employeesFromSameCompany = session
					.createQuery("from Employee where company='" + companyName + "'").list();

			// display the employees from list
			displayTheEmployeesFromList(employeesFromSameCompany);

		} else {
			System.out.println("company doesn't exist");

		}
		// commit the transaction
		session.getTransaction().commit();
	}

	// method for retrieve and view a employee, by a selected id
	private static void viewEmployeebyIdFromDB(BufferedReader reader, SessionFactory factory)
			throws HibernateException, NumberFormatException, IOException {
		
		
		
		
		
		
		
		// create session
		Session session = factory.getCurrentSession();
		// begin transaction
		session.beginTransaction();
		System.out.println("Please type the employee id");
		int employeeInputedId = new Integer(reader.readLine());

		Employee retrievedEmployee = session.get(Employee.class, employeeInputedId);

		System.out.println("\n\n" + retrievedEmployee.toString());
		// commit the transaction
		session.getTransaction().commit();
	}

	// method for selecting the action: add, edit, or delete employee from DB
	private static String selectAction(BufferedReader reader) throws IOException {
		System.out.println("\n\n\nIf you want to add a employee to database, please enter 1 \n"
				+ "If you want to view a employee details, please enter 2\n"
				+ "If you want to view all employees for a selected company, please enter 3\n"
				+ "If you want to delete a employee from the database, please enter 4 ");
		String selectedAction = reader.readLine();
		return selectedAction;
	}

	// method for adding a new employee to the database
	private static void saveEmployeeToDB(BufferedReader reader, SessionFactory factory)
			throws HibernateException, IOException {
		// create session
		Session session = factory.getCurrentSession();
		// begin transaction
		session.beginTransaction();
		// user input the first name, the last name, and the company
		System.out.println("please type the employee first name");
		String fistName = reader.readLine();
		System.out.println("please type the employee last name");
		String lastName = reader.readLine();
		System.out.println("please type the employee company");
		String company = reader.readLine();

		// create new Employee
		Employee employee = new Employee(fistName, lastName, company);

		// save the object to the database;
		session.save(employee);
		// commit the transaction
		session.getTransaction().commit();
	}

	// method to delete user from the database
	private static void deleteUserFromDb(BufferedReader reader, SessionFactory factory)
			throws HibernateException, IOException {
		Session session = factory.getCurrentSession();
		session.beginTransaction();

		// retrieve all employees from DB
		System.out.println("\n\n\ntype the id of the employee to be deleted");
		List<Employee> employees = session.createQuery("from Employee").list();

		// create a list where we will put the retrieved id's
		List<String> ids = new ArrayList<>();

		// show in console available id's
		for (Employee e : employees) {
			System.out.println(e.getId());
			ids.add("" + e.getId());
		}

		boolean b = true;
		while (b) {
			// user input the id to delete
			String idToDelete = reader.readLine();

			// if id exist, the user with this id, will be deleted
			if (ids.contains(idToDelete)) {
				session.createQuery("delete from Employee where id=" + idToDelete).executeUpdate();
				session.getTransaction().commit();
				break;
			}
			// if user input inexistent id1`
			else {
				System.out.println("invalid id");
				continue;
			}
		}
	}

	// display the employees from list in console
	private static void displayTheEmployeesFromList(Collection<Employee> employeesFromSameCompany) {
		System.out.println("\n\n");
		for (Employee emp : employeesFromSameCompany) {
			System.out.println(emp.toString());
		}
	}

	// methods

}
