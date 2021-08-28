package com.zc.file.admin.config;

import com.zc.file.admin.model.ZFileConfig;
import com.zc.file.admin.service.ZFileConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈〉
 *
 * @author zchao
 * @create 2021/8/28
 */
@Slf4j
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private ZFileConfigService zFileConfigService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        List<ZFileConfig> fileSourceConfs = zFileConfigService.findFileSourceConfs();
        if (fileSourceConfs != null && fileSourceConfs.size() > 0) {
            List<ZFileConfig> localConfigs = fileSourceConfs.stream().filter(config -> config.getClientType().equals(ZcFileCons.SourceType.SOURCE_TYPE_LOCAL))
                    .collect(Collectors.toList());
            for (ZFileConfig local : localConfigs) {
                log.info("添加静态资源访问路径{}-{}",local.getPathPatterns(),local.getBasePath());
                registry.addResourceHandler(local.getPathPatterns())
                        .addResourceLocations("file:" + local.getBasePath().replace("\\","/"))
                        .setCachePeriod(0);

            }
        }
    }
}
