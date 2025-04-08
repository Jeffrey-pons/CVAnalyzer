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
        <h2>Upload Files & Text</h2>

        <div class="text-section">
          <h3>Offre d'emploi :</h3>
          <textarea v-model="jobOffer" placeholder="Écrivez votre offre ici..."></textarea>
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
        <h2>💬 Conversation</h2>
        <div class="chat-messages">
          <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.sender]">
            <strong v-if="msg.sender === 'user'">👤 Vous:</strong>
            <strong v-else>🤖 Bot:</strong>
            <p>{{ msg.text }}</p>
          </div>

          <div v-if="loading" class="message bot loading-message">
            <strong>🤖 Bot:</strong>
            <p><span class="spinner">🔄</span> Réflexion en cours...</p>
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
    };
  },
  mounted() {
    const saved = localStorage.getItem("chatHistory");
    if (saved) this.history = JSON.parse(saved);
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

      this.loading = true;
      try {
        const response = await axios.post("http://localhost:9090/cv/compare-cvs", formData);
        const botReply = response.data || "Aucune donnée reçue";
        this.messages = [{ sender: "bot", text: botReply }];
        this.conversationId = Date.now();
        this.showChat = true;
        this.saveToHistory();
      } catch (error) {
        this.messages = [{ sender: "bot", text: "❌ Erreur : " + (error.response?.data?.message || "Problème inconnu") }];
        this.showChat = true;
      } finally {
        this.loading = false;
      }
    },
    async sendMessage() {
      if (!this.userMessage.trim()) return;
      this.messages.push({ sender: "user", text: this.userMessage });
      this.loading = true;
      try {
        const response = await axios.post("http://localhost:9090/cv/follow-up", {
          message: this.userMessage,
          conversationId: this.conversationId
        });
        this.messages.push({ sender: "bot", text: response.data || "Pas de réponse" });
        this.saveToHistory();
      } catch (error) {
        this.messages.push({ sender: "bot", text: "❌ Erreur IA" });
      } finally {
        this.userMessage = "";
        this.loading = false;
      }
    },
    loadHistory(item) {
      this.messages = item.messages;
      this.conversationId = item.id;
      this.showChat = true;
    },
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleString();
    },
    deleteHistory(id) {
  this.history = this.history.filter(item => item.id !== id);
  localStorage.setItem("chatHistory", JSON.stringify(this.history));

  // Si la discussion supprimée était en cours
  if (this.conversationId === id) {
    this.messages = [];
    this.showChat = false;
    this.conversationId = null;
  }
}

  },
};
</script>

<style scoped>
.main-layout {
  display: flex;
}

.sidebar {
  width: 270px;
  background: #f0f0f0;
  padding: 20px;
  border-right: 1px solid #ccc;
  height: 100vh;
  overflow-y: auto;
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
  border-left: 4px solid #007bff;
  border-radius: 6px;
  transition: all 0.2s;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.history-item:hover {
  background-color: #e9f3ff;
}

.history-title {
  font-weight: bold;
  color: #007bff;
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
  margin-bottom: 8px;
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

.drop-area {
  border: 2px dashed #007bff;
  padding: 25px;
  margin-bottom: 20px;
  border-radius: 12px;
  transition: background-color 0.2s ease;
}

.drop-area:hover {
  background-color: #f0f8ff;
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
  background-color: #007bff;
  color: white;
  border: none;
  padding: 12px 25px;
  font-size: 1em;
  cursor: pointer;
  margin-top: 15px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}
 
button:hover {
  background-color: #0056b3;
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

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>