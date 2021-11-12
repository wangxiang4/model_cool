import request from '@/utils/request'
const scope = 'server'

// 登录方法
export function login(username, password, time, code, realKey) {
  const grant_type = 'password'

  return request({
    url: '/oauth/token',
    headers: {
      isToken:false,
      'Authorization': 'Basic Y29vbDpjb29s'
    },
    method: 'post',
    params: { username, password, time, grant_type, scope, code, realKey }
  })
}

export const refreshToken = (refresh_token) => {
  const grant_type = 'refresh_token'

  return request({
    url: '/oauth/token',
    headers: {
      'isToken': false,
      'Authorization': 'Basic Y29vbDpjb29s',
    },
    method: 'post',
    params: { refresh_token, grant_type, scope }
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/system/user/info',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/token/logout',
    method: 'delete'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captcha/image/'+Date.now(),
    method: 'get'
  })
}
