<template>
   <!-- 팝업 창 -->
   <div class="modal-detail">
    <div @click="openStartDialog" class="close-button">
      <v-icon>mdi-close</v-icon>
    </div>
    <v-form ref="form" lazy-validation>
    <v-card class="custom-card">
      <v-text-field label="여행 이름" 
        v-model="tripPlanName" 
        placeholder="최대 8글자 까지만 입력해 주세요."
        :rules="tripPlanNameRules"
        class="gray-text-field no-cursor "
        disabled></v-text-field>
      <v-text-field label="여행자 이름" 
        v-model="tripUserName" 
        placeholder="여행자 본인 이름을 입력해 주세요."
        class="gray-text-field no-cursor"
        disabled></v-text-field>
      <v-select
        label="여행 연도"
        v-model="tripYear"
        :items="tripYears"
        item-title="string"
        item-value="value"
        class="no-cursor"
      ></v-select>
      <div class="date-fields">
      <v-text-field
        label="여행 시작 날짜"
        v-model="tripStartDate"
        placeholder="MMDD"  
        :rules="tripStartDateRules"
        class="no-cursor"
      ></v-text-field>
      <v-text-field
        label="여행 종료 날짜"
        v-model="tripEndDate"
        placeholder="MMDD"  
        :rules="tripEndDateRules"
        class="end-date-field no-cursor"
      ></v-text-field>
      </div>
      <v-checkbox 
        :label="checkboxLabel"
        v-model="showDetailFields"
        class="no-cursor"></v-checkbox>
      <div v-if="showDetailFields" class="detail-fields">
      <v-select
        label="여행 시작 시간"
        v-model="tripStartTime"
        :items="tripStartTimes"
        item-title="string"
        item-value="value"
        class="no-cursor"
      ></v-select>
      <v-select
        label="여행 일정 단위"
        v-model="tripPlanUnit"
        :items="tripPlanUnits"
        item-title="string"
        item-value="value"
        class="plan-unit-field no-cursor"
      ></v-select>
      </div>
      <v-card-actions>
        <v-btn @click="openStartDialog">이전으로</v-btn>
        <v-btn @click="makeTrip" color="primary">여행 만들기</v-btn>
      </v-card-actions>
      <!-- v-alert 추가 -->
      <v-alert 
        v-model="alert"
        type="warning"
        class="disabled-alert"
        transition="scale-transition"
        closable
         prominent
        >
        {{ alertMessage }}
      </v-alert>
    </v-card>
    </v-form>
  </div>
</template>
<script>
export default {
  name: "TripSelectDetail",
  components: {
  
  },
  data(){
    return {
      tripPlanName: "",
      tripUserName: "",
      companionName: "",
      tripYear: new Date().getFullYear(),
      tripYears: [],
      tripStartTime: "0900",
      tripStartTimes: [
        {string: "00시00분", value: "0000"}, {string: "01시00분", value: "0100"}, {string: "02시00분", value: "0200"},
        {string: "03시00분", value: "0300"}, {string: "04시00분", value: "0400"}, {string: "05시00분", value: "0500"},
        {string: "06시00분", value: "0600"}, {string: "07시00분", value: "0700"}, {string: "08시00분", value: "0800"},
        {string: "09시00분", value: "0900"}, {string: "10시00분", value: "1000"}, {string: "11시00분", value: "1100"},
        {string: "12시00분", value: "1200"}, {string: "13시00분", value: "1300"}, {string: "14시00분", value: "1400"},
        {string: "15시00분", value: "1500"}, {string: "16시00분", value: "1600"}, {string: "17시00분", value: "1700"},
        {string: "18시00분", value: "1800"}, {string: "19시00분", value: "1900"}, {string: "20시00분", value: "2000"},
        {string: "21시00분", value: "2100"}, {string: "22시00분", value: "2200"}, {string: "23시00분", value: "2300"},
      ],
      tripPlanUnit: 60,
      tripPlanUnits: [
        {string : "20분", value: 20},{string : "30분", value: 30},
        {string : "1시간", value: 60},{string : "2시간", value: 120},
        {string : "3시간", value: 180},{string : "4시간", value: 240},
        {string : "6시간", value: 360},
      ],
      showDetailFields: false,  // v-checkbox 상태를 추적하는 변수 추가
      tripPlanNameRules: [
          v => !!v || '여행 이름은 필수사항 입니다.',
          v => !( v && v.length > 8) || '여행이름은 8자 이상 입력할 수 없습니다.'
      ],
      tripStartDateRules: [
        v => !!v || '여행 시작 날짜는 필수사항 입니다.',
        v => /^[0-9]*$/.test(v) || '여행 시작 날짜는 숫자만 입력 가능합니다.',
        v => !( v && v.length != 4) || '여행 종료 날짜는 4자리 입니다.',
        v => this.checkDate(v),
      ],
      tripEndDateRules: [
        v => !!v || '여행 종료 날짜는 필수사항 입니다.',
        v => /^[0-9]*$/.test(v) || '여행 종료 날짜는 숫자만 입력 가능합니다.',
        v => !( v && v.length != 4) || '여행 종료 날짜는 4자리 입니다.',
        v => this.checkDate(v),
      ],
      tripStartDate: "",
      tripEndDate: "",
      checkboxLabel: "여행 상세 기간 입력시 체크 \n (체크하지 않을 시 기본값[09시 / 1시간] 사용)",
      alertMessage: "", // v-alert에 표시할 메시지
      alert: false,

    }
  },
  filters: {
    nl2br: function (value) {
      return value.replace(/\n/g, '<br>');
    },
  },
  created(){
   
  },
  mounted(){
     this.emitter.on('submitDetail', (data)=>{
        this.tripUserName = data.tripUserName;
        this.tripPlanName = data.tripPlanName;

        console.log("data.tripUserName :: " + data.tripUserName)
        console.log("data.companionName :: " + data.companionName)
     });

    for(let i=0; i < 30; i++){
        let value = 2010+i;
        this.tripYears[i] = {"string" : value+"년" , "value" : 2010+i};
    }

  },
  methods: {
    test(){
      this.alert = !alert
    },
    closeDialog() {
      this.$emit("closeDialog");
    },
    openStartDialog() {
      this.$emit("openStartDialog");
    },
    checkDate(date){
      const moment = require('moment');
      const checkFlag = moment(this.tripYear+date, "YYYYMMDD").isValid();

      if(!checkFlag){
        return "날짜 형식이 아닙니다.";
      }
    },
    async makeTrip(){// form validate() 사용할것 해야함!
      await this.$refs.form.validate().then(result => {
        if (result.valid) {
            const moment = require('moment');
            const startDateString = this.tripYear+this.tripStartDate;
            const endDateString = this.tripYear+this.tripEndDate;
            const formattedStartDate = startDateString.substring(0, 4)+"-"+parseInt(startDateString.substring(4, 6))+"-"+ startDateString.substring(6, 8);
            const formattedEndDate = endDateString.substring(0, 4)+"-"+parseInt(endDateString.substring(4, 6))+"-"+ endDateString.substring(6, 8);
            const startDateMoment = moment(formattedStartDate);
            const endDateMoment = moment(formattedEndDate);

            const planAllNum = endDateMoment.diff(startDateMoment, 'days')+1;
            const compareFlag = moment(startDateMoment).isBefore(endDateMoment);

            console.log(compareFlag);
            if(!compareFlag){
              this.alertMessage = "여행시작 날짜는 여행종료 날짜 보다 작아야합니다."
              this.alert = true;
            }else{  
              this.alertMessage = ""; // 에러 메시지 초기화
              this.alert = false;
              const tripUser = {
                tripUserName: this.tripUserName,
                tripProjYn: "Y",
              };

            
              const tripPlan = {
                tripPlanNm: this.tripPlanName,
                tripPlannerNm: this.tripUserName,
                tripPlanStDt: formattedStartDate,
                tripPlanEdDt: formattedEndDate,
                tripPlanDefaultYn : "Y",
                tripPlanStTime: this.tripStartTime,
                tripPlanTimeUnit: this.tripPlanUnit,
                tripPlanAllNum: planAllNum,
              }
              
              this.$axios.post('/trip/user/info', tripUser)
              .then(userResponse => {
                  // 성공적으로 전송된 경우의 처리
                  console.log(userResponse.data);
                  // userResponse

                  return this.$axios.post(`/trip/plan/info`, tripPlan);
                })
                .then(planResponse => {
                  console.log("success planResponse");
                  console.log(planResponse.data);

                  // session값 호출
                  return this.$axios.get('/common/api/getSessionValue')

                }).then(sessionResponse => {
                  console.log("success sessionResponse");
                  console.log(sessionResponse.data);

                  // 세션값 저장
                  const projectNoSession = sessionResponse.data.projectNoSession;
                  const userNoSession = sessionResponse.data.userNoSession;

                  // vue-session 모듈 인식을 못함..
                  // this.$session.set('projectNoSession', projectNoSession);
                  // this.$session.set('userNoSession', userNoSession);

                  // sessionStorage 사용
                  sessionStorage.setItem('projectNoSession', projectNoSession);
                  sessionStorage.setItem('userNoSession', userNoSession);


                  // 팝업창 클로징 부모 컴포넌트 closeDialog 등록 하여 이벤트 호출
                  this.$emit("closeDialog");

                  // router 이동
                  this.$router.push('/schedule');
                  

                }).catch(error => {
                  // 전송 중 오류가 발생한 경우의 처리
                  console.error(error);
                });
              
            }
        }
      })
    },
  }
};
</script>

<style scoped>
.modal-detail {
  background-color: white;
  padding: 20px;
  width: 35%;
  position: relative;
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

/* v-text-field의 간격을 좁게 조절하는 부분 */
.v-text-field, .v-select, .v-checkbox{
  margin-bottom: -5px; /* 간격을 조절할 크기로 설정 (원하는 값으로 조절) */
}

.date-fields, .detail-fields{
  display: flex;
  justify-content: space-between;
}

.end-date-field , .plan-unit-field {
  margin-left: 10px; /* 조절할 간격 크기 */
}

.gray-text-field {
  color: black;
}

.v-card-actions {
  justify-content: flex-end;
}

.disabled-alert{
  margin: auto;
  text-align: center;
  width: 50%; 
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.scale-transition {
  transition: transform 0.3s;
}
</style>
