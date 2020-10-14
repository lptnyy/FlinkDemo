package com.flink.test;
import java.io.IOException;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

public class IPTest {
    public static void main(String[] args) throws DbMakerConfigException, IOException {
      //ip
      String ip="59.108.79.80";

      //根据ip进行位置信息搜索
      DbConfig config = new DbConfig();

      //获取ip库的位置（放在src下）（直接通过测试类获取文件Ip2RegionTest为测试类）
      String dbfile = IPTest.class.getResource("/ip2region.db").getPath();

      DbSearcher searcher = new DbSearcher(config, dbfile);

      //采用Btree搜索
      DataBlock block = searcher.btreeSearch(ip);

      //打印位置信息（格式：国家|大区|省份|城市|运营商）
      System.out.println(block.getRegion());
    }
}
