import { createRouter, createWebHistory } from "vue-router";
// import TripSelect from "@/components/TripSelect.vue";

const routerOptions = [
  { path: "/oqplanner", redirect: "/select" },
  { path: "/select", name: "TripSelect", component: "TripSelect", menu: "tripSelect" },
  { path: "/schedule", name: "TripSchedule", component: "TripSchedule", menu: "tripSchedule" },
  { path: "/cost", name: "TripCost", component: "TripCost", menu: "tripCost" },
  { path: "/favorits", name: "TripFavorits", component: "TripFavorits", menu: "tripFavorits" },
  { path: "/share", name: "TripRanking", component: "TripRanking", menu: "tripRanking" },
];

const routes = routerOptions.map(route => {
  return {
    ...route,
    component: () => import(`../components/${route.menu}/${route.component}.vue`)
  };
});


const router = createRouter({
  history: createWebHistory('/'),
  mode: 'history', // 'history' 모드 명시적으로 설정
  routes
});

export default router;
