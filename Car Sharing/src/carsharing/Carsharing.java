package carsharing;

import carsharing.db.Database;
import carsharing.db.model.Car;
import carsharing.db.model.Company;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Carsharing {
    private final Scanner scanner = new Scanner(System.in);
    private final Database database;

    public Carsharing(String dbName) {
        database = Database.createDB(dbName);
    }

    public void run() {
        mainMenu();
        database.closeDB();
    }

    public void mainMenu() {
        final String menuMessage = "1. Log in as a manager\n" +
                                    "0. Exit";
        int action;
        do {
            System.out.println(menuMessage);

            action = scanner.nextInt();
            System.out.println();
            if (action == 1) {
                managerMenu();
            }

        } while(action != 0);
    }

    public void managerMenu() {
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
                                companyMenu(choice, name);
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

    public void companyMenu(int companyId, String companyName) {
        String companyMenuStr = "'" + companyName + "' company\n" +
                                "1. Car list\n" +
                                "2. Create a car\n" +
                                "0. Back";
        int choice;
        do {
            System.out.println(companyMenuStr);
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: {
                    List<Car> carsList = database.getAllCompanyCars(companyId);
                    if (!carsList.isEmpty()) {
                        System.out.println("Car list:");

                        int counter = 1;

                        for (Car car : carsList) {
                            System.out.println(counter + ". " + car.getCarName());
                            counter++;
                        }
                    } else {
                        System.out.println("The car list is empty!");
                    }
                    System.out.println();
                    break;
                }

                case 2: {
                    System.out.println("Enter the car name:");
                    String carName = scanner.nextLine();
                    if (carName.matches("\\d+")) {
                        System.out.println("The car list is empty!");
                    } else {
                        database.createCar(carName, companyId);
                    }
                    System.out.println();
                    break;
                }
            }

        } while (choice != 0);

    }

}
