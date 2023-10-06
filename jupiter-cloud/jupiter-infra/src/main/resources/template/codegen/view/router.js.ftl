/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const ${package.ModuleName}Router = {
  path: '/${package.ModuleName}',
  component: Layout,
  redirect: '${controllerMappingHyphen}',
  name: '${package.ModuleName}Manage',
  meta: {
    title: '${table.comment}管理',
    icon: 'table'
  },
  children: [
    {
      path: '${controllerMappingHyphen}',
      component: () => import('@/views/${package.ModuleName}/${entity?lower_case}'),
      name: '${entity}',
      meta: { title: '${table.comment}', icon: 'list' }
    }
  ]
}
export default ${package.ModuleName}Router
