import { createRouter, createWebHistory } from "vue-router";
// import TripSelect from "@/components/TripSelect.vue";

const routerOptions = [
  { path: "/select", name: "TripSelect", component: "TripSelect" },
];

const routes = routerOptions.map(route => {
  return {
    ...route,
    component: () => import(`../components/${route.component}.vue`)
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
