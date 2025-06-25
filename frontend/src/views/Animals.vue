<template>
  <div>
    <h2>Διαθέσιμα Ζώα</h2>
<<<<<<< HEAD
    <router-link v-if="hasRole('ADMIN') || hasRole('SHELTER')" to="/animals/add" class="btn">Προσθήκη Ζώου</router-link>
=======
>>>>>>> b3ddf7906a718a82dc3d9088842cf9a6c5445701
    <div v-if="animals.length === 0">Κανένα ζώο διαθέσιμο.</div>
    <ul v-else>
      <li v-for="a in animals" :key="a.id" class="animal-card">
        <h3>{{ a.name }}</h3>
        <p>Είδος: {{ a.type }}</p>
        <p>Φύλο: {{ a.gender }}</p>
        <p>Ηλικία: {{ a.age }}</p>
<<<<<<< HEAD
        <router-link :to="`/animals/${a.id}`" class="btn">Λεπτομέρειες</router-link>
        <button @click="requestAnimal(a.id)" v-if="a.req === 0">Αίτηση Υιοθεσίας</button>
        <template v-if="a.req === 1 && (hasRole('ADMIN') || hasRole('SHELTER'))">
          <button @click="approveAnimal(a.id)">Έγκριση</button>
          <button @click="denyAnimal(a.id)">Απόρριψη</button>
        </template>
        <button @click="deleteAnimal(a.id)" v-if="hasRole('ADMIN')">Διαγραφή</button>
=======
        <button @click="requestAnimal(a.id)">Αίτηση Υιοθεσίας</button>
>>>>>>> b3ddf7906a718a82dc3d9088842cf9a6c5445701
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return { animals: [] }
  },
  mounted() {
    const kc = this.$keycloak
    fetch('http://localhost:8080/Animal', {
      headers: { Authorization: `Bearer ${kc.token}` }
    })
      .then(r => r.json())
      .then(data => (this.animals = data))
      .catch(() => alert('Σφάλμα ανάκτησης ζώων'))
  },
  methods: {
    requestAnimal(id) {
      fetch(`http://localhost:8080/Request/new`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${this.$keycloak.token}`
        },
        body: JSON.stringify({ animalId: id })
      })
        .then(() => alert('Αίτηση υποβλήθηκε'))
        .catch(() => alert('Σφάλμα κατά την αίτηση'))
<<<<<<< HEAD
    },
    approveAnimal(id) {
      fetch(`http://localhost:8080/Animal/Delete/${id}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${this.$keycloak.token}` }
      })
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα έγκρισης'))
    },
    denyAnimal(id) {
      fetch(`http://localhost:8080/Animal/Deny/${id}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${this.$keycloak.token}` }
      })
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα απόρριψης'))
    },
    deleteAnimal(id) {
      fetch(`http://localhost:8080/Animal/Delete/${id}`, {
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
=======
>>>>>>> b3ddf7906a718a82dc3d9088842cf9a6c5445701
    }
  }
}
</script>

<style scoped>
.animal-card {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 8px;
}
</style>
