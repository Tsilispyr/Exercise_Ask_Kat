<template>
  <div>
    <h2>Χρήστες</h2>
    <ul>
      <li v-for="u in users" :key="u.id">
        {{ u.username }} — {{ u.email }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return { users: [] }
  },
  mounted() {
    const kc = this.$keycloak
    fetch('http://localhost:8080/users', {
  headers: {
    Authorization: `Bearer ${this.$keycloak.token}`
  }
})
      .then(r => r.json())
      .then(data => (this.users = data))
      .catch(() => alert('Σφάλμα ανάκτησης χρηστών'))
  }
}
</script>
