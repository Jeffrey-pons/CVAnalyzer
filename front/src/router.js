import { createRouter, createWebHistory } from 'vue-router';
import HomePage from './components/HomePage.vue';
import UploadPdf from "./components/UploadPdf.vue";
import PromptSettings from "./components/PromptSettings.vue";

const routes = [
  {
    path: '/',
    redirect: '/home-page'  // Redirection Homepage
  },
  {
    path: '/home-page', 
    name: 'HomePage',
    component: HomePage,
  },
  {
    path: '/analyse-cv',
    name: 'CVAnalysis',
    component: UploadPdf,
  },
  {
    path: "/parametres-prompts",
    name: "PromptSettings",
    component: PromptSettings,
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
