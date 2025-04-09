<template>
    <div class="home-container">
      <section class="hero">
        <h1>Bienvenue sur <span class="highlight">CV Analyzer</span></h1>
        <p>L'outil intelligent pour analyser, comparer et améliorer vos CV grâce à l'intelligence artificielle.</p>
        <a href="/analyse-cv" class="cta-button">Commencer l’analyse</a>
      </section>
  
      <section class="features">
        <h2>Fonctionnalités clés</h2>
        <ul>
          <li>🔍 Analyse automatique des CV</li>
          <li>📊 Scoring intelligent</li>
          <li>✏️ Suggestions d’amélioration personnalisées</li>
          <li>🧠 Paramétrage de l’intelligence artificielle</li>
        </ul>
      </section>
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
  }
  
  .hero p {
    font-size: 1.2rem;
    margin-bottom: 40px;
  }
  
  .cta-button {
    background-color: #f0a500;
    color: #333;
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
    text-align: left;
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
  </style>
  