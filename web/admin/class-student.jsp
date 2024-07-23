<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
                <form class="form-inline">
                    <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                        <i class="fa fa-bars"></i>
                    </button>
                </form>
                <form class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
                    <div class="input-group">
                        <input type="text" class="form-control bg-light border-0 small" placeholder="Search for..."
                               aria-label="Search" aria-describedby="basic-addon2">
                        <div class="input-group-append">
                            <button class="btn btn-primary" type="button">
                                <i class="fas fa-search fa-sm"></i>
                            </button>
                        </div>
                    </div>
                </form>
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item dropdown no-arrow d-sm-none">
                        <a class="nav-link dropdown-toggle" href="#" id="searchDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-search fa-fw"></i>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in"
                             aria-labelledby="searchDropdown">
                            <form class="form-inline mr-auto w-100 navbar-search">
                                <div class="input-group">
                                    <input type="text" class="form-control bg-light border-0 small"
                                           placeholder="Search for..." aria-label="Search"
                                           aria-describedby="basic-addon2">
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="button">
                                            <i class="fas fa-search fa-sm"></i>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </li>
                    <div class="topbar-divider d-none d-sm-block"></div>
                    <li class="nav-item dropdown no-arrow">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="mr-2 d-none d-lg-inline text-gray-600 small">${account.username}</span>
                        <img class="img-profile rounded-circle" src="../img/undraw_profile.svg">
                    </a>
                    <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                         aria-labelledby="userDropdown">
                        <a class="dropdown-item" href="#">
                            <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                            Profile
                        </a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                            <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                            Logout
                        </a>
                    </div>
                </li>
            </ul>
        </nav>
        <div class="container-fluid">
            <h1 class="h3 mb-2 text-gray-800">Class ${classList[0].classes.class_name}</h1>
            <div class="card shadow mb-4">
                <div class="card-header py-3 row">
                    <a class="m-0 font-weight-bold text-primary col-md-3" type="button" href="student-report?type=Mid term&class=${param.cid}">Export mid term report</a>
                    <a class="m-0 font-weight-bold text-primary col-md-3" type="button" href="student-report?type=Final&class=${param.cid}">Export final report</a>
                    <a href="${pageContext.request.contextPath}/admin/student-report?class=${param.cid}&reportType=passFail" class="m-0 font-weight-bold text-primary col-md-3">
                        Export Pass/Fail Report
                    </a>
                    <a class="m-0 font-weight-bold text-primary col-md-3" type="button" href="class-student?cid=${param.cid}&action=export">Export Student list</a>
                </div>
                <div class="card-body">
                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#addStudentModal">
                        Add Student
                    </button>
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
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Student</th>
                                    <th>RollNumber</th>
                                    <th>Email</th>
                                    <th>Phone Number</th>
                                    <th>Major</th>
                                    <th>Company</th>
                                    <th>Job Title</th>
                                    <th>Link CV</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="c" items="${classList}" varStatus="status">
                                    <tr> 
                                        <td>${status.index + 1}</td>
                                        <td><a href="${pageContext.request.contextPath}/common/profile?id=${c.student.account.id}">${c.student.fullName}</a></td>
                                        <td>${c.student.rollNumber}</td>
                                        <td>${c.student.account.email}</td>
                                        <td>${c.student.account.phone}</td>
                                        <td>${c.student.major}</td>
                                        <td>${c.student.company}</td>
                                        <td>${c.student.jobTitle}</td>
                                        <td>
                                            <button type="button" class="btn btn-primary" onclick="window.open('${c.student.linkCv}', '_blank')">
                                                View CV
                                            </button>
                                            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteStudentModal${c.id}">Delete</button>
                                        </td>
                                    </tr>
                                    <!-- Delete Student Modal -->
                                <div class="modal fade" id="deleteStudentModal${c.id}" tabindex="-1" role="dialog" aria-labelledby="deleteStudentModalLabel${c.id}" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="deleteStudentModalLabel${c.id}">Delete Student from Class</h5>
                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <form action="class-student" method="post">
                                                <div class="modal-body">
                                                    <input type="hidden" name="userClass_id" value="${c.id}"/>
                                                    <input type="hidden" name="cid" value="${c.classes.class_id}"/>
                                                    Are you sure you want to delete ${c.student.fullName} from this class?
                                                </div>
                                                <div class="modal-footer">
                                                    <input type="hidden" name="action" value="delete"/>
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

    <!-- Add Student Modal -->
    <div class="modal fade" id="addStudentModal" tabindex="-1" role="dialog" aria-labelledby="addStudentModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addStudentModalLabel">Add Student to ${classList[0].classes.class_name}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="class-student" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="class_id" value="${param.cid}"/>
                        <input type="hidden" name="action" value="add"/>
                        <label for="studentSelect" class="form-text">Choose Students</label>
                        <select id="studentSelect" class="form-control" name="student_ids" multiple required>
                            <c:forEach var="student" items="${studentsNotInClass}">
                                <option value="${student.student_id}">${student.fullName} (${student.rollNumber})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add Students</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp"></jsp:include>

    <!-- Include jQuery and Select2 library -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script>
                                                $(document).ready(function () {
                                                    $('#studentSelect').select2({
                                                        placeholder: "Select students",
                                                        allowClear: true
                                                    });
                                                });
    </script>
