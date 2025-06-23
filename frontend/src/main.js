import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import keycloak from './keycloak'

const app = createApp(App)

keycloak.init({
  onLoad: 'login-required',
  checkLoginIframe: false, // για localhost ώστε να μην κολλάει η login ροή
  pkceMethod: 'S256'       // σύγχρονη πρακτική ασφαλούς login
}).then(authenticated => {
  if (authenticated) {
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
  } else {
    window.location.reload()
  }
}).catch(err => {
  console.error('Keycloak init failed', err)
})




