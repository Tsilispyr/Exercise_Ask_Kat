<template>
  <div>
    <h2>Shelter Dashboard</h2>
    <nav>
      <router-link to="/animals">Τα Κατοικίδιά μου</router-link> |
      <router-link to="/animals/add">Καταχώριση Κατοικιδίου</router-link> |
      <router-link to="/requests">Αιτήσεις Υιοθεσίας</router-link>
    </nav>
    <section>
      <h3>Ενέργειες Καταφυγίου</h3>
      <ul>
        <li>Καταχώριση νέων κατοικίδιων</li>
        <li>Εξέταση αιτήσεων υιοθεσίας</li>
        <li>Επικοινωνία με ενδιαφερόμενους</li>
      </ul>
    </section>
    <router-view/>
  </div>
</template>
