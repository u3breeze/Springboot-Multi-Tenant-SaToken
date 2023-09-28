import Cookies from 'js-cookie'

const TokenKey = 'Authorization'

export function getToken() {
  return Cookies.get(TokenKey)
}
