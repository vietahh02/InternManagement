<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${currentAccount.roleAccount.role_id == 3}">
    <jsp:include page="lecturer_header.jsp"></jsp:include>
</c:if>
<c:if test="${currentAccount.roleAccount.role_id == 2}">
    <jsp:include page="student_header.jsp"></jsp:include>
</c:if>

<div id="content-wrapper" class="d-flex flex-column">

    <div id="content">

        <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Class list</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Send new</h6>
                    <c:if test="${not empty sessionScope.notification}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert" style="text-align: center">
                            ${sessionScope.notification}
                            <button type="button" class="btn-danger" data-dismiss="alert" aria-label="Close">x</button>
                        </div>
                        <%
                            // Clear the notification after displaying it
                            session.removeAttribute("notification");
                        %>
                    </c:if>
                    <c:if test="${not empty sessionScope.notificationErr}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert"  style="text-align: center">
                            ${sessionScope.notificationErr}
                            <button type="button" class="btn-danger" data-dismiss="alert" >x</button>
                        </div>
                        <%
                            // Clear the notification after displaying it
                            session.removeAttribute("notificationErr");
                        %>
                    </c:if>
                </div>

                <div class="card-body">
                    <div class="table-responsive">
                        <form action="support" method="POST">
                            <div class="modal-body">
                                <label class="form-text">Title</label>
                                <select id="tit" name="title" class="form-control" >
                                    <c:forEach items="${types}" var="ty">
                                        <option value="${ty.type_id}">${ty.title}</option>
                                    </c:forEach>
                                </select>
                                <hr>
                                <label class="form-text">Content</label>
                                <textarea class="form-control" name="content" type="text" placeholder="Enter question content" rows="6" required=""></textarea>
                                <button type="submit" class="btn btn-success" style="margin-top: 20px">Send</button>
                            </div>
                        </form>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Type</th>
                                    <th>Content</th>
                                    <th>Create Date</th>
                                    <th>Process Note</th>
                                    <th>Status</th>
                                    <th>End Date</th>
                                </tr>
                            </thead>
                            <tbody>

                                <c:forEach items="${list}" var="sup" varStatus="stat">
                                    <tr>
                                        <td>${stat.index + 1}</td>
                                        <td>${sup.ts.title}</td>
                                        <td>${sup.content}</td>
                                        <td>${sup.create_date}</td>
                                        <td>${sup.process_note}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${sup.status == 'in progress'}">
                                                    <button class="btn btn-info btn-sm">
                                                        <span class="text">${sup.status}</span>
                                                    </button>
                                                </c:when>
                                                <c:when test="${sup.status == 'approved'}">
                                                    <button class="btn btn-success btn-sm">
                                                        <span class="text">${sup.status}</span>
                                                    </button>
                                                </c:when>

                                                <c:otherwise>
                                                    <button class="btn btn-secondary btn-sm">
                                                        <span class="text">${sup.status}</span>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${sup.end_date}</td>
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

<jsp:include page="footer.jsp"></jsp:include>

