<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<jsp:include page="header.jsp"></jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
        <jsp:include page="header-content.jsp"></jsp:include>
            <div class="container-fluid">
                <div class="main-body">
                    <h1 class="h3 mb-2 text-gray-800">Question list</h1>
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
                        session.removeAttribute("notificationErr");
                    %>
                </c:if>

                <div class="row gutters-sm">
                    <div class="col-md-12">
                        <div class="card mb-3">
                            <div class="table-responsive">
                                <table id="dataTable" class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Title</th>
                                            <th>Content</th>
                                            <th>Send time</th>
                                            <th>Answer</th>
                                            <th>Status</th>
                                            <th>Reply time</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="q" items="${questions}" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>  ${q.title}    </td>
                                                <td><button class="btn btn-sm btn-success" data-toggle="modal" data-target="#question${q.id}">View Content</button></td>
                                                <td>${q.created_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                                                <td><button class="btn btn-sm btn-success"data-toggle="modal" data-target="#answer${q.id}">View Answer</button></td>
                                                <td style="color:
                                                    <c:choose>
                                                        <c:when test="${q.status eq 'Processing'}">
                                                            red
                                                        </c:when>

                                                        <c:when test="${q.status eq 'Answered'}">
                                                            green
                                                        </c:when>
                                                    </c:choose>
                                                    ">
                                                    ${q.status}
                                                </td>  
                                                <td>${q.updated_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                                                <td>
                                                    <i data-toggle="modal" data-target="#update${q.id}" class="fa fa-pen"></i>
                                                </td>
                                            </tr>

                                        <div class="modal fade" id="update${q.id}" tabindex="-1" role="dialog" aria-labelledby="gradeModalLabel" aria-hidden="true">
                                            <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="gradeModalLabel">Reply question</h5>
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true">&times;</span>
                                                        </button>
                                                    </div>
                                                    <form name="updateForm${q.id}" id="updateForm${q.id}" action="question" method="POST" onsubmit="return validateUpdateForm(${q.id})">
                                                        <div class="modal-body">
                                                            <input type="hidden" name="action" value="update">
                                                            <input type="hidden" name="id" value="${q.id}">
                                                            <label class="form-text">Title</label>
                                                            <input class="form-control" name="title" readonly="" value="${q.title}" type="text" placeholder="Enter question title">

                                                            <hr>

                                                            <label class="form-text">Question</label>
                                                            <textarea class="form-control" readonly="" name="content" type="text" placeholder="Enter question content" rows="6">${q.content}</textarea>
                                                            <span id="updateDescriptionError${q.id}" style="color: red; font-size: smaller"></span>
                                                            <label class="form-text">Answer</label>
                                                            <textarea class="form-control"   name="answer" type="text" placeholder="Enter answer" rows="6">${q.reply}</textarea>
                                                            <span id="updateAnswerError${q.id}" style="color: red; font-size: smaller"></span>

                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                            <button type="submit" class="btn btn-success">Save changes</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal fade" id="question${q.id}" tabindex="-1" role="dialog" aria-labelledby="gradeModalLabel" aria-hidden="true">
                                            <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="gradeModalLabel">Question detail</h5>
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true">&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <label class="form-text">Content</label>
                                                        <textarea readonly class="form-control" name="content" type="text" placeholder="Enter question content" rows="6">${q.content}</textarea>
                                                        <span id="updateDescriptionError${q.id}" style="color: red; font-size: smaller"></span>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal fade" id="answer${q.id}" tabindex="-1" role="dialog" aria-labelledby="gradeModalLabel" aria-hidden="true">
                                            <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="gradeModalLabel">Answer detail</h5>
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true">&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <label class="form-text">Answer</label>
                                                        <textarea readonly class="form-control" name="content" type="text" placeholder="This question has not been answered by the lecturer" rows="6">${q.reply}</textarea>
                                                        <span id="updateDescriptionError${q.id}" style="color: red; font-size: smaller"></span>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                    </div>
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
    </div>
</div>

<script>
    function validateUpdateForm(id) {
        var isValid = true;

        // Get form elements
        var title = document.forms["updateForm" + id]["title"].value;
        var content = document.forms["updateForm" + id]["content"].value;
        var content = document.forms["updateForm" + id]["answer"].value;

        // Clear previous error messages
        document.getElementById("updateTitleError" + id).innerText = "";
        document.getElementById("updateDescriptionError" + id).innerText = "";
        document.getElementById("updateAnswerError" + id).innerText = "";

        // Validate title
        if (title === "") {
            document.getElementById("updateTitleError" + id).innerText = "Title is required.";
            isValid = false;
        }

        // Validate content
        if (content === "") {
            document.getElementById("updateAnswerError" + id).innerText = "Answer is required.";
            isValid = false;
        }

        return isValid;
    }
</script>
<jsp:include page="footer.jsp"></jsp:include>
