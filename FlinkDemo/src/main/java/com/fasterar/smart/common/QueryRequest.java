package com.fasterar.smart.common;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * @author Jason
 */
@Data
@ToString
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;

    private int pageSize = 10;

    private int pageNum = 1;

    private String field;

    private String order;
}
