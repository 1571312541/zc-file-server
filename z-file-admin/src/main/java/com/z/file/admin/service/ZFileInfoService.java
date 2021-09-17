package com.z.file.admin.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.z.file.admin.mapper.ZFileInfoMapper;
import com.z.file.admin.model.ZFileInfo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.z.file.FileInfo;
import com.z.file.recorder.FileRecorder;

/**
 * 用来将文件上传记录保存到数据库，这里使用了 MyBatis-Plus 和 Hutool 工具类
 */
@Service
public class ZFileInfoService extends ServiceImpl<ZFileInfoMapper, ZFileInfo> implements FileRecorder {


    /**
     * 保存文件信息到数据库
     */
    @Override
    public boolean record(FileInfo info) {
        ZFileInfo detail = BeanUtil.copyProperties(info, ZFileInfo.class);
        boolean b = save(detail);
        if (b) {
            info.setId(detail.getId());
        }
        return b;
    }

    /**
     * 根据 url 查询文件信息
     */
    @Override
    public FileInfo getByUrl(String url) {
        return BeanUtil.copyProperties(getOne(new QueryWrapper<ZFileInfo>().eq("url",url)),FileInfo.class);
    }

    /**
     * 根据 url 删除文件信息
     */
    @Override
    public boolean delete(String url) {
        return remove(new QueryWrapper<ZFileInfo>().eq("url",url));
    }
}


