<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <h2>Strona główna</h2>
           <ul>
               <li><a href="/loginImpl">Logowanie</a></li>
               <li><a href="/shop">Oferta</a></li>
               <li><a href="/transaction">Tranzakcja</a></li>
           </ul>
  <c:choose>
      <c:when test="0">True</c:when>
      <c:otherwise>False</c:otherwise>
  </c:choose>
  </body>
</html>