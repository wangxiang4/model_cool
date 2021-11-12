import request from '@/utils/request'

//初始化
export function InitData() {
  return request({
    url: '/model/base',
    method: 'get'
  })
}

//查询分类列表
export function listClass(query) {
  return request({
    url: '/model/base/class/list',
    method: 'get',
    params: query
  })
}

//查询型号列表
export function listType(query) {
  return request({
    url: '/model/base/type/list',
    method: 'get',
    params: query
  })
}

//查询属性列表
export function listAttribute(query) {
  return request({
    url: '/model/base/attribute/list',
    method: 'get',
    params: query
  })
}
