package com.fasterar.smart.server.flink.entity.input;
import java.util.Date;
import lombok.Data;

/**
 * @author mango
 */
@Data
public class ShareHistoryDayInput {

    private Integer state;

    private Date start;

    private Date end;

    private Integer userId;

    private String linkTitle;

    private String linkType;
}
