import { createRouter, createWebHistory } from "vue-router";
// import TripSelect from "@/components/TripSelect.vue";

const routerOptions = [
  { path: "/", redirect: "/select" },
  { path: "/select", name: "TripSelect", component: "TripSelect", menu: "tripSelect" },
  { path: "/schedule", name: "TripSchedule", component: "TripSchedule", menu: "tripSchedule" },
  { path: "/cost", name: "TripCost", component: "TripCost", menu: "tripCost" },
  { path: "/favorits", name: "TripFavorits", component: "TripFavorits", menu: "tripFavorits" },
  { path: "/share", name: "", component: "", menu: "" },
];

const routes = routerOptions.map(route => {
  return {
    ...route,
    component: () => import(`../components/${route.menu}/${route.component}.vue`)
  };
});


const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
