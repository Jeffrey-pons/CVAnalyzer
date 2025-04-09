import { createRouter, createWebHistory } from 'vue-router';
import HomePage from './components/HomePage.vue';
import UploadPdf from "./components/UploadPdf.vue";
// import Settings from '../pages/Settings.vue'; 

const routes = [
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
