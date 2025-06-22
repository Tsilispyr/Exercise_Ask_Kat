import { createRouter, createWebHistory } from 'vue-router'
import Home from './views/Home.vue'
import Animals from './views/Animals.vue'
import Requests from './views/Requests.vue'
import Users from './views/Users.vue'
import Admin from './views/Admin.vue'
import Doctor from './views/Doctor.vue'
import Shelter from './views/Shelter.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/animals', component: Animals, meta: { role: 'USER' } },
  { path: '/requests', component: Requests, meta: { role: 'USER' } },
  { path: '/users', component: Users, meta: { role: 'ADMIN' } },
  { path: '/admin', component: Admin, meta: { role: 'ADMIN' } },
  { path: '/doctor', component: Doctor, meta: { role: 'DOCTOR' } },
  { path: '/shelter', component: Shelter, meta: { role: 'SHELTER' } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

