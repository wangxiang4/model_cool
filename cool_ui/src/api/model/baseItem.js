import request from '@/utils/request'

// 查询模型数据列表
export function listData(query) {
  return request({
    url: '/model/productItem/list',
    method: 'get',
    params: query
  })
}

// 查询模型数据详细
export function getData(dictCode) {
  return request({
    url: '/model/productItem/' + dictCode,
    method: 'get'
  })
}


// 新增模型数据
export function addData(data) {
  return request({
    url: '/model/productItem/save',
    method: 'post',
    data: data
  })
}

// 修改模型数据
export function editData(data) {
  return request({
    url: '/model/productItem/update',
    method: 'put',
    data: data
  })
}

// 删除模型数据
export function delData(dictCode) {
  return request({
    url: '/model/productItem/remove/' + dictCode,
    method: 'delete'
  })
}

