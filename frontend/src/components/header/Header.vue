<template>
    <v-toolbar app>
      <span class="hidden-sm-and-up">
        <v-toolbar-side-icon @click="sidebar = !sidebar">
        </v-toolbar-side-icon>
      </span>
      <v-toolbar-title>
        <router-link to="/" style="cursor: pointer">
          {{ appTitle }}
        </router-link>
      </v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
</template>
<script>
export default {
  name: "App",
  data(){
    return {
      appTitle: 'OQPlanner',
      sidebar: false,
    //   menuItems: [
    //       { title: 'Home', path: '/home', icon: 'home' },
    //       { title: 'Sign Up', path: '/signup', icon: 'face' },
    //       { title: 'Sign In', path: '/signin', icon: 'lock_open' }
    //  ]
    }
  },
};
</script>

<style scoped>


</style>
