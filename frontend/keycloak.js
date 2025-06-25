import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'http://localhost:8081/',
  realm: 'petsystem',
  clientId: 'frontend-vue'
});

export default keycloak;
