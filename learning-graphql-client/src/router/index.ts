import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/chapter01',
    name: 'chapter01',
    component: ()=> import(/* webpackChunkName: "chapters" */ '../views/chapter01-入门.vue')
  },
  {
    path: '/chapter02',
    name: 'chapter02',
    component: ()=> import(/* webpackChunkName: "chapters" */ '../views/chapter02-Schema和类型.vue')
  },
  {
    path: '/chapter03',
    name: 'chapter03',
    component: ()=> import(/* webpackChunkName: "chapters" */ '../views/chapter03-查询和变更.vue')
  },
  {
    path: '/chapter04',
    name: 'chapter04',
    component: ()=> import(/* webpackChunkName: "chapters" */ '../views/chapter04-验证.vue')
  },
  {
    path: '/chapter05',
    name: 'chapter05',
    component: ()=> import(/* webpackChunkName: "chapters" */ '../views/chapter05-执行.vue')
  },
  {
    path: '/chapter06',
    name: 'chapter06',
    component: ()=> import(/* webpackChunkName: "chapters" */ '../views/chapter06-内省.vue')
  },
  {
    path: '/chapter07',
    name: 'chapter07',
    component: ()=> import(/* webpackChunkName: "chapters" */ '../views/chapter07-最佳实践.vue')
  },
  {
    path: '/chapter99',
    name: 'chapter99',
    component: ()=> import(/* webpackChunkName: "chapters" */ '../views/chapter99-实践.vue')
  }

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
