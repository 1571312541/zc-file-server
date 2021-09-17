package com.z.file.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.z.file.admin.mapper.ZFileConfigMapper;
import com.z.file.admin.model.ZFileConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZFileConfigService extends ServiceImpl<ZFileConfigMapper, ZFileConfig> {

    @Autowired
    private ZFileConfigMapper zFileConfigMapper;

    public List<ZFileConfig> findFileSourceConfs(){
        try {
            return zFileConfigMapper.selectList(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
