import request from '@/utils/request'

// 登录方法
export function login(comcode, username, password, code, uuid) {
  const data = {
    comcode: comcode,
    username: username,
    password: password,
    code: code,
    uuid: uuid,
  }
  return request({
    url: '/login',
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}
