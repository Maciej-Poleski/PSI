import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Maciej Poleski
 * Date: 03.05.13
 * Time: 22:09
 */
public class CardServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Card card = (Card) request.getSession().getAttribute("card");
        if (card == null)
            card = new Card();
        int itemId = Integer.parseInt(request.getParameter("item_id") != null ? request.getParameter("item_id") : "-1");
        Card.Item item = card.getItem(itemId);
        if (item != null) {
            item.setCount(item.getCount() - 1);
            if (item.getCount() == 0)
                card.removeItem(item);
        }
        request.getSession().setAttribute("card", card);
        List<DatabaseUtils.Item> items = DatabaseUtils.getAllAvailableItems();
        List<DatabaseUtils.Item> resultItems = new ArrayList<>();
        for (Card.Item cardItem : card.getItems()) {
            DatabaseUtils.Item databaseItem = null;
            for (DatabaseUtils.Item dbItem : items) {
                if (dbItem.id == cardItem.getId()) {
                    databaseItem = dbItem;
                    break;
                }
            }
            if (databaseItem != null) {
                resultItems.add(new DatabaseUtils.Item(cardItem.getId(), databaseItem.getName(), databaseItem.getPrice(), cardItem.getCount()));
            }
        }
        request.setAttribute("items", resultItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("card.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
