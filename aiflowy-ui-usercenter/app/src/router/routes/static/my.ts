import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    name: 'My',
    path: '/my',
    meta: {
      title: '我的',
      hideInBreadcrumb: true,
    },
    children: [
      {
        name: 'AssetLibrary',
        path: '/assetLibrary',
        component: () => import('#/views/assetLibrary/index.vue'),
        meta: {
          icon: 'svg:asset-library',
          order: 77,
          title: '我的素材',
        },
      },
      {
        name: 'ExecHistory',
        path: '/execHistory',
        component: () => import('#/views/execHistory/index.vue'),
        meta: {
          icon: 'svg:exec-history',
          order: 88,
          title: '执行记录',
        },
      },
      {
        name: 'ExecHistoryDetails',
        path: '/execHistory/:id',
        component: () => import('#/views/execHistory/details/index.vue'),
        meta: {
          title: '执行记录',
          hideInMenu: true,
          hideInTab: true,
          hideInBreadcrumb: true,
          activePath: '/execHistory',
        },
      },
      {
        name: 'PersonalCenter',
        path: '/personalCenter',
        component: () => import('#/views/personalCenter/index.vue'),
        meta: {
          icon: 'svg:people',
          order: 99,
          title: '个人中心',
        },
      },
    ],
  },
];

export default routes;
