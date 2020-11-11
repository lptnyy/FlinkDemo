package com.fasterar.smart.server.flink.configure;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextUtils implements ApplicationContextAware {

  /**
   * 上下文对象实例
   */
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextUtils.applicationContext = applicationContext;
  }

  public static void init() {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    applicationContext = context;
  }

  /**
   * 获取applicationContext
   *
   * @return
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 通过name获取 Bean.
   *
   * @param name
   * @return
   */
  public static Object getBean(String name) {
    if (applicationContext == null) {
      init();
    }
    return getApplicationContext().getBean(name);
  }

  /**
   * 通过class获取Bean.
   *
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(Class<T> clazz) {
    if (applicationContext == null) {
      init();
    }
    return getApplicationContext().getBean(clazz);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   *
   * @param name
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    if (applicationContext == null) {
      init();
    }
    return getApplicationContext().getBean(name, clazz);
  }
}
