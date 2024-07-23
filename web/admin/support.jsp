<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">

        <div id="content">

        <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Support list</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Support</h6>
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
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${listSupport}" var="sup" varStatus="stat">
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
                                        <td>
                                            <c:if test="${sup.status == 'in progress'}">
                                                <button class="btn btn-warning btn-sm" data-toggle="modal" data-target="#edit${sup.suport_id}">
                                                    Answer 
                                                </button>
                                            </c:if>
                                        </td>
                                    </tr>


                                <div class="modal fade" id="edit${sup.suport_id}" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="addModalLabel">Answer Support</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form name="supportMana" id="taskForm" action="support" method="POST" onsubmit="return validateForm()">
                                                <div class="modal-body">

                                                    <input type='text' name='id' value='${sup.suport_id}' hidden>
                                                    <label class="form-text">Status</label>
                                                    <input type="radio" id="approve" name="status" value="1" checked>
                                                    <label for="approve" style="margin-right: 35px">Approve</label>
                                                    <input type="radio" id="reject" name="status" value="2"  >
                                                    <label for="reject">Reject</label>

                                                    <label class="form-text">Process Note</label>
                                                    <textarea class="form-control" name="note" placeholder="Enter task note">${sup.process_note}</textarea>
                                                    <span id="descriptionError" style="color: red; font-size: smaller"></span>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                                                    <button type="submit" class="btn btn-success btn-sm">Update</button>
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
</div>

<jsp:include page="footer.jsp"></jsp:include>

