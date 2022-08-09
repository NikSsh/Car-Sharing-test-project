package carsharing.db;

import carsharing.db.model.Car;
import carsharing.db.model.Company;
import carsharing.db.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    static final String JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:file:./db/";
    private final Connection connection;
    private static Database db = null;
    private static final String SQL_GET_ALL_COMPANIES = "SELECT * FROM COMPANY;";
    private static final String SQL_ADD_NEW_COMPANY = "INSERT INTO COMPANY (NAME) VALUES (?);";
    private static final String SQL_ADD_NEW_CAR = "INSERT INTO CAR (NAME,COMPANY_ID) VALUES (?,?)";
    private static final String SQL_ADD_NEW_CUSTOMER = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
    private static final String SQL_RENT_CAR = "UPDATE CAR SET CUSTOMER_ID = ? WHERE ID = ?;";
    private static final String SQL_RENT_CAR2 = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?;";
    private static final String SQL_RETURN_CAR = "UPDATE CAR SET CUSTOMER_ID = NULL WHERE CUSTOMER_ID = ?;" +
            "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?;";


    private Database (String dbName) {
        DB_URL = DB_URL + dbName;
        connection = getConnection();
        dropCARTable();
        dropCOMPANYTable();
        dropCUSTOMERTable();
        addCOMPANYTable();
        addCARTable();
        addCUSTOMERTable();
        addForeignKeyCarTable();
    }

    public static Database createDB(String dbName) {
        if (db == null) {
            db = new Database(dbName);
        }
        return db;
    }

    public void closeDB() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void dropCOMPANYTable() {
        try (Statement statement = connection.createStatement()) {
            String ifExistSqlQueue = "DROP TABLE IF EXISTS COMPANY;";
            statement.executeUpdate(ifExistSqlQueue);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public void dropCARTable() {
        try (Statement statement = connection.createStatement()) {
            String ifExistSqlQueue = "DROP TABLE IF EXISTS CAR;";
            statement.executeUpdate(ifExistSqlQueue);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public void dropCUSTOMERTable() {
        try (Statement statement = connection.createStatement()) {
            String ifExistSqlQueue = "DROP TABLE IF EXISTS CUSTOMER;";
            statement.executeUpdate(ifExistSqlQueue);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public Map<Integer, Company> getAllCompanies() {
        Map<Integer, Company> resultMap = new HashMap<>();
        try (ResultSet resultSet = connection.prepareStatement(SQL_GET_ALL_COMPANIES).executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                resultMap.put(id, new Company(name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public List<Car> getAllAvailableCompanyCars(int companyId) {
        final String SQL_GET_ALL_COMPANY_CARS = "SELECT * FROM CAR WHERE CUSTOMER_ID IS NULL AND COMPANY_ID = " + companyId + " ORDER BY ID;";
        List<Car> carsList = new ArrayList<>();
        try (ResultSet resultSet = connection.prepareStatement(SQL_GET_ALL_COMPANY_CARS).executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                carsList.add(new Car(id, name));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return carsList;
    }

    public List<Customer> getAllCustomers() {
        final String SQL_GET_ALL_CUSTOMERS = "SELECT * FROM CUSTOMER ORDER BY ID;";
        List<Customer> customersList = new ArrayList<>();
        try (ResultSet resultSet = connection.prepareStatement(SQL_GET_ALL_CUSTOMERS).executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                customersList.add(new Customer(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersList;
    }

    public boolean rentCar(int customerId, int carId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_RENT_CAR);
             PreparedStatement statement2 = connection.prepareStatement(SQL_RENT_CAR2)) {
            statement.setInt(1, carId);
            statement.setInt(2, customerId);
            statement2.setInt(1, customerId);
            statement2.setInt(2, carId);
            int result = statement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean returnRentedCar(int customerId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_RETURN_CAR)) {
            statement.setInt(1, customerId);
            statement.setInt(2, customerId);
            int result = statement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Map<String, String> getCustomerCarInfo(int customerId) {
        final String SQL_GET_ALL_COMPANY_CARS = "SELECT CAR.NAME, COMPANY.NAME \n" +
                "FROM CAR \n" +
                "INNER JOIN COMPANY\n" +
                "ON CAR.COMPANY_ID = COMPANY.ID\n" +
                "WHERE CAR.CUSTOMER_ID = " + customerId + ";";

        try (ResultSet resultSet = connection.prepareStatement(SQL_GET_ALL_COMPANY_CARS).executeQuery()) {
            if (resultSet.next()) {
                String carName = resultSet.getString(1);
                String companyName = resultSet.getString(2);
                return Map.of("carName", carName, "companyName", companyName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Map.of();
    }

    public void createCompany(String companyName) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_ADD_NEW_COMPANY)) {
            statement.setString(1, companyName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public void createCar(String carName, int companyId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_ADD_NEW_CAR)) {
            statement.setString(1, carName);
            statement.setInt(2, companyId);
            int result = statement.executeUpdate();
            if (result > 0) {
                System.out.println("The car was added");
            } else {
                System.out.println("The car list is empty!");
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public boolean createCustomer(String name) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_ADD_NEW_CUSTOMER)) {
            statement.setString(1, name);
            int result = statement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return false;
    }

    private Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL);
            con.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Failed to get connection with db, check the url" + e.getMessage());
        }
        return con;
    }

    private void addCOMPANYTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY (\n"
                    + "     ID INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,\n"
                    + "     NAME VARCHAR(24) UNIQUE NOT NULL"
                    + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void addCARTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS CAR (\n"
                    + "     ID INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,\n"
                    + "     NAME VARCHAR(24) UNIQUE NOT NULL,"
                    + "     COMPANY_ID INT NOT NULL,"
                    + "     CUSTOMER_ID INT,"
                    + "     CONSTRAINT fk_company_id FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)"
                    + "     ON DELETE CASCADE "
                    + "     ON UPDATE CASCADE"
                    + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private void addCUSTOMERTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER (\n"
                    + "     ID INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,\n"
                    + "     NAME VARCHAR(24) UNIQUE NOT NULL,"
                    + "     RENTED_CAR_ID INT,"
                    + "     CONSTRAINT fk_rented_car_id FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)"
                    + "     ON DELETE CASCADE "
                    + "     ON UPDATE CASCADE"
                    + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addForeignKeyCarTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "ALTER TABLE CAR\n" +
                    "  ADD FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(ID)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
