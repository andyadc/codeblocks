package com.andyadc.codeblocks.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author andy.an
 * @since 2018/5/7
 */
@MapperScan({"com.andyadc.codeblocks.mapper"})
@Configuration
public class MyBatisConfig {
}
