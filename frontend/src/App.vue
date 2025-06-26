<template>
  <div id="app">
    <Navbar />
    <router-view />
  </div>
</template>

<script setup>
import { provide } from 'vue';
import keycloak from './keycloak';
import Navbar from './components/Navbar.vue';

provide('$keycloak', keycloak);
</script>

<style>
nav {
  margin-bottom: 20px;
}
</style>

