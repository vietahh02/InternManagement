<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
        <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Events</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">All Events</h6>
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
                        <h3>Available Events</h3>
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Title</th>
                                    <th>Description</th>
                                    <th>Start Time</th>
                                    <th>End Time</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="event" items="${events}">
                                    <tr>
                                        <td>${event.eventId}</td>
                                        <td>${event.title}</td>
                                        <td>${event.description}</td>
                                        <td>${event.formattedStartTime}</td>
                                        <td>${event.formattedEndTime}</td>
                                        <td>
                                            <form action="events" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to register for this event?');">
                                                <input type="hidden" name="action" value="register"/>
                                                <input type="hidden" name="event_id" value="${event.eventId}"/>
                                                <button type="submit" class="btn btn-primary">Register</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="table-responsive">
                        <h3>My Registrations</h3>
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Event ID</th>
                                    <th>Title</th>
                                    <th>Status</th>
                                    <th>Registered At</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="registration" items="${registrations}">
                                    <tr>
                                        <td>${registration.eventId}</td>
                                        <td>${events.stream().filter(e -> e.eventId == registration.eventId).findFirst().get().title}</td>
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
                                        <td>${registration.formattedRegisteredAt}</td>
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
