<template>
  <div class="main-layout">
    <!-- Partie gauche : Historique -->
    <div class="sidebar">
      <h3 class="sidebar-title">🕓 Historique</h3>
      <ul class="history-list">
        <li v-for="item in history" :key="item.id" class="history-item">
  <div @click="loadHistory(item)" style="flex: 1; cursor: pointer;">
    <div class="history-title">{{ item.title }}</div>
    <div class="history-date">{{ formatDate(item.date) }}</div>
  </div>
  <button @click.stop="deleteHistory(item.id)" title="Supprimer">🗑️</button>
</li>

      </ul>
    </div>

    <!-- Partie centrale : formulaire + chatbot -->
    <div class="main-container">
      <!-- Formulaire -->
      <div class="upload-container">
        <img src="../assets/cv-icon.png" alt="">
        <h2>Analysez plusieurs <span class="highlight">CV</span> en fonction d’une <span class="highlight">offre d’emploi</span></h2>

        <div class="text-section">
          <div class="title-section">
          <h3>Offre d'emploi</h3>
          <img class="icon-star" src="../assets/icon-star.png" alt="">
        </div>
          <textarea v-model="jobOffer" placeholder="Écrivez votre offre ici..."></textarea>
        </div>
        <div class="text-section">
          <div class="title-section">
        <h3>Déposez vos cv </h3>
        <img class="icon-star" src="../assets/icon-star.png" alt="">
      </div>
        <div class="drop-area" @dragover.prevent @drop="handleDrop">
          <p>
            Glissez-déposez des fichiers ou
            <label class="custom-file-upload">
              <input type="file" multiple @change="handleFileUpload" accept="application/pdf" hidden />
              Choisir des fichiers
            </label>
          </p>
        </div>
        </div>

        <ul v-if="selectedFiles && selectedFiles.length > 0">
          <li v-for="(file, index) in selectedFiles" :key="index">
            <span>{{ file.name }}</span>
            <button @click="removeFile(index)">❌</button>
          </li>
        </ul>

        <button @click="uploadPdfs" :disabled="selectedFiles.length === 0 || loading">
          {{ loading ? 'Envoi en cours...' : 'Envoyer les fichiers' }}
        </button>

        <div v-if="loading" class="loading-indicator">
          ⏳ Analyse en cours, veuillez patienter...
        </div>
      </div>

      <!-- Chatbot -->
      <div class="chat-box" v-if="showChat">
        <div class="chat-header">
    <h2>💬 Conversation</h2>
    <button class="close-chat" @click="showChat = false" title="Fermer">❌</button>
  </div>
        <div class="chat-messages">
          <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.sender]">
            <strong v-if="msg.sender === 'user'">👤 Vous : </strong>
            <strong v-else>🤖 Agent IA :</strong>
            <p>{{ msg.text }}</p>
          </div>

          <div v-if="loading" class="message bot loading-message">
            <strong>🤖 Agent IA :</strong>
            <p><span class="spinner">🔄</span> Réflexion en cours...</p>
          </div>
        </div>
        <div v-if="conversationCvs.length > 0" class="attached-cvs">
        <h4>📎 CVs associés :</h4>
         <ul>
          <li v-for="cv in conversationCvs" :key="cv.filename">
            <a :href="`http://localhost:9090/cv/download/${cv.id}`" target="_blank">{{ cv.filename }}</a>
          </li>
         </ul>
         </div>
         <div v-if="templates.length > 0" class="suggestions">
  <h4>💡 Questions suggérées :</h4>
  <div class="suggestion-list">
    <button
      v-for="(question, index) in templates"
      :key="index"
      @click="userMessage = question"
      class="suggestion-btn"
    >
      {{ question }}
    </button>
  </div>
</div>

        <div class="chat-input">
          <input v-model="userMessage" @keyup.enter="sendMessage" placeholder="Posez une question..." />
          <button @click="sendMessage" :disabled="loading || !userMessage.trim()">Envoyer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "UploadPdfWithHistory",
  data() {
    return {
      selectedFiles: [],
      jobOffer: "",
      loading: false,
      messages: [],
      userMessage: "",
      showChat: false,
      conversationId: null,
      history: [],
      conversationCvs: [],
      templates: [],
    };
  },
  mounted() {
  const promptSettings = JSON.parse(localStorage.getItem("promptSettings") || "{}");
  this.templates = promptSettings.templates || [];

  // Chargement historique déjà présent
  axios.get("http://localhost:9090/cv/conversations")
    .then(res => this.history = res.data)
    .catch(e => console.error("Erreur de chargement de l'historique : ", e));
},

  methods: {
    handleFileUpload(event) {
      this.selectedFiles = [...this.selectedFiles, ...event.target.files];
    },
    handleDrop(event) {
      this.selectedFiles = [...this.selectedFiles, ...event.dataTransfer.files];
    },
    removeFile(index) {
      this.selectedFiles.splice(index, 1);
    },
    saveToHistory() {
      const entry = {
        id: this.conversationId || Date.now(),
        date: new Date().toISOString(),
        title: this.jobOffer.slice(0, 50) || "Sans titre",
        messages: this.messages,
      };
      this.conversationId = entry.id;
      const updated = this.history.filter(h => h.id !== entry.id);
      updated.push(entry);
      this.history = updated;
      localStorage.setItem("chatHistory", JSON.stringify(updated));
    },
    async uploadPdfs() {
  if (this.selectedFiles.length === 0) {
    alert("Veuillez sélectionner au moins un fichier PDF.");
    return;
  }

  let formData = new FormData();
  this.selectedFiles.forEach(file => formData.append("files", file));
  formData.append("jobOffer", this.jobOffer);
  formData.append("systemPrompt", this.getPromptFromSettings("compare"));

  this.loading = true;

  try {
    const response = await axios.post("http://localhost:9090/cv/compare-cvs", formData);
    
    const { message, conversationId } = response.data;
    this.messages = [{ sender: "bot", text: message }];
    this.conversationId = conversationId;
    this.showChat = true;
    this.saveToHistory();

  } catch (error) {
    this.messages = [{ sender: "bot", text: "❌ Erreur : " + (error.response?.data?.message || "Problème inconnu") }];
    this.showChat = true;
  } finally {
    this.loading = false;
  }
}
,
async sendMessage() {
  if (!this.userMessage.trim()) return;

  // Sécurité : s'assurer que this.messages est bien un tableau
  if (!Array.isArray(this.messages)) {
    this.messages = [];
  }

  this.messages.push({ sender: "user", text: this.userMessage });
  this.loading = true;

  try {
    const customPrompt = this.getPromptFromSettings("chat");
    const response = await axios.post(`http://localhost:9090/cv/follow-up/${this.conversationId}`, {
      message: this.userMessage,
      systemPrompt: customPrompt
    });

    this.messages.push({ sender: "bot", text: response.data || "Pas de réponse" });
    this.saveToHistory();

  } catch (error) {
    this.messages.push({ sender: "bot", text: "❌ Erreur IA" });
  } finally {
    this.userMessage = "";
    this.loading = false;
  }
}
,
async loadHistory(item) {
  this.loading = true;
  try {
    const res = await axios.get(`http://localhost:9090/cv/conversations/${item.id}`);
    this.messages = res.data.messages;
    this.conversationCvs = res.data.cvs || [];
    this.conversationId = item.id;
    this.showChat = true;
  } catch (e) {
    console.error("Erreur chargement conversation :", e);
  } finally {
    this.loading = false;
  }
}
,

    formatDate(dateStr) {
      return new Date(dateStr).toLocaleString();
    },
    async deleteHistory(id) {
  try {
    await axios.delete(`http://localhost:9090/cv/conversations/${id}`);
    
    // Met à jour l'historique local après suppression côté BDD
    this.history = this.history.filter(item => item.id !== id);

    // Si la conversation supprimée était celle en cours
    if (this.conversationId === id) {
      this.messages = [];
      this.showChat = false;
      this.conversationId = null;
    }
  } catch (error) {
    console.error("Erreur lors de la suppression :", error);
    alert("❌ Impossible de supprimer la conversation.");
  }
}
,
getPromptFromSettings(type) {
  const settings = JSON.parse(localStorage.getItem("promptSettings") || "{}");
  return type === "compare"
    ? settings.systemPrompt || ""
    : settings.customPrompt || "";
}



  },
};
</script>

<style scoped>
.main-layout {
  display: flex;
  flex: 1;
}
html, body {
  height: 100%; 
  margin: 0;
  display: flex;
  flex-direction: column; 
}

.sidebar {
  width: 270px;
  background: #f0f0f0;
  padding: 20px;
  border-right: 1px solid #ccc;
  overflow-y: auto; 
  display: flex;
  flex-direction: column; 
  height: 1000px;
}

.sidebar-title {
  font-size: 1.2em;
  margin-bottom: 15px;
  color: #333;
}

.history-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.history-item {
  cursor: pointer;
  margin-bottom: 12px;
  padding: 12px;
  background: #fff;
  border-left: 4px solid #f0a500;
  border-radius: 6px;
  transition: all 0.2s;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.history-item:hover {
  background-color: #e9f3ff;
}

.history-title {
  font-weight: bold;
  color: #f0a500;
  font-size: 0.95em;
}

.history-date {
  font-size: 0.8em;
  color: #666;
  margin-top: 4px;
}

.main-container {
  flex: 1;
  padding: 20px;
  display: flex;
  gap: 30px;
  justify-content: center;
}

.upload-container {
  max-width: 500px;
  padding: 30px;
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.06);
  text-align: center;
  height: auto;
}
.upload-container img {
  max-width: 100px;
}

h2 {
  font-size: 1.6em;
  margin-bottom: 25px;
  font-weight: bold;
  color: #333;
}

.text-section {
  margin-bottom: 20px;
  text-align: left;
}

.text-section h3 {
  font-size: 1.1em;
  margin-bottom: 18px;
  font-weight: 600;
  color: #333;
}

textarea {
  width: 100%;
  height: 100px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  resize: none;
  font-size: 0.95em;
  font-family: inherit;
}
textarea:focus-visible{
  border: 2px solid #f0a500;
  outline: none;
}
.drop-area {
  border: 2px dashed #f0a500;
  padding: 25px;
  margin-bottom: 40px;
  border-radius: 12px;
  transition: background-color 0.2s ease;
}

.drop-area:hover {
  background-color: rgba(202, 189, 11, 0.089);
}

.custom-file-upload {
  display: inline-block;
  padding: 8px 16px;
  background-color: #f8f8f8;
  border: 1px solid #ccc;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 10px;
  font-size: 0.95em;
}

.custom-file-upload:hover {
  background-color: #eee;
}

ul {
  list-style-type: none;
  padding: 0;
  text-align: left;
  margin-bottom: 20px;
}
 
ul li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  padding: 10px;
  margin-bottom: 8px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
 
ul li span {
  font-size: 1em;
  color: #333;
}
 
ul li button {
  background-color: transparent;
  border: none;
  color: #e74c3c;
  font-size: 1.2em;
  cursor: pointer;
  transition: color 0.2s ease;
}
 
ul li button:hover {
  color: #c0392b;
}
 
button {
  background-color: #f0a500;
  color: white;
  border: none;
  padding: 12px 25px;
  font-size: 1.1em;
  cursor: pointer;
  font-weight: bold;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}
 
button:hover {
  background-color: #fff;
    color: #f0a500;
    border: 2px solid #f0a500;
}

.loading-indicator {
  margin-top: 15px;
  color: #555;
}

.chat-box {
  flex: 1;
  background: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.chat-messages {
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: 10px;
}

.message {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 10px;
  white-space: pre-wrap;
}

.message.user {
  background: #dcf8c6;
  text-align: right;
}

.message.bot {
  background: #eef;
  text-align: left;
}

.chat-input {
  display: flex;
  gap: 10px;
}

.chat-input input {
  flex: 1;
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #ccc;
}

.spinner {
  animation: spin 1s linear infinite;
  display: inline-block;
  margin-right: 5px;
}
.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.history-item button {
  background: transparent;
  border: none;
  color: #e74c3c;
  font-size: 1.1em;
  cursor: pointer;
}
.history-item button:hover {
  color: #c0392b;
}
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.chat-header h2 {
  margin: 0;
  font-size: 1.5em;
}

.close-chat {
  background: transparent;
  border: none;
  font-size: 1.2em;
  cursor: pointer;
  color: #888;
  transition: color 0.2s;
}
.attached-cvs {
  margin-top: 20px;
  background-color: #f9f9f9;
  padding: 10px 15px;
  border-radius: 10px;
}
.attached-cvs ul {
  padding-left: 20px;
}
.highlight {
    color: #f0a500;
    margin-bottom: 30px;
  }
  .icon-star {
  width: 25px;
  height: auto;
}
.title-section{
  display: flex;
  gap: 6px;
  justify-content: left;
  align-items: center;
}
.suggestions {
  margin-bottom: 15px;
}

.suggestion-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.suggestion-btn {
  background-color: #8a8585;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 6px 12px;
  font-size: 0.9em;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.suggestion-btn:hover {
  background-color: #f0a500;
  color: white;
  border-color: #f0a500;
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>