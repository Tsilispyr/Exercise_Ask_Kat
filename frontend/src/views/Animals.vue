<template>
  <div>
    <h2>Διαθέσιμα Ζώα</h2>
    <router-link v-if="hasRole('ADMIN') || hasRole('shelter')" to="/animals/add" class="btn">Προσθήκη Ζώου</router-link>
    <div v-if="animals.length === 0">Κανένα ζώο διαθέσιμο.</div>
    <ul v-else>
      <li v-for="a in animals" :key="a.id" class="animal-card">
        <h3>{{ a.name }}</h3>
        <p>Είδος: {{ a.type }}</p>
        <p>Φύλο: {{ a.gender }}</p>
        <p>Ηλικία: {{ a.age }}</p>
        <router-link :to="`/animals/${a.id}`" class="btn">Λεπτομέρειες</router-link>
        <button @click="requestAnimal(a.id)" v-if="a.req === 0 && hasRole('USER')">Αίτηση Υιοθεσίας</button>
        <template v-if="a.req === 1 && (hasRole('ADMIN') || hasRole('shelter'))">
          <button @click="approveAnimal(a.id)">Έγκριση</button>
          <button @click="denyAnimal(a.id)">Απόρριψη</button>
        </template>
        <button @click="deleteAnimal(a.id)" v-if="hasRole('ADMIN')">Διαγραφή</button>
      </li>
    </ul>
  </div>
</template>

<script>
import api from '../api';
export default {
  data() {
    return { animals: [] }
  },
  mounted() {
    api.get('/animals')
      .then(r => {
        this.animals = r.data;
        console.log('Fetched animals:', this.animals);
      })
      .catch(() => alert('Σφάλμα ανάκτησης ζώων'))
  },
  methods: {
    requestAnimal(id) {
      api.post('/requests/new', { animalId: id })
        .then(() => alert('Αίτηση υποβλήθηκε'))
        .catch(() => alert('Σφάλμα κατά την αίτηση'))
    },
    approveAnimal(id) {
      api.post(`/animals/${id}/approve`)
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα έγκρισης'))
    },
    denyAnimal(id) {
      api.post(`/animals/${id}/deny`)
        .then(() => this.reload())
        .catch(() => alert('Σφάλμα απόρριψης'))
    },
    deleteAnimal(id) {
      api.delete(`/animals/${id}`)
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

<style scoped>
.animal-card {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 8px;
}
</style>
