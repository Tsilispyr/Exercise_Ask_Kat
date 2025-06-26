<template>
  <header class="navbar">
    <h1>🐾 Pet Adoption App</h1>
    <nav class="nav-links">
      <template v-if="isAuthenticated">
        <span class="user-info">
          Είστε συνδεδεμένος ως <b>{{ username }}</b> (<b>{{ mainRole }}</b>)
        </span>
        <router-link to="/">Αρχική</router-link>
        <router-link to="/animals">Ζώα</router-link>
        <router-link to="/requests" v-if="hasRole('ADMIN') || hasRole('USER') || hasRole('doctor') || hasRole('shelter')">Αιτήσεις</router-link>
        <router-link to="/users" v-if="hasRole('ADMIN')">Χρήστες</router-link>
        <router-link to="/admin" v-if="hasRole('ADMIN')">Admin</router-link>
        <router-link to="/doctor" v-if="hasRole('doctor')">Doctor</router-link>
        <router-link to="/shelter" v-if="hasRole('shelter')">Shelter</router-link>
        <button @click="logout">Logout</button>
      </template>
      <template v-else>
        <button @click="register">Register</button>
        <button @click="login">Login</button>
      </template>
    </nav>
  </header>
</template>
<script>
export default {
  computed: {
    keycloak() {
      return this.$keycloak || {};
    },
    isAuthenticated() {
      return this.keycloak.authenticated === true;
    },
    roles() {
      return this.keycloak.tokenParsed?.realm_access?.roles || [];
    },
    username() {
      return this.keycloak.tokenParsed?.preferred_username || this.keycloak.tokenParsed?.email || 'Χρήστης';
    },
    mainRole() {
      // Επιστρέφει τον πρώτο σημαντικό ρόλο (ADMIN, doctor, shelter, USER)
      const priority = ['ADMIN', 'doctor', 'shelter', 'USER'];
      const userRoles = this.roles.map(r => r.toUpperCase());
      for (const p of priority) {
        if (userRoles.includes(p.toUpperCase())) return p;
      }
      return this.roles[0] || '-';
    }
  },
  methods: {
    login() {
      if (this.keycloak.login) {
        this.keycloak.login();
      }
    },
    register() {
      if (this.keycloak.register) {
        this.keycloak.register();
      }
    },
    logout() {
      if (this.keycloak.logout) {
        this.keycloak.logout({ redirectUri: window.location.origin });
      }
    },
    hasRole(role) {
      return this.roles.map(r => r.toLowerCase()).includes(role.toLowerCase());
    }
  }
}
</script> 