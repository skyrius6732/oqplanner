<template>
    <v-toolbar app>
      <v-toolbar-title>
        <router-link to="/select" style="cursor: pointer" class="logo-link">
          <v-icon>mdi-calendar-text</v-icon>
          {{ appTitle }}
        </router-link>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on" @click="exitTrip" class="exit-button">
            <v-icon>mdi-exit-to-app</v-icon>
          </v-btn>
        </template>
        <span>여행 나가기</span>
      </v-tooltip>
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
  methods: {
    exitTrip() {
      // 여행 나가기 버튼 클릭 시 동작

       // 메뉴 비활성화를 위한 Footer.vue에 이벤트 등록
      this.emitter.emit('exitTrip');
      // sessionStorage.removeItem('projectNoSession');
      // sessionStorage.removeItem('userNoSession');
      sessionStorage.clear()

      console.log('projectNoSession', sessionStorage.getItem("projectNoSession"));
      console.log('userNoSession',sessionStorage.getItem("userNoSession"));

      this.$router.push('/select'); 

     
    },
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

.exit-button {
  margin-right: 16px;
}
</style>
