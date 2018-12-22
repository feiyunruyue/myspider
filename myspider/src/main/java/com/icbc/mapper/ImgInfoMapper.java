package com.icbc.mapper;

import com.icbc.model.ImgInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import java.util.List;

public interface ImgInfoMapper {

    @Insert("insert into img_info (url, title) values (#{url}, #{title})")
    @Options(useGeneratedKeys = true)
    int insert(ImgInfo info);

    int insertBatch(List<ImgInfo> list);
}
