package com.cool.biz.base.mapper;

import com.cool.biz.base.entity.MouldFourAttribute;
import com.cool.core.data.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 菜鸟小王子
 * @create 2020-09-28
 */

@Repository
public interface MouldFourAttributeMapper extends BaseRepository<MouldFourAttribute> {

    List<MouldFourAttribute> findList(String id);

}
