/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const ${package.ModuleName}Router = {
  path: '/${package.ModuleName}',
  component: Layout,
  redirect: '/${package.ModuleName}/list',
  name: '${package.ModuleName}Manage',
  meta: {
    title: '${package.ModuleName}',
    icon: 'table'
  },
  children: [
    {
      path: '${table.entityPath}/list',
      component: () => import('@/views/${package.ModuleName}/${entity}-list'),
      name: '${entity}List',
      meta: { title: '${table.comment} List' }
    },
	{
	  path: '${table.entityPath}/create',
	  component: () => import('@/views/${package.ModuleName}/create-${entity}'),
	  name: 'Create${entity}',
	  meta: { title: 'Create ${entity}', icon: 'edit' }
	},
	{
	  path: '${table.entityPath}/edit/:id(\\d+)',
	  component: () => import('@/views/${package.ModuleName}/edit-${entity}'),
	  name: 'Edit${entity}',
	  meta: { title: 'Edit ${entity}', noCache: true, activeMenu: '/${package.ModuleName}/${table.entityPath}/list' },
	  hidden: true
	}
  ]
}
export default ${package.ModuleName}Router
