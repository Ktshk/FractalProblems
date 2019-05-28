<%--
  Created by IntelliJ IDEA.
  User: Katushka
  Date: 27.05.2019
  Time: 15:43
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<head>
    <link rel="icon" href="/images/logo.png">
    <title>Задачи от Фрактала - Рекорды</title>

</head>

<body style="margin:0px; background: #fbfbfb">
<div class="first-line">
    <div class="header">
        <img class="logo_img" src="/images/logo.png"/>
        <h1 class="header-text">Задачи от Фрактала</h1>
    </div>
</div>

<div class="table-title">
    <h1 class="table-title-text">Таблица рекордов</h1>
</div>

<div class="records-table">
    <table class="records-table-1" border=2 width="45%">
        <colgroup>
            <col span="1" style="width: 18%">
            <col span="1" style="width: 42%;">
            <col span="1" style="width: 20%;">
            <col span="1" style="width: 20%;">
        </colgroup>

        <tr class="table-header">
            <th class="table-cell">Место</th>
            <th class="table-cell">Имя</th>
            <th class="table-cell">Задачи</th>
            <th class="table-cell">Баллы</th>
        </tr>
        <c:forEach var="record" items="${requestScope.records}">
            <c:choose>
                <c:when test="${record.userId==requestScope.userId}">
                    <tr id="selected" class="selected-row">
                </c:when>
                <c:otherwise>
                    <tr>
                </c:otherwise>
            </c:choose>
            <td class="position table-cell"><c:out value="${record.position}"/></td>
            <td class="user-name-item table-cell"><c:out value="${record.userName}"/></td>
            <td class="problem-count-item table-cell"><c:out value="${record.totalProblemCount}"/></td>
            <td class="points-item table-cell"><c:out value="${record.totalPoints}"/></td>
            </tr>
        </c:forEach>
    </table>
</div>


<style>
    .first-line {
        background: #E2E2E2;
        width: 100%;
        box-shadow: 0 0 10px grey;
    }

    .header {
        display: flex;
        align-items: center;
        margin-left: 16px;
    }

    .header-text {
        font-family: BlinkMacSystemFont, Roboto, Open Sans, Helvetica Neue, sans-serif;
        font-size: 60px;
        font-weight: 600;
        margin-left: 24px;
    }

    .table-title-text {
        margin-top: 64px;
        text-align: center;
        font-family: BlinkMacSystemFont, Roboto, Open Sans, Helvetica Neue, sans-serif;
        font-size: 52px;
        font-weight: 500;
    }

    .logo_img {
        width: 120px;
        height: 120px;
    }

    .table-header {
        background: #eeeeee;
    }

    .records-table {
        font-family: BlinkMacSystemFont, Roboto, Open Sans, Helvetica Neue, sans-serif;
        font-size: 32px;
        max-width: 960px;
        margin: 0 auto;
    }

    .records-table-1 {
        width: 100%;
        border-spacing: 0;
        border: none;
    }

    .table-cell {
        border-bottom: 1px solid #dbdbdb;
        padding: 12px 8px;
        border-collapse: separate;
        border-left: none;
        border-right: none;
        border-top: none;
        font-size: 32px;
        font-weight: 500;
    }

    .points-item {
        text-align: center;
    }

    .problem-count-item {
        text-align: center;
    }

    .position {
        text-align: center;
    }

    .selected-row {
        background: #FFF5DD;
    }


</style>

</body>
</html>
