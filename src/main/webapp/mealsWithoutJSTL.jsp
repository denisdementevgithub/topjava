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
    <th>Id</th>
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    <th></th>
    <th></th>
  </tr>
  <%
    List<MealTo> allMealTo = (List<MealTo>) request.getAttribute("allMealTo");
    for (int i = 0; i < allMealTo.size(); i++) {
      MealTo mT = allMealTo.get(i);
      if (mT.isExcess()) {
        out.println("<tr style=\"color: red;\">");
      } else {
        out.println("<tr style=\"color: green;\">");
      }
      out.println("<td>" + mT.getId() + "</td>");
      out.println("<td>" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(mT.getDateTime()) + "</td>");
      out.println("<td>" + mT.getDescription() + "</td>");
      out.println("<td>" + mT.getCalories() + "</td>");
      out.println("<td> " +
              "<a href=\"meals?action=edit&mealToId=" + i + "\">Update</a>" + "</td>");
      out.println("<td> " +
              "<a href=\"meals?action=delete&mealToId=" + i + "\">Delete</a>" + "</td>");

    }
  %>
</table>
</body>
</html>