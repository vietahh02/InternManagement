
package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Support {
    private int suport_id;
    private TypeSupport ts;
    private String content;
    private String create_date;
    private String process_note;
    private String status;
    private String end_date;
    private Account account;
}
