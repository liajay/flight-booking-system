import api from './request.js'

// 航班相关API
export const flightApi = {
  // 统一的航班查询接口（支持所有查询条件）
  queryFlights(params = {}) {
    return api.get('/flights', { params })
  }
}

export default flightApi
