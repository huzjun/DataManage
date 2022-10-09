import service from '@/utils/api'


export function exportList(data) {
  return service({
    url: '/eitask/',
    method: 'post',
    data
  })
}

export function diTaskList(data){
    return service({
      url: '/eitask/list',
      method: 'post',
      data
    })
}

export function importList(data) {
  return service({
    url: '/eitask/',
    method: 'put',
    data
  })
}
