package carsharing;

public class Main {


    public static void main(String[] args) {
        String dbName = "test";

        for (int i = 0; i < args.length; i++) {
             if ("-databaseFileName".equals(args[i])) {
                 dbName = args[i + 1];
                 break;
             }
        }

        Carsharing carsharing = new Carsharing(dbName);
        carsharing.run();

    }
}