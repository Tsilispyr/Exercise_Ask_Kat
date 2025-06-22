<template>
  <div id="app">
    <header class="navbar">
      <h1>🐾 Pet Adoption App</h1>
      <nav v-if="keycloak.authenticated" class="nav-links">
        <router-link to="/">Αρχική</router-link>
        <router-link to="/animals">Ζώα</router-link>
        <router-link to="/requests">Αιτήσεις</router-link>
        <router-link to="/users">Χρήστες</router-link>
        <router-link to="/admin" v-if="hasRole('ADMIN')">Admin</router-link>
	<router-link to="/doctor" v-if="hasRole('DOCTOR')">Doctor</router-link>
	<router-link to="/shelter" v-if="hasRole('SHELTER')">Shelter</router-link>

        <button @click="logout">Logout</button>
      </nav>
    </header>

    <main>
      <p v-if="keycloak.authenticated">
        Χρήστης: {{ keycloak.tokenParsed.preferred_username }}<br/>
        Ρόλοι: {{ roles.join(', ') }}
      </p>
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  computed: {
    keycloak() {
      return this.$keycloak
    },
    roles() {
      return this.keycloak.tokenParsed.realm_access.roles
    }
  },
  methods: {
    logout() {
      this.keycloak.logout({ redirectUri: window.location.origin })
    },
  hasRole(role) {
    return this.roles.includes(role)
  }
  }
}
</script>

<style scoped>
.navbar {
  background-color: #298;
  color: white;
  padding: 10px 20px;
}
.nav-links {
  display: flex;
  gap: 15px;
  margin-top: 10px;
}
.nav-links a, .nav-links button {
  color: white;
  text-decoration: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 5px 10px;
}
.nav-links a:hover, .nav-links button:hover {
  background-color: #555;
}
main {
  margin: 20px;
}
</style>

