<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>

    <div id="content-wrapper" class="d-flex flex-column">

        <div id="content">

            <!-- Including header content -->
        <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800"></h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Schedule</h6>
                    </div>
                    <div class="card-body">

                        <div class="container-calendar">
                            <div class="calendar">
                                <div class="head-calendar">
                                    <button id="left" class="button-calendar" onclick="prevMonth()" >
                                        <i
                                            class="fa-solid fa-chevron-left"
                                            style="
                                            color: #ffffff;
                                            position: relative;
                                            font-size: 50px;
                                            cursor: pointer;
                                            "
                                            ></i>
                                    </button>
                                    <div class="month_year">
                                        <div id="month_year">JUNE 2014</div>
                                    </div>
                                    <button id="right" onclick="nextMonth()" class="button-calendar">
                                        <i
                                            class="fa-solid fa-chevron-right"
                                            style="
                                            color: #ffffff;
                                            position: relative;
                                            font-size: 50px;
                                            cursor: pointer;
                                            "
                                            ></i>
                                    </button>
                                </div>
                                <div class="dates" id="dates"></div>
                            </div>
                            <div class="calendar days">
                                <div>SU</div>
                                <div>MO</div>
                                <div>TU</div>
                                <div>WE</div>
                                <div>TH</div>
                                <div>FR</div>
                                <div>SA</div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="../vendor/jquery/jquery.min.js"></script>
    <script>
const nowDate = new Date();
let nowYear = nowDate.getFullYear();
let nowMonth = nowDate.getMonth();
const months = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December"
];

function dates(year, month, listDate) {
  const date = new Date(year, month, 1);
  const dates = document.getElementById("dates");
  const lastDay = new Date(year, month + 1, 0);

  dates.innerHTML = "";

  document.getElementById("month_year").innerHTML =
    months[nowMonth] + " " + year;

  const day = new Date(year, month, 0).getDate();
  const date1 = new Date(year, month - 1, day - date.getDay());
  for (let i = date1.getDate() + 1; i <= day; i++) {
    const newElement = document.createElement("div");
    newElement.className = "dayOutOfMonth";
    if (
      date1.getFullYear() === nowDate.getFullYear() &&
      date1.getMonth() === nowDate.getMonth() &&
      i === nowDate.getDate()
    ) {
      newElement.className = "today";
    }
    newElement.innerText = i;
    dates.appendChild(newElement);
  }

  for (let i = 1; i <= lastDay.getDate(); i++) {
    const newElement = document.createElement("div");
    if (
      date.getFullYear() === nowDate.getFullYear() &&
      date.getMonth() === nowDate.getMonth() &&
      i === nowDate.getDate()
    ) {
      newElement.className = "today";
    }
    const date3 = new Date(year, month, i);
    let check = checkDate(listDate, formatDate(date3));
    if (date3.getDay() === 6 || date3.getDay() === 0 || check === null && compareDates(date3,nowDate) === -1) {
      newElement.innerText = i;
    }
     else if (check !== null) {
      const newE1 = document.createElement("p");
      newE1.innerHTML = i;
      const newE2 = document.createElement("h5");
         if (check === 'present') {
      newE2.style.color = "green";
      newE2.innerHTML = "present";
         }else {
      newE2.style.color = "red";
      newE2.innerHTML = "absent";
         }
      newElement.appendChild(newE1);
      newElement.appendChild(newE2);
    } else {
      const newE1 = document.createElement("p");
      newE1.innerHTML = i;
      const newE2 = document.createElement("h5");
      newE2.style.color = "#a5a5a5";
      newE2.innerHTML = "not yet";
      newElement.appendChild(newE1);
      newElement.appendChild(newE2);
    }

    dates.appendChild(newElement);
  }

  k = 1;
  for (let i = lastDay.getDay(); i < 6; i++) {
    const newElement = document.createElement("div");
    newElement.className = "dayOutOfMonth";
    if (
      lastDay.getFullYear() === nowDate.getFullYear() &&
      lastDay.getMonth() === nowDate.getMonth() &&
      k === nowDate.getDate()
    ) {
      newElement.className = "today";
    }
    newElement.innerText = k;
    dates.appendChild(newElement);
    k++;
  }
}

function prevMonth() {
  nowMonth--;
  if (nowMonth < 0) {
    nowYear--;
    nowMonth = 11;
  }
  changeAtt();
}

function nextMonth() {
  nowMonth++;
  if (nowMonth > 11) {
    nowYear++;
    nowMonth = 0;
  }
  changeAtt();
}

function checkDate(list, date) {
    for(let i=0;i<list.length;i++) {
        console.log(list[i].date, date);
        if (list[i].date === date) {
            return list[i].status;
        }
    }
    return null;
}

function changeAtt() {
        const options = {
            method: 'POST'
        };
        let m;
        if(nowMonth+1 < 10) {
            m = '0'+(nowMonth+1);
        }else {
            m = (nowMonth+1);
        }
        fetch(`attendanceReport?date=`+nowYear+'-'+m, options)
                .then(function (response) {
                    return response.json(response);
                })
                .then(function (data) {
                    dates(nowYear, nowMonth, data);
                });
}

function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');

  return year+'-'+month+'-'+day;
}

function compareDates(date1, date2) {
  if(date1.getFullYear() === date2.getFullYear() && date1.getMonth() === date2.getMonth() && date1.getDate() === date2.getDate()) {
    return 0;
  }
  const time1 = date1.getTime();
  const time2 = date2.getTime();

  if (time1 < time2) {
    return -1;
  } else if (time1 > time2) {
    return 1; 
  } else {
    return 0;
  }
}

changeAtt();
    </script>

<jsp:include page="footer.jsp"></jsp:include>
