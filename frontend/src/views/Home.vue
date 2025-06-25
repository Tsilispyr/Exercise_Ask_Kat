<template>
  <div>
    <h2>Αρχική Σελίδα</h2>
    <p>Καλωσόρισες, {{ keycloak.tokenParsed?.preferred_username }}</p>
  </div>
</template>

<script>
export default {
  computed: {
    keycloak() {
      return this.$keycloak
    }
  }
}
</script>


