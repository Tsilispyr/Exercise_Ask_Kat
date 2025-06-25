<template>
  <div>
    <h2>Citizen Dashboard</h2>
    <nav>
      <router-link to="/animals">Αναζήτηση Κατοικίδιων</router-link> |
      <router-link to="/requests">Οι Αιτήσεις μου</router-link>
    </nav>
    <section>
      <h3>Ενέργειες Πολίτη</h3>
      <ul>
        <li>Αναζήτηση κατοικίδιων προς υιοθεσία</li>
        <li>Υποβολή αίτησης υιοθεσίας</li>
        <li>Προγραμματισμός επίσκεψης</li>
      </ul>
    </section>
    <router-view/>
  </div>
</template> 