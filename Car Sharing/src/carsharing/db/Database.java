package carsharing.db;

import carsharing.db.model.Car;
import carsharing.db.model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements CompanyDAO{
    static final String JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:file:./db/";
    private final Connection connection;
    private static Database db = null;
    private static final String SQL_GET_ALL_COMPANIES = "SELECT * FROM COMPANY;";
    private static final String SQL_ADD_NEW_COMPANY = "INSERT INTO COMPANY (NAME) VALUES (?);";
    private static final String SQL_ADD_NEW_CAR = "INSERT INTO CAR (NAME,COMPANY_ID) VALUES (?,?)";

    private Database (String dbName) {
        DB_URL = DB_URL + dbName;
        connection = getConnection();
        dropCARTable();
        dropCOMPANYTable();
        addCOMPANYTable();
        addCARTable();
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

    @Override
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

    @Override
    public List<Car> getAllCompanyCars(int companyId) {
        final String SQL_GET_ALL_COMPANY_CARS = "SELECT * FROM CAR ORDER BY ID;";
        List<Car> carsList = new ArrayList<>();
        try (ResultSet resultSet = connection.prepareStatement(SQL_GET_ALL_COMPANY_CARS).executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                carsList.add(new Car(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carsList;
    }


    @Override
    public void createCompany(String companyName) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_ADD_NEW_COMPANY)) {
            statement.setString(1, companyName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    @Override
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

    private Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL);
            con.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Failed to get connection with db, check the url\n" + e.getMessage());
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
                    + "     CONSTRAINT fk_company_id FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)"
                    + "     ON DELETE CASCADE "
                    + "     ON UPDATE CASCADE"
                    + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
