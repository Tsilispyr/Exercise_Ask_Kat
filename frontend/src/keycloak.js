import Keycloak from 'keycloak-js'

const port = window.location.port

let clientId = 'frontend-vue'
if (port === '5173') {
  clientId = 'frontend-dev'
}

const keycloak = new Keycloak({
  url: 'http://localhost:8083/',
  realm: 'petsystem',
  clientId: clientId
})

export default keycloak
