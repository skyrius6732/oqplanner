<template>
    <v-toolbar app>
      <v-toolbar-title>
        <router-link to="/select" style="cursor: pointer" class="logo-link">
          <v-icon>mdi-calendar-text</v-icon>
          {{ appTitle }}
        </router-link>
      </v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
</template>
<script>
export default {
  name: "AppHeader",
  data(){
    return {
      appTitle: 'OQPlanner',
      sidebar: false,
    }
  },
};
</script>

<style scoped>

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  margin-left: 50px;
  color: inherit;
}

.logo-link v-icon {
  margin-right: 8px;
  font-size: 24px;
}
</style>
