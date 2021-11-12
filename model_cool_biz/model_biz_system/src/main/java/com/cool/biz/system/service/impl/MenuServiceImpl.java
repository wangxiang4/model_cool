package com.cool.biz.system.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.Menu;
import com.cool.biz.system.mapper.MenuMapper;
import com.cool.biz.system.service.MenuService;
import com.cool.biz.system.vo.MenuVo;
import com.cool.biz.system.vo.MetaVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *<p>
 *菜单权限表 服务实现类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper,Menu> implements MenuService {

    /**
     * 通过角色编号查询权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuListByRoleId(Integer roleId) {
        return baseMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param roleId 角色ID
     * @return 菜单信息
     */
    @Override
    public List<Integer> selectMenusByRoleId(Integer roleId) {
        return baseMapper.selectMenusByRoleId(roleId);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<MenuVo> buildMenus(List<Menu> menus) {
        List<MenuVo> menuVoList = menus.stream().map(menu -> {
            MenuVo menuVo = new MenuVo();
            menuVo.setName(StrUtil.upperFirst(menu.getPath()));
            if (menu.getParentId() == 0 && !StrUtil.contains(menu.getPath(), "http")) {
                menuVo.setPath("/" + menu.getPath());
            } else {
                menuVo.setPath(menu.getPath());
            }
            menuVo.setHidden("1".equals(menu.getStatus()));
            menuVo.setComponent(menu.getComponent());
            menuVo.setMeta(MetaVo.builder().title(menu.getName()).icon(menu.getIcon()).noCache("0".equals(menu.getNoCache()) ? true : false).build());
            List<Menu> cMenus = menu.getChildren();
            if (cMenus != null && cMenus.size() > 0 && "M".equals(menu.getType())) {
                menuVo.setAlwaysShow(true);
                menuVo.setRedirect("noRedirect");
                menuVo.setChildren(buildMenus(cMenus));
            } else {
                menuVo.setAlwaysShow(false);
            }
            return menuVo;
        }).collect(Collectors.toList());
        return menuVoList;
    }

    /**
     * 构建树
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    @Override
    public List<Menu> buildTree(List<Menu> list, int parentId) {
        List<Menu> menuList = new ArrayList<>();
        for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext(); ) {
            Menu t = iterator.next();
            if (t.getParentId().intValue() == parentId) {
                recursion(list, t);
                menuList.add(t);
            }
        }
        return menuList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursion(List<Menu> list, Menu t) {
        // 得到子节点列表
        List<Menu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Menu tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<Menu> it = childList.iterator();
                while (it.hasNext()) {
                    Menu n = it.next();
                    recursion(list, n);
                }
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Menu> list, Menu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 得到子节点列表
     */
    private List<Menu> getChildList(List<Menu> list, Menu t) {
        List<Menu> tlist = new ArrayList();
        Iterator<Menu> it = list.iterator();
        while (it.hasNext()) {
            Menu n = it.next();
            if (n.getParentId().intValue() == t.getId().intValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

}
