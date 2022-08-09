package carsharing;

import carsharing.db.Database;
import carsharing.db.model.Car;
import carsharing.db.model.Company;
import carsharing.db.model.Customer;
import carsharing.menus.ChooseCustomerMenu;
import carsharing.menus.CustomerMenu;
import carsharing.menus.ManagerMenu;
import carsharing.menus.MenuChooser;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Carsharing {
    private final Scanner scanner = new Scanner(System.in);
    private final Database database;
    private final MenuChooser menuChooser = new MenuChooser();

    public Carsharing(String dbName) {
        database = Database.createDB(dbName);
    }

    public void run() {
        mainMenu();
        database.closeDB();
    }

    public void mainMenu() {
        final String menuMessage = "1. Log in as a manager\n" +
                                   "2. Log in as a customer\n" +
                                   "3. Create a customer\n" +
                                   "0. Exit";
        int action;
        do {
            System.out.println(menuMessage);

            action = scanner.nextInt();
            System.out.println();

            switch (action) {
                case 1: {
                    menuChooser.setChosenMenu(new ManagerMenu(database));
                    menuChooser.executeMenu();
                    break;
                }

                case 2: {
                    menuChooser.setChosenMenu(new ChooseCustomerMenu(database));
                    menuChooser.executeMenu();
                    break;
                }

                case 3: {
                    createCustomer();
                    break;
                }
                case 0: {
                    System.out.println("Bye!");
                    break;
                }
                default : {
                    System.out.println("Unknown command");
                }
            }

        } while(action != 0);
    }

    public void createCustomer() {
        System.out.println("Enter the customer name:");
        String customerName = scanner.next();
        scanner.nextLine();
        boolean success = database.createCustomer(customerName);
        if (success) {
            System.out.println("The customer was added!");
        }
    }

}
