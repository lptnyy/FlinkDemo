package com.fasterar.smart.server.flink.skin;
import com.fasterar.smart.server.flink.entity.AggregRegionSum;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlRegionTestSkin extends RichSinkFunction<ConcurrentHashMap<String, AggregRegionSum>> {
  Connection connection;
  PreparedStatement stmt = null;

  public void getConnection() throws ClassNotFoundException, SQLException {
//    HikariConfigration hikariConfigration = HikariConfigration.getInstance();
//    connection = hikariConfigration.getDs().getConnection();
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
  public void invoke(ConcurrentHashMap<String, AggregRegionSum> value, Context context) throws Exception {
    value.forEach((k,v) ->{
      try {
        AggregRegionSum aggregLocalSum = queryAggregLocalSum(v);
        if (aggregLocalSum == null) {
            saveAggregLocalSum(v);
        } else {
          aggregLocalSum.setSum(aggregLocalSum.getSum()+ v.getSum());
          updateAggregLocalSum(aggregLocalSum);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  public void updateAggregLocalSum(AggregRegionSum value) throws SQLException {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("update aggreg_region_sum t");
    stringBuffer.append(" set t.country='").append(value.getCountry()).append("'");
    stringBuffer.append(",t.city='").append(value.getCity()).append("'");
    stringBuffer.append(",t.province='").append(value.getProvince()).append("'");
    stringBuffer.append(",t.operators='").append(value.getOperators()).append("'");
    stringBuffer.append(",t.sum=").append(value.getSum());
    stringBuffer.append(" where t.id=").append(value.getId());
    stmt = connection.prepareStatement(stringBuffer.toString());
    stmt.executeUpdate();
  }

  public void saveAggregLocalSum(AggregRegionSum value) throws SQLException {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("insert into aggreg_region_sum(country,city,province,sum,operators)");
    stringBuffer.append("values (?,?,?,?,?)");
    stmt = connection.prepareStatement(stringBuffer.toString());
    stmt.setString(1, value.getCountry());
    stmt.setString(2, value.getCity());
    stmt.setString(3, value.getProvince());
    stmt.setInt(4, value.getSum());
    stmt.setString(5, value.getOperators());
    stmt.executeUpdate();
  }

  public AggregRegionSum queryAggregLocalSum(AggregRegionSum value) throws SQLException {
    AggregRegionSum aggregLocalSum = null;
    String sql = "select t.id,t.country,t.city,t.province,t.sum,t.operators "
        + "from aggreg_region_sum t "
        + " where t.country='"+value.getCountry()+"'"
        + " and t.city='"+value.getCity()+"'"
        + " and t.province='"+value.getProvince()+"'"
        + " and t.operators='"+value.getOperators()+"'";

    stmt = connection.prepareStatement(sql);
    ResultSet resultSet = stmt.executeQuery();
    while (resultSet.next()){
      aggregLocalSum = new AggregRegionSum();
      aggregLocalSum.setCountry(resultSet.getString("country"));
      aggregLocalSum.setCity(resultSet.getString("city"));
      aggregLocalSum.setProvince(resultSet.getString("province"));
      aggregLocalSum.setOperators(resultSet.getString("operators"));
      aggregLocalSum.setSum(resultSet.getInt("sum"));
      aggregLocalSum.setId(resultSet.getInt("id"));
    }
    // try 里释放掉 测试demo 随意写
    stmt.close();
    resultSet.close();
    return aggregLocalSum;
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
