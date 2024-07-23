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

                            <span class="mr-2 d-none d-lg-inline text-gray-600 small">${accounts.username}</span>
                        <img class="img-profile rounded-circle" src="../img/undraw_profile.svg">
                    </a>
                    <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                         aria-labelledby="userDropdown">
                        <a class="dropdown-item" href="#">
                            <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                            Profile
                        </a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/logout" data-toggle="modal" data-target="#logoutModal">
                            <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                            Logout
                        </a>
                    </div>
                </li>
            </ul>
        </nav>
        <div class="container-fluid">
            <h1 class="h3 mb-2 text-gray-800">User list</h1>
            <button  type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#add">Add new Lecturer</button>
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
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <!--<h6 class="m-0 font-weight-bold text-primary">All classes</h6>-->
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Address</th>
                                    <th>Phone</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="a" items="${account}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/common/profile?id=${a.id}"> ${a.username}</a>

                                        </td>
                                        <td>${a.email}</td>
                                        <td>${a.address}</td>
                                        <td>${a.phone}</td>
                                        <td style="color: ${a.roleAccount.role_name == 'Lecturer' ? 'orange' : (a.roleAccount.role_name == 'Student' ? 'green' : 'black')}">
                                            ${a.roleAccount.role_name}
                                        </td>
                                        <td style="color: ${a.status eq 'active' ? 'green' : 'red'};">
                                            ${a.status}
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/admin/user-managament" method="POST" onsubmit="return confirmStatusChange()">
                                                <input type="hidden" name="action" value="change-status"> 
                                                <input type="hidden" name="id" value="${a.id}"> 
                                                <input type="hidden" name="status" value="${a.status}"> 
                                                <button type="submit" class="btn btn-${a.status eq 'active' ? 'success' : 'danger'} "><i class="fa fa-${a.status eq 'active' ? 'ban' : 'unlock'}"></i></button>
                                            </form>
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
    <div class="modal fade" id="add" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addModalLabel">Add new Lecturer</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <form id="addClassForm" action="user-managament" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="add"/>
                        <label class="form-text">Full name</label>
                        <input class="form-control" name="name" type="text" placeholder="Enter full name">
                        <span id="nameError" style="color: red; display: none;">Please enter a full name</span>

                        <label class="form-text">Employee number</label>
                        <input class="form-control" name="employeeNumber" type="text" placeholder="Enter Employee number">
                        <span id="employeeNumberError" style="color: red; display: none;">Please enter an employee number</span>

                        <label class="form-text">Email</label>
                        <input class="form-control" name="email" type="text" placeholder="Enter email">
                        <span id="emailError" style="color: red; display: none;">Please enter an email</span>

                        <label class="form-text">Phone</label>
                        <input class="form-control" name="phone" type="text" placeholder="Enter phone number">
                        <span id="phoneError" style="color: red; display: none;">Please enter a phone number</span>
                        <label class="form-text">Department</label>
                        <input class="form-control" name="department" type="text" placeholder="Enter  department">

                        <label class="form-text">Specialization</label>
                        <input class="form-control" name="specialization" type="text" placeholder="Enter specialization">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                        <button class="btn btn-sm btn-info" type="submit" value="Upload">Upload</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    function confirmStatusChange() {
        return confirm('Are you sure you want to change the status of this user?');
    }
</script>
<script>
    document.getElementById('addClassForm').addEventListener('submit', function (event) {
        let valid = true;

        // Get the form elements
        const nameInput = document.querySelector('input[name="name"]');
        const employeeNumberInput = document.querySelector('input[name="employeeNumber"]');
        const emailInput = document.querySelector('input[name="email"]');
        const phoneInput = document.querySelector('input[name="phone"]');

        // Error span elements
        const nameError = document.getElementById('nameError');
        const employeeNumberError = document.getElementById('employeeNumberError');
        const emailError = document.getElementById('emailError');
        const phoneError = document.getElementById('phoneError');

        // Reset error messages
        nameError.style.display = 'none';
        employeeNumberError.style.display = 'none';
        emailError.style.display = 'none';
        phoneError.style.display = 'none';

        // Validate full name
        if (nameInput.value.trim() === '') {
            nameError.style.display = 'block';
            valid = false;
        }

        // Validate employee number
        if (employeeNumberInput.value.trim() === '') {
            employeeNumberError.style.display = 'block';
            valid = false;
        }

        // Validate email
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (emailInput.value.trim() === '' || !emailPattern.test(emailInput.value)) {
            emailError.style.display = 'block';
            valid = false;
        }

        // Validate phone number
        const phonePattern = /^0\d{9}$/;
        if (phoneInput.value.trim() === '' || !phonePattern.test(phoneInput.value)) {
            phoneError.style.display = 'block';
            valid = false;
        }

        // If the form is not valid, prevent submission
        if (!valid) {
            event.preventDefault();
        }
    });
</script>
<jsp:include page="footer.jsp"></jsp:include>

