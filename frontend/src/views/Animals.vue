<template>
  <div>
    <h2>Διαθέσιμα Ζώα</h2>
    <div v-if="animals.length === 0">Κανένα ζώο διαθέσιμο.</div>
    <ul v-else>
      <li v-for="a in animals" :key="a.id" class="animal-card">
        <h3>{{ a.name }}</h3>
        <p>Είδος: {{ a.type }}</p>
        <p>Φύλο: {{ a.gender }}</p>
        <p>Ηλικία: {{ a.age }}</p>
        <button @click="requestAnimal(a.id)">Αίτηση Υιοθεσίας</button>
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
