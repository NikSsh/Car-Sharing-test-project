package carsharing.menus;

public class MenuChooser {
    private Menu chosenMenu;

    public void setChosenMenu(Menu chosenMenu) {
        this.chosenMenu = chosenMenu;
    }

    public void executeMenu() {
        chosenMenu.execute();
    }
}
