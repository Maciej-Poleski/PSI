<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <style type="text/css">
        .c1 {
            border: 1px solid black;
            border-collapse: collapse;
        }

        .c2 {
            border: 1px solid black;
        }

        .c3 {
            background-color: gainsboro;
        }
    </style>
</head>
<body>
<jsp:include page="menu.jsp"/>
<hr/>
<c:choose>
    <c:when test="${result}">
        Zakupy zakończone pomyślnie
    </c:when>
    <c:otherwise>
        Zakupy zakończone niepowodzeniem
    </c:otherwise>
</c:choose>
</body>
</html>