package com.z.file;

import com.z.file.config.FileStorageAutoConfiguration;
import com.z.file.config.FileStorageProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 可以在启动类加入此注解 启用文件存储，会自动根据配置文件进行加载
 *  已过时，可以不加入此注解。
 *  resources/META-INF/spring.factories 已配置
 *
 * @author zchao
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({FileStorageAutoConfiguration.class, FileStorageProperties.class})
@Deprecated
public @interface EnableFileStorage {
}
