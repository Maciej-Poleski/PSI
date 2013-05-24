import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Maciej Poleski
 * Date: 03.05.13
 * Time: 21:13
 */
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (DatabaseUtils.tryLogin(request.getParameter("login"), request.getParameter("password"))) {
            request.getSession().setAttribute("authenticated", true);
            request.getSession().setAttribute("user", request.getParameter("login"));
        } else {
            request.setAttribute("authenticated", false);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("logged.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
