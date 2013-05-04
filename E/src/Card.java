import java.util.ArrayList;
import java.util.List;

/**
 * User: Maciej Poleski
 * Date: 04.05.13
 * Time: 13:49
 */
public class Card {
    List<Item> items = new ArrayList<>();

    public Item getItem(int itemId) {
        for (Item item : items) {
            if (item.getId() == itemId)
                return item;
        }
        return null;
    }

    public boolean add(Item item) {
        return items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public static class Item {
        int id;
        int count;

        public Item(int id, int count) {

            this.id = id;
            this.count = count;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Item{");
            sb.append("id=").append(id);
            sb.append(", count=").append(count);
            sb.append('}');
            return sb.toString();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
