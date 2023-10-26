import { createRouter, createWebHistory } from "vue-router";
// import TripSelect from "@/components/TripSelect.vue";

const routerOptions = [
  { path: "/", name: "", component: "", menu: "" },
  { path: "/select", name: "TripSelect", component: "TripSelect", menu: "tripSelect" },
  { path: "/schedule", name: "TripSchedule", component: "TripSchedule", menu: "tripSchedule" },
  { path: "/cost", name: "TripCost", component: "TripCost", menu: "tripCost" },
  { path: "/favorits", name: "", component: "", menu: "" },
  { path: "/share", name: "", component: "", menu: "" },
];

const routes = routerOptions.map(route => {
  return {
    ...route,
    component: () => import(`../components/${route.menu}/${route.component}.vue`)
  };
});

// const routes = [
//   // path별 view 등록
//   {
//       path:'/select',
//       // component: () => import("../components/TripSelect.vue"),
//       name: "TripSelect",
//       component: TripSelect,
//   },
// ]


const router = createRouter({
  history: createWebHistory(),
  routes
});


// path 별 메서드 호출

// router.beforeEach((to, from, next) => {
//   if (to.path === '/select') {
//     console.log('Going to /select');
//   }
//   next();
// });

export default router;
