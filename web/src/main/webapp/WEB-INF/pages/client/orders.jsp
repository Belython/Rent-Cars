<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         errorPage="/WEB-INF/pages/error.jsp" %>
<html>
<head>
    <%@ include file="/WEB-INF/pages/modules/work.jsp" %>
    <title><spring:message code="title_main"/></title>
</head>
<body>
<loc id="bg" class="login">
    <jsp:include page="/WEB-INF/pages/modules/menu/language_menu.jsp"/>
</loc>
<hgroup>
    <%@ include file="/WEB-INF/pages/modules/slogan.jsp" %>
</hgroup>
<nav>
    <%@ include file="/WEB-INF/pages/modules/menu/client_menu.jsp" %>
</nav>
<section id="bg" class="overlay">
    <div>
        <%@ include file="/WEB-INF/pages/modules/menu/profile_menu.jsp" %>
    </div>
</section>
<inbody>
    <h2><spring:message code="all_orders"/></h2>
    <div>
<h2>${message_error_delete}</h2>
        <form method="POST" action="client_orders">
            <table>
                <tr>
                    <td colspan="2" width="10%">Сортировать по:
                    </td>
                    <td width="15%">
                        <select name="sort_name">
                            <option value="id">номеру заказа</option>
                            <option value="startDate">дата начала</option>
                            <option value="endDate">дата конца</option>
                            <option value="amount">стоимости</option>
                            <option value="orderStatus">статусу</option>
                        </select></td>
                    <td width="10%">Возрастанию</td>
                    <td width="15%"><input type="radio" name="sort_type" value="true" checked></td>
                    <td width="10%">Убыванию</td>
                    <td width="15%"><input type="radio" name="sort_type" value="false"></td>
                    <td width="15"><select name="perPages" id="perPages">
                        <option value="5">5</option>
                        <option value="10">10</option>
                        <option value="20">20</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select></td>
                    <td colspan="2" width="10%"></td>
                </tr>
                <tr>
                    <td colspan="10"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td width="5%"><spring:message code="number_of_order"/></td>
                    <td width="10%"><spring:message code="auto_brand"/></td>
                    <td width="10%"><spring:message code="auto_model"/></td>
                    <td width="15%"><spring:message code="order_start_date"/></td>
                    <td width="15%"><spring:message code="order_end_date"/></td>
                    <td width="15%"><spring:message code="other_info"/></td>
                    <td width="10%"><spring:message code="order_status"/></td>
                    <td width="10%"><spring:message code="order_price"/></td>
                    <td colspan="2" width="10%"></td>
                </tr>
            </table>
            <c:forEach var="order" items="${orders}">
                <table>
                    <tr>
                        <td colspan="10"></td>
                    </tr>
                    <tr>
                        <td width="5%"><c:out value="${order.id}"/></td>
                        <td width="10%"><c:out value="${order.car.brand.brandName}"/></td>
                        <td width="10%"><c:out value="${order.car.model}"/></td>
                        <td width="15%"><fmt:formatDate value="${order.startDate}"/></td>
                        <td width="15%"><fmt:formatDate value="${order.endDate}"/></td>
                        <td width="15%"><c:out value="${order.message}"/></td>
                        <td width="10%"><c:out value="${order.orderStatus.status}"/></td>
                        <td width="10%"><c:out value="${order.amount}"/></td>
                        <c:choose>
                            <c:when test="${order.orderStatus.id == 1}">
                                <td width="5%"><a
                                        href="${pageContext.servletContext.contextPath}/edit_order?orderId=${order.id}"><img
                                        src="${pageContext.servletContext.contextPath}/resources/images/edit.png"></a>
                                </td>
                                <td width="5%"><a
                                        href="${pageContext.servletContext.contextPath}/delete_order?orderId=${order.id}"><img
                                        src="${pageContext.servletContext.contextPath}/resources/images/delete.png"></a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td width="5%"></td>
                                <td width="5%"></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </table>
            </c:forEach>
            <input type="submit" value="<spring:message code="button_show"/>"/><br/>
            <jsp:include page="/WEB-INF/pages/modules/pagination/pagination_orders.jsp"/>
        </form>
    </div>
</inbody>
</body>
</html>