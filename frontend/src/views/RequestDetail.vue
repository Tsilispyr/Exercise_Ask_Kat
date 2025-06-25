<template>
  <div v-if="request">
    <h2>Λεπτομέρειες Αίτησης</h2>
    <div>
      <strong>Όνομα:</strong> {{ request.name }}<br />
      <strong>Είδος:</strong> {{ request.type }}<br />
      <strong>Φύλο:</strong> {{ request.gender }}<br />
      <strong>Ηλικία:</strong> {{ request.age }}<br />
    </div>
    <div>
      <button v-if="hasRole('ADMIN')" @click="adminApprove">Admin Approve</button>
      <button v-if="hasRole('DOCTOR') || hasRole('ADMIN')" @click="doctorApprove">Doctor Approve</button>
      <button @click="deleteRequest">Διαγραφή</button>
      <button @click="$router.back()">Επιστροφή</button>
    </div>
  </div>
  <div v-else>
    <p>Φόρτωση...</p>
  </div>
</template>

<script>
export default {
  data() {
    return { request: null }
  },
  mounted() {
    const id = this.$route.params.id
    fetch(`http://localhost:8080/Request/${id}`, {
      headers: { Authorization: `Bearer ${this.$keycloak.token}` }
    })
      .then(r => r.json())
      .then(data => (this.request = data))
      .catch(() => alert('Σφάλμα ανάκτησης αίτησης'))
  },
  methods: {
    adminApprove() {
      fetch(`http://localhost:8080/Request/Approve/${this.request.id}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${this.$keycloak.token}` }
      })
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα admin έγκρισης'))
    },
    doctorApprove() {
      fetch(`http://localhost:8080/Request/ApproveD/${this.request.id}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${this.$keycloak.token}` }
      })
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα doctor έγκρισης'))
    },
    deleteRequest() {
      fetch(`http://localhost:8080/Request/Delete/${this.request.id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${this.$keycloak.token}` }
      })
        .then(() => this.$router.push('/requests'))
        .catch(() => alert('Σφάλμα διαγραφής'))
    },
    reload() {
      this.mounted()
    },
    hasRole(role) {
      return this.$keycloak.tokenParsed.realm_access.roles.includes(role)
    }
  }
}
</script> 