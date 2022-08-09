package carsharing.menus;

import carsharing.db.Database;
import carsharing.db.model.Car;
import carsharing.db.model.Company;

import java.util.List;
import java.util.Map;

public class CustomerMenu extends Menu{
    private final int customerId;
    public CustomerMenu(Database database, int customerId) {
        super(database);
        this.customerId = customerId;
    }

    @Override
    void execute() {
        final String customerMenu = "1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back";
        int action;
        do {
            System.out.println(customerMenu);
            action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1: {
                    if (!database.getCustomerCarInfo(customerId).isEmpty()) {
                        System.out.println("You've already rented a car!");
                    } else {
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
                                    rentCar(choice, customerId);
                                    break;
                                }
                            } while (choice != 0);
                        } else {
                            System.out.println("The company list is empty!");
                            System.out.println();
                        }
                    }
                    break;
                }

                case 2: {
                    boolean success = database.returnRentedCar(customerId);
                    if (success) {
                        System.out.println("You've returned a rented car!");
                    } else {
                        System.out.println("You didn't rent a car");
                    }
                    break;
                }

                case 3: {
                    Map<String, String> customerCarInfoMap = database.getCustomerCarInfo(customerId);

                    if (customerCarInfoMap.isEmpty()) {
                        System.out.println("You didn't rent a car!");
                    } else {
                        System.out.println("Your rented car:");
                        System.out.println(customerCarInfoMap.get("carName"));
                        System.out.println("Company:");
                        System.out.println(customerCarInfoMap.get("companyName"));

                    }
                }
            }
        } while (action != 0);
    }

    private void rentCar(int companyId, int customerId) {
        List<Car> carsList = database.getAllAvailableCompanyCars(companyId);
        if (!carsList.isEmpty()) {
            int choice;
            do {
                System.out.println("Car list:");
                int counter = 1;
                for (Car car : carsList) {
                    System.out.println(counter + ". " + car.getCarName());
                    counter++;
                }
                System.out.println("0. Back");

                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice != 0) {
                    Car chosenCar = carsList.get(choice - 1);
                    boolean success = database.rentCar(customerId, chosenCar.getId());
                    if (success) {
                        System.out.println("You rented " + chosenCar.getCarName() + "\n");
                    }
                    break;
                }
            } while (choice != 0);
        } else {
            System.out.println("There are no available cars!");
        }
    }
}
