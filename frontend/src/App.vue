<template>
  <v-app>
    <Header></Header>
    <v-main>
      <v-container fluid>
         <router-view></router-view>
      </v-container>
    </v-main>
    <template v-if="$store.getters.isLoggedIn">
      <chatRoom></chatRoom>
    </template>
    <Footer
      ref="footer">
    </Footer>
  </v-app>
</template>

<script>
import Header from './components/header/Header.vue';
import Footer from './components/footer/Footer.vue';
import chatRoom from './components/chatRoom/chatRoom.vue';

export default {
  name: 'App',

  components: {
    Header,
    Footer,
    chatRoom,
  },

  data: () => ({
    //
  }),
}
</script>

<style>

</style>
