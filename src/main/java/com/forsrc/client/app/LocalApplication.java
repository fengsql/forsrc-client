package com.forsrc.client.app;

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
import java.net.UnknownHostException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class})
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.forsrc.client.*", "com.forsrc.**.*"})
@MapperScan({"com.forsrc.**.dao"})
@Slf4j
public class LocalApplication {

  public static void main(String[] args) {
    log.info("application start.");
    ConfigurableApplicationContext applicationContext = null;
    try {
      SpringApplication springApplication = new SpringApplication(LocalApplication.class);

      applicationContext = springApplication.run(args);
      //
      Environment env = applicationContext.getEnvironment();
      String protocol = "http";
      if (env.getProperty("server.ssl.key-store") != null) {
        protocol = "https";
      }

      log.info("\n----------------------------------------------------------\n\t" +
          "Application '{}' is running! Access URLs:\n\t" +
          "Local: \t\t{}://localhost:{}\n\t" +
          "Url: \t{}://{}:{}\n\t" +
          "Profile(s): \t{}\n----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        protocol,
        env.getProperty("server.port"),
        protocol,
        InetAddress.getLocalHost().getHostAddress(),
        env.getProperty("server.port"),
        env.getActiveProfiles());

      log.info("init application.");
    } catch (UnknownHostException ignored) {
      log.error("LocalApplication error!");
    } finally {
      applicationContext.close();
    }
  }
}
