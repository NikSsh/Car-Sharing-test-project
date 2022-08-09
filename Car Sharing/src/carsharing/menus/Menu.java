package carsharing.menus;

import carsharing.db.Database;

import java.util.Scanner;

abstract class Menu {
    protected  final static Scanner scanner = new Scanner(System.in);
    protected static final MenuChooser menuChooser = new MenuChooser();
    protected final Database database;

    public Menu (Database database) {
        this.database = database;
    }
    abstract void execute();
}
