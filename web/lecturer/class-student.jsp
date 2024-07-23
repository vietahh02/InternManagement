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
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">All classes</h6>
                </div>
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
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Student</th>
                                    <th>RollNumber</th>
                                    <th>Report Student</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="c" items="${classList}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td><a href="${pageContext.request.contextPath}/common/profile?id=${c.student.account.id}">${c.student.fullName}</a></td>

                                        <td>${c.student.rollNumber}</td>
                                        <td><button  type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#add${c.student.student_id}">Add New</button></td>
                                    </tr>
                                <div class="modal fade" id="add${c.student.student_id}" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="addModalLabel">Report Student</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form id="addForm${c.student.student_id}" action="report" method="post" onsubmit="return validateAddForm(${c.student.student_id})">
                                                <div class="modal-body">
                                                    <input type="hidden" name="action" value="add"/>
                                                    <input type="hidden" name="sid" value="${c.student.student_id}"/>
                                                    <input type="hidden" name="cid" value="${param.cid}"/>

                                                    <label class="form-text">Select type</label>
                                                    <select class="form-control" name="type" id="type${c.student.student_id}">
                                                        <option value="">Select Type</option>
                                                        <option value="Mid term">Mid Term</option>
                                                        <option value="Final">Final</option>
                                                    </select>
                                                    <span id="typeError${c.student.student_id}" style="color: red;"></span>

                                                    <label class="form-text">Knowledge</label>
                                                    <input class="form-control" name="knowledge" id="knowledge${c.student.student_id}" step="0.1" type="number" placeholder="Enter knowledge grade">
                                                    <span id="knowledgeError${c.student.student_id}" style="color: red;"></span>
                                                    <hr>

                                                    <label class="form-text">Soft Skill</label>
                                                    <input class="form-control" name="soft_skill" id="soft_skill${c.student.student_id}" step="0.1" type="number" placeholder="Enter Soft Skill grade">
                                                    <span id="softSkillError${c.student.student_id}" style="color: red;"></span>
                                                    <hr>

                                                    <label class="form-text">Attitude</label>
                                                    <input class="form-control" name="attitude" id="attitude${c.student.student_id}"  step="0.1" type="number" placeholder="Enter attitude grade">
                                                    <span id="attitudeError${c.student.student_id}" style="color: red;"></span>
                                                    <hr>

                                                    <textarea class="form-control" name="content" id="content${c.student.student_id}" placeholder="Enter content" rows="4"></textarea>
                                                    <span id="contentError${c.student.student_id}" style="color: red;"></span>
                                                    <hr>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                                                    <button class="btn btn-sm btn-info" type="submit">Add</button>
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
<script>
    function validateAddForm(studentId) {
        var type = document.getElementById('type' + studentId).value;
        var knowledge = document.getElementById('knowledge' + studentId).value.trim(); // Trim to handle empty spaces
        var softSkill = document.getElementById('soft_skill' + studentId).value.trim(); // Trim to handle empty spaces
        var attitude = document.getElementById('attitude' + studentId).value.trim(); // Trim to handle empty spaces
        var content = document.getElementById('content' + studentId).value.trim(); // Trim to handle empty spaces

        // Validate type not null
        if (type === "") {
            document.getElementById('typeError' + studentId).innerText = "Please select a type.";
            return false;  // Prevent form submission
        } else {
            document.getElementById('typeError' + studentId).innerText = "";
        }

        // Validate content not null


        // Validate knowledge, soft skill, attitude (optional)
        if (knowledge.trim() === "") {
            document.getElementById('knowledgeError' + studentId).innerText = "Knowledge grade is required.";
            return false; // Prevent form submission
        } else if (isNaN(knowledge) || knowledge < 0 || knowledge > 10) {
            document.getElementById('knowledgeError' + studentId).innerText = "Knowledge grade must be between 0 and 10.";
            return false; // Prevent form submission
        } else {
            document.getElementById('knowledgeError' + studentId).innerText = "";
        }
        var softSkill = document.getElementById('soft_skill' + studentId).value.trim();
        if (softSkill.trim() === "") {
            document.getElementById('softSkillError' + studentId).innerText = "Soft Skill grade is required.";
            return false; // Prevent form submission
        } else if (isNaN(softSkill) || softSkill < 0 || softSkill > 10) {
            document.getElementById('softSkillError' + studentId).innerText = "Soft Skill grade must be between 0 and 10.";
            return false; // Prevent form submission
        } else {
            document.getElementById('softSkillError' + studentId).innerText = "";
        }

        var attitude = document.getElementById('attitude' + studentId).value.trim();
        if (attitude.trim() === "") {
            document.getElementById('attitudeError' + studentId).innerText = "Attitude grade is required.";
            return false; // Prevent form submission
        } else if (isNaN(attitude) || attitude < 0 || attitude > 10) {
            document.getElementById('attitudeError' + studentId).innerText = "Attitude grade must be between 0 and 10.";
            return false; // Prevent form submission
        } else {
            document.getElementById('attitudeError' + studentId).innerText = "";
        }
        if (content === "") {
            document.getElementById('contentError' + studentId).innerText = "Content cannot be empty.";
            return false; // Prevent form submission
        } else {
            document.getElementById('contentError' + studentId).innerText = "";
        }
        // Reset error messages if validation passes
        document.getElementById('typeError' + studentId).innerText = "";

        return true; // Form submission allowed
    }
</script>

<jsp:include page="footer.jsp"></jsp:include>

