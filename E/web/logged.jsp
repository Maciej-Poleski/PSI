<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<jsp:include page="menu.jsp"/>
<hr/>
Efekt: <c:choose>
    <c:when test="${authenticated}">Zalogowano ${user}</c:when>
    <c:otherwise>Logowanie nie powiodło się</c:otherwise>
</c:choose>
</body>
</html>