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
<h1>Oferta:</h1>


<table width="100%" class="c1">
    <tr>
        <th class="c2">Nazwa</th>
        <th class="c2">Cena</th>
        <th class="c2">Dostępność</th>
        <th class="c2">Akcja</th>
    </tr>
    <c:forEach var="item" items="${items}" varStatus="status">
        <c:choose>
            <c:when test="${status.index%2==1}">
                <tr class="c3">
            </c:when>
            <c:otherwise>
                <tr>
            </c:otherwise>
        </c:choose>
        <td class="c2">${item.name}</td>
        <td class="c2">${item.price}</td>
        <td class="c2">${item.count}</td>
        <td class="c2"><div style="text-align: center;">
            <form action="/offerImpl" method="post">
                <input type="hidden" name="item_id" value="${item.id}"/>
                <input type="submit" value="Dodaj do koszyka"/>
            </form></div></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>