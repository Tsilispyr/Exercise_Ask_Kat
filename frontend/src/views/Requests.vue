<template>
  <div>
    <h2>Αιτήσεις Υιοθεσίας</h2>
    <ul>
      <li v-for="r in requests" :key="r.id">
        {{ r.name }} — Admin: {{ r.adminApproved }}, Doctor: {{ r.docApproved }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return { requests: [] }
  },
  mounted() {
    const kc = this.$keycloak
    fetch('http://localhost:8080/Request', {
      headers: { Authorization: `Bearer ${kc.token}` }
    })
      .then(r => r.json())
      .then(data => (this.requests = data))
      .catch(() => alert('Σφάλμα ανάκτησης αιτήσεων'))
  }
}
</script>
