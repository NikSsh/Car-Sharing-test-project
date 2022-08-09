package carsharing.menus;

import carsharing.db.Database;
import carsharing.db.model.Company;

import java.util.Map;

public class ManagerMenu extends Menu {
    public ManagerMenu(Database database) {
        super(database);
    }

    @Override
    void execute() {
        final String managerMenuMessage = "1. Company list\n" +
                "2. Create a company\n" +
                "0. Back";
        int action;
        do {
            System.out.println(managerMenuMessage);

            action = scanner.nextInt();
            scanner.nextLine();
            switch (action) {
                case 1: {
                    Map<Integer, Company> companies = database.getAllCompanies();
                    if (!companies.isEmpty()) {
                        int choice;
                        do {
                            System.out.println("Choose the company:");
                            for (var entry : companies.entrySet()) {
                                System.out.println(entry.getKey() + ". " + entry.getValue().getCompanyName());
                            }
                            System.out.println("0. Back");
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            if (companies.containsKey(choice)) {
                                String name = companies.get(choice).getCompanyName();
                                menuChooser.setChosenMenu(new CompanyMenu(database, name , choice));
                                menuChooser.executeMenu();
                            }
                        } while (choice != 0);
                    } else {
                        System.out.println("The company list is empty!");
                        System.out.println();
                    }
                    break;
                }

                case 2: {
                    System.out.println("Enter the company name:");
                    String companyName = scanner.nextLine();
                    database.createCompany(companyName);
                    System.out.println("The company was created");
                    System.out.println();
                    break;
                }

                case 0: {
                    break;
                }
            }


        } while (action != 0);
    }
}
