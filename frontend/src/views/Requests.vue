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
import api from '../api';
export default {
  data() {
    return { requests: [] }
  },
  mounted() {
    api.get('/requests')
      .then(r => (this.requests = r.data))
      .catch(() => alert('Σφάλμα ανάκτησης αιτήσεων'))
  },
  methods: {
    adminApprove(id) {
      api.post(`/requests/${id}/approve`)
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα admin έγκρισης'))
    },
    doctorApprove(id) {
      api.post(`/requests/${id}/approve-doctor`)
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα doctor έγκρισης'))
    },
    deleteRequest(id) {
      api.delete(`/requests/${id}`)
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα διαγραφής'))
    },
    reload() {
      this.mounted()
    },
    hasRole(role) {
      const roles = this.$keycloak.tokenParsed?.realm_access?.roles?.map(r => r.toLowerCase()) || [];
      return roles.includes(role.toLowerCase());
    }
  }
}
</script>
