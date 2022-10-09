import service from '@/utils/api'


export function exportList(data) {
  return service({
    url: '/eitask/',
    method: 'post',
    data
  })
}

export function eiTaskList(data){
    return service({
      url: '/eitask/list',
      method: 'post',
      data
    })
}

export function eiTaskDelete(id){
  return service({
    url: '/eitask/'+id,
    method: 'delete'
  })
}


export function importList(data) {
  return service({
    url: '/eitask/',
    method: 'put',
    data
  })
}
