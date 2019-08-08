import request from '@/utils/request'

export function get${entity}List(data) {
  return request({
    url: '${contextPath!""}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/list',
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

export function get${entity}Detail(id) {
  return request({
    url: '${contextPath!""}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/' + id,
    method: 'get'
  })
}

export function update${entity}(data) {
  return request({
    url: '${contextPath!""}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>',
    method: 'put',
    params: data
  })
}

export function remove${entity}(id) {
  return request({
    url: '${contextPath!""}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/' + id,
    method: 'delete'
  })
}
