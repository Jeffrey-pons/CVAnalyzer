<template>
    <div class="home-container">
      <section class="hero">
        <h1>Bienvenue sur <span class="highlight">CV Analyzer</span></h1>
        <p>L'outil intelligent pour analyser, comparer et améliorer vos CV grâce à l'intelligence artificielle.</p>
        <p>Ajustez le comportement de l’IA selon votre secteur, les types de postes à pourvoir et vos préférences.</p>
        <div class="button-container">
          <router-link to="/analyse-cv" class="cta-button">Commencer l’analyse</router-link>
          <router-link to="/parametres-prompts" class="cta-button alt">Paramétrer l'IA</router-link>
        </div>
      </section>
  <div class="section-container">
      <section class="features">
        <div class="title-section">
          <h2>Fonctionnalités clés</h2>
          <img class="icon-star" src="../assets/icon-star.png" alt="">
        </div>
        <ul>
          <li>🔍 Analyse automatique des CV</li>
          <li>📊 Scoring intelligent</li>
          <li>✏️ Suggestions d’amélioration personnalisées</li>
          <li>🧠 Paramétrage de l’intelligence artificielle</li>
        </ul>
      </section>
      <section class="use-cases">
        <div class="title-section">
          <h2>Cas d’usage</h2>
          <img class="icon-star" src="../assets/icon-star.png" alt="">
      </div>
          <ul>
            <li>👨‍💼 Pour les recruteurs : analyser plusieurs CV rapidement pour identifier le meilleur profil.</li>
            <li>📝 Pour les candidats : obtenir des suggestions pour améliorer leur CV et se démarquer.</li>
          </ul>
      </section>
    </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "HomePage",
  };
  </script>
  
  <style scoped>
  .home-container {
    padding: 40px 20px;
    text-align: center;
    color: #333;
    font-family: 'Segoe UI', sans-serif;
  }
  
  .hero {
    background-color: #f5f5f5;
    padding: 40px 20px;
    border-radius: 12px;
    margin-bottom: 40px;
  }
  
  .hero h1 {
    font-size: 2.5rem;
    margin-bottom: 10px;
  }
  
  .highlight {
    color: #f0a500;
    margin-bottom: 30px;
  }
  
  .hero p {
    font-size: 1.2rem;
  }
  
  .cta-button {
    background-color: #f0a500;
    color: white;
    padding: 12px 24px;
    border-radius: 8px;
    font-weight: bold;
    text-decoration: none;
    font-size: 1rem;
    transition: all 0.3s ease;
  }
  
  .cta-button:hover {
    background-color: #fff;
    color: #f0a500;
    border: 2px solid #f0a500;
  }
  
  .features {
    text-align: justify;
    max-width: 600px;
    margin: 0 auto;
  }
  
  .features h2 {
    font-size: 1.8rem;
    margin-bottom: 20px;
    color: #222;
    text-align: center;
  }
  
  .features ul {
    list-style: none;
    padding: 0;
  }
  
  .features li {
    font-size: 1.1rem;
    margin: 10px 0;
    padding-left: 10px;
  }
  .use-cases {
  text-align: justify;
  max-width: 600px;
  margin: 40px auto;
  font-size: 1.1rem;
  color: #444;
}

.use-cases h2 {
  font-size: 1.8rem;
  margin-bottom: 20px;
  color: #222;
  text-align: center;
}

.use-cases ul {
  list-style: none;
  padding: 0;
}

.use-cases li {
  margin: 10px 0;
  padding-left: 10px;
}
.section-container {
  display:flex;
  align-items: center;
}
.section-container section {
  flex: 1;
}

.cta-button.alt {
  background-color: #ffffff;
  color: #f0a500;
  border: 2px solid #f0a500;
}

.cta-button.alt:hover {
  background-color: #f0a500;
  color: #fff;
}
.button-container{
margin-top: 30px;
display: flex;
gap: 30px;
justify-content: center;
}
.icon-star {
  width: 30px;
  height: auto;
  margin-top: 10px;
}
.title-section{
  display: flex;
  gap: 10px;
  justify-content: center;
  align-items: center;
}
  </style>
  