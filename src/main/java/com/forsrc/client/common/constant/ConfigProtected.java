package com.forsrc.client.common.constant;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "project.generator.output")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class ConfigProtected {

  private List<String> protectedFilePath;

}