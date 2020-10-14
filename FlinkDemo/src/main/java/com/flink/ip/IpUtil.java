package com.flink.ip;
import com.flink.entity.IpEntity;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

public class IpUtil {

   static IpUtil ipUtil = null;
   public static IpUtil getInstance() {
     if (ipUtil == null) {
       synchronized (IpUtil.class){
         if (ipUtil == null) {
           try {
             return  new IpUtil().init();
           } catch (Exception e){
              e.printStackTrace();
              System.exit(0);
           }
         }
       }
     }
     return ipUtil;
   }

   org.lionsoul.ip2region.DbSearcher searcher = null;

  /**
   * 初始化ip
   * @return
   * @throws DbMakerConfigException
   * @throws FileNotFoundException
   */
   private IpUtil init() throws DbMakerConfigException, FileNotFoundException {
     //根据ip进行位置信息搜索
     org.lionsoul.ip2region.DbConfig config = new DbConfig();
     //获取ip库的位置（放在src下）（直接通过测试类获取文件Ip2RegionTest为测试类）
     String dbfile = IpUtil.class.getResource("/ip2region.db").getPath();
     searcher = new DbSearcher(config, dbfile);
     return this;
   }

  /**
   * 获取ip信息
   * @param ip
   * @return
   * @throws IOException
   */
   public IpEntity getIpInfo(String ip) throws IOException {
     //采用Btree搜索
     DataBlock block = searcher.btreeSearch(ip);
     return new IpEntity(block.getRegion());
   }
}
