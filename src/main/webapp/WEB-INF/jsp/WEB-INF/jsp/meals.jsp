<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<fmt:setBundle basename="messages.app"/>

<html>
<jsp:include page="../../fragments/headTag.jsp"/>
<body>
<jsp:include page="../../fragments/bodyHeader.jsp"/>
<br>
<section>
    <h2><fmt:message key="meal.title"/></h2>
    <form method="get" action="meals/filter">
        <input type="hidden" name="action" value="filter">
        <dl>
            <dt><fmt:message key="meal.fromDate"/></dt>
            <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="meal.toDate"/></dt>
            <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="meal.fromTime"/></dt>
            <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="meal.toTime"/></dt>
            <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
        </dl>
        <button type="submit"><fmt:message key="meal.filter"/></button>
    </form>
    <hr/>
    <a href="meals/create"><fmt:message key="meal.addMeal"/></a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><fmt:message key="meal.date"/></th>
            <th><fmt:message key="meal.description"/></th>
            <th><fmt:message key="meal.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr data-meal-excess="${meal.excess}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals/update/${meal.id}"><fmt:message key="app.update"/></a></td>
                <td><a href="meals/delete/${meal.id}"><fmt:message key="app.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="../../fragments/footer.jsp"/>
</body>
</html>