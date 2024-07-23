<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<c:if test="${account.roleAccount.role_id == 3}">
    <jsp:include page="lecturer_header.jsp"></jsp:include>
</c:if>
<c:if test="${account.roleAccount.role_id == 2}">
    <jsp:include page="student_header.jsp"></jsp:include>
</c:if>
 <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">

        <jsp:include page="header-content.jsp"></jsp:include>
             <div class="container-fluid">
                <div class="main-body">
                       <h1 class="h3 mb-2 text-gray-800">New list</h1>
                    <div class="row gutters-sm">
                        <div class="col-md-12">
                            <div class="card mb-3">
                                <div class="table-responsive">
                                    <table id="dataTable" class="table table-bordered">
                                        <tbody>
                                        <c:forEach var="n" items="${news}" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>${n.created_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))} :<a href="${pageContext.request.contextPath}/common/new-detail?id=${n.id}"">${n.title}</a> </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>

