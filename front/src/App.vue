<template>
  <div id="app">
    <AppHeader />
    <main class="main-content">
      <router-view />
    </main> 
    <AppFooter />
  </div>
</template>

<script>
import AppHeader from './components/Header.vue';
import AppFooter from './components/Footer.vue'

export default {
  name: "App",
  components: {
    AppHeader,
    AppFooter,
  }
};
</script>
<style>
body {
  margin: auto;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.main-content {
  flex: 1; 
}
</style>
