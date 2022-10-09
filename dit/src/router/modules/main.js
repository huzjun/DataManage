/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/views/layout/Layout'

const _import = require('../_import_' + process.env.NODE_ENV)

const mainRouter = {
    path: '/main',
    component: Layout,
    redirect: '/main/business',
    name: '首页配置',
    meta: {
      title: '首页管理',
      icon: "example"
    },
    children: [
      {
        path: 'business',
        name: '栏目中心',
        component: _import('tree/index'),
        meta: {
          title: '栏目中心'
        }
      },
      {
        path: 'partycommand',
        name: '党建统领',
        component: _import('layout/InnerLayout'),
        redirect: '/main/partycommand/leaderIndicate',
        meta: {
          title: '党建统领'
        },
        children: [
          {
            path: 'leaderIndicate',
            name: '领导批示',
            component: _import('tree/index'),
            meta: {
              title: '领导批示'
            }
          },
          {
            path: 'focusWork',
            name: '重点工作',
            component: _import('tree/index'),
            meta: {
              title: '重点工作'
            }
          },
          {
            path: 'statement',
            name: '七张清单',
            component: _import('tree/index'),
            meta: {
              title: '七张清单'
            }
          }
        ]
      }
    ]
  }


  export default mainRouter
