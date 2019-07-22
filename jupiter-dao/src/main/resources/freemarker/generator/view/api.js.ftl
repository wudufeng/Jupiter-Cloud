import request from '@/utils/request'

<#assign prefix = '${contextPath!""}/<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>' />
export function get${entity}List(data) {
  return request({
    url: '${prefix}/page',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

export function add${entity}(data) {
  return request({
    url: '${prefix}',
    method: 'post',
    params: data
  })
}

export function get${entity}Detail(data) {
  return request({
    url: '${prefix}',
    method: 'get',
    params: data
  })
}

export function update${entity}(data) {
  return request({
    url: '${prefix}',
    method: 'put',
    params: data
  })
}