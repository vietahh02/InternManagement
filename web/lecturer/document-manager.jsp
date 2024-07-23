<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Including header -->
<jsp:include page="header.jsp"></jsp:include>

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">

            <!-- Including header content -->
        <jsp:include page="header-content.jsp"></jsp:include>

            <!-- Docu management -->
            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Document management</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">

                        <!-- Add new button -->
                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#add">Add document</button>

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
                <div class="card-body">
                    <div class="table-responsive">

                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">

                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Title</th>
                                    <th>Uploaded By</th>
                                    <th>Upload Date</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="document" items="${listDocument}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${document.title}</td>
                                        <td>${document.uploadedBy.lecturer.fullName}</td>
                                        <td>${document.uploadDate}</td>
                                        <td>
                                            <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#edit${document.documentId}">Update</button>
                                            <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete${document.documentId}">
                                                Delete
                                            </button>
                                            <a href="${pageContext.request.contextPath}/uploads/${fn:substring(document.filePath, 8, fn:length(document.filePath))}" class="btn btn-info btn-sm" download>
                                                Download
                                            </a>
                                        </td>
                                    </tr>

                                    <!-- Update Modal -->
                                <div class="modal fade" id="edit${document.documentId}" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="addModalLabel">Update Document</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form action="document-manager" method="post" enctype="multipart/form-data">
                                                <div class="modal-body">
                                                    <input type="hidden" name="action" value="update"/>
                                                    <input type="hidden" name="document_id" value="${document.documentId}"/>
                                                    <input type="hidden" name="current_file" value="${document.filePath}"/>
                                                    <div class="form-group">
                                                        <label for="title">Title:</label>
                                                        <input type="text" class="form-control" id="title" name="title" value="${document.title}" required>
                                                        <div class="form-group">
                                                            <label for="file">File (Current: 
                                                                <c:set var="filePath" value="${document.filePath}"/>
                                                                <c:set var="fileName" value="${fn:substring(filePath, 8, fn:length(filePath))}"/>
                                                                ${fileName}
                                                                ):</label>
                                                            <input type="file" class="form-control" id="file" name="file" >
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                                        <button type="submit" class="btn btn-primary">Upload</button>
                                                    </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                        </div>
                        <!-- Delete Document Modal -->
                        <div class="modal fade" id="delete${document.documentId}" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="deleteModalLabel">Delete Document</h5>
                                        <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <form action="document-manager" method="post">
                                        <div class="modal-body">
                                            <input type="hidden" name="action" value="delete"/>
                                            <input type="hidden" name="document_id" value="${document.documentId}"/>
                                            <p>Are you sure you want to delete the document titled "${document.title}"?</p>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                            <button type="submit" class="btn btn-danger">Delete</button>
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

<!-- Add New Task Modal -->
<div class="modal fade" id="add" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addModalLabel">add new document</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <form action="document-manager" method="post" enctype="multipart/form-data">
                <div class="modal-body">
                    <input type="hidden" name="action" value="upload"/>
                    <div class="form-group">
                        <label for="title">Title:</label>
                        <input type="text" class="form-control" id="title" name="title" required>
                    </div>
                    <div class="form-group">
                        <label for="file">File:</label>
                        <input type="file" class="form-control" id="file" name="file" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Upload</button>
                </div>
            </form>

        </div>
    </div>
</div>
</div>

<jsp:include page="footer.jsp"></jsp:include>
