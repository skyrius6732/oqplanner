<template>
   <div class="modal-start">
      <!-- <div @click="closeDialog" class="close-button">
        <v-icon>mdi-close</v-icon>
      </div> -->
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
            :rules="companionNameRules"
            placeholder="여행 동행자 이름을 입력해 주세요." 
            class="no-cursor"></v-text-field> -->
        </v-form>
        <v-snackbar v-model="snackbar" timeout="3000" class="snackbar-center" flex>
          {{ tripPlanName }}, 여행자님 이용해 주셔서 감사합니다.
        </v-snackbar>
        <v-card-actions class="button-container">
          <!-- 확인 및 취소 버튼 -->
          <v-btn @click="submitCompanion" class="action-button button-style">같이가기</v-btn>
          <v-btn @click="submitTrip(this.flag)" class="action-button button-style">여행하기</v-btn>
        </v-card-actions>
          <v-alert 
          v-model="alert"
          class="disabled-alert  black-and-white"
          transition="scale-transition"
          closable
          prominent
          >
          {{ alertMessage }}
        </v-alert>
      </v-card>
    </div>
</template>
<script>
export default {
  name: "TripSelectStart",

  mounted(){
     
  },
  data(){
    return {
        snackbar: false, // snackbar를 숨기기 위한 상태 변수
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
        companionNameRules:[
          v => !!v || '동행자 이름은 필수사항 입니다.',
          v => /^[가-힣a-zA-Z\s]*$/.test(v) || '동행자 이름은 한글/영어만 입력 가능합니다.',
          v => !( v && v.length > 15) || '동행자 이름은 8자 이상 입력할 수 없습니다.',
          // v => this.checkCompanion(v)
        ],
        alertMessage: "", // v-alert에 표시할 메시지
        alert: false,
    }
  },
  methods: {
   
    closeDialog() {
      this.$emit("closeDialog");
    },
    async submitCompanion(){
      await this.$refs.form.validate().then(result => {
          if (result.valid) {
            const tripPlan ={
              tripPlannerNm: this.tripUserName,
              tripPlanNm: this.tripPlanName,
              tripFlag: 'oldFlag', 
            }

            let awsUrl = '/trip/plan/info';
            // let awsUrl = 'http://ec2-13-209-225-123.ap-northeast-2.compute.amazonaws.com:8080/oqplanner/trip/plan/info';
            // let url = '/oqplanner/trip/plan/info';

            this.$axios.get(awsUrl, { 
              params: tripPlan,
            }).then(planResponse => {
                const tripPlanNo = planResponse.data.tripPlanNo;

                if(tripPlanNo == undefined){
                  this.alertMessage = "존재하지 않는 여행 입니다.";
                  this.alert = true;
                }else{
                  // 기존에 있을 시 세션값 호출
                  this.snackbar = true;

                   this.$axios.get('/common/api/getSessionValue')
                   .then(sessionResponse => {
                    console.log("success sessionResponse");
                    console.log(sessionResponse.data);

                    // 세션값 저장
                    const projectNoSession = sessionResponse.data.projectNoSession;
                    const userNoSession = sessionResponse.data.userNoSession;


                    // sessionStorage 사용
                    sessionStorage.setItem('projectNoSession', projectNoSession);
                    sessionStorage.setItem('userNoSession', userNoSession);


                   
                    setTimeout(() => {
                      // 팝업창 클로징 부모 컴포넌트 closeDialog 등록 하여 이벤트 호출
                      this.$emit("closeDialog");
                      this.$router.push('/schedule');

                      // Vuex store의 login 액션을 호출하여 로그인 상태를 변경하여 채팅방 보여주기
                      this.$store.dispatch('login');
                    }, 3000);

         

          
                    
                  }).catch(error => {
                    // 전송 중 오류가 발생한 경우의 처리
                    console.error(error);
                  }).finally(()=>{
                    console.log('this.$refs', this.$refs);
                    
                    setTimeout(() => {
                       // 메뉴 비활성화를 위한 Footer.vue에 이벤트 등록
                      //  this.$refs.footer.registerEventHandlers();
                      this.emitter.emit('accompanyTrip');
                    }, 3000);
                   
                  })


                }
            })
            .catch(error => {
              // 전송 중 오류가 발생한 경우의 처리
              console.error(error);
            });
            
          }else{
            console.log('validate fail');
          }
        })
    },
    async submitTrip(flag){
      await this.$refs.form.validate().then(result => {
          if (result.valid) {
            const tripPlan ={
              tripPlannerNm: this.tripUserName,
              tripPlanNm: this.tripPlanName,
              tripFlag: 'newFlag', 
            }

            this.$axios.get('/trip/plan/info', { 
              params: tripPlan,
            }).then(planResponse => {
                  const tripPlanNo = planResponse.data.tripPlanNo;

                  if(tripPlanNo == undefined){
                  console.log("planRes", planResponse.data.tripPlanNo);


                  this.inputs.tripPlanName = this.tripPlanName;
                  this.inputs.tripUserName = this.tripUserName;
                  

                  // submitTrip의 이름으로 이벤트로 걸어
                  // TripSelect.vue에서 @submitTrip=submitTrip 실행
                  this.$emit("submitTrip", flag);
                  console.log(flag);

                  this.emitter.emit('submitDetail', this.inputs);

                }else{
                  this.alertMessage = "이미 생성된 여행 입니다.";
                  this.alert = true;
                }
            })
            .catch(error => {
              // 전송 중 오류가 발생한 경우의 처리
              console.error(error);
            });
            
          }else{
            console.log('validate fail');
          }
        })
        
    },
  }
};
</script>

<style scoped>

.black-and-white {
  background-color: #333;
  color: #fff; /* White text */
}

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

.disabled-alert{
  margin: auto;
  text-align: center;
  width: 100%; 
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.button-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.action-button {
  flex: 1;
  margin: 0 10px;
  color: #fff;
}

.action-button.primary {
  background-color: #333;
}

.button-style{
   background-color: #333;
   color: #fff;
   font-size: 13px;
   margin-left: 5px;
   margin-top: 10px; /* 적용 버튼을 조금 아래로 이동 */
}

.snackbar-center {
    position: fixed;
    top: 27%;
    left: 49.6%;
    transform: translate(-50%, -50%);
  }

</style>
