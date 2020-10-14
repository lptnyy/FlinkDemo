package com.flink.skin;
import com.flink.db.HikariConfigration;
import com.flink.entity.BigFrom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class MysqlDataSkin extends RichSinkFunction<BigFrom> {
  Connection connection;
  PreparedStatement stmt = null;


  public void getConnection() throws ClassNotFoundException, SQLException {
    HikariConfigration hikariConfigration = HikariConfigration.getInstance();
    connection = hikariConfigration.getDs().getConnection();
    System.out.println(connection);
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    getConnection();
  }

  @Override
  public void close() throws Exception {
    super.close();
    if (connection != null) {
      connection.close();
    }
    if (stmt != null) {
      stmt.close();
    }
  }

  @Override
  public void invoke(BigFrom value, Context context) throws Exception {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("insert into data_event_value(");
    stringBuffer.append("attribute_id")
        .append(",uid")
        .append(",tid")
        .append(",cid")
        .append(",sid")
        .append(",tp")
        .append(",fav")
        .append(",name")
        .append(",t")
        .append(",p")
        .append(",ps")
        .append(",dp")
        .append(",dps")
        .append(",ts")
        .append(",create_time")
        .append(") values (")
        .append("?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",?")
        .append(",now()")
        .append(")");
    stmt = connection.prepareStatement(stringBuffer.toString());
    stmt.setInt(1, checkInt(value.getAttributeId()));
    stmt.setInt(2, checkInt(value.getBigData().getUid()));
    stmt.setString(3, checkStr(value.getBigData().getTid()));
    stmt.setInt(4, checkInt(value.getBigData().getCid()));
    stmt.setInt(5, checkInt(value.getBigData().getSid()));
    stmt.setString(6, checkStr(value.getBigData().getTp()));
    stmt.setInt(7, checkInt(value.getBigData().getFav()));
    stmt.setString(8, checkStr(value.getBigData().getName()));
    stmt.setString(9, checkStr(value.getBigData().getT()));
    stmt.setInt(10, checkInt(value.getBigData().getP()));
    stmt.setInt(11, checkInt(value.getBigData().getPs()));
    stmt.setInt(12, checkInt(value.getBigData().getDp()));
    stmt.setInt(13, checkInt(value.getBigData().getDps()));
    stmt.setString(14, checkStr(value.getBigData().getTs()));
    stmt.executeUpdate();
  }

  private Integer checkInt(Integer op) {
    if (op == null) {
      return 0;
    }
    return op;
  }

  private String checkStr(Object str) {
    if (str == null) {
      return "";
    }
    return str.toString();
  }
}
