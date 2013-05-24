import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Maciej Poleski
 * Date: 03.04.13
 * Time: 22:03
 */
public class LoginServlet extends HttpServlet {

    Map<String, String> accounts = new HashMap<>();

    {
        accounts.put("login1", "password1");
        accounts.put("login2", "password2");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        chooseAction(request, response);
    }

    private void chooseAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (request.getParameter("logout") != null) {
            endOfSession(request, response);
        } else if (session.getAttribute("authenticated") == true) {
            offerLogout(request, response);
        } else {
            offerLogin(request, response);
        }
    }

    private void endOfSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("/");
    }

    private void offerLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("Zalogowany jako: <b>"+request.getSession().getAttribute("login")+"</b><br/>");
        writer.println("<form action=/loginImpl><input type=hidden name=logout value=2 /><input type=submit value=\"Wyloguj\" /></form>");
    }

    private void offerLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (triedAuthentication(request, response)) {
            if (!authenticate(request, response)) {
                reportAuthenticationError(request, response);
            } else {
                goAheadAuthenticated(request, response);
            }
        } else
            response.sendRedirect("login.html");
    }

    private boolean triedAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("login") != null;
    }

    private void goAheadAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter outputStream = response.getWriter();
        outputStream.println("Poprawnie uwierzytelniono: <b>" + request.getParameter("login") + "</b><br />");
        HttpSession session = request.getSession();
        session.setAttribute("authenticated", true);
        session.setAttribute("login",request.getParameter("login"));
        outputStream.println("<a href=\"/\">Strona główna</a>");
    }

    private void reportAuthenticationError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter outputStream = response.getWriter();
        outputStream.println("Błąd uwierzytelniania: zła nazwa użytkownika lub hasło");
    }

    private boolean authenticate(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        if (login == null)
            return false;
        String password = request.getParameter("password");
        if (password == null)
            return false;
        String correctPassword = accounts.get(login);
        return correctPassword != null && correctPassword.equals(password);
    }
}
