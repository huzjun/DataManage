import service from '@/utils/api'


export function fetchList(query) {
  return service({
    url: '/business/',
    method: 'get',
    params: query
  })
}

export function coreBusinessMenu(){
  return service({
    url: "/business/core",
    method: "get"
  })
}

export function businessCategoryMenu(){
  return service({
    url: "/business/category",
    method: "get"
  })
}
