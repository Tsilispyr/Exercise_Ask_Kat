<template>
  <div>
    <h2>Προσθήκη Ζώου</h2>
    <form @submit.prevent="addAnimal">
      <div>
        <label>Όνομα:</label>
        <input v-model="animal.name" required />
      </div>
      <div>
        <label>Είδος:</label>
        <input v-model="animal.type" required />
      </div>
      <div>
        <label>Φύλο:</label>
        <select v-model="animal.gender" required>
          <option value="MALE">Αρσενικό</option>
          <option value="FEMALE">Θηλυκό</option>
        </select>
      </div>
      <div>
        <label>Ηλικία:</label>
        <input v-model="animal.age" type="number" min="0" required />
      </div>
      <button type="submit">Προσθήκη</button>
      <button type="button" @click="$router.back()">Ακύρωση</button>
    </form>
  </div>
</template>

<script>
import api from '../api';
export default {
  data() {
    return {
      animal: { name: '', type: '', gender: 'MALE', age: 0 }
    }
  },
  methods: {
    addAnimal() {
      api.post('/animals', this.animal)
        .then(() => this.$router.push('/animals'))
        .catch(() => alert('Σφάλμα προσθήκης ζώου'))
    }
  }
}
</script> 