import request from '@/utils/request'

// 查询在线用户列表
export function list(query) {
  return request({
    url: '/monitor/online/list',
    method: 'get',
    params: query
  })
}

// 强退用户
export function forceLogout(userId, device) {
  return request({
    url: '/monitor/online/' + userId + '/' + device,
    method: 'delete'
  })
}
