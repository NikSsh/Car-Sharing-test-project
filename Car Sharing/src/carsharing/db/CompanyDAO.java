package carsharing.db;

import carsharing.db.model.Car;
import carsharing.db.model.Company;

import java.util.List;
import java.util.Map;

public interface CompanyDAO {
    Map<Integer, Company> getAllCompanies();
    List<Car> getAllCompanyCars(int companyId);
    public void createCompany(String companyName);
    public void createCar(String carName,int companyId);

}
