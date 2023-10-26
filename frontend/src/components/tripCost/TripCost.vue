<template>
  <v-container class="custom-container">
    <v-row>
      <v-col class="m-1">
        <v-row>
         <v-select
          v-model="numberOfCompanions"
          :items="[1, 2, 3, 4]"
          label="동행자 수"
          @update:modelValue="clickComNumber"
          :disabled="textDisabled"
          class="margin-right"
        ></v-select>
        </v-row>
      </v-col>
      <v-col v-for="(companion, index) in companions" :key="index" class="m-1">
        <v-row>
         <v-text-field 
            outlined class="gray-text-field margin-right"
            :label="`동행자 ${index + 1}`"
            :rules="companionNameRules"
            :disabled="textDisabled"
            v-model="companion.tripCompanionNm">
        </v-text-field>
        </v-row>
      </v-col>
      <v-col class="m-4">
      <template v-if="companionEdit">
          <v-row>
            <v-btn @click="companionApply" class="button-style center-button">적용</v-btn>
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
    
    <!-- 밑줄 -->
    <v-row class="underline-row">
      <v-col>
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row class="overlay" v-if="isOverlayVisible" @click="hideOverlay">
        <!-- 불투명한 영역 -->
    </v-row>
    <TripPublicCost 
      v-if="isPublicCostVisible"/>

      <!-- 밑줄 -->
      <!-- <v-row class="underline-row"  v-if="contentFlag">
        <v-col>
          <v-divider></v-divider>
        </v-col>
      </v-row> -->

      <v-row  v-if="contentFlag">
        <v-col>
          <div>
            <span class="subtitle-detail">개인 비용</span>
          </div>
        </v-col>
      </v-row>

      <v-row  v-if="contentFlag">
        <v-col v-for="(costs, index) in privateCosts" :key="index" :cols="4">
          <v-card class="custom-card">
            <v-card-title class="title margin-bottom">
              동행자 {{ index+1 }}
              ( {{ costs.companionName }} )
            </v-card-title>
            <v-card-text class="schedule-contents">
              <v-row class="icon-row">
                <v-col :cols="1">
                  <v-icon class="icon">mdi-currency-krw</v-icon>
                </v-col>
                <v-col :cols="11">
                  <h3 class="subtitle">여행 비용</h3>
                    {{ costs.cost.toLocaleString() }} 원
                </v-col>
              </v-row>

              <v-row class="icon-row">
                <v-col :cols="1">
                  <v-icon class="icon">mdi-comment-outline</v-icon>
                </v-col>
                <v-col :cols="11">
                  <h3 class="subtitle">여행 비용 리스트</h3>
                  <ul v-if="costs.costNote">
                      <li v-for="(note, index) in costs.costNote.split('/').filter(value => value.trim() !== '').slice(0,6)" :key="index">
                        {{ note.length > 20 ? note.slice(0, 20) + ' ...' : note.trim() }}
                      </li>
                  </ul>
                </v-col>
              </v-row>
            </v-card-text>
            <v-row class="icon-row">
              <v-col :cols="12" class="d-flex justify-end">
                  <v-btn class="button-margin button-style" @click="viewCost(index)">비용 보기</v-btn>
              </v-col>
            </v-row>
          </v-card>
        </v-col>
      </v-row>
      <TripPrivateCost 
        v-model="isModalVisible"
        @closeDialog="closeDialog"/>

  </v-container>
</template>


<script>
import TripPrivateCost from './TripPrivateCost.vue';
import TripPublicCost from './TripPublicCost.vue';

export default {
  components: {
    TripPrivateCost,
    TripPublicCost
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
        companionExistFlag: false,
        isModalVisible: false,
        isPublicCostVisible: false,
        tripProjectNo: "",
        tripUserNo: "",
        numberOfCompanions: 3, // 동행자 수
        companions: Array.from({ length:3 }, () =>({
           tripProjectNo: "",
           tripCompanionNm: "",
           tripCompanionOrder: "",
        })),
        // 각 동행자의 이름을 담을 배열
        isOverlayVisible: true, // 불투명한 영역의 표시 여부
        textDisabled: false,
        companionEdit: true,
        contentFlag: false,
        companionNameRules:[
          v => !!v || '동행자 이름은 필수사항 입니다.',
          v => /^[가-힣a-zA-Z\s]*$/.test(v) || '동행자 이름은 한글/영어만 입력 가능합니다.',
          v => !( v && v.length > 15) || '동행자 이름은 8자 이상 입력할 수 없습니다.',
          // v => this.checkCompanion(v)
        ],
        privateCosts: [
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집' },
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집/맛있엉', },
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집/숙소가는데왤케멀어', },
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집/', },
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집', },
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집', },
        ],
    };
  },
  methods: {
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

        console.log(response);

      }).catch(error => {
        console.log(error);
      }) 
    },

    companionApply() {
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
        }) 
      }else{  // 동행자 수정
         this.$axios.put('/trip/companion/info/list', tripCompanion)
        .then(response=>{
          console.log(response);
        }).catch(error => {
          console.log(error);
        }) 
      }

      this.textDisabled = true;       // 동행자 입력란
      this.contentFlag = true;        // 임시 지워야할듯
      this.isOverlayVisible = false;  // 오버레이(불투명창)
      this.companionEdit= false;      // 동행자 입력란
      this.isPublicCostVisible=true;  // 공통 비용 부분
      
    },

    companionModify(){
      this.textDisabled = false;
      this.contentFlag = false;
      this.isOverlayVisible = true;
      this.companionEdit= true;
    },

    clickComNumber(){
        // this.companions = Array(this.numberOfCompanions).fill('');
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
    viewCost(){
      // this.isModalVisible = true 모달 표시
      console.log("viewCost isModalVisible", this.isModalVisible);
      this.isModalVisible = true;
    },
    allReset(){

    },
    closeDialog(){
      this.isModalVisible = false;
    }
  },
};
</script>

<style scoped>

.custom-container {
  width: 80%;
}

.custom-card {
  height: 100%;
}

.subtitle-detail {
  margin-top: -50px;
  color: #333;
  font-size: 17px;
  font-weight: bold;
}

.button-style{
   background-color: #333;
   color: #fff;
   font-size: 13px;
   margin-left: 5px;
   margin-top: 10px; /* 적용 버튼을 조금 아래로 이동 */
}

/* v-text-field의 간격을 좁게 조절하는 부분 */
.v-text-field{
    width: 100%; /* 100%로 변경 */
}

.underline-row{
    margin-top: -20px;
}

.overlay {
  position: fixed;
  width: 78%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 불투명한 배경 색상 */
  z-index: 999; /* 다른 컴포넌트 위에 표시되도록 설정 */
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: not-allowed; /* 클릭을 막음 */
}

.v-text-field {
  /* width: 100%; 전체 화면의 80%로 조절 */
  margin-top: 10px;
}

.button-margin{
    margin-left: 10px;
    margin-right: 10px;
    margin-bottom: 10px;
}

.center-button {
  display: flex;
  align-items: center;
  margin-top: 20px; 
}

.margin-right{
  margin-right: 10px;
}

.schedule-contents{
  height: 250px;
}

.subtitle {
  color: #333;
  margin-bottom: 10px;
}

.title {
  background-color: #333;
  color: #fff;
  padding-left: 10px;
  padding-right: 0px;
  padding-top: 0px;
  padding-bottom: 0px;
  font-size: 15px;
}

.margin-bottom{
  margin-bottom: 20px;
}

</style>