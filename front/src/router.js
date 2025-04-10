import { createRouter, createWebHistory } from 'vue-router';
import HomePage from './components/HomePage.vue';
import UploadPdf from "./components/UploadPdf.vue";
import PromptSettings from "./components/PromptSettings.vue";
// import Settings from '../pages/Settings.vue'; 

const routes = [
  {
    path: '/', 
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
//   {
//     path: '/parametres',
//     name: 'Settings',
//     component: Settings,
//   },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
