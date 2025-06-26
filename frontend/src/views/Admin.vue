<template>
  <div>
    <h2>Admin Dashboard</h2>
    <RequestList />
    <AdoptionRequestList />
    <UserList />
  </div>
</template>
<script>
import RequestList from '../components/RequestList.vue';
import AdoptionRequestList from '../components/AdoptionRequestList.vue';
import UserList from '../components/UserList.vue';
export default {
  components: { RequestList, AdoptionRequestList, UserList }
}
</script>
