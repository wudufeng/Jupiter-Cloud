/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const ${package.ModuleName}Router = {
  path: '/${package.ModuleName}',
  component: Layout,
  redirect: '/${controllerMappingHyphen}/list',
  name: '${package.ModuleName}Manage',
  meta: {
    title: '${table.comment}管理',
    icon: 'table'
  },
  children: [
    {
      path: '${controllerMappingHyphen}/list',
      component: () => import('@/views/${package.ModuleName}/${entity}'),
      name: '${entity}',
      meta: { title: '${table.comment}列表', icon: 'list' }
    }
  ]
}
export default ${package.ModuleName}Router
