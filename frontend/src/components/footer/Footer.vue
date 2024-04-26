<template>
  <v-bottom-navigation flex>
      <v-btn
        v-for="menu in menus"
        :key="menu.title"
        :to="menu.path"
        @click="openPage(menu.title)"
        :disabled="menu.title === 'Select' ? !isSelecting : isSelecting"
      >
      <!-- 위의 버튼이 작동하는 부분  disabled 차이 -->
      <!-- 아래는 개발을 위한 임시 부분 -->
       <!-- <v-btn
        v-for="menu in menus"
        :key="menu.title"
        :to="menu.path"
        @click="openPage(menu.title)"
      
      > -->
       <v-icon >{{menu.icon}}</v-icon>
       <span>{{menu.text}}</span>
       <span>{{menu.title}}</span>
    </v-btn>
   
</v-bottom-navigation>
</template>

<script>

export default {
  name: 'AppFooter',
  created(){
  
  },
  mounted(){

  
    this.emitter.on('makeTrip', ()=>{
      this.$nextTick(() => {
        // 여행이 생성되면 isSelecting 값을 false로 변경
        console.log('makeTrip emitter');
        this.isSelecting = false;

        // 상태를 저장
        localStorage.setItem('isSelecting', JSON.stringify(this.isSelecting));
      });
     });

    this.emitter.on('accompanyTrip', () => {
     this.$nextTick(() => {
        // 여행이 적용되면 isSelecting 값을 false로 변경
        console.log('accompanyTrip emitter');
        this.isSelecting = false;

         // 상태를 저장
        localStorage.setItem('isSelecting', JSON.stringify(this.isSelecting));
      });
    });

     this.emitter.on('exitTrip', ()=>{
       this.isSelecting = true;
        this.$nextTick(() => {
        // 여행을 나가면 isSelecting 값을 false로 변경
        console.log('exitTrip emitter');

        // 상태를 저장
        localStorage.setItem('isSelecting', JSON.stringify(this.isSelecting));
        });
     });
  },
  data() {
    return {
      menus: [
        { title: 'Select', path: '/select', icon: 'mdi-home', text: '여행 선택', isActive: true},
        { title: 'Schedule', path: '/schedule', icon: 'mdi-text-box-search-outline', text: '여행 일정', isActive: false },
        { title: 'Cost', path: '/cost', icon: 'mdi-wallet', text: '여행 비용', isActive: false },
        { title: 'Favorits', path: '/favorits', icon: 'mdi-star-box', text: '여행 즐찾', isActive: false },
        { title: 'Share', path: '/share', icon: 'mdi-share-variant', text: '여행 추천', isActive: false },
      ],

      //선택 중인지 여부를 나타내는 상태
      isSelecting: true,
    };
  },
  methods: {
    openPage(title) {
      if(title == 'Select'){
         // 여행 선택 시 true
        console.log('여행선택시');
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

  /* 아이콘과 텍스트의 크기를 줄입니다. */
.v-btn .v-icon {
  font-size: 15px; /* 아이콘 크기 조절 */
}

.v-btn span {
  font-size: 10px; /* 텍스트 크기 조절 */
}

.v-btn {
  padding: 0px; /* 위, 아래 여백을 조절하여 버튼의 크기를 줄입니다. */
  min-width: auto; /* 최소 너비를 자동으로 설정하여 내용에 맞게 버튼의 크기를 조절합니다. */
}

</style>
