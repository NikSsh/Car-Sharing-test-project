package carsharing.db.model;

public class Car {
    private final String carName;
    private int id;

    public Car (int id, String carName) {
        this.id = id;
        this.carName = carName;
    }

    public String getCarName() {
        return carName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (id != car.id) return false;
        return carName.equals(carName);
    }

    @Override
    public int hashCode() {
        int result = carName.hashCode();
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carName='" + carName + '\'' +
                ", id=" + id +
                '}';
    }
}
