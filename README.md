# 📄 CV Analyzer

CV Analyzer est une application intelligente qui permet de **comparer plusieurs CV à une offre d’emploi** et d'identifier **le candidat le plus pertinent**.
 - Les technologies utilisées : **Vue.js**, **Java Spring Boot** et **MySQL**.

L’IA utilisée est propulsée par **Ollama**, qui analyse le contenu des CV et les compare avec les critères de l’offre d’emploi pour renvoyer le candidat le plus en accord avec l'offre d'emploi.

---

## 🚀 Fonctionnalités principales

- 📤 **Upload de plusieurs CV** (PDF)
- 📝 **Soumission d'une offre d’emploi**
- 🤖 **Analyse intelligente** des CV par IA
- 🏆 **Classement automatique** du meilleur CV selon l’offre
- 💾 **Sauvegarde des offres, CVs et résultats** en base de données

---

## 🧰 Tech Stack

### Frontend
- ✅ [Vue.js 3](https://vuejs.org/)

### Backend
- ☕️ [Spring Boot](https://spring.io/projects/spring-boot)
- 🧠 [Ollama](https://ollama.com/) (Intelligence Artificielle)
- 🗃️ [MySQL](https://www.mysql.com/)

---

## ⚙️ Installation & Lancement

### Pré-requis

- Node.js (>= 16)
- Java 17+
- MySQL
- Ollama installé et fonctionnel localement

---

### 🔧 Backend (Java Spring)

1. Clone du projet :

```bash

 git clone https://github.com/votre-compte/CVAnalyzer.git
 cd CVAnalyzer/back

```

2. Configuration de la base de données MySQL :

```bash

 spring.datasource.url=jdbc:mysql://localhost:3306/cv_analyzer
 spring.datasource.username=root
 spring.datasource.password=your_password

```

3. Création de la base de donnée dans MySQL
```bash

 CREATE DATABASE cv_analyzer;

```

4. Lancer le backend
```bash

 mvn spring-boot:run

```

---

### 🖼️ Frontend (Vue.js)

1. Se déplacer dans le dossier frontend
```bash

 git clone https://github.com/votre-compte/CVAnalyzer.git
 cd CVAnalyzer/front

```

6. Installer les dépendances
```bash

 npm install

```


7. Lancer l'application frontend
```bash

 npm run serve

```

---

### 🤖 Ollama 
- Télécharger et installer Ollama : https://ollama.com
- Lancer le modele Ollama : 
```bash

 ollama run llama3.2

```

---

## 🤝 Contribution
Les contributions sont bienvenues ! N'hésitez pas à forker, ouvrir une issue ou proposer une PR.

---

## 📜 Licence
Projet open source.

---

## 👨‍💻 Réalisé par

Ce projet a été réalisé dans le cadre du Master Expert dev à Ynov Bordeaux (projet fictif) par :

- Jeffrey Pons
- Nada Taha
- Fatma Bellout

---