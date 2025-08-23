import api from './request.js'

// 航班相关API
export const flightApi = {
  // 查询所有航班（分页）
  queryFlights(params = {}) {
    return api.get('/inventory/api/flights', { params })
  },

  // 根据航班号查询航班详情
  getFlightByNumber(flightNumber) {
    return api.get(`/inventory/api/flights/${flightNumber}`)
  },

  // 根据路线查询航班
  queryFlightsByRoute(departureCity, arrivalCity, params = {}) {
    return api.get('/inventory/api/flights/route', {
      params: {
        departureCity,
        arrivalCity,
        ...params
      }
    })
  },

  // 根据航空公司查询航班
  queryFlightsByAirline(airline, params = {}) {
    return api.get(`/inventory/api/flights/airline/${airline}`, { params })
  },

  // 根据日期范围查询航班
  queryFlightsByDateRange(params = {}) {
    return api.get('/inventory/api/flights/date-range', { params })
  },

  // 获取有效航班
  queryActiveFlights(params = {}) {
    return api.get('/inventory/api/flights/active', { params })
  }
}

export default flightApi
