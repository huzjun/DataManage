/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/views/layout/Layout'

const _import = require('../_import_' + process.env.NODE_ENV)

const setttingRouter = {
    path: '/setting',
    component: Layout,
    redirect: '/setting/module',
    name: '系统设置',
    meta: {
      title: '系统设置',
      icon: 'tree'
    },
    children: [
      {
        path: 'module',
        component: _import('setting/module'),
        name: '模块管理',
        meta: {
          title: '模块管理'
        },
      },
      {
        path: 'api',
        name: '接口管理',
        component: _import('setting/api'),
        meta: {
          title: '接口管理'
        },
      },
    ]
  }


export default setttingRouter
