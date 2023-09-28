import Cookies from 'js-cookie'

const TokenKey = 'Authorization'

export function getToken() {
  return Cookies.get(TokenKey)
}

// 登录成功后，后端sa-token已存储token到cookies中了
// export function setToken(token) {
//   return Cookies.set(TokenKey, token)
// }

export function removeToken() {
  return Cookies.remove(TokenKey)
}
