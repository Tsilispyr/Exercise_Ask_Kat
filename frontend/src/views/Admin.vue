<template>
  <div>
    <h2>Admin Dashboard</h2>
    <nav>
      <router-link to="/users">Διαχείριση Χρηστών</router-link> |
      <router-link to="/animals">Έγκριση Κατοικίδιων</router-link> |
      <router-link to="/requests">Αιτήσεις Υιοθεσίας</router-link>
    </nav>
    <section>
      <h3>Ενέργειες Διαχειριστή</h3>
      <ul>
        <li>Επιβεβαίωση νέων προφίλ καταφυγίου</li>
        <li>Έγκριση/απόρριψη καταχωρίσεων κατοικίδιων</li>
        <li>Διαχείριση λογαριασμών χρηστών</li>
        <li>Παρακολούθηση αιτημάτων υιοθεσίας</li>
      </ul>
    </section>
    <router-view/>
  </div>
</template>
