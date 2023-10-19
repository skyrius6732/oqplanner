<template>
  <v-bottom-navigation grow>
    <!-- <v-btn
        v-for="menu in menus"
        :key="menu.title"
        :to="menu.path"
        @click="openPage(menu.title)"
        :disabled="menu.title != 'Select' && isSelecting"
      > -->
      <v-btn
        v-for="menu in menus"
        :key="menu.title"
        :to="menu.path"
        @click="openPage(menu.title)"
      >
       <v-icon>{{menu.icon}}</v-icon>
       <span>{{menu.text}}</span>
    </v-btn>
</v-bottom-navigation>
</template>

<script>

export default {
  name: 'AppFooter',
  data() {
    return {
      menus: [
        { title: 'Select', path: '/select', icon: 'mdi-home', text: '여행 선택' },
        { title: 'Schedule', path: '/schedule', icon: 'mdi-text-box-search-outline', text: '여행 일정' },
        { title: 'Cost', path: '/cost', icon: 'mdi-wallet', text: '여행 비용' },
        { title: 'Favorits', path: '/favorits', icon: 'mdi-star-box', text: '여행 즐찾' },
        { title: 'Share', path: '/share', icon: 'mdi-share-variant', text: '여행 추천' },
      ],

      //선택 중인지 여부를 나타내는 상태
      isSelecting: false,
    };
  },
  methods: {
    openPage(title) {
      if(title == 'Select'){
         // 여행 선택 시 true
        this.isSelecting = true;

        // 여행 선택 버튼 클릭 시 TripSelect.vue의 openDialog 메서드 호출
        this.emitter.emit('openSelect', true);
      }else if(title == 'Schedule'){
        // this.isSelecting = true;
        // this.emitter.emit('openSchedule', true);
      }
      else{
        // this.isSelecting = false;
      }
      
    },
  }
};
</script>

<style scoped>


</style>
