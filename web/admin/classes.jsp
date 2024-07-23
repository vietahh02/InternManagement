<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
        <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Class list</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#add">Add class</button>
                        <a href="../static/InternCandidate.xlsx" type="button" class="btn btn-success btn-sm">Download file template</a>
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
                                    <th>STT</th>
                                    <th>Name</th>
                                    <th>Student</th>
                                    <th>Lecturer</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="c" items="${classList}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${c.class_name}</td>
                                        <td><a href="class-student?cid=${c.class_id}">${c.studentNumber}</a></td>
                                        <td>${c.lecturer.fullName}</td>
                                        <td style="width: 260px">
                                            <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#import${c.class_id}">Import Student</button>
                                            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#changeLecturer${c.class_id}">Change Lecturer</button>
                                        </td>
                                    </tr>
                                    <!-- Import Student Modal -->
                                <div class="modal fade" id="import${c.class_id}" tabindex="-1" role="dialog" aria-labelledby="importModalLabel${o.objective_id}" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="importModalLabel${c.class_id}">Import student to ${c.class_name}</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form action="class-list" method="post" enctype="multipart/form-data">
                                                <div class="modal-body">
                                                    <input required type="file" name="file" accept=".xls,.xlsx"/>
                                                    <input type="hidden" name="action" value="import"/>
                                                    <input type="hidden" name="cid" value="${c.class_id}"/>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                                                    <button class="btn btn-sm btn-info" type="submit" value="Upload">Upload</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <!-- Change Lecturer Modal -->
                                <div class="modal fade" id="changeLecturer${c.class_id}" tabindex="-1" role="dialog" aria-labelledby="changeLecturerModalLabel${c.class_id}" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="changeLecturerModalLabel${c.class_id}">Change Lecturer for ${c.class_name}</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form action="class-list" method="post">
                                                <div class="modal-body">
                                                    <input type="hidden" name="cid" value="${c.class_id}"/>
                                                    <input type="hidden" name="action" value="change"/>
                                                    <label class="form-text">Choose Lecturer</label>
                                                    <select required class="form-control" name="newLid">
                                                        <option class="form-control" value="">Select lecturer</option>
                                                        <c:forEach var="l" items="${l}">
                                                            <option value="${l.lecturer_id}" 
                                                                    <c:if test="${l.lecturer_id == c.lecturer.lecturer_id}">selected</c:if>>
                                                                ${l.fullName}
                                                            </option>
                                                        </c:forEach>

                                                    </select>
                                                    <span id="lecturerError${c.class_id}" style="color: red; display: none;">Please select a lecturer</span>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                                                    <button class="btn btn-sm btn-info" type="submit">Save</button>
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
                    <h5 class="modal-title" id="addModalLabel">Add new class</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <form id="addClassForm" action="class-list" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="add"/>
                        <label class="form-text">Class name</label>
                        <input class="form-control" name="name" type="text" placeholder="Enter class name">
                        <span id="nameError" style="color: red; display: none;">Please enter a class name</span>
                        <hr>
                        <label class="form-text">Choose Lecturer</label>
                        <select required class="form-control" name="lid">
                            <option class="form-control" value="">Select lecturer</option>
                            <c:forEach var="l" items="${lecturers}">
                                <option value="${l.lecturer_id}">${l.fullName}</option>
                            </c:forEach>
                        </select>
                        <span id="lecturerError" style="color: red; display: none;">Please select a lecturer</span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                        <button class="btn btn-sm btn-info" type="submit">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

<script>
    document.getElementById('addClassForm').addEventListener('submit', function (event) {
        let valid = true;

        // Get the form elements
        const nameInput = document.querySelector('input[name="name"]');
        const lecturerSelect = document.querySelector('select[name="lid"]');

        // Error span elements
        const nameError = document.getElementById('nameError');
        const lecturerError = document.getElementById('lecturerError');

        // Reset error messages
        nameError.style.display = 'none';
        lecturerError.style.display = 'none';

        // Validate class name
        if (nameInput.value.trim() === '') {
            nameError.style.display = 'block';
            valid = false;
        }

        // Validate lecturer selection
        if (lecturerSelect.value === '') {
            lecturerError.style.display = 'block';
            valid = false;
        }

        // If the form is not valid, prevent submission
        if (!valid) {
            event.preventDefault();
        }
    });
</script>
