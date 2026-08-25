import { createRouter, createWebHistory } from 'vue-router'
import LoginVue from '@/views/LoginVue.vue'
import NotFoundVue from '@/views/NotFound.vue'
import IndexVue from '@/views/IndexVue.vue'
import DishType from '@/views/DishType.vue'
import ResetPassword from '@/views/ResetPassword.vue'
import DishManager from '@/views/DishManager.vue'
import UserInfo from '@/views/UserInfo.vue'
import UserChangePhoto from '@/views/UserChangePhoto.vue'
import VoucherManger from '@/views/VoucherManger.vue'
import SetMealManager from '@/views/SetMealManager.vue'
import SetMealDetail from '@/views/SetMealDetail.vue'
import BannnerManager from '@/views/BannnerManager.vue'
import OrderManager from '@/views/OrderManager.vue'
import DataShow from '@/views/DataShow.vue'
import ChatWithUsers from '@/views/ChatWithUsers.vue'
const routes = [
  {
    path: '/',
    name: 'home',
    component: LoginVue
  },
  {
    path: '/login',
    name: 'LoginVue',
    component: LoginVue
  },
  {
    path: '/index',
    name: "IndexVue",
    component: IndexVue,
    children: [
      {
        path: '/dish/type',
        component: DishType,
      },
      {
        path: '/dish/manager',
        component: DishManager,
      },
      {
        path: '/user/resetpassword',
        component: ResetPassword
      },
      {
        path: '/user/changephoto',
        component: UserChangePhoto,
      },
      {
        path: '/user/info',
        component: UserInfo
      },
      {
        path: '/voucher/manager',
        component: VoucherManger,
      },
      {
        path: '/banner/manager',
        component: BannnerManager
      },
      {
        path: '/setmeal/manager',
        name: 'SetMealManager',
        component: SetMealManager,
      },
      {
        path: '/setmeal/setmealdetail/:id',
        name: 'SetMealDetail',
        component: SetMealDetail,
      },
      {
        path: '/order/Manager/',
        name: 'OrderManager',
        component: OrderManager,
      },
      {
        path: '/data/show',
        name: 'DataShow',
        component: DataShow
      },
      {
        path: '/chat/chat',
        name: 'ChatWithUsers',
        component: ChatWithUsers
      },
    ]
  },
  {
    path: '/404',
    name: 'NotFoundVue',
    component: NotFoundVue
  },
  {
    path: '/:catchAll(.*)',
    redirect: "/404",
  }
]

const routers = createRouter({
  history: createWebHistory(),
  routes
})

export default routers
