package com.fasterar.smart.server.flink.skin;
import com.fasterar.smart.server.flink.entity.AggregLocalSum;
import com.fasterar.smart.server.flink.utils.RedissonUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlLocalTestSkin extends RichSinkFunction<ConcurrentHashMap<String, AggregLocalSum>> {
  Connection connection;
  PreparedStatement stmt = null;
  RedissonUtil redissonUtil;

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
  public void invoke(ConcurrentHashMap<String, AggregLocalSum> value, Context context) throws Exception {
    value.forEach((k,v) ->{
      try {
        redissonUtil.lock(k, 1);
        AggregLocalSum aggregLocalSum = queryAggregLocalSum(v);
        if (aggregLocalSum == null) {
            saveAggregLocalSum(v);
        } else {
          aggregLocalSum.setSum(aggregLocalSum.getSum()+ v.getSum());
          updateAggregLocalSum(aggregLocalSum);
        }
        redissonUtil.unlock(k);
      } catch (SQLException e) {
        redissonUtil.unlock(k);
        e.printStackTrace();
      }
    });
  }

  public void updateAggregLocalSum(AggregLocalSum value) throws SQLException {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("update aggreg_local_sum t");
    stringBuffer.append(" set t.uid=").append(value.getUid());
    stringBuffer.append(",t.sum=").append(value.getSum());
    stringBuffer.append(",t.ip='").append(value.getIp()).append("'");
    stringBuffer.append(" where t.id=").append(value.getId());
    stmt = connection.prepareStatement(stringBuffer.toString());
    stmt.executeUpdate();
  }

  public void saveAggregLocalSum(AggregLocalSum value) throws SQLException {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("insert into aggreg_local_sum(ip,sum,uid)");
    stringBuffer.append("values (?,?,?)");
    stmt = connection.prepareStatement(stringBuffer.toString());
    stmt.setString(1, value.getIp());
    stmt.setInt(2, value.getSum());
    stmt.setInt(3, value.getUid());
    stmt.executeUpdate();
  }

  public AggregLocalSum queryAggregLocalSum(AggregLocalSum value) throws SQLException {
    AggregLocalSum aggregLocalSum = null;
    String sql = "select t.id,t.uid,t.ip,t.sum from aggreg_local_sum t where t.ip ='"+value.getIp()+"' and "+ "t.uid="+value.getUid();
    stmt = connection.prepareStatement(sql);
    ResultSet resultSet = stmt.executeQuery();
    while (resultSet.next()){
      aggregLocalSum = new AggregLocalSum();
      aggregLocalSum.setUid(resultSet.getInt("uid"));
      aggregLocalSum.setIp(resultSet.getString("ip"));
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
