import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Maciej Poleski
 * Date: 05.04.13
 * Time: 21:25
 */
public class ShopServlet extends HttpServlet {

    List<ProductInfo> products;

    @Override
    public void init() throws ServletException {
        super.init();
        products = Collections.synchronizedList(new ArrayList<ProductInfo>());
        products.add(new ProductInfo("Uran U235", "Wzbogacony uran na wagę (cena za kilogram)", 1000, 5));
        products.add(new ProductInfo("AK-47", "Znany jako Kałasznikow (cena za sztukę)", 5, 1));
        getServletContext().setAttribute("products", products);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        if (parseCurrentRequest(request, response))
            displayOffer(request, response);
        else
            displayError(request, response);
    }

    private void displayError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("Błąd: aby dodawać do koszyka musisz być zalogowany");
    }

    private void displayOffer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("<h2>Produkty:</h2>");
        synchronized (products) {
            for (ProductInfo productInfo : products) {
                writer.println("<b>" + productInfo.getName() + "</b>: " + productInfo.getDescription() + "<br/>");
                writer.println("Dostępność: " + productInfo.getAmount() + ", cena: " + productInfo.getCost() + "<br/>");
                writer.println("<form action=\"/shop\" method=post><input type=hidden value=\"" + productInfo.getName() + "\" name=\"name\"/><input type=text value=1 name=count /><input type=submit value=\"Dodaj do koszyka\" /></form><br/>");
                writer.println("<br/>");
            }
        }
        writer.println("<a href=\"/\">Strona główna</a>");
    }

    private boolean parseCurrentRequest(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("name") == null)
            return true;
        if (!checkSession(request, response))
            return false;
        Card card = (Card) request.getSession().getAttribute("card");
        if (card == null) {
            card = new Card();
            request.getSession().setAttribute("card", card);
        }
        String productName = request.getParameter("name");
        int count = Integer.parseInt(request.getParameter("count"));
        ProductInfo selectedProduct = null;
        for (ProductInfo product : products) {
            if (product.getName().equals(productName)) {
                selectedProduct = product;
                break;
            }
        }
        if (selectedProduct == null)
            return false;
        card.addProduct(selectedProduct, count);
        return true;
    }

    private boolean checkSession(HttpServletRequest request, HttpServletResponse response) {
        Object authenticated = request.getSession().getAttribute("authenticated");
        return authenticated != null && authenticated.equals(true);
    }
}
