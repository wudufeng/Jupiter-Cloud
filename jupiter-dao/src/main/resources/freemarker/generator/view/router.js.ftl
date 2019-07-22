/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const ${package.ModuleName}Router = {
  path: '/${package.ModuleName}',
  component: Layout,
  redirect: '/${controllerMappingHyphen}/list',
  name: '${package.ModuleName}Manage',
  meta: {
    title: '${package.ModuleName}',
    icon: 'table'
  },
  children: [
    {
      path: '${controllerMappingHyphen}/list',
      component: () => import('@/views/${package.ModuleName}/${entity}List'),
      name: '${entity}List',
      meta: { title: '${table.comment} List' }
    },
    {
      path: '${controllerMappingHyphen}/create',
      component: () => import('@/views/${package.ModuleName}/${entity}Detail'),
      name: 'Create${entity}',
      meta: { title: 'Create ${entity}', icon: 'edit' }
    },
    {
      path: '${controllerMappingHyphen}/edit/:id(\\d+)',
      component: () => import('@/views/${package.ModuleName}/${entity}Detail'),
      name: 'Edit${entity}',
      meta: { title: 'Edit ${entity}', noCache: true, activeMenu: '/${package.ModuleName}/${controllerMappingHyphen}/list' },
      hidden: true
    }
  ]
}
export default ${package.ModuleName}Router
