package com.cool.auth.handler;

import com.cool.core.data.handler.BaseMetaObjectHandler;
import com.cool.core.security.util.SecurityUtil;
import org.springframework.stereotype.Component;

/**
 * @author 菜王
 * @create 2020-11-06
 */
@Component
public class MetaObjectHandler extends BaseMetaObjectHandler {

    @Override
    protected Object getUserName() {
        try {
            return SecurityUtil.getUser().getUsername();
        } catch (Exception e) {
            return "";
        }
    }

}
