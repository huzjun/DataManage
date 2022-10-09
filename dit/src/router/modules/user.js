/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/views/layout/Layout'

const _import = require('../_import_' + process.env.NODE_ENV)

const userRouter = {
    path: '/user',
    component: Layout,
    redirect: '/user/',
    name: '',
    meta: {
      title: '用户权限',
      icon: 'table'
    },
    children: [
      {
        path: '',
        name: '用户列表',
        component: _import(
          'user/user'
        ),
        meta: {
          title: '用户列表',
          icon: 'user'
        },
        menu: 'user'
      },
      {
        path: 'role',
        name: '权限管理',
        component: _import(
          'user/role'
        ),
        meta: {
          title: '权限管理',
          icon: 'password'
        },
        menu: 'role'
      }
    ]
  }


export default userRouter
