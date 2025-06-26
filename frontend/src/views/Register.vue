<template>
  <div class="register">
    <h2>Register</h2>
    <form @submit.prevent="register">
      <input v-model="username" placeholder="Username" required />
      <input v-model="email" type="email" placeholder="Email" required />
      <input v-model="password" type="password" placeholder="Password" required />
      <select v-model="role" required>
        <option disabled value="">Select role</option>
        <option value="ROLE_USER">Citizen</option>
        <option value="ROLE_DOCTOR">Doctor</option>
        <option value="ROLE_SHELTER">Shelter</option>
      </select>
      <button type="submit">Register</button>
    </form>
    <div v-if="message">{{ message }}</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      email: "",
      password: "",
      role: "",
      message: ""
    };
  },
  methods: {
    async register() {
      try {
        const res = await fetch("/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username: this.username,
            email: this.email,
            password: this.password,
            role: this.role
          })
        });
        const data = await res.json();
        this.message = data.message || "Registration submitted. Awaiting admin approval.";
      } catch (e) {
        this.message = "Registration failed.";
      }
    }
  }
};
</script>

<style scoped>
.register-form {
  max-width: 400px;
  margin: 2rem auto;
  padding: 2rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  background: #fafafa;
}
.register-form label {
  display: block;
  margin-bottom: 0.2rem;
}
.register-form input, .register-form select {
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.5rem;
}
.message {
  margin-top: 1rem;
  color: #007b00;
  font-weight: bold;
}
</style> 