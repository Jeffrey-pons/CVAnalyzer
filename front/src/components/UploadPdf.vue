<template>
  <div class="upload-container">
    <h2>Upload Files & Text</h2>
 
    <!-- Champ de texte -->
    <div class="text-section">
      <h3>Offre d'emploi :</h3>
      <textarea
        v-model="jobOffer"
        placeholder="Écrivez votre offre ici..."
      ></textarea>
    </div>
 
    <!-- Zone de Drag & Drop ou sélection de fichier -->
    <div class="drop-area" @dragover.prevent @drop="handleDrop">
      <p>
        Glissez-déposez des fichiers ou
        <input type="file" multiple @change="handleFileUpload" accept="application/pdf" />
      </p>
    </div>
 
    <!-- Affichage des fichiers sélectionnés -->
    <ul v-if="selectedFiles.length > 0">
      <li v-for="(file, index) in selectedFiles" :key="index">
        <span>{{ file.name }}</span>
        <button @click="removeFile(index)">❌</button>
      </li>
    </ul>
 
    <!-- Bouton d'Upload -->
    <button @click="uploadPdfs" :disabled="selectedFiles.length === 0 || loading">
      {{ loading ? 'Envoi en cours...' : 'Envoyer les fichiers' }}
    </button>
 
    <!-- Indicateur de chargement -->
    <div v-if="loading" class="loading-indicator">
      ⏳ Analyse en cours, veuillez patienter...
    </div>
 
    <!-- Affichage du texte extrait -->
    <div v-if="extractedText && !loading" class="extracted-text">
      <h2>📜 Texte extrait :</h2>
      <pre>{{ extractedText }}</pre>
    </div>
  </div>
</template>
 
<script>
import axios from "axios";
 
export default {
  name: "UploadPdf",
  props: {
    msg: String,
  },
  data() {
    return {
      selectedFiles: [],
      extractedText: "",
      jobOffer: "",
      loading: false,
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
        this.extractedText = response.data || "Aucune donnée reçue";
      } catch (error) {
        this.extractedText = `Erreur : ${error.response?.data?.message || "Problème inconnu"}`;
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>
 
<style scoped>
.upload-container {
  max-width: 500px;
  margin: auto;
  padding: 30px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}
 
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
 
.extracted-text {
  margin-top: 20px;
  padding: 15px;
  background-color: #333;
  color: white;
  border-radius: 8px;
  text-align: left;
  white-space: pre-wrap;
}
</style>
 