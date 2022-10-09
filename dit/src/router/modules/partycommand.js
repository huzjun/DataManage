/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/views/layout/Layout'

const _import = require('../_import_' + process.env.NODE_ENV)

const partycommandRouter =
  {
    path: '/partycommand',
    name: '党建统领',
    component: Layout,
    redirect: '/partycommand/leaderIndicate',
    meta: {
      title: '党建统领',
      icon: 'tree'
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


export default partycommandRouter
