package carsharing.menus;

import carsharing.db.Database;
import carsharing.db.model.Customer;

import java.util.List;

public class ChooseCustomerMenu extends Menu {

    public ChooseCustomerMenu(Database database) {
        super(database);
    }

    @Override
    void execute() {
        List<Customer> customerList = database.getAllCustomers();

        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty");
        } else {
            StringBuilder menuBuilder = new StringBuilder();
            customerList.forEach(customer ->
                    menuBuilder.append(customer.getId()).append(". ").append(customer.getName()).append("\n"));
            menuBuilder.append("0. Back\n");
            String customerMenu = menuBuilder.toString();
            int action;
            do {
                System.out.println(customerMenu);

                action = scanner.nextInt();
                scanner.nextLine();

                if (action > 0 && action <= customerList.size()) {
                    menuChooser.setChosenMenu(new CustomerMenu(database, action));
                    menuChooser.executeMenu();
                }


            } while (action != 0);
        }
    }
}
