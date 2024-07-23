<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${account.roleAccount.role_id == 3}">
    <jsp:include page="lecturer_header.jsp"></jsp:include>
</c:if>
<c:if test="${account.roleAccount.role_id == 2}">
    <jsp:include page="student_header.jsp"></jsp:include>
</c:if>
<c:if test="${account.roleAccount.role_id == 1}">
    <jsp:include page="admin_header.jsp"></jsp:include>
</c:if>
<div id="content-wrapper" class="d-flex flex-column">
    <div id="content">


        <jsp:include page="header-content.jsp"></jsp:include>
            <div class="container">
                <div class="main-body">


                <c:if test="${a.roleAccount.role_id == 2}">
                    <div class="row gutters-sm">
                        <div class="col-md-4 mb-3">
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex flex-column align-items-center text-center">
                                        <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle" width="150">
                                        <div class="mt-3">
                                            <h4>${a.username}</h4>
                                            <p class="text-secondary mb-1">Roll number:  ${a.student.rollNumber}</p>
                                            <p class="text-muted font-size-sm">DOB:  ${a.student.birthDate}</p>
                                            <p class="text-muted font-size-sm">School Year: ${a.student.schoolYear}</p>
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
                                            <input class="form-control" type="hidden" name="studentId" value="${a.student.student_id}" >
                                            <input class="form-control" type="hidden" name="cid" value="${a.id}" >
                                            <input class="form-control" type="hidden" name="action" value="edit-student" >
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Full name</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                ${a.student.fullName}
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Email</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                ${a.email}
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Phone</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                ${a.phone}
                                            </div>
                                        </div>
                                        <hr>

                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Address</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                ${a.address}

                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">

                                            <div class="col-sm-12">
                                                <a type="button" href="javascript:history.back()" class="btn btn-info">Back</a>

                                            </div>

                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${a.roleAccount.role_id == 3}">
                    <div class="row gutters-sm">
                        <div class="col-md-4 mb-3">
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex flex-column align-items-center text-center">
                                        <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle" width="150">
                                        <div class="mt-3">
                                            <h4>${a.username}</h4>
                                            <p class="text-secondary mb-1">Lecturer number:  ${a.lecturer.employeeNumber}</p>
                                            <p class="text-muted font-size-sm">Department  ${a.lecturer.department}</p>
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
                                            <input class="form-control" type="hidden" name="lId" value="${a.lecturer.lecturer_id}" >
                                            <input class="form-control" type="hidden" name="cid" value="${a.id}" >
                                            <input class="form-control" type="hidden" name="action" value="edit-lecturer" >
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Full name</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                ${a.lecturer.fullName}
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Email</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                ${a.email}
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Phone</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                ${a.phone}
                                            </div>
                                        </div>
                                        <hr>

                                        <hr>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <h6 class="mb-0">Address</h6>
                                            </div>
                                            <div class="col-sm-9 text-secondary">
                                                ${a.address}

                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">

                                            <div class="col-sm-12">
                                                <a type="button" href="javascript:history.back()" class="btn btn-info">Back</a>

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

