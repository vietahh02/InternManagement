<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>

<div id="content-wrapper" class="d-flex flex-column">
    <div id="content">
        <jsp:include page="header-content.jsp"></jsp:include>

        <div class="container-fluid">
            <h1 class="h3 mb-2 text-gray-800">Event Management</h1>
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">All Events</h6>
                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#createEventModal">
                        Create Event
                    </button>
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
                                        <td>${event.startTime}</td>
                                        <td>${event.endTime}</td>
                                        <td>
                                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#editEventModal${event.eventId}">
                                                Edit
                                            </button>
                                            <a href="event-manager?action=viewRegistrations&event_id=${event.eventId}" class="btn btn-info">View Registrations</a>
                                            <form action="event-manager" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="delete"/>
                                                <input type="hidden" name="event_id" value="${event.eventId}"/>
                                                <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this event?');">Delete</button>
                                            </form>
                                        </td>
                                    </tr>

                                    <!-- Edit Event Modal -->
                                    <div class="modal fade" id="editEventModal${event.eventId}" tabindex="-1" role="dialog" aria-labelledby="editEventModalLabel" aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="editEventModalLabel">Edit Event</h5>
                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                </div>
                                                <form action="event-manager" method="post">
                                                    <div class="modal-body">
                                                        <input type="hidden" name="action" value="update"/>
                                                        <input type="hidden" name="event_id" value="${event.eventId}"/>
                                                        <div class="form-group">
                                                            <label for="title">Title:</label>
                                                            <input type="text" class="form-control" id="title" name="title" value="${event.title}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="description">Description:</label>
                                                            <textarea class="form-control" id="description" name="description" required>${event.description}</textarea>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="start_time">Start Time:</label>
                                                            <input type="datetime-local" class="form-control" id="start_time" name="start_time" value="${event.startTime.toString().replace(' ', 'T')}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="end_time">End Time:</label>
                                                            <input type="datetime-local" class="form-control" id="end_time" name="end_time" value="${event.endTime.toString().replace(' ', 'T')}" required>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                                        <button type="submit" class="btn btn-primary">Update Event</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Create Event Modal -->
    <div class="modal fade" id="createEventModal" tabindex="-1" role="dialog" aria-labelledby="createEventModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createEventModalLabel">Create Event</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="event-manager" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="create"/>
                        <div class="form-group">
                            <label for="title">Title:</label>
                            <input type="text" class="form-control" id="title" name="title" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Description:</label>
                            <textarea class="form-control" id="description" name="description" required></textarea>
                        </div>
                        <div class="form-group">
                            <label for="start_time">Start Time:</label>
                            <input type="datetime-local" class="form-control" id="start_time" name="start_time" required>
                        </div>
                        <div class="form-group">
                            <label for="end_time">End Time:</label>
                            <input type="datetime-local" class="form-control" id="end_time" name="end_time" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Create Event</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>
