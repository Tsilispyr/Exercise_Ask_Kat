<template>
  <div v-if="animal">
    <h2>Λεπτομέρειες Ζώου</h2>
    <div>
      <strong>Όνομα:</strong> {{ animal.name }}<br />
      <strong>Είδος:</strong> {{ animal.type }}<br />
      <strong>Φύλο:</strong> {{ animal.gender }}<br />
      <strong>Ηλικία:</strong> {{ animal.age }}<br />
    </div>
    <!-- Edit form and actions for admin/shelter will be added in next steps -->
    <button @click="$router.back()">Επιστροφή</button>
  </div>
  <div v-else>
    <p>Φόρτωση...</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      animal: null
    }
  },
  mounted() {
    const id = this.$route.params.id
    fetch(`http://localhost:8080/Animal/${id}`, {
      headers: { Authorization: `Bearer ${this.$keycloak.token}` }
    })
      .then(r => r.json())
      .then(data => (this.animal = data))
      .catch(() => alert('Σφάλμα ανάκτησης ζώου'))
  }
}
</script> 