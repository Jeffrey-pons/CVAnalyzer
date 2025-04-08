import { createRouter, createWebHistory } from 'vue-router';
// import HomePage from '../pages/HomePage.vue'; 
import UploadPdf from "./components/UploadPdf.vue";
// import Settings from '../pages/Settings.vue'; 

const routes = [
//   {
//     path: '/',
//     name: 'Home',
//     component: HomePage,
//   },
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
