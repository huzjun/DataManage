import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '@/views/layout/Layout'

import partycommandRouter from './modules/partycommand'
import businessRouter from './modules/business'
import userRouter from './modules/user'
import settingRouter from './modules/setting'


const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)

export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: '数据导入工具',
        component: _import('dashboard/index'),
        meta: {
          title: '数据导入工具'
        }
      }
    ]
  },
]
export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  // {
  //   path: '/demo',
  //   component: Layout,
  //   redirect: '/demo/article',
  //   meta: {
  //     icon: 'tree'
  //   },
  //   children: [
  //     {
  //       path: 'article',
  //       name: '文章',
  //       component: _import('article/article'),
  //       meta: {
  //         title: '文章',
  //         icon: 'example'
  //       },
  //       menu: 'article'
  //     }
  //   ]
  // },
  // partycommandRouter, // 首页管理
  businessRouter, // 业务模块
  settingRouter, //系统设置
  userRouter,
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

