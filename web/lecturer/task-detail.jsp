<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    <!-- CSS Styles -->

    .status {
        font-weight: bold;
    }
    .status.in-progress {
        color: orange;
    }
    .status.done {
        color: green;
    }
</style>

<!-- Include header -->
<jsp:include page="header.jsp"></jsp:include>

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <!-- Include header content -->
        <jsp:include page="header-content.jsp"></jsp:include>

            <!-- Task Detail -->
            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Task Detail</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">

                        <!-- Success Notification -->
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

                    <!-- Error Notification -->
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

                <!-- Task Details -->
                <div class="row">
                    <div class="col-12">
                        <div class="card-body">
                            <!-- Task Details -->
                            <div class="row mb-3">
                                <div class="col-2"><strong>Title:</strong></div>
                                <div class="col-8"><span>${task.title}</span></div>
                                        <c:if test="${task.status == 'done'}">
                                    <div class="col-2"><button type="button" class="btn btn-sm btn-info" data-toggle="modal" data-target="#grade">Grade</button></div>
                                </c:if>
                            </div>
                            <div class="row mb-3">
                                <div class="col-2"><strong>Description:</strong></div>
                                <div class="col-10"><span>${task.description}</span></div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-2"><strong>Status:</strong></div>
                                <div class="col-10"><span class="status">${task.status}</span></div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-2"><strong>Grade:</strong></div>
                                <div class="col-10"><span>${task.grade}</span></div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-2"><strong>Drive link:</strong></div>
                                <div class="col-10">
                                    <c:if test="${task.link_code == null || task eq ''}">
                                        <span style="color: red">
                                            not uploaded 
                                        </span>
                                    </c:if>
                                    <c:if test="${task.link_code != null && task ne ''}">
                                        <span>
                                            <a href="${task.link_code}"target="_blank">Drive link</a>
                                        </span>
                                    </c:if>



                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-2"><strong>Comment:</strong></div>
                                <div class="col-12">

                                    <!-- Add Comment Form -->
                                    <form action="task-detail" method="POST">
                                        <textarea name="comment" class="form-control"></textarea>
                                        <input type="hidden" name="action" value="add-comment">
                                        <input type="hidden" name="tid" value="${param.id}">
                                        <hr>
                                        <button class="btn btn-success btn-sm"> Add comment</button>
                                    </form>
                                    <hr>
                                    <br>

                                    <!-- Comments -->
                                    <c:forEach var="c" items="${comments}">
                                        <c:choose>
                                            <c:when test="${c.lecturer != null}">
                                                <span style="color: orange; font-weight: bold">
                                                    ${c.lecturer.fullName} (Lecturer)
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="font-weight: bold">
                                                    ${c.student.fullName} (Student)
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                        </span>
                                        : ${c.comment}  

                                        <!-- Edit Comment Modal -->
                                        <c:if test="${account.getId() == c.lecturer.accountLecturer.id}">
                                            <i class="fas fa-pen" style="margin-left: 10px; cursor: pointer;" data-toggle="modal" data-target="#editCommentModal${c.comment_id}"></i>

                                            <!-- Modal -->
                                            <div class="modal fade" id="editCommentModal${c.comment_id}" tabindex="-1" role="dialog" aria-labelledby="editCommentModalLabel${c.comment_id}" aria-hidden="true">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title" id="editCommentModalLabel${c.comment_id}">Edit Comment</h5>
                                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                <span aria-hidden="true">&times;</span>
                                                            </button>
                                                        </div>

                                                        <!-- Edit Comment Form -->
                                                        <form action="task-detail" method="POSt">
                                                            <div class="modal-body">
                                                                <!-- Text area for editing comment -->
                                                                <input type="hidden" name="action" value="edit-comment">
                                                                <input type="hidden" name="cid" value="${c.comment_id}">
                                                                <input type="hidden" name="tid" value="${param.id}">
                                                                <textarea name="comment" class="form-control">${c.comment}</textarea>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                <button type="submit" class="btn btn-success">Save changes</button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                        <span style="float: right">${c.time}</span>
                                        <hr>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="modal fade" id="grade" tabindex="-1" role="dialog" aria-labelledby="gradeModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="gradeModalLabel">Grade Task</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="task-detail" method="POSt">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="grade-task">
                            <input type="hidden" name="tid" value="${param.id}">
                            <input name="grade" id="gradeInput" type="number" step="0.1" class="form-control" value="${task.grade}" required>
                    <span id="gradeError" style="color: red;"></span>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-success">Save changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Include Footer -->
        <jsp:include page="footer.jsp"></jsp:include>

<script>
function validateGradeForm() {
    var grade = document.getElementById('gradeInput').value.trim();
    
    // Validate if grade is not empty
    if (grade === "") {
        document.getElementById('gradeError').innerText = "Grade is required.";
        return false; // Prevent form submission
    }
    
    // Validate if grade is a valid number between 0 and 10
    if (isNaN(grade) || parseFloat(grade) < 0 || parseFloat(grade) > 10) {
        document.getElementById('gradeError').innerText = "Grade must be a number between 0 and 10.";
        return false; // Prevent form submission
    }
    
    // Clear any previous error messages
    document.getElementById('gradeError').innerText = "";
    
    return true; // Allow form submission
}
</script>

