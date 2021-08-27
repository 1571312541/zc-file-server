package com.zc.file.admin;

import com.zc.file.admin.config.DynamicFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 〈〉
 *
 * @author zc
 * @date 2021/8/27 0027
 */
@Component
public class FileApplicationRunner implements ApplicationRunner {

    @Autowired
    private DynamicFileStorage dynamicFileStorage;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        dynamicFileStorage.init();
    }
}
