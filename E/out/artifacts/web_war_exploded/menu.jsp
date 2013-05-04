<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table width="100%" border="0">
    <tr>
        <td align="center" width="20%"><a href="register.jsp">Rejestracja</a></td>
        <td align="center" width="20%"><c:choose><c:when test="${authenticated}">
            <a href="logout.jsp">Wyloguj</a></c:when><c:otherwise>
            <a href="login.jsp">Logowanie</a></c:otherwise></c:choose></td>
        <td align="center" width="20%"><a href="/cardImpl">Koszyk</a></td>
        <td align="center" width="20%"><a href="/buyImpl">Transakcja</a></td>
        <td align="center" width="20%"><a href="/offerImpl">Oferta</a></td>
    </tr>
</table>