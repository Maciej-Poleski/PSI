import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * User: Maciej Poleski
 * Date: 03.05.13
 * Time: 22:09
 */
public class OfferServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Card card = (Card) request.getSession().getAttribute("card");
        if (card == null)
            card = new Card();
        int itemId = Integer.parseInt(request.getParameter("item_id"));
        Card.Item item = card.getItem(itemId);
        if (item == null) {
            card.add(new Card.Item(itemId, 1));
        } else {
            item.setCount(item.getCount() + 1);
        }
        request.getSession().setAttribute("card", card);
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<DatabaseUtils.Item> items = DatabaseUtils.getAllAvailableItems();
        request.setAttribute("items", items);
        RequestDispatcher dispatcher = request.getRequestDispatcher("offer.jsp");
        dispatcher.forward(request, response);
    }
}
