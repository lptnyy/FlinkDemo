package com.fasterar.smart.server.flink.entity.input;

import java.util.Date;
import lombok.Data;

/**
 * @author mango
 */
@Data
public class PerformanceDayInput {

    private Integer state;

    private Date start;

    private Date end;
    private Integer companyId;
}
