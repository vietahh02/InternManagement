<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
        <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Event Registrations for ${event.title}</h1>
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Registrations</h6>
                </div>
                <div class="card-body">
                    <c:if test="${not empty sessionScope.notification}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            ${sessionScope.notification}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <%
                            session.removeAttribute("notification");
                        %>
                    </c:if>

                    <c:if test="${not empty sessionScope.notificationErr}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${sessionScope.notificationErr}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <%
                            session.removeAttribute("notificationErr");
                        %>
                    </c:if>

                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>User ID</th>
                                    
                                    <th>Status</th>
                                    <th>Registered At</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="registration" items="${registrations}">
                                    <tr>
                                        <td>${registration.registrationId}</td>
                                        <td>${registration.userId}</td>
                                       
                                        <c:if test="${registration.status eq 'Pending'}"> 
                                            <td>
                                                <span style="color: orange">${registration.status}</span>
                                            </td>
                                        </c:if>
                                        <c:if test="${registration.status eq 'Rejected'}"> 
                                            <td>
                                                <span style="color: red">${registration.status}</span>
                                            </td>
                                        </c:if>
                                        <c:if test="${registration.status eq 'Accepted'}"> 
                                            <td>
                                                <span style="color: green">${registration.status}</span>
                                            </td>
                                        </c:if>
                                        <td>${registration.registeredAt}</td>
                                        <td>
                                            <form action="event-manager" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="updateStatus"/>
                                                <input type="hidden" name="registration_id" value="${registration.registrationId}"/>
                                                <input type="hidden" name="event_id" value="${registration.eventId}"/>
                                                <select name="status" onchange="this.form.submit()">
                                                    <option value="Pending" ${registration.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                                    <option value="Accepted" ${registration.status == 'Accepted' ? 'selected' : ''}>Accepted</option>
                                                    <option value="Rejected" ${registration.status == 'Rejected' ? 'selected' : ''}>Rejected</option>
                                                </select>
                                            </form>
                                        </td>
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
