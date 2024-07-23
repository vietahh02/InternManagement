<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<jsp:include page="header.jsp"></jsp:include>
<div id="content-wrapper" class="d-flex flex-column">
    <div id="content">
        <jsp:include page="header-content.jsp"></jsp:include>

        <div class="container-fluid">
            <h1 class="h3 mb-2 text-gray-800">News list</h1>
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#add">Add New</button>
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
                        <div class="alert alert-danger alert-dismissible fade show" role="alert" style="text-align: center">
                            ${sessionScope.notificationErr}
                            <button type="button" class="btn-danger" data-dismiss="alert">x</button>
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
                                    <th>No</th>
                                    <th>Title</th>
                                    <th>Created At</th>
                                    <th>Updated At</th>
                                    <th>Created By</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="n" items="${news}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${n.title}</td>
                                        <td>${n.created_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                                        <td>${n.updated_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                                        <td>${n.created_by.username}</td>
                                        <td style="display: flex; gap: 5px;">
                                            <button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#edit${n.id}">Edit</button>
                                            <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete${n.id}">Delete</button>
                                        </td>
                                    </tr>
                                    <!-- Delete Modal -->
                                    <div class="modal fade" id="delete${n.id}" tabindex="-1" role="dialog" aria-labelledby="importModalLabel${n.id}" aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="importModalLabel${n.id}">Confirmation</h5>
                                                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                        <span aria-hidden="true">×</span>
                                                    </button>
                                                </div>
                                                <form action="new-list" method="post">
                                                    <div class="modal-body">
                                                        <span> Are you sure want to delete this new ?</span>
                                                        <span style="color: red">Remember that this action cannot be undone</span>
                                                        <input type="hidden" name="action" value="delete"/>
                                                        <input type="hidden" name="id" value="${n.id}"/>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">No</button>
                                                        <button class="btn btn-sm btn-info" type="submit" value="Add">Yes</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Edit Modal -->
                                    <div class="modal fade" id="edit${n.id}" tabindex="-1" role="dialog" aria-labelledby="edit${n.id}" aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="importModalLabel${n.id}">Update new </h5>
                                                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                        <span aria-hidden="true">×</span>
                                                    </button>
                                                </div>
                                                <form id="editForm" action="new-list" method="post" onsubmit="return validateFormEdit()">
                                                    <div class="modal-body">
                                                        <input type="hidden" name="id" value="${n.id}"/>
                                                        <input type="hidden" name="action" value="edit"/>
                                                        <label class="form-text">Title</label>
                                                        <input class="form-control" name="title" type="text" value="${n.title}" placeholder="Enter title">
                                                        <span id="editTitleError" style="color: red;"></span>
                                                        <hr>
                                                        <textarea class="form-control" name="content" rows="15" placeholder="Enter content">${n.content}</textarea>
                                                        <span id="editContentError" style="color: red;"></span>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                                                        <button class="btn btn-sm btn-info" type="submit" value="Save">Save</button>
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
    <div class="modal fade" id="add" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addModalLabel">Add news</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <form id="addForm" action="new-list" method="post" onsubmit="return validateForm()">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="add"/>
                        <label class="form-text">Title</label>
                        <input class="form-control" name="title" type="text" placeholder="Enter title">
                        <span id="titleError" style="color: red;"></span>
                        <hr>
                        <textarea class="form-control" name="content" placeholder="Enter content"></textarea>
                        <span id="contentError" style="color: red;"></span>
                        <hr>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                        <button class="btn btn-sm btn-info" type="submit" value="Upload">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    function validateForm() {
        var title = document.forms["addForm"]["title"].value.trim();
        var content = document.forms["addForm"]["content"].value.trim();
        document.getElementById('titleError').innerText = '';
        document.getElementById('contentError').innerText = '';

        if (title === "") {
            document.getElementById("titleError").innerText = "Title cannot be empty";
            return false;
        }

        if (content === "") {
            document.getElementById("contentError").innerText = "Content cannot be empty";
            return false;
        }

        return true;
    }

    function validateFormEdit() {
        var title = document.forms["editForm"]["title"].value.trim();
        var content = document.forms["editForm"]["content"].value.trim();
        document.getElementById('editTitleError').innerText = '';
        document.getElementById('editContentError').innerText = '';

        if (title === "") {
            document.getElementById("editTitleError").innerText = "Title cannot be empty";
            return false;
        }

        if (content === "") {
            document.getElementById("editContentError").innerText = "Content cannot be empty";
            return false;
        }

        return true;
    }
</script>
<jsp:include page="footer.jsp"></jsp:include>
