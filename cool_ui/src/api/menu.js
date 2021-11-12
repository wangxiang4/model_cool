import request from '@/utils/request'

// 获取菜单
export const getMenus = () => {
  return request({
    url: '/system/menu',
    method: 'get'
  })
}
