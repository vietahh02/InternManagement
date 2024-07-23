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
<c:if test="${account.roleAccount.role_id == 1}">
    <jsp:include page="student_header.jsp"></jsp:include>
</c:if>
<div id="content-wrapper" class="d-flex flex-column">
    <div id="content">


        <jsp:include page="header-content.jsp"></jsp:include>
            <div class="container">
                <div class="main-body">
                    
                    <div class="row gutters-sm">
                        <div class="col-md-12">

                            <h3>${news.title} </h3> 
                        <div class="col-sm-12">
                            <span class="col-md-10">${news.created_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</span>

                            <span style="float: right"> Post by: ${news.created_by.username}</span> <br><br>
                        </div>
                        <div class="col-sm-12">
                            <text>${news.content}</text>
                        </div>
                        <br>
                        <div class="col-sm-12">
                            <a type="button" href="javascript:history.back()" class="btn btn-info">Back</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</div>
<jsp:include page="footer.jsp"></jsp:include>

