<template>
  <div class="main-container">
    <!-- Partie gauche : formulaire -->
    <div class="upload-container">
      <h2>Upload Files & Text</h2>

      <div class="text-section">
        <h3>Offre d'emploi :</h3>
        <textarea
          v-model="jobOffer"
          placeholder="Écrivez votre offre ici..."
        ></textarea>
      </div>

      <div class="drop-area" @dragover.prevent @drop="handleDrop">
        <p>
          Glissez-déposez des fichiers ou
          <input type="file" multiple @change="handleFileUpload" accept="application/pdf" />
        </p>
      </div>

      <ul v-if="selectedFiles.length > 0">
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

    <!-- Partie droite : chatbot (affiché après envoi) -->
    <div class="chat-box" v-if="showChat">
      <h2>💬 Conversation</h2>
      <div class="chat-messages">
  <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.sender]">
    <strong v-if="msg.sender === 'user'">👤 Vous:</strong>
    <strong v-else>🤖 Bot:</strong>
    <p>{{ msg.text }}</p>
  </div>

  <!-- 🔄 Affichage d’un message temporaire du bot pendant le chargement -->
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
</template>

<script>
import axios from "axios";

export default {
  name: "UploadPdf",
  data() {
    return {
      selectedFiles: [],
      jobOffer: "",
      loading: false,
      messages: [],
      userMessage: "",
      showChat: false
    };
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
    async uploadPdfs() {
      if (this.selectedFiles.length === 0) {
        alert("Veuillez sélectionner au moins un fichier PDF.");
        return;
      }

      let formData = new FormData();
      this.selectedFiles.forEach((file) => {
        formData.append("files", file);
      });
      formData.append("jobOffer", this.jobOffer);

      this.loading = true;
      try {
        const response = await axios.post("http://localhost:9090/cv/compare-cvs", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });

        const botReply = response.data || "Aucune donnée reçue";

        // Réinitialiser la conversation avant d'afficher la nouvelle
        this.messages = [];
        this.messages.push({ sender: "bot", text: botReply });
        this.showChat = true;
      } catch (error) {
        const errorMessage = error.response?.data?.message || "Problème inconnu";
        this.messages = [{ sender: "bot", text: `❌ Erreur : ${errorMessage}` }];
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
          message: this.userMessage
        });

        this.messages.push({ sender: "bot", text: response.data || "Pas de réponse" });
      } catch (error) {
        this.messages.push({
          sender: "bot",
          text: `❌ Erreur : ${error.response?.data?.message || "Problème inconnu"}`
        });
      } finally {
        this.userMessage = "";
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.main-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 30px;
  padding: 20px;
}

/* Partie gauche */
.upload-container {
  flex: 1;
  max-width: 500px;
  padding: 30px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}

/* Partie droite : chatbot */
.chat-box {
  flex: 1;
  max-width: 500px;
  background-color: #fff;
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.chat-messages {
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: 15px;
  padding-right: 5px;
}

.message {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 10px;
  white-space: pre-wrap;
}

.message.bot {
  background-color: #eef;
  text-align: left;
}

.message.user {
  background-color: #dcf8c6;
  text-align: right;
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

.chat-input button {
  padding: 10px 15px;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 8px;
  cursor: pointer;
}

.chat-input button:disabled {
  background-color: #aaa;
}

/* Styles communs */
h2 {
  font-size: 1.5em;
  margin-bottom: 20px;
  color: #333;
}

.text-section {
  margin-bottom: 20px;
  text-align: left;
}

.text-section h3 {
  font-size: 1.2em;
  margin-bottom: 8px;
  color: #555;
}

textarea {
  width: 100%;
  height: 100px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 10px;
  resize: none;
  font-size: 1em;
}

.drop-area {
  border: 2px dashed #007bff;
  padding: 30px;
  margin-bottom: 15px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.drop-area:hover {
  background-color: #f0f8ff;
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
.loading-message {
  font-style: italic;
  color: #555;
}

.spinner {
  animation: spin 1s linear infinite;
  display: inline-block;
  margin-right: 5px;
  font-size: 1.2em;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

</style>
