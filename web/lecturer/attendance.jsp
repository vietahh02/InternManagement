<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">

        <div id="content">

            <!-- Including header content -->
        <jsp:include page="header-content.jsp"></jsp:include>

            <!-- Success Notification -->
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

        <div class="container-fluid">
            <h1 class="h3 mb-2 text-gray-800">Class list: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="date-now"></span></h1>
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">All classes</h6>
                </div>
                <div class="card-body">

                    <div class="table-responsive">
                        <form action="${run == "add" ? "attendance" : "updateAttendance"}" method="post" style="width: 100%">

                            <div style="text-align: center; margin: auto">
                                <input type="date" id="date-in"/>
                                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                    <tr>
                                        <th>STT</th>
                                        <th>Roll Number</th>
                                        <th>Full Name</th>
                                        <th>Attendance</th>
                                    </tr>

                                    <c:forEach items="${list}" var="stu" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1}</td>
                                            <td>${stu.rollNumber}</td>
                                            <td>${stu.fullName}</td>
                                            <td>
                                                <input type="checkbox" name="att" class="che" value="${stu.rollNumber}" ${stu.status == "present" ? "checked" : ""} >
                                            </td>
                                        </tr>
                                    </c:forEach>

                                </table>
                                <button style="margin-top: 15px" id="button">Save</button>

                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="../vendor/jquery/jquery.min.js"></script>
<script>
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }
    if (mm < 10) {
        mm = '0' + mm;
    }
    today = yyyy + '-' + mm + '-' + dd;
    $('#date-now').html(today);
    $('#date-in').val(today);

    $('#date-in').change(function () {

        changeAtt();

    });

    function changeAtt() {
        console.log($('#date-in').val(), ${sessionScope.classId});
        fetch(`viewAttendance?who=lec&date=` + $('#date-in').val() + `&classId=` + ${sessionScope.classId})
                .then(function (response) {
                    return response.json(response);
                })
                .then(function (data) {
                    let read = `<tr>
                                    <th>STT</th>
                                    <th>Roll Number</th>
                                    <th>Full Name</th>
                                    <th>Attendance</th>
                                </tr>`;
                    let length = data.length;
                    if (length < 1) {
                        read += '<tr><td colspan="4">No Data</td></tr>';
                    }
                    for (i = 0; i < length; i++) {
                        read += '<tr><td>' + (i + 1) + '</td>'
                                + '<td>' + data[i].rollNumber + '</td>'
                                + '<td>' + data[i].fullName + '</td><td>'
                                + '<input type="checkbox" name="att" class="che" value="' + data[i].rollNumber + '" ';
                        if (data[i].status === "present") {
                            read += 'checked></td></tr>';
                        } else {
                            read += '></td></tr>';
                        }
                    }
                    console.log(data);
                    console.log(read);
                    $('#dataTable').html(read);
                    console.log($('#root1').html());
                    $('#date-now').html($('#date-in').val());
                    if (today === $('#date-in').val()) {
//                        location.reload();
                        $('#button').show()();
                        $('.che').removeAttr('disabled');
                    } else {
                        $('#button').hide();
                        $('.che').attr('disabled', '');
                    }
                });
    }
    ;

</script>

<jsp:include page="footer.jsp"></jsp:include>
