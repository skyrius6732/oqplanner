<template>
   <div class="modal-start">
      <div @click="closeDialog" class="close-button">
        <v-icon>mdi-close</v-icon>
      </div>
        <v-card class="custom-card">
         <div class="welcome-message">
            <p class="welcome-text">환영합니다</p>
            <p class="app-name"><span class="accent-color">OQplanner</span>와 함께</p>
            <p class="invite-text">여행을 떠나시겠어요?</p>
        </div>
        <v-form ref="form" lazy-validation>
          <v-text-field 
            label="여행 이름" 
            v-model="tripPlanName" 
            placeholder="여행 이름을 입력해 주세요."
            :rules="tripPlanNameRules"
            class="name-input no-cursor">
          </v-text-field>
          <v-text-field 
            label="여행자 이름" 
            v-model="tripUserName" 
            placeholder="여행자 본인 이름을 입력해 주세요."
            :rules="tripUserNameRules"
            class="name-input no-cursor">
          </v-text-field>
          <!-- <v-text-field 
            label="동행자 이름" 
            v-model="companionName" 
            :rules="CompanionNameRules"
            placeholder="여행 동행자 이름을 입력해 주세요." 
            class="no-cursor"></v-text-field> -->
        </v-form>
        <v-card-actions>
          <!-- 확인 및 취소 버튼 -->
          <v-btn @click="submitCompanion" color="primary">동행하기</v-btn>
          <v-btn @click="submitTrip(this.flag)" color="primary">여행하기</v-btn>
        </v-card-actions>
      </v-card>
    </div>
</template>
<script>
export default {
  name: "TripSelectStart",
  data(){
    return {
        inputs: {},
        tripPlanName: "", //여행 이름 데이터
        tripUserName: "", // 여행자 이름 데이터
        companionName: "", // 동행자 이름 데이터
        flag: true,
        tripPlanNameRules: [
          v => !!v || '여행 이름은 필수사항 입니다.',
          v => !( v && v.length > 8) || '여행 이름은 8자 이상 입력할 수 없습니다.'
        ],
        tripUserNameRules: [
          v => !!v || '여행자 이름은 필수사항 입니다.',
          v => /^[가-힣a-zA-Z\s]*$/.test(v) || '여행자 이름은 한글/영어만 입력 가능합니다.',
          v => !( v && v.length > 15) || '여행자 이름은 8자 이상 입력할 수 없습니다.',
        ],
        CompanionNameRules:[
          v => !!v || '동행자 이름은 필수사항 입니다.',
          v => /^[가-힣a-zA-Z\s]*$/.test(v) || '동행자 이름은 한글/영어만 입력 가능합니다.',
          v => !( v && v.length > 15) || '동행자 이름은 8자 이상 입력할 수 없습니다.',
          // v => this.checkCompanion(v)
        ]
    }
  },
  methods: {
   
    closeDialog() {
      this.$emit("closeDialog");
    },
    submitCompanion(){

    },
    async submitTrip(flag){
      await this.$refs.form.validate().then(result => {
          if (result.valid) {
            console.log('validate pass');

            //   flags.startFlag = true;
            //   flags.overlay = true;
            
            console.log("여행 이름:", this.tripPlanName);
            console.log("여행자 이름:", this.tripUserName);

            this.inputs.tripPlanName = this.tripPlanName;
            this.inputs.tripUserName = this.tripUserName;
            

            // submitTrip의 이름으로 이벤트로 걸어
            // TripSelect.vue에서 @submitTrip=submitTrip 실행
            this.$emit("submitTrip", flag);
            this.emitter.emit('submitDetail', this.inputs);

            // this.$refs.form.reset();
          }else{
            console.log('validate fail');
          }
        })
        

   
       
    

    },
  }
};
</script>

<style scoped>
.modal-start {
  background-color: white;
  padding: 20px;
  position: relative;
  width: 20%;
}


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

.custom-card {
  margin-top: 20px;
  position: relative;
}

.welcome-message{
  margin-top: 10px;
  margin-bottom: 10px;
  position: relative;
  max-width: 500px; /* 최대 너비 설정 (원하는 값으로 조절) */
  width: 100%; /* 너비 100%로 설정하여 부모에 맞게 조절 */
}

.welcome-text {
  text-align: center;
  font-size: 15px; /* 글자 크기 조절 */
  color: #333; /* 글자 색상 설정 */
}

.app-name {
  text-align: center;
}

.accent-color {
  font-weight: bold;
  color: #333; /* 글자 색상 설정 */
}

.invite-text {
  text-align: center;
  color: #333; /* 글자 색상 설정 */
}

.v-card-actions {
  display: flex;
  justify-content: center;
}

</style>
