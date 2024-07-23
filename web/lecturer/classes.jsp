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
                        <h6 class="m-0 font-weight-bold text-primary">All classes</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Name</th>
                                        <th>Student</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <!--                            <tfoot>
                                                                <tr>
                                                                    <th>Name</th>
                                                                </tr>
                                                            </tfoot>-->
                                <tbody>
                                <c:forEach var="c" items="${classList}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${c.class_name}</td>
                                        <td><a href="class-student?cid=${c.class_id}">${c.studentNumber}</a></td>
                                        <td><a class="btn btn-info" href="objective?cid=${c.class_id}">Detail</a></td>
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

