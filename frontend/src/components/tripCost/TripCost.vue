<template>
  <v-container class="custom-container">
    <v-form ref="form" lazy-validation>
    <v-row>
      <v-col class="m-1">
        <v-row>
         <v-select
          v-model="numberOfCompanions"
          :items="[1, 2, 3, 4]"
          label="동행자 수"
          @update:modelValue="clickComNumber"
          :disabled="companionNumDisabled"
          class="margin-right"
        ></v-select>
        </v-row>
        <v-snackbar v-model="snackbar" timeout="3000" class="snackbar-center" flex>
          동행자 수는 한번 적용 후 변경이 불가하오니 참고 부탁드립니다.
        </v-snackbar>
      </v-col>
      <v-col v-for="(companion, index) in companions" :key="index" class="m-1">
        <v-row>
         <v-text-field 
            outlined class="gray-text-field margin-right"
            :label="`동행자 ${index + 1}`"
            :rules="companionNameRules"
            :disabled="companionNmDisabled"
            v-model="companion.tripCompanionNm">
        </v-text-field>
        </v-row>
      </v-col>
      <v-col class="m-4">
      <template v-if="companionEdit">
          <v-row>
            <v-btn @click="companionApply" size="x-large" class="apply-button-style center-button">적용</v-btn>
          </v-row>
      </template>
      <template v-else>
          <v-row>
            <v-btn @click="companionModify" class="button-style center-button">동행자 수정</v-btn>
            <v-btn @click="allReset" class="button-style center-button">전체 초기화</v-btn>
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
        <span v-if="!isLoading" class="no-results-text">적용 버튼을 눌러 여행 비용을 등록 해주세요.</span>
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
  created(){
    this.tripProjectNo = sessionStorage.getItem("projectNoSession");
    this.tripUserNo = sessionStorage.getItem("userNoSession");

     
    // 임시코드 (빌드없이 프론트단 사용을 위한...)
    // 추후에 지워야함
    if(!sessionStorage.getItem("projectNoSession")){
      this.tripProjectNo = "c5bf464bf576";
    }else{
      this.tripProjectNo = sessionStorage.getItem("projectNoSession")
    }

    if(!sessionStorage.getItem("userNoSession")){
      this.tripUserNo = "3bb8aff388ab";
    }else{
      this.tripUserNo = sessionStorage.getItem("userNoSession")
    }

    console.log(this.tripProjectNo);
    console.log(this.tripUserNo);



  },
  mounted(){
    this.companionSelect();
  },
  data() {
    return {
        snackbar: false, // snackbar를 숨기기 위한 상태 변수
        companionExistFlag: false,
        isModalVisible: false,
        isCostVisible: false,
        tripProjectNo: "",
        tripUserNo: "",
        numberOfCompanions: 1, // 동행자 수
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
        contentFlag: false,
        companionNameRules:[
          v => !!v || '동행자 이름은 필수사항 입니다.',
          v => /^[가-힣a-zA-Z\s]*$/.test(v) || '동행자 이름은 한글/영어만 입력 가능합니다.',
          v => !( v && v.length > 8) || '동행자 이름은 8자 이상 입력할 수 없습니다.',
          // v => this.checkCompanion(v)
        ],
    };
  },
  methods: {
    // checkCompanion(v){
    //   // if(v){

    //   // }
    // },
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



        console.log(response);

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
      await this.$refs.form.validate().then(result => {
        if (result.valid) {
              const tripCompanion = this.companions.map(item=> {
              console.log("item", item);
              return {
                tripProjectNo: this.tripProjectNo,
                tripCompanionNm: item.tripCompanionNm,
                tripCompanionOrder: item.tripCompanionOrder,
              }
          });

          if(this.companionExistFlag == false){  // 동행자 신규 저장
            this.$axios.post('/trip/companion/info', tripCompanion)
            .then(response=>{
              console.log(response);
            }).catch(error => {
              console.log(error);
            }).finally(()=>{

            }) 
          }else{  // 동행자 수정
            this.$axios.put('/trip/companion/info/list', tripCompanion)
            .then(response=>{
              console.log(response);
            }).catch(error => {
              console.log(error);
            }).finally(()=>{ 
              
            }) 
          }
          this.isCostVisible=true;  // 공통 비용 부분
          this.companionNumDisabled=true; // 동행자 수 입력란
          this.companionNmDisabled = true; // 동행자 입력란      
          this.contentFlag = true;        // 임시 지워야할듯
          this.isOverlayVisible = false;  // 오버레이(불투명창)
          this.companionEdit= false;      // 동행자 입력란
              
        }
      })
    },
  

    companionModify(){
      this.isCostVisible = false;  // 공통 비용 부분
      this.companionNmDisabled = false;
      this.contentFlag = false;
      this.isOverlayVisible = true;
      this.companionEdit= true;
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

</style>