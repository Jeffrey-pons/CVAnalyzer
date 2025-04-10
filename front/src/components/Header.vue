<template>
  <header class="app-header">
    <div class="logo">
      <img src="@/assets/icon-startSize.png" alt="Logo" />
      <h3>CV Analyzer</h3>
    </div>
    <nav>
      <ul class="nav-links">
        <li><router-link to="/home-page">Accueil</router-link></li>
        <li><router-link to="/analyse-cv">Analyse des CV</router-link></li>
        <li><router-link to="/parametres-prompts">Paramètres IA</router-link></li>
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
  padding: 0px 20px;
  color: white;
}

.logo {
  display: flex;
  font-weight: bold;
  align-items: center;
  gap: 10px;
}
.logo img {
  width: 35px;
  height: 35px;
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
