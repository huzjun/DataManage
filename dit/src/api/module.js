import service from '@/utils/api'


export function fetchList(query) {
  return service({
    url:'/module/',
    method:'get',
    params:query
  })
}

export function gets() {
  return service({
    url: '/module/',
    method: 'get'
  })
}


export function get(id) {
  return service({
    url: '/module/'+id,
    method: 'get'
  })
}

export function add(module) {
  return service.post('/module/',module)
}

export function save(module) {
  return service.put('/module/',module)
}

export function del(id) {
  return service({
    url: '/module/'+id,
    method: 'delete'
  })
}

