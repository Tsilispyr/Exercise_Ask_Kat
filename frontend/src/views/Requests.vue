<template>
  <div>
    <h2>Αιτήσεις Υιοθεσίας</h2>
    <router-link v-if="hasRole('ADMIN') || hasRole('SHELTER')" to="/animals/add" class="btn">Add New Pet</router-link>
    <table v-if="requests.length > 0" class="table">
      <thead>
        <tr>
          <th>Όνομα</th>
          <th>Είδος</th>
          <th>Φύλο</th>
          <th>Ηλικία</th>
          <th>Ενέργειες</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in requests" :key="r.id">
          <td>{{ r.name }}</td>
          <td>{{ r.type }}</td>
          <td>{{ r.gender }}</td>
          <td>{{ r.age }}</td>
          <td>
            <router-link :to="`/requests/${r.id}`" class="btn">Λεπτομέρειες</router-link>
            <button v-if="hasRole('ADMIN')" @click="adminApprove(r.id)">Admin Approve</button>
            <button v-if="hasRole('DOCTOR') || hasRole('ADMIN')" @click="doctorApprove(r.id)">Doctor Approve</button>
            <button @click="deleteRequest(r.id)">Διαγραφή</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else>Καμία αίτηση βρέθηκε.</div>
  </div>
</template>

<script>
export default {
  data() {
    return { requests: [] }
  },
  mounted() {
    const kc = this.$keycloak
    fetch('http://localhost:8080/Request', {
      headers: { Authorization: `Bearer ${kc.token}` }
    })
      .then(r => r.json())
      .then(data => (this.requests = data))
      .catch(() => alert('Σφάλμα ανάκτησης αιτήσεων'))
  },
  methods: {
    adminApprove(id) {
      fetch(`http://localhost:8080/Request/Approve/${id}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${this.$keycloak.token}` }
      })
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα admin έγκρισης'))
    },
    doctorApprove(id) {
      fetch(`http://localhost:8080/Request/ApproveD/${id}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${this.$keycloak.token}` }
      })
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα doctor έγκρισης'))
    },
    deleteRequest(id) {
      fetch(`http://localhost:8080/Request/Delete/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${this.$keycloak.token}` }
      })
        .then(() => this.reload())
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
