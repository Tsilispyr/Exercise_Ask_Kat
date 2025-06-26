<template>
  <div>
    <h2>Pending Requests</h2>
    <ul>
      <li v-for="req in requests" :key="req.id">
        {{ req.animalName }} - {{ req.status }}
        <button v-if="canApprove" @click="approve(req.id)">Approve</button>
      </li>
    </ul>
  </div>
</template>
<script>
import api from '../api';
export default {
  data() { return { requests: [] }; },
  computed: {
    canApprove() {
      const roles = this.$keycloak.tokenParsed?.realm_access?.roles || [];
      return roles.includes('ADMIN') || roles.includes('DOCTOR') || roles.includes('SHELTER');
    }
  },
  async mounted() {
    const res = await api.get('/requests/pending');
    this.requests = res.data;
  },
  methods: {
    async approve(id) {
      await api.post(`/requests/${id}/approve`);
      this.requests = this.requests.filter(r => r.id !== id);
    }
  }
}
</script> 