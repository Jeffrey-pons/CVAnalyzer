<template>
    <div class="prompt-settings">
      <h2>⚙️ Paramètres des <span class="highlight">Prompts IA</span></h2>
  
      <div class="section">
        <div class="title-section">
        <label for="systemPrompt">Prompt Système (personnalité du bot)</label>
        <img class="icon-star" src="../assets/icon-star.png" alt="">
      </div>
        <textarea v-model="systemPrompt" placeholder="Ex : Tu es un assistant expert RH..."></textarea>
      </div>
  
      <div class="section">
        <div class="title-section">
        <label for="customPrompt">Prompt personnalisé par défaut</label>
        <img class="icon-star" src="../assets/icon-star.png" alt="">
      </div>
        <textarea v-model="customPrompt" placeholder="Ex : Prends en compte uniquement les expériences professionnelles..."></textarea>
      </div>
  
      <div class="section">
        <label>📌 Templates de questions fréquentes :</label>
        <ul>
          <li v-for="(q, index) in templates" :key="index">
            <input v-model="templates[index]" />
            <button @click="removeTemplate(index)">❌</button>
          </li>
        </ul>
        <button @click="addTemplate">➕ Ajouter un template</button>
      </div>
      
  
      <button @click="saveSettings">💾 Enregistrer</button>
  
      <p v-if="saved" class="saved-message">✅ Paramètres enregistrés !</p>
    </div>
  </template>
  
  <script>
  export default {
    name: "PromptSettings",
    data() {
      return {
        systemPrompt: "",
        customPrompt: "",
        templates: [],
        language : "fr",
        saved: false,
      };
    },
    mounted() {
      const saved = JSON.parse(localStorage.getItem("promptSettings") || "{}");
      this.systemPrompt = saved.systemPrompt || "";
      this.customPrompt = saved.customPrompt || "";
      this.templates = saved.templates || [];
    },
    methods: {
      saveSettings() {
        localStorage.setItem(
          "promptSettings",
          JSON.stringify({
            systemPrompt: this.systemPrompt,
            customPrompt: this.customPrompt,
            templates: this.templates,
          })
        );
        this.saved = true;
        setTimeout(() => (this.saved = false), 2000);
      },
      addTemplate() {
        this.templates.push("");
      },
      removeTemplate(index) {
        this.templates.splice(index, 1);
      },
    },
  };
  </script>
  
  <style scoped>
  html, body {
  height: 100%; 
  margin: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
  .prompt-settings {
    max-width: 70%;
    align-content: center;
    margin: auto;
    padding: 30px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    margin-top: 80px; /* Espace au-dessus du formulaire */
    margin-bottom: 80px; 
  }
  .section {
    margin-bottom: 25px;
  }
  label {
    font-weight: bold;
    display: block;
    margin-bottom: 8px;
    font-size: 1.2em;
  }
  textarea,
  input {
    width: 100%;
    padding: 10px;
    font-size: 1em;
    border-radius: 8px;
    border: 1px solid #ccc;
    margin-bottom: 10px;
    font-family: inherit;
  }
  ul {
    padding-left: 0;
    list-style: none;
  }
  li {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  button {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.2s ease;
  }
  button:hover {
    background-color: #0056b3;
  }
  .saved-message {
    margin-top: 15px;
    color: green;
  }
  .icon-star {
  width: 30px;
  height: auto;
  margin-bottom: 10px;
}
.title-section{
  display: flex;
  gap: 10px;
  align-items: center;
}
.highlight {
    color: #f0a500;
    margin-bottom: 30px;
  }
  
  </style>
  