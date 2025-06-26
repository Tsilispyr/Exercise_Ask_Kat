<template>
  <div>
    <h2 v-if="isAdminOrDoctor">Pending Requests</h2>
    <h2 v-else>My Requests</h2>
    <ul>
      <li v-for="req in requests" :key="req.id">
        {{ req.animalName || req.name }} - {{ req.status || (req.adminApproved === 1 && req.docApproved === 1 ? 'Approved' : 'Pending') }}
        <button v-if="canApprove" @click="approve(req.id)">Approve</button>
        <button v-if="canDeny" @click="deny(req.id)">Deny</button>
      </li>
    </ul>
  </div>
</template>
<script>
import api from '../api';
export default {
  data() { return { requests: [] }; },
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
      res = await api.get('/requests/pending');
    } else {
      res = await api.get('/requests/mine');
    }
    this.requests = res.data;
    console.log('Fetched requests:', this.requests);
  },
  methods: {
    async approve(id) {
      if (!id) {
        console.error('Tried to approve with undefined id!', id);
        return;
      }
      await api.post(`/requests/${id}/approve`);
      this.requests = this.requests.filter(r => r.id !== id);
    },
    async deny(id) {
      if (!id) {
        console.error('Tried to deny with undefined id!', id);
        return;
      }
      await api.post(`/requests/${id}/deny`);
      this.requests = this.requests.filter(r => r.id !== id);
    }
  }
}
</script> 