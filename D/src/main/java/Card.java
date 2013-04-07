import java.util.ArrayList;
import java.util.List;

/**
 * User: Maciej Poleski
 * Date: 05.04.13
 * Time: 22:08
 */
public class Card {
    private List<ProductReference> products = new ArrayList<>();

    public List<ProductReference> getProducts() {
        return products;
    }

    public void addProduct(ProductInfo product, int count) {
        for (ProductReference ref : products) {
            if (ref.product.equals(product)) {
                ref.count += count;
                return;
            }
        }
        products.add(new ProductReference(product, count));
    }

    static class ProductReference {
        ProductInfo product;
        int count;

        ProductReference(ProductInfo product, int count) {
            this.product = product;
            this.count = count;
        }
    }

}
