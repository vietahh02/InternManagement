<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${currentAccount.roleAccount.role_id == 3}">
    <jsp:include page="lecturer_header.jsp"></jsp:include>
</c:if>
<c:if test="${currentAccount.roleAccount.role_id == 2}">
    <jsp:include page="student_header.jsp"></jsp:include>
</c:if>
<div id="content-wrapper" class="d-flex flex-column">
    <div id="content">


        <jsp:include page="header-content.jsp"></jsp:include>
            <div class="container">
                <div class="main-body">

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
                <c:if test="${currentAccount.roleAccount.role_id == 2}">
                    <div class="row gutters-sm">
                        <div class="col-md-4 mb-3">
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex flex-column align-items-center text-center">
                                        <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle" width="150">
                                        <div class="mt-3">
                                            <h4>${currentAccount.username}</h4>
                                            <p class="text-secondary mb-1">Roll number:  ${currentAccount.student.rollNumber}</p>
                                            <p class="text-muted font-size-sm">DOB:  ${currentAccount.student.birthDate}</p>
                                            <p class="text-muted font-size-sm">School Year: ${currentAccount.student.schoolYear}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div class="col-md-8">
                            <div class="card mb-3">
                                <form action="my-profile" method="POST">
                                    <div class="card-body">
                                        <div class="row">
                                            <input class="form-control" type="hidden" name="studentId" value="${currentAccount.student.student_id}" >
                                            <input class="form-control" type="hidden" name="cid" value="${currentAccount.id}" >
                                            <input class="form-control" type="hidden" name="action" value="edit-student" >
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Full name</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                <input class="form-control" type="text" name="fullName" value="${currentAccount.student.fullName}" >
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Email</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                <input class="form-control" readonly="" disabled="" type="text" name="email" value="${currentAccount.email}" >
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Phone</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                <input class="form-control"  type="text" name="phone" value="${currentAccount.phone}" >
                                            </div>
                                        </div>
                                        <hr>

                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Address</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                <input class="form-control"  type="text" name="address" value=" ${currentAccount.address}" >

                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <button type="submit" class="btn btn-info">Save</button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${currentAccount.roleAccount.role_id == 3}">
                    <div class="row gutters-sm">
                        <div class="col-md-4 mb-3">
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex flex-column align-items-center text-center">
                                        <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle" width="150">
                                        <div class="mt-3">
                                            <h4>${currentAccount.username}</h4>
                                            <p class="text-secondary mb-1">Lecturer number:  ${currentAccount.lecturer.employeeNumber}</p>
                                            <p class="text-muted font-size-sm">Department  ${currentAccount.lecturer.department}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div class="col-md-8">
                            <div class="card mb-3">
                                <form action="my-profile" method="POST">
                                    <div class="card-body">
                                        <div class="row">
                                            <input class="form-control" type="hidden" name="lId" value="${currentAccount.lecturer.lecturer_id}" >
                                            <input class="form-control" type="hidden" name="cid" value="${currentAccount.id}" >
                                            <input class="form-control" type="hidden" name="action" value="edit-lecturer" >
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Full name</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                <input class="form-control" type="text" name="fullName" value="${currentAccount.lecturer.fullName}" >
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Email</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                <input class="form-control" readonly="" disabled="" type="text" name="email" value="${currentAccount.email}" >
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Phone</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                <input class="form-control"  type="text" name="phone" value="${currentAccount.phone}" >
                                            </div>
                                        </div>
                                        <hr>

                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Address</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                <input class="form-control"  type="text" name="address" value=" ${currentAccount.address}" >

                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <button type="submit" class="btn btn-info">Save</button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:if>

            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>

