<template>
  <UploadPdf />
</template>

<script>
import UploadPdf from "./components/UploadPdf.vue";
export default {
  name: "App",
  components: {
    UploadPdf,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
