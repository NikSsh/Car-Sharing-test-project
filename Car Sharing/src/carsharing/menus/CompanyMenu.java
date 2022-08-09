package carsharing.menus;

import carsharing.db.Database;
import carsharing.db.model.Car;

import java.util.List;
import java.util.Scanner;

public class CompanyMenu extends Menu {
    private final String companyName;
    private final int companyId;

    public CompanyMenu(Database database, String companyName, int companyId) {
        super(database);
        this.companyName = companyName;
        this.companyId = companyId;
    }

    @Override
    void execute() {
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
                    List<Car> carsList = database.getAllAvailableCompanyCars(companyId);
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
