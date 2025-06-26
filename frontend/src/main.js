import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import keycloak from './keycloak'

keycloak.init({ onLoad: 'check-sso', checkLoginIframe: false }).then(authenticated => {
  const app = createApp(App)
  app.config.globalProperties.$keycloak = keycloak

  // ✅ Προσθήκη route guard
  router.beforeEach((to, from, next) => {
    const roles = keycloak.tokenParsed?.realm_access?.roles || []
    if (to.meta.role && !roles.includes(to.meta.role)) {
      return next('/') // ή προς κάποια forbidden σελίδα αν φτιάξουμε
    }
    next()
  })

  app.use(router)
  app.mount('#app')
}).catch(err => {
  console.error('Keycloak init failed', err)
  // Render the app even if Keycloak init fails
  const app = createApp(App)
  app.config.globalProperties.$keycloak = keycloak
  app.use(router)
  app.mount('#app')
})




