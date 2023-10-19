<template>
  <div class="modal-container" v-if="overlay">
    <!-- 투명한 배경 -->
    <div class="overlay" @click="closeOverlay"></div>

   <TripSelectStart
    @closeDialog="closeDialog"
    @submitTrip="submitTrip"
    :class="{'disabled-modal': this.startFlag}"/>

   <TripSelectDetail
    @closeDialog="closeDialog"
    @openStartDialog="openStartDialog(true)"
    :class="{'disabled-modal': this.detailFlag}"/>
   
    
  </div>
</template>

<script>
import TripSelectStart from './TripSelectStart.vue'
import TripSelectDetail from './TripSelectDetail.vue'



export default {
  components: { 
    TripSelectStart,
    TripSelectDetail
  },
  name: "TripSelect",

  data() {
    return {
      overlay: true,
      startFlag: false,
      detailFlag: true,
      name: "",
      email: "",

    };
  },
  mounted(){
    // Footer.vue에서 openDialog 이름으로 걸어준 메서드의 실체부
     this.emitter.on('openSelect', (data)=>{
             this.overlay = data;
             this.startFlag = !data;
             this.detailFlag = data;
     });
  },
  methods: {
    openStartDialog(flag) {
      this.overlay = flag;
      this.startFlag = !flag;
      this.detailFlag = flag;
    },
    openDetailDialog(flag) {
      this.overlay = flag;
      this.startFlag = flag;
      this.detailFlag = !flag;
    },
    closeDialog() {
      this.overlay = false;
    },

    submitTrip(flag) {
      this.openDetailDialog(flag);
    },
    closeOverlay() {
      // 투명한 배경 클릭 시 팝업 창 닫기
      this.closeDialog();
    },
  },
};
</script>

<style scoped>
/* 기존 스타일 유지 */
.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.disabled-modal{
  display: none;
}




</style>
