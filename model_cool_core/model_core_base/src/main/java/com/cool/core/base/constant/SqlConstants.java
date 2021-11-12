package com.cool.core.base.constant;


/**
* @Author: 菜王
* @Date: 2020/11/3
* sql常量脚本
*/
public interface SqlConstants {

    String QUERY_USER = "select id, user_name userName, nick_name nickName,password,status from sys_user where user_name = ?";

    String QUERY_ROLES = "select role_id roleId from sys_user_role where user_id = ?";

    String QUERY_PREMS = "select m.perms from sys_menu m left join sys_role_menu rm on rm.menu_id = m.id where rm.role_id = ? and m.type = 'F' and m.perms is not null";

    String OPER_LOG = "insert into sys_oper_log (type, title, method, user_agent, oper_name, client_id, oper_url, oper_ip, oper_addr, oper_param, status, error_msg, execute_time, oper_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    String LOGIN_LOG = "insert into sys_login_log (login_name, login_type, login_ip, login_addr, user_agent, status, msg, login_time) values (?,?,?,?,?,?,?,?)";

    String ROLE_DATASCOPE = "select data_scope dataScope from sys_role where id = ?";

    String ADD_FILEINFO = "insert into sys_file_info (name, type, format, size, path, create_by,create_time) values (?,?,?,?,?,?,?)";

    String MENU_TREE = "select id, name, parent_id parentId from sys_menu where type != 'F' and del_flag = '0'";
}
