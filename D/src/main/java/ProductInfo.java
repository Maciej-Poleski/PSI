import java.io.Serializable;

/**
 * User: Maciej Poleski
 * Date: 05.04.13
 * Time: 21:28
 */
class ProductInfo implements Serializable {
    private String name;
    private String description;
    private int amount;
    private int cost;

    public ProductInfo(String name, String description, int amount, int cost) {

        this.name = name;
        this.description = description;
        this.amount = amount;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductInfo that = (ProductInfo) o;

        if (amount != that.amount) return false;
        if (cost != that.cost) return false;
        if (!description.equals(that.description)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + amount;
        result = 31 * result + cost;
        return result;
    }

    int getCost() {
        return cost;
    }

    void setCost(int cost) {
        this.cost = cost;
    }
}
