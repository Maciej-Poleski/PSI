import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * User: Maciej Poleski
 * Date: 05.04.13
 * Time: 22:29
 */
public class TransactionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(prepareResponse(request, response));
    }

    private String prepareResponse(HttpServletRequest request, HttpServletResponse response) {
        if (!sessionIsOk(request, response)) {
            return "Błąd: musisz być zalogowanym, żeby przejść do kasy";
        }
        if (weAreCommitting(request, response)) {
            return commit(request, response);
        }
        Card card = (Card) request.getSession().getAttribute("card");
        if (card == null) {
            card = new Card();
            request.getSession().setAttribute("card", card);
        }
        String result = "Witaj " + request.getSession().getAttribute("login") + "!<br/>";
        result += "Oto twoja lista zakupów:<br/><br/>";
        if (card.getProducts().size() > 0) {
            for (Card.ProductReference ref : card.getProducts()) {
                result += "<b>" + ref.product.getName() + "</b>, Ilość: " + ref.count + "<br/>";
            }
            result += "<form action=/transaction method=post><input type=hidden name=commit value=yes /><input type=submit value=\"Zapłać\" /></form>";
        } else {
            result += "Koszyk jest pusty!";
        }
        return result;
    }

    private String commit(HttpServletRequest request, HttpServletResponse response) {
        List<ProductInfo> products = (List<ProductInfo>) getServletContext().getAttribute("products");
        Card card = (Card) request.getSession().getAttribute("card");
        if (card == null) {
            card = new Card();
            request.getSession().setAttribute("card", card);
        }
        synchronized (products) {
            for (Card.ProductReference ref : card.getProducts()) {
                if (ref.count > ref.product.getAmount())
                    return errorNotEnoughProductsInStore(request, response, ref);
            }
            int price = 0;
            for (Card.ProductReference ref : card.getProducts()) {
                ref.product.setAmount(ref.product.getAmount() - ref.count);
                price += ref.product.getCost() * ref.count;
            }
            request.getSession().removeAttribute("card");
            return "Kupiłeś produkty za " + price;
        }
    }

    private String errorNotEnoughProductsInStore(HttpServletRequest request, HttpServletResponse response, Card.ProductReference ref) {
        return "W sklepie nie ma wystarczającej ilości egzemplarzy produktu <b>" + ref.product.getName() + "</b><br/>" +
                "Jest " + ref.product.getAmount() + ", a potrzebujesz " + ref.count + "<br/>";
    }

    private boolean weAreCommitting(HttpServletRequest request, HttpServletResponse response) {
        Object commit = request.getParameter("commit");
        return commit != null && commit.equals("yes");
    }

    private boolean sessionIsOk(HttpServletRequest request, HttpServletResponse response) {
        Object authenticated = request.getSession().getAttribute("authenticated");
        return authenticated != null && authenticated.equals(true);
    }
}
