<template>
  <div>
    <h3>{{ user && user.id ? 'Edit User' : 'Create User' }}</h3>
    <form @submit.prevent="save">
      <div>
        <label>Username:</label>
        <input v-model="form.username" required />
      </div>
      <div>
        <label>Email:</label>
        <input v-model="form.email" type="email" required />
      </div>
      <div v-if="!user || !user.id">
        <label>Password:</label>
        <input v-model="form.password" type="password" required />
      </div>
      <div>
        <label>Roles:</label>
        <select v-model="form.role" required>
          <option value="ROLE_USER">Citizen</option>
          <option value="ROLE_DOCTOR">Doctor</option>
          <option value="ROLE_SHELTER">Shelter</option>
          <option value="ROLE_ADMIN">Admin</option>
        </select>
      </div>
      <div>
        <label>Status:</label>
        <select v-model="form.status" required>
          <option value="pending">Pending</option>
          <option value="approved">Approved</option>
        </select>
      </div>
      <button type="submit">Save</button>
    </form>
  </div>
</template>

<script>
export default {
  props: ['user'],
  data() {
    return {
      form: {
        username: this.user?.username || '',
        email: this.user?.email || '',
        password: '',
        role: this.user?.roles ? this.user.roles[0] : 'ROLE_USER',
        status: this.user?.status || 'pending',
      }
    };
  },
  methods: {
    save() {
      // Επιστρέφει το user object με τα νέα δεδομένα
      const updatedUser = {
        ...this.user,
        username: this.form.username,
        email: this.form.email,
        status: this.form.status,
        roles: [this.form.role],
      };
      if (this.form.password) {
        updatedUser.password = this.form.password;
      }
      this.$emit('saved', updatedUser);
    }
  }
}
</script> 