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
    <c:when test="${result==1}">OK</c:when>
    <c:otherwise>Wystąpił jakiś błąd (kod ${result})</c:otherwise>
</c:choose>
</body>
</html>