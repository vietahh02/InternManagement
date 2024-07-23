<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!-- Including header -->
<jsp:include page="header.jsp"></jsp:include>

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">

            <!-- Including header content -->
        <jsp:include page="header-content.jsp"></jsp:include>

            <!-- Task management -->
            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Report management</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">


                        <li class="nav-item dropdown" style="float: right">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Filter by Type
                            </a>
                            <div class="dropdown-menu dropdown-menu-right animated--grow-in" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item ${param.type == null ? 'active' : ''}" href="report?cid=${param.oid}">All</a>
                            <a class="dropdown-item ${param.type == 'Mid term' ? 'active' : ''}" href="report?cid=${param.oid}&amp;type=Mid term">Mid term</a>
                            <a class="dropdown-item ${param.type == 'Final' ? 'active' : ''}" href="report?cid=${param.oid}&amp;type=Final">Final</a>
                        </div>
                    </li>
                    <li class="nav-item dropdown" style="float: right">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Filter by Class
                        </a>
                        <div class="dropdown-menu dropdown-menu-right animated--grow-in" aria-labelledby="navbarDropdown" style="max-height: 300px; overflow-y: auto;">
                            <a class="dropdown-item ${param.cid == null ? 'active' : ''}" href="report?type=${param.type}">All</a>
                            <c:forEach var="q" items="${listClass}" varStatus="status">
                                <a class="dropdown-item ${param.cid != null && param.cid == q.class_id ? 'active' : ''}" href="report?cid=${q.class_id}&amp;type=${param.type}">${q.class_name}</a>
                            </c:forEach>
                        </div>
                    </li>

                </div>
                <div class="py-3 row">
                    <c:if test="${not empty param.cid}">
                        <a class="m-0 font-weight-bold text-primary col-md-3" ></a>
                        <a class="m-0 font-weight-bold text-primary col-md-3" type="button" href="student-report?type=Mid term&class=${param.cid}">Export mid term report</a>
                        <a class="m-0 font-weight-bold text-primary col-md-3" type="button" href="student-report?type=Final&class=${param.cid}">Export final report</a>
                        <a href="${pageContext.request.contextPath}/admin/student-report?class=${param.cid}&reportType=passFail" class="m-0 font-weight-bold text-primary col-md-3">
                            Export Pass/Fail Report
                        </a>

                    </c:if>
                    <c:if test="${empty param.cid}">
                        <a class="m-0 font-weight-bold text-primary col-md-3" ></a>
                        <a class="m-0 font-weight-bold text-primary col-md-5" onclick="showAlert()"  >Export mid term report</a>
                        <a class="m-0 font-weight-bold text-primary col-md-4" onclick="showAlert()" >Export final report</a>
                        <a  class="m-0 font-weight-bold text-primary col-md-4"  onclick="showAlert()">
                            Export Pass/Fail Report
                        </a>

                    </c:if>
                </div>
                <div class="card-body">
                    <div class="table-responsive">

                        <table id="dataTable" class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Type</th>
                                    <th>Content</th>
                                    <th>Class</th>
                                    <th>Lecturer</th>
                                    <th>Student</th>
                                    <th>Created at</th>
                                    <th>Update at</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="q" items="${questions}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td style=" color: ${q.type eq 'Mid term' ? 'green' : 'red'}">  ${q.type}    </td>
                                        <td>${q.content}</td>
                                        <td>${q.class_name}</td>
                                        <td>${q.lecturer_name}</td>
                                        <td>${q.student_name}</td>
                                        <td>${q.created_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>

                                        <td>${q.updated_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div><script>
            function showAlert() {
                alert(`Please select a class to export!`);
            }
        </script>
    </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
