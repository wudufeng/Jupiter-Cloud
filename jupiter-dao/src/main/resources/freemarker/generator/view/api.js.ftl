import request from '@/utils/request'

export function get${entity}List(data) {
  return request({
    url: '${contextPath!""}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/page',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

export function add${entity}(data) {
  return request({
    url: '${contextPath!""}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>',
    method: 'post',
    params: data
  })
}

export function get${entity}Detail(data) {
  return request({
    url: '${contextPath!""}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>',
    method: 'get',
    params: data
  })
}

export function update${entity}(data) {
  return request({
    url: '${contextPath!""}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>',
    method: 'put',
    params: data
  })
}
