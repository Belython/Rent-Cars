<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <%@ include file="/WEB-INF/pages/modules/menu/admin_menu.jsp" %>
</nav>
<section id="bg" class="overlay">
    <div>
        <%@ include file="/WEB-INF/pages/modules/menu/profile_menu.jsp" %>
    </div>
</section>
<inbody>
    <h2><spring:message code="all_users"/></h2>
    <h2>${message_error_get_users}</h2>
    <div>
        <form  method="POST" action="get_all_users" >
            <table>
                <tr>
                    <td width="5%"><spring:message code="client_id"/></td>
                    <td width="15%"><spring:message code="client_name"/></td>
                    <td width="10%"><spring:message code="client_sur_name"/></td>
                    <td width="20%"><spring:message code="client_email"/></td>
                    <td width="15%"><spring:message code="client_phone"/></td>
                    <td width="15%"><spring:message code="client_passport"/></td>
                    <td width="10%"><spring:message code="client_passport_start"/></td>
                    <td width="10%"><spring:message code="client_passport_end"/></td>
                </tr>
            </table>
            <c:forEach var="client" items="${users}" >
                <table>


                    <tr><td colspan="7"> </td></tr>
                    <tr>
                        <td width="5%"><c:out value="${client.id}"/></td>
                        <td width="15%"><c:out value="${client.name}"/></td>
                        <td width="10%"><c:out value="${client.surname}"/></td>
                        <td width="20%"><c:out value="${client.email}" /></td>
                        <td width="15%"><c:out value="${client.phone}"/></td>
                        <td width="15%"><c:out value="${client.passports.passport}"/></td>
                        <td width="10%"><fmt:formatDate value="${client.passports.passportIssueDate}"/></td>
                        <td width="10%"><fmt:formatDate value="${client.passports.passportEndDate}"/></td>
                    </tr>
                </table>
            </c:forEach>
            <input type="submit" value="<spring:message code="button_show"/>" />
            <input name="pages" value="0"/>
            <select name="perPages">
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="50">50</option>
                <option value="100">100</option></select>
        </form>
    </div>
</inbody>
</body>
</html>