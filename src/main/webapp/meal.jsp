<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru" >
<head>
    <title>Meal</title>
    <script>
        function cancelAction() {
            document.getElementById("form").reset();
            window.location.href="meals";
        }
    </script>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meal</h2>
<div>
    <form id="form" method="post">
        <input type="hidden" name="id"
               value="${mealTo.getId()}">
        <br>
        <label> Date:
            <input type="datetime-local" name="dateTime"
                   value="${mealTo.getDateTime()}">
            <br>
        </label>
        <label> Description:
            <input type="text" name="description" value="${mealTo.getDescription()}">
            <br>
        </label>
        <label> Calories:
            <input type="text" name="calories" value="${mealTo.getCalories()}">
            <br>
        </label>
        <input type="submit" value="Save"/>
        <input type="button" onclick="cancelAction()" value="Cancel"/>


    </form>

</div>
</body>
</html>