<template>
  <div>
    <h2 v-if="isAdminOrDoctor">Pending Adoptions</h2>
    <h2 v-else>My Adoptions</h2>
    <ul>
      <li v-for="req in adoptionRequests" :key="req.id">
        {{ req.animalName || req.name }} - {{ req.status || 'Pending' }}
        <button v-if="canApprove" @click="approve(req.id)">Approve</button>
        <button v-if="canDeny" @click="deny(req.id)">Deny</button>
      </li>
    </ul>
  </div>
</template>
<script>
import api from '../api';
export default {
  data() { return { adoptionRequests: [] }; },
  computed: {
    roles() {
      return (this.$keycloak.tokenParsed?.realm_access?.roles || []).map(r => r.toLowerCase());
    },
    isAdminOrDoctor() {
      return this.roles.includes('admin') || this.roles.includes('doctor');
    },
    isUserOrShelter() {
      return this.roles.includes('user') || this.roles.includes('shelter');
    },
    canApprove() {
      return this.isAdminOrDoctor;
    },
    canDeny() {
      return this.isAdminOrDoctor;
    }
  },
  async mounted() {
    let res;
    if (this.isAdminOrDoctor) {
      res = await api.get('/adoptions/pending');
    } else {
      res = await api.get('/adoptions/mine');
    }
    this.adoptionRequests = res.data;
  },
  methods: {
    async approve(id) {
      await api.post(`/adoptions/${id}/approve`);
      this.adoptionRequests = this.adoptionRequests.filter(r => r.id !== id);
    },
    async deny(id) {
      await api.post(`/adoptions/${id}/deny`);
      this.adoptionRequests = this.adoptionRequests.filter(r => r.id !== id);
    }
  }
}
</script> 