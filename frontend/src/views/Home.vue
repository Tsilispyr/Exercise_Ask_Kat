<template>
  <div>
    <h2>Καλώς ήρθατε στην Πλατφόρμα Υιοθεσίας Κατοικίδιων</h2>
    <div v-if="role === 'ADMIN'">
      <router-link to="/admin">Μετάβαση στο Admin Dashboard</router-link>
    </div>
    <div v-else-if="role === 'SHELTER'">
      <router-link to="/shelter">Μετάβαση στο Shelter Dashboard</router-link>
    </div>
    <div v-else-if="role === 'DOCTOR'">
      <router-link to="/doctor">Μετάβαση στο Doctor Dashboard</router-link>
    </div>
    <div v-else>
      <router-link to="/citizen">Μετάβαση στο Citizen Dashboard</router-link>
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    keycloak() {
      return this.$keycloak || {};
    },
    roles() {
      return this.keycloak.tokenParsed?.realm_access?.roles || [];
    },
    role() {
      if (this.roles.includes('ADMIN')) return 'ADMIN';
      if (this.roles.includes('SHELTER')) return 'SHELTER';
      if (this.roles.includes('DOCTOR')) return 'DOCTOR';
      return 'CITIZEN';
    }
  }
}
</script>


