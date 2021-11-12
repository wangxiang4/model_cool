package com.cool.core.data.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author 菜王
 * @create 2020-11-06
 */
public class BaseMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName("createTime", metaObject);
        if (createTime == null) {
            setFieldValByName("createTime", new Date(), metaObject);
        }

        Object createBy = getFieldValByName("createBy", metaObject);
        if (createBy == null) {
            setFieldValByName("createBy", getUserName(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object fieldValue = getFieldValByName("updateTime", metaObject);
        if (fieldValue == null) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        Object updateBy = getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            setFieldValByName("updateBy", getUserName(), metaObject);
        }
    }

    protected Object getUserName() {
        return "";
    }

}
