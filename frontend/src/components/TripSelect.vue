<template>
  <div class="modal-container" v-if="overlay">
    <!-- 투명한 배경 -->
    <div class="overlay" @click="closeOverlay"></div>

    <!-- 팝업 창 -->
    <div class="modal">
      <div @click="closeDialog" class="close-button">
        <v-icon>mdi-close</v-icon>
      </div>

      <v-card class="custom-card">
        <v-text-field label="이름" v-model="name" class="name-input no-cursor"></v-text-field>
        <v-text-field label="이메일" v-model="email" class="no-cursor"></v-text-field>

        <v-card-actions>
          <!-- 확인 및 취소 버튼 -->
          <v-btn @click="submitForm" color="primary">확인</v-btn>
          <v-btn @click="closeDialog">취소</v-btn>
        </v-card-actions>
      </v-card>
    </div>
  </div>
</template>

<script>


export default {
  name: "TripSelect",

  data() {
    return {
      overlay: true,
      name: "",
      email: "",
    };
  },
  mounted(){
     this.emitter.on('openDialog', (data)=>{
             console.log('date :: ' + data);
             this.overlay = data;
     });
  },
  methods: {
    openDialog() {
      this.overlay = true;
    },
    closeDialog() {
      this.overlay = false;
      // 폼 초기화
      this.name = "";
      this.email = "";
    },
    submitForm() {
      // 여기에서 입력 데이터를 처리하는 로직을 추가할 수 있습니다.
      // 예를 들면, API 호출 등
      console.log("이름:", this.name);
      console.log("이메일:", this.email);
      // 폼 닫기
      this.closeDialog();
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

.modal {
  background-color: white;
  padding: 20px;
  width: 70%;
  position: relative;
}

/* 수정된 부분 */
.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
  z-index: 2; /* 다른 요소들보다 위에 위치하도록 설정 */
}

.close-button v-icon {
  font-size: 24px; /* 원하는 크기로 조절 */
  width: 24px; /* 원하는 크기로 조절 */
  height: 24px; /* 원하는 크기로 조절 */
}

.no-cursor {
  caret-color: transparent; /* 커서를 투명하게 설정하여 감춤 */
}
/* ---------------- */

.custom-card {
  margin-top: 20px;
  position: relative;
}
</style>
