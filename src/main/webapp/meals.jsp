<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<ul style="font-size: large">
    <li><a href="meals?action=addMeal">Add meal</a></li>
</ul>
<table border-collapse: collapse;
       border="1";>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="mealTo" items="${allMealTo}">
        <c:if test="${mealTo.isExcess() == true}">
            <tr style="color: red">
        </c:if>
        <c:if test="${mealTo.isExcess() != true}">
            <tr style="color: green">
        </c:if>
        <td> ${DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(mealTo.getDateTime())}</td>
        <td> ${mealTo.getDescription()}</td>
        <td> ${mealTo.getCalories()}</td>
        <td>
            <a href="meals?action=edit&mealToId=${mealTo.getId()}">Update</a>
        </td>
        <td>
            <a href="meals?action=delete&mealToId=${mealTo.getId()}">Delete</a>
        </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
