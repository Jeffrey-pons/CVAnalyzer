<template>
  <header class="app-header">
    <div class="logo">
      <img src="@/assets/logo-cv-analyzer.png" alt="Logo" />
    </div>
    <nav>
      <ul class="nav-links">
        <li><a href="/">Accueil</a></li>
        <li><a href="/analyse-cv">Analyse des CV</a></li>
        <li><a href="/parametres">Paramètres IA</a></li>
      </ul>
    </nav>
  </header>
</template>

<script>
export default {
  name: "AppHeader",
};
</script>

<style scoped>
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #333;
  padding: 10px 20px;
  color: white;
}

.logo img {
  width: 50px;
  height: auto;
}

nav .nav-links {
  list-style-type: none;
  margin: 0;
  padding: 0;
  display: flex;
}

nav .nav-links li {
  margin: 0 10px;
}

nav .nav-links a {
  text-decoration: none;
  color: white;
  font-weight: bold;
  font-size: 16px;
}

nav .nav-links a:hover {
  color: #f0a500;
}
</style>
