<template>
  <v-container class="custom-container">
    <v-form ref="form" lazy-validation>
    <v-row>
      <v-col class="m-1">
        <v-row>
         <v-select
          v-model="numberOfCompanions"
          :items="[0, 1, 2, 3, 4]"
          label="여행 인원을 선택 해주세요(본인 포함)"
          @update:modelValue="clickComNumber"
          :disabled="companionNumDisabled"
          class="margin-right"
        ></v-select>
        </v-row>
        <v-snackbar v-model="snackbar" timeout="3000" class="snackbar-center" flex>
          여행자 수는 한번 적용 후 변경이 불가하오니 참고 부탁드립니다.
        </v-snackbar>
      </v-col>
      <v-col v-for="(companion, index) in companions" :key="index" class="m-1">
        <v-row>
         <v-text-field 
            outlined class="gray-text-field margin-right"
            :label="`여행자 ${index + 1}`"
            :rules="companionNameRules"
            :disabled="companionNmDisabled"
            v-model="companion.tripCompanionNm"
            @keyup.enter="companionApply">
        </v-text-field>
        </v-row>
      </v-col>
      <v-col class="m-4">
      <template v-if="companionEdit">
          <v-row>
             <v-btn @click="companionApply" 
             :class="{'apply-button-style-mobile center-button' : this.mobileFlag === true,
                       'apply-button-style center-button' : this.mobileFlag === false 
             }"
             >적용</v-btn>
          </v-row>
      </template>
      <template v-else>
          <v-row>
            <v-btn v-if="numberOfCompanions != 0" @click="companionModify" 
            :class="{'apply-button-style-mobile-modify-reset center-button' : this.mobileFlag === true,
                       'apply-button-style center-button' : this.mobileFlag === false 
             }">여행자 수정</v-btn>
            <v-btn @click="startShowResetDialog" :class="{'apply-button-style-mobile-modify-reset center-button' : this.mobileFlag === true,
                       'apply-button-style center-button' : this.mobileFlag === false 
             }">전체 초기화</v-btn>
          </v-row>
      </template>
      </v-col>
      <!-- <v-col class="m-7"></v-col> -->
    </v-row>
    </v-form>
    
    <!-- 밑줄 -->
    <v-row class="underline-row">
      <v-col>
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row v-if="isOverlayVisible" @click="hideOverlay">
      <div class="overlay">
        <span class="no-results-text">적용 버튼을 눌러 여행 비용을 등록 해주세요.</span>
        <!-- 불투명한 영역 -->
      </div>
    </v-row>
    <TripPublicCost 
      v-if="isCostVisible"/>

    <TripPrivateCost
     v-if="isCostVisible"
     :companions="companions"
     ref="privateCostMethod"
     @viewCost="viewCost"/>

    <TripPrivateCostDetail 
        v-model="isModalVisible"
        @closeDialog="closeDialog"/>

  </v-container>

  <v-dialog
      v-model="resetDialog"
      persistent
      width="auto"
    >
      <v-card>
        <v-card-title :class="{'text-h5': this.mobileFlag === false,
        'mobile-font': this.mobileFlag === true}"
        >
          여행 비용 전체 초기화 하시겠습니까?
        </v-card-title>
        <v-card-text>등록 하신 여행자, 여행 공통비용, 여행 개인비용이 모두 삭제 됩니다.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            variant="text"
            @click="resetDialog = false"
            class="button-style"
          >
            취소
          </v-btn>
          <v-btn
            variant="text"
            @click="startReset"
            class="button-style"
          >
            확인
          </v-btn>
        </v-card-actions>
      </v-card>
  </v-dialog>
</template>


<script>
import TripPrivateCostDetail from './TripPrivateCostDetail.vue';
import TripPublicCost from './TripPublicCost.vue';
import TripPrivateCost from './TripPrivateCost.vue';

export default {
  components: {
    TripPrivateCostDetail,
    TripPublicCost,
    TripPrivateCost
  },
  computed: {
  // applyButtonClass() {
    
  //   if (this.isMobile()) {
  //     return 'apply-button-style-mobile center-button';
  //   } else {
  //     return 'apply-button-style center-button';
  //   }
  // }
  },
  created(){
    this.isMobile();
    this.tripProjectNo = sessionStorage.getItem("projectNoSession");
    this.tripUserNo = sessionStorage.getItem("userNoSession");
    console.log('projectNoSession', sessionStorage.getItem("projectNoSession"));
      console.log('userNoSession',sessionStorage.getItem("userNoSession"));
     
    // 임시코드 (빌드없이 프론트단 사용을 위한...)
    // 추후에 지워야함
    // if(sessionStorage.getItem("projectNoSession")){
    //   this.tripProjectNo = "c5bf464bf576";
    // }else{
    //   this.tripProjectNo = sessionStorage.getItem("projectNoSession")
    // }

    // if(sessionStorage.getItem("userNoSession")){
    //   this.tripUserNo = "3bb8aff388ab";
    // }else{
    //   this.tripUserNo = sessionStorage.getItem("userNoSession")
    // }

    console.log(this.tripProjectNo);
    console.log(this.tripUserNo);



  },
  mounted(){
    this.companionSelect();
  },
  data() {
    return {
        resetDialog: false,
        snackbar: false, // snackbar를 숨기기 위한 상태 변수
        companionExistFlag: false,
        isModalVisible: false,
        isCostVisible: false,
        tripProjectNo: "",
        tripUserNo: "",
        numberOfCompanions: 0, // 여행자 수
        companions: Array.from({ length:3 }, () =>({
           tripProjectNo: "",
           tripCompanionNm: "",
           tripCompanionOrder: "",
        })),
        // 각 동행자의 이름을 담을 배열
        isOverlayVisible: true, // 불투명한 영역의 표시 여부
        companionNmDisabled: false,
        companionNumDisabled: false,
        companionEdit: true,
        companionNameRules:[
          v => !!v || '여행자 이름은 필수사항 입니다.',
          v => /^[가-힣a-zA-Z\s]*$/.test(v) || '여행자 이름은 한글/영어만 입력 가능합니다.',
          v => !( v && v.length > 8) || '여행자 이름은 8자 이상 입력할 수 없습니다.',
          // v => this.checkCompanion(v)
        ],
        mobileFlag: "",
    };
  },
  methods: {
     isMobile() {
      // 모바일 화면 여부를 확인하는 로직을 여기에 추가
      // 윈도우 객체에서 innerWidth 속성을 사용하여 현재 창의 너비를 가져옴
      const screenWidth = window.innerWidth;
      if( screenWidth > 768){
        this.mobileFlag = false;
      }else{
         this.mobileFlag = true;
      }

    },
    startShowResetDialog(){
       this.resetDialog = true;
    },
    startReset(){
      this.resetDialog = false;
      this.allReset();
    },
    companionSelect(){
      
      const tripCompanion = {
         tripProjectNo: this.tripProjectNo,
      }

      this.$axios.get('/trip/companion/info/list', {
        params: tripCompanion, 
      })
      .then(response=>{
        
        let arrayLength = response.data.length;
        this.companionExistFlag = arrayLength > 0 ? true : false; 
        this.companions = response.data;
        this.numberOfCompanions = arrayLength;



        console.log('companionSelect response', response);

      }).catch(error => {
        console.log(error);
      }).finally(()=>{
        if(this.companionExistFlag){
          this.companionNumDisabled = true;
        }else{
          this.companionNumDisabled = false;
        }
      }) 
    },

    async companionApply() {
      // validate 메서드를 await로 감싸서 프로미스를 반환하도록 변경
      const result = await this.$refs.form.validate();

      // validate 메서드의 반환값을 확인
      if (result.valid) {
        const tripCompanion = this.companions.map(item => {
          console.log("item", item);
          return {
            tripProjectNo: this.tripProjectNo,
            tripCompanionNm: item.tripCompanionNm,
            tripCompanionOrder: item.tripCompanionOrder,
          };
        });

        if (this.companionExistFlag == false) {
          // 여행자 신규 저장
          console.log('여행자 신규 저장');
          for (const element of this.companions) {

                console.log('저장시 this.companions element', element);
                // this.tripProjectNo 추가
                const elementWithProjectNo = { ...element, tripProjectNo: this.tripProjectNo };
                console.log('저장시 this.companions element', elementWithProjectNo);

                // await 사용
                try {
                  const response = await this.$axios.post('/trip/companion/info', elementWithProjectNo);
                  console.log('new companion', response.data);
                  // companionNo를 기존 데이터에 추가
                  const companionWithNo = { ...elementWithProjectNo, tripCompanionNo: response.data };
                  // 기존 companions를 새로운 배열로 교체
                  this.companions = this.companions.map(c => (c === element ? companionWithNo : c));
                } catch (error) {
                  console.log(error);
                }
          }
        } else {
          // 여행자 수정
          console.log('여행자 기존 수정');
          try {
            const response = await this.$axios.put('/trip/companion/info/list', tripCompanion);
            console.log(response);
          } catch (error) {
            console.log(error);
          }
        }

        this.isCostVisible = true;
        this.companionNumDisabled = true;
        this.companionNmDisabled = true;
        this.isOverlayVisible = false;
        this.companionEdit = false;
      }
      // 모든 저장이 완료된 후에 하위 컴포넌트 mount
      this.$nextTick(() => {
        // 여기서 하위 컴포넌트의 로직을 실행하거나 필요한 동작을 수행할 수 있습니다.
      });
    },
  

    companionModify(){
      this.isCostVisible = false;  // 공통 비용 부분
      this.companionNmDisabled = false;
      this.isOverlayVisible = true;
      this.companionEdit= true;
    },

    companionReset(){
      this.isCostVisible = false;  // 공통 비용 부분
      this.companionNmDisabled = false;
      this.companionNumDisabled = false;
      this.isOverlayVisible = true;
      this.companionEdit= true;
      this.numberOfCompanions = 0;
      this.companions = [];
    },

    clickComNumber(){
        // this.companions = Array(this.numberOfCompanions).fill('');
        this.snackbar = true;
        this.companions = Array.from({ length:this.numberOfCompanions }, () =>({
           tripProjectNo: "",
           tripCompanionNm: "",
           tripCompanionOrder: "",
        }));
    },
    hideOverlay() {
      // 불투명한 영역을 감추는 메서드
      // this.isOverlayVisible = false;
    },
    viewCost(index){
      // 이 부분에서 index 값을 사용할 수 있습니다.
      console.log('Received index from TripPrivateCost:', index);
      // this.isModalVisible = true 모달 표시
      console.log("viewCost isModalVisible", this.isModalVisible);
      this.isModalVisible = true;
    },
    allReset(){

      let param = {
        tripProjectNo : this.tripProjectNo,
      }
      this.$axios.delete('/trip/cost/info/all', {data: param})
      .then(() => {

          return this.$axios.delete(`/trip/companion/info`, param); 
      })
      .then(() => {

      }).catch(()=>{
          
      }).finally(()=>{
          // this.isCostVisible=true;  // 공통 비용 부분
          // this.companionNumDisabled=true; // 여행자 수 입력란
          // this.companionNmDisabled = true; // 여행자 입력란      
          // this.isOverlayVisible = false;  // 오버레이(불투명창)
          // this.companionEdit= false;      // 여행자 입력란
          // this.$router.push('cost');
          this.companionReset();

      })
    },
    closeDialog(){
      this.$refs.privateCostMethod.costShow();
      this.isModalVisible = false;
    },

  },
};
</script>

<style scoped>

.custom-container {
  width: 80%;
}


.button-style{
   background-color: #333;
   color: #fff;
   font-size: 13px;
   margin-left: 5px;
   margin-top: 10px; /* 적용 버튼을 조금 아래로 이동 */
}

.apply-button-style{
   background-color: #333;
   color: #fff;
   font-size: 15px;
   margin-left: 5px;
   font-weight: bold;
   height: 55px;
}

/* v-text-field의 간격을 좁게 조절하는 부분 */
.v-text-field{
    width: 100%; /* 100%로 변경 */
}

.underline-row{
    margin-top: -20px;
}

.overlay {
  position: absolute;
  width: 78%;
  height: 70%;
  background-color: rgba(0, 0, 0, 0.5); /* 불투명한 배경 색상 */
  z-index: 999; /* 다른 컴포넌트 위에 표시되도록 설정 */
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: not-allowed; /* 클릭을 막음 */
  border-radius: 10px;
}

.v-text-field {
  /* width: 100%; 전체 화면의 80%로 조절 */
  margin-top: 10px;
}

.center-button {
  display: flex;
  align-items: center;
  margin-top: 13px; 
}

.margin-right{
  margin-right: 10px;
}


  .snackbar-center {
    position: fixed;
    top: 10%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

.no-results-text {
    color: white; /* 흰색 글씨 */
    font-size: 18px;
    font-weight: bold;
}


.apply-button-style-mobile {
  /* 모바일 버튼 스타일 */
  font-size: 12px;
  padding: 8px 16px;
  height: 50px;
  background-color: #333;
  color: #fff;
  font-size: 15px;
  font-weight: bold;
}

.apply-button-style-mobile-modify-reset {
  font-size: 8px;
  height: 20px;
  background-color: #333;
  color: #fff;
  font-weight: bold;
}

.mobile-font{
  font-size: 16px;
  font-weight: bold;
}

@media screen and (max-width: 768px) {
  .custom-container {
    width: 100%; /* 모바일 화면에 대응하여 전체 너비로 조정 */
  }

  .button-style,
  .apply-button-style {
    font-size: 10px; /* 버튼 글자 크기를 줄임 */
  }

  .center-button {
    margin-top: 10px; /* 버튼 간격을 조절 */
  }

  .margin-right {
    margin-right: 5px; /* 여행자 수 선택 항목과 텍스트 필드 간격을 조절 */
  }

  .v-text-field {
    margin-top: 5px; /* 텍스트 필드 간격을 조절 */
  }

  .snackbar-center {
    top: 5%; /* 스낵바 위치를 조정 */
  }

  .no-results-text {
    font-size: 14px; /* 텍스트 크기를 줄임 */
  }

  .overlay {
    width: 90%;
    height: 65%;

  }
}


</style>