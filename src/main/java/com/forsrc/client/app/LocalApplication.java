package com.forsrc.client.app;

import com.forsrc.client.common.constant.ConfigForsrc;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.util.Arrays;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class})
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.forsrc.client.*", "com.forsrc.**.*"})
@MapperScan({"com.forsrc.**.dao"})
@Slf4j
public class LocalApplication {

  public static void main(String[] args) {
    log.info("application start.");
    ConfigurableApplicationContext applicationContext = null;
    SpringApplication springApplication = new SpringApplication(LocalApplication.class);
    applicationContext = springApplication.run(args);
    try {
      //
      Environment env = applicationContext.getEnvironment();
      String version = ConfigForsrc.VERSION;
      String appName = env.getProperty("spring.application.name");
      String profile = Arrays.toString(env.getActiveProfiles());
      String host = InetAddress.getLocalHost().getHostAddress();
      String port = env.getProperty("server.port");
      String protocol = "http";
      if (env.getProperty("server.ssl.key-store") != null) {
        protocol = "https";
      }
      String msg = "\n";
      msg += "===================================================================================================\n";
      msg += "                                      -- Forsrc Client " + version + " --\n";
      msg += "    Application start ok.\n";
      msg += "    App:     " + appName + "\n";
      msg += "    Profile: " + profile + "\n";
      msg += "    Url:     " + protocol + "://" + host + ":" + port + "\n";
      msg += "===================================================================================================";
      log.info(msg);
    } catch (Exception ignored) {
    } finally {
      applicationContext.close();
    }
  }
}
