/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/views/layout/Layout'

const _import = require('../_import_' + process.env.NODE_ENV)

const businessRouter = {
  path: '/coreBusiness',
  component: Layout,
  redirect: '/coreBusiness/manager',
  name: '业务模块',
  meta: {
    title: '业务模块',
    icon: 'excel'
  },
  children: [
    {
      path: 'manager',
      name: '业务管理',
      component: _import('business/manager/index'),
      meta: {
        title: '业务管理'
      },
    },
    {
      path: 'content',
      component: _import('business/core/index'),
      name: '核心业务',
      meta: {
        title: '核心业务',
      },
    }
  ]
}


export default businessRouter
