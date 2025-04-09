<template>
  <div id="app">
    <AppHeader />
    <router-view />  
  </div>
</template>

<script>
import AppHeader from './components/Header.vue';

export default {
  name: "App",
  components: {
    AppHeader,
  }
};
</script>
<style>
body {
  margin: auto;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
}
</style>
