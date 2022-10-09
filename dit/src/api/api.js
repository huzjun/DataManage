import service from '@/utils/api'


export function fetchList(query) {
  return service({
    url:'/api/',
    method:'get',
    params:query
  })
}

export function gets() {
  return service({
    url: '/api/all',
    method: 'get'
  })
}


export function get(id) {
  return service({
    url: '/api/'+id,
    method: 'get'
  })
}

export function add(api) {
  return service.post('/api/',api)
}

export function save(api) {
  return service.put('/api/',api)
}

export function del(id) {
  return service({
    url: '/api/'+id,
    method: 'delete'
  })
}

