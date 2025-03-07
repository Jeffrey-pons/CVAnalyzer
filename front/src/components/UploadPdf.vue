<template>
  <div class="flex justify-center items-center min-h-screen bg-gradient-to-r from-blue-500 to-green-500 p-6">
    <div class="glassmorphism p-8 w-full max-w-lg">
      <h1 class="text-3xl font-bold text-white text-center mb-6 drop-shadow-md">{{ msg }}</h1>

      <!-- Formulaire -->
      <form @submit.prevent="uploadPdfs" class="space-y-6">
        
        <!-- Offre d'emploi -->
        <div>
          <h1 class="block text-white font-semibold mb-2">Offre d'emploi :</h1>
          <textarea
            v-model="jobOffer"
            class="w-full p-3 border border-gray-300 rounded-lg bg-white bg-opacity-20 text-white placeholder-white focus:outline-none focus:ring-2 focus:ring-blue-300 transition"
            rows="10"
            placeholder="Écrivez votre offre ici..."
          ></textarea>
        </div>

        <!-- Upload fichier -->
        <div>
          <label class="block text-white font-semibold mb-2">Télécharger des fichiers PDF :</label>
          <input
            type="file"
            @change="handleFileUpload"
            accept="application/pdf"
            multiple
            class="w-full border border-gray-300 rounded-lg p-3 cursor-pointer bg-white bg-opacity-20 text-white focus:outline-none focus:ring-2 focus:ring-green-300 transition"
          />
        </div>

        <!-- Liste des fichiers sélectionnés -->
        <div v-if="selectedFiles.length > 0" class="bg-white bg-opacity-20 p-4 rounded-lg border border-gray-300 shadow-md mt-4">
          <h3 class="text-white font-semibold mb-2">Fichiers sélectionnés :</h3>
          <ul class="list-disc list-inside text-white">
            <li v-for="(file, index) in selectedFiles" :key="index">
              <span class="text-green-300">📄</span> <span>{{ file.name }}</span>
            </li>
          </ul>
        </div>

        <!-- Bouton pour envoyer les fichiers -->
        <button
          type="submit"
          :disabled="selectedFiles.length === 0 || loading"
          class="w-full neon-button text-white font-bold py-3 px-4 rounded-lg"
        >
          {{ loading ? 'Envoi en cours...' : 'Envoyer les fichiers' }}
        </button>
      </form>

      <!-- Indicateur de chargement -->
      <div v-if="loading" class="text-center text-white mt-6">
        ⏳ Analyse en cours, veuillez patienter...
      </div>

      <!-- Affichage du texte extrait -->
      <div v-if="extractedText && !loading" class="mt-6 p-6 bg-black bg-opacity-75 border-l-4 border-blue-400 rounded-lg text-white shadow-lg terminal-effect">
        <h2 class="text-xl font-semibold text-blue-300 mb-2">📜 Texte extrait :</h2>
        <pre class="whitespace-pre-wrap">{{ extractedText }}</pre>
      </div>
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
      loading: false, // Indicateur de chargement
    };
  },
  methods: {
    handleFileUpload(event) {
      const files = event.target.files;
      if (files.length) {
        this.selectedFiles = [...this.selectedFiles, ...files];
      }
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

      this.loading = true; // Activer le chargement

      try {
    const response = await axios.post("http://localhost:9090/cv/compare-cvs", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    console.log("Response from backend:", response.data);

    this.extractedText = response.data || "Aucune donnée reçue";
  } catch (error) {
    console.error("Erreur complète :", error.response);
    this.extractedText = `Erreur : ${error.response?.data?.message || "Problème inconnu"}`;
  } finally {
    this.loading = false;
  }
    },
  },
};
</script>

<style scoped>
.glassmorphism {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.neon-button {
  background: linear-gradient(90deg, #ff8a00, #e52e71);
  transition: all 0.3s ease-in-out;
  box-shadow: 0 0 10px rgba(255, 138, 0, 0.5);
}
.neon-button:hover {
  box-shadow: 0 0 20px rgba(255, 138, 0, 0.8);
  transform: scale(1.05);
}

.terminal-effect {
  font-family: "Courier New", monospace;
  animation: blinkText 1s infinite;
}

@keyframes blinkText {
  50% { opacity: 0.7; }
}
</style>
