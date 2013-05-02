<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rejestracja</title>
</head>
<body>
<jsp:include page="menu.jsp"/>
<hr/>
<form action="/registerImpl" method="post">
    <table border="0">
        <tr>
            <td>Login:</td>
            <td>
                <input type="text" name="login"/>
            </td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password" /></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" /></td>
        </tr>
    </table>
</form>
</body>
</html>