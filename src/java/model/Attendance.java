
package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Attendance {
    private String attendanceId;
    private String status;
    private String date;
    private Lecturer lec;
}