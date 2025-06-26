<template>
  <div>
    <h2>Users</h2>
    <ul>
      <li v-for="user in users" :key="user.id">
        {{ user.username }} - {{ user.email }} - {{ user.roles.join(', ') }}
        <button @click="editUser(user)">Edit</button>
      </li>
    </ul>
    <UserForm v-if="selectedUser" :user="selectedUser" @saved="fetchUsers" />
  </div>
</template>
<script>
import api from '../api';
import UserForm from './UserForm.vue';
export default {
  components: { UserForm },
  data() { return { users: [], selectedUser: null }; },
  async mounted() { this.fetchUsers(); },
  methods: {
    async fetchUsers() {
      const res = await api.get('/users');
      this.users = res.data;
    },
    editUser(user) { this.selectedUser = user; }
  }
}
</script> 