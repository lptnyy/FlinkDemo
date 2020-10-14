package com.flink.ip;
import com.flink.entity.IpEntity;
import java.io.FileNotFoundException;
import java.io.IOException;
public class IpUtil {
   static IpUtil ipUtil = null;
   static Okhttp okhttp = Okhttp.getInstance();
   static final String ipHttp = "http://192.168.1.146:30011/ip?ip=";
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

  /**
   * 初始化ip
   * @return
   * @throws FileNotFoundException
   */
   private IpUtil init() {
     return this;
   }

  /**
   * 获取ip信息
   * @param ip
   * @return
   * @throws IOException
   */
   public IpEntity getIpInfo(String ip) throws IOException {
     ip = okhttp.get(null, ipHttp+ip);
     return new IpEntity(ip);
   }
}
