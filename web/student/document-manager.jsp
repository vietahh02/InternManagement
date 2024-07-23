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
                <h1 class="h3 mb-2 text-gray-800">Document</h1>
                <div class="card shadow mb-4">

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
                                            <a href="${pageContext.request.contextPath}/uploads/${fn:substring(document.filePath, 8, fn:length(document.filePath))}" class="btn btn-info btn-sm" download>
                                                Download
                                            </a>
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
