<template>
  <div>
    <h2>Adoption Requests</h2>
    <ul>
      <li v-for="req in adoptionRequests" :key="req.id">
        {{ req.animalName }} - {{ req.status }}
        <button v-if="canApprove" @click="approve(req.id)">Approve</button>
      </li>
    </ul>
  </div>
</template>
<script>
import api from '../api';
export default {
  data() { return { adoptionRequests: [] }; },
  computed: {
    canApprove() {
      const roles = this.$keycloak.tokenParsed?.realm_access?.roles?.map(r => r.toLowerCase()) || [];
      return roles.includes('admin') || roles.includes('shelter');
    }
  },
  async mounted() {
    const res = await api.get('/adoptions/pending');
    this.adoptionRequests = res.data;
  },
  methods: {
    async approve(id) {
      await api.post(`/adoptions/${id}/approve`);
      this.adoptionRequests = this.adoptionRequests.filter(r => r.id !== id);
    }
  }
}
</script> 