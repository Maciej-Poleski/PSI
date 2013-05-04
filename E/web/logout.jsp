<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logowanie</title>
</head>
<body>
<jsp:include page="menu.jsp"/>
<hr/>
<c:choose>
    <c:when test="${authenticated}">
        <form action="/logoutImpl" method="post">
            Zalogowany jako: ${user}
            <input type="submit" value="Wyloguj"/>
        </form>
    </c:when>
    <c:otherwise>
        Nie jesteś zalogowany
    </c:otherwise>
</c:choose>

</body>
</html>