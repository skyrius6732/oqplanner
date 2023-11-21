<template>
  <v-app>
    <Header></Header>
    <v-main>
      <v-container fluid>
         <router-view></router-view>
      </v-container>
    </v-main>

    <Footer
      ref="footer">
    </Footer>
  </v-app>
</template>

<script>
import Header from './components/header/Header.vue';
import Footer from './components/footer/Footer.vue';



export default {
  name: 'App',

  components: {
    Header,
    Footer,
  },

  data: () => ({
    //
  }),
}
</script>
