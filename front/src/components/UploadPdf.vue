<template>
  <div class="upload-container">
    <label for="file-upload" class="upload-label">
      <i class="fas fa-file-upload upload-icon"></i>
      <span v-if="fileName">{{ fileName }}</span>
      <span v-else>Upload PDF</span>
    </label>
    <input
      type="file"
      id="file-upload"
      accept="application/pdf"
      @change="handleFileUpload"
      hidden
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: "UploadPdf",
      fileName: "",
    };
  },
  methods: {
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.fileName = file.name;
      }
    },
  },
};
</script>

<style scoped>
.upload-container {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  cursor: pointer;
  border: 2px dashed #ccc;
  padding: 20px;
  width: 200px;
  text-align: center;
  border-radius: 10px;
}

.upload-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.upload-icon {
  font-size: 30px;
  color: #666;
  margin-bottom: 10px;
}

.upload-label span {
  font-size: 14px;
  color: #444;
}
</style>
