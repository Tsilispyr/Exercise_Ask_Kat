import { createRouter, createWebHistory } from 'vue-router'
import Home from './views/Home.vue'
import Animals from './views/Animals.vue'
import Requests from './views/Requests.vue'
import Users from './views/Users.vue'
import Admin from './views/Admin.vue'
import Doctor from './views/Doctor.vue'
import Shelter from './views/Shelter.vue'
import AnimalDetail from './views/AnimalDetail.vue'
import AddAnimal from './views/AddAnimal.vue'
import RequestDetail from './views/RequestDetail.vue'
import Citizen from './views/Citizen.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/animals', component: Animals },
  { path: '/requests', component: Requests },
  { path: '/requests/:id', component: RequestDetail },
  { path: '/users', component: Users },
  { path: '/admin', component: Admin },
  { path: '/doctor', component: Doctor },
  { path: '/shelter', component: Shelter },
  { path: '/citizen', component: Citizen },
  { path: '/animals/:id', component: AnimalDetail },
  { path: '/animals/add', component: AddAnimal },
  { path: '/forbidden', component: { template: '<div>Απαγορευμένη Πρόσβαση</div>' } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

