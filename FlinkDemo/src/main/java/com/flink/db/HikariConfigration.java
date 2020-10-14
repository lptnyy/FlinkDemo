package com.flink.db;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariConfigration {

  HikariDataSource ds;
  static HikariConfigration hikariConfigration;
  public static HikariConfigration getInstance(){
      if (hikariConfigration == null) {
        synchronized (HikariConfigration.class){
          if (hikariConfigration == null) {
            return new HikariConfigration().init();
          }
        }
      }
      return hikariConfigration;
  }

  private HikariConfigration init(){
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:mysql://192.168.1.144:3306/big_data_test?useSSL=false");
    config.setUsername("root");
    config.setPassword("123456");
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    config.addDataSourceProperty("maxPoolSize","100");
    config.addDataSourceProperty("minIdle", "20");
    ds = new HikariDataSource(config);
    return this;
  }

  public HikariDataSource getDs() {
    return ds;
  }

  public void setDs(HikariDataSource ds) {
    this.ds = ds;
  }
}
