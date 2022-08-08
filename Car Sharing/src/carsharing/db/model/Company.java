package carsharing.db.model;

public class Company {
    private final String companyName;

    public Company(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        return companyName.equals(company.companyName);
    }

    @Override
    public int hashCode() {
        return companyName.hashCode();
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyName='" + companyName + '\'' +
                '}';
    }
}
