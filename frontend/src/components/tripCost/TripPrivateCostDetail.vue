<template>
    <v-dialog class="custom-dialog">
    <v-container class="custom-container">
        <v-row>
            <div class="close-button" @click="closeDialog"><v-icon>mdi-close</v-icon></div>
        </v-row>
        <v-row>
            <v-col>
                <div>
                     <span class="subtitle-detail">{{ tripCompanionNm }}의 개인 비용</span>
                </div>
            </v-col>
            <v-col style="width: 80px; text-align: right;" v-if="!isEditing">
            <v-btn @click="costModify" class="button-style">비용 수정</v-btn>
            </v-col>
            <v-col style="width: 80px; text-align: right;" v-if="isEditing">
            <v-btn @click="addRow" class="button-style"><v-icon>mdi-plus</v-icon></v-btn>
            <v-btn @click="removeRow" class="button-style"><v-icon>mdi-minus</v-icon></v-btn>
            <v-btn @click="costReset" class="button-style">비용 초기화</v-btn>
            <v-btn @click="costSave" class="button-style">비용 저장</v-btn>
            </v-col>

            <!-- 행의 높이를 50px로 가정 -->
            <v-table
                fixed-header
                :height="550"
                style="width: 100%"
                class="custom-table"
            >
                <thead>
                    <tr>
                        <th class="text-center" style="width: 10%">NO.</th>
                        <th class="text-center" style="width: 14%">사용처</th>
                        <th class="text-center" style="width: 13%">비용(원)</th>
                        <th class="text-center" style="width: 65%">메모</th>
                    </tr>
                </thead>
                <tbody>
                <tr
                    v-for="(item, index) in privateCosts"
                    :key="item.name"
                >
                <template v-if="!isEditing">
                    <td class="text-center">{{ item.costOrder }}</td>
                    <td class="text-center">{{ item.costUse }}</td>
                    <td class="text-center">{{ item.cost.toLocaleString() }}</td>
                    <td>{{ item.costNote }}</td>
                </template>
                <template v-else>
                <td class="text-center">
                    {{ item.costOrder }}
                </td>
                <td>
                    <v-text-field 
                    outlined class="gray-text-field"
                    :rules="costUseRules"
                    v-model="privateCosts[index].costUse" >
                    </v-text-field>
                </td>
                <td>
                    <v-text-field 
                    outlined class="gray-text-field"
                    :rules="costRules"
                    v-model="privateCosts[index].cost">
                    </v-text-field>
                </td>
                <td>
                    <v-text-field 
                    outlined class="gray-text-field"
                    :rules="costNoteRules"
                    v-model="privateCosts[index].costNote">
                    </v-text-field>
                </td>
                </template>   
                </tr>
                <tr v-if='privateCosts.length > 0'>
                    <td class="text-center">합계</td>
                    <td></td>
                    <td class="text-center">
                        <strong> {{ privateCostSum }} </strong>
                    </td>
                </tr>
                </tbody>
            </v-table>
        </v-row>
        </v-container>
    </v-dialog>
</template>

<script>
export default {
  components: {

  },
  name: 'TripPrivateCostDetail',
  //   props: ['isModalVisible'],
  created(){
    this.tripProjectNo = sessionStorage.getItem("projectNoSession");
    this.tripUserNo = sessionStorage.getItem("userNoSession");

    // // 임시코드 (빌드없이 프론트단 사용을 위한...)
    // // 추후에 지워야함
    // if(!sessionStorage.getItem("projectNoSession")){
    //   this.tripProjectNo = "c5bf464bf576";
    // }else{
    //   this.tripProjectNo = sessionStorage.getItem("projectNoSession")
    // }

    // if(!sessionStorage.getItem("userNoSession")){
    //   this.tripUserNo = "3bb8aff388ab";
    // }else{
    //   this.tripUserNo = sessionStorage.getItem("userNoSession")
    // }

  },
  mounted(){

    // TripPrivateCost.vue에서 전달받은 객체 
    this.emitter.on('submitPrivateDetail', (data)=>{
        this.tripCompanionNm = data.companionNm;
        this.tripCompanionNo = data.companionNo;

        this.costShow();
        console.log("this.companionNo", this.tripCompanionNo);
     });

     
  },
  data(){
    return {
        // modalVisible: this.isModalVisible,
        isEditing: false, // 수정 중인지 여부를 나타내는 플래그
        tripCompanionNm: '',
        tripCompanionNo: '',
        privateCostSum: 0,
        privateCosts: [{
                companionNo: '',
                companionNm: '',
                tripPrivatCostList: [
                    {
                    costNo: '',
                    costOrder: '',
                    costUse: '',
                    cost: 0,
                    costNote: '',
                    }
                ],  
        }],
        tripSaveCost: [],
        tripUdpateCost: [],
        tripDeleteCost: [],
        costUseRules:[
           v => !( v && v.length > 10 ) || '사용처 입력은 최대 10글자까지 가능합니다.( ' + v.length + '글자 )',
        ],
        costRules:[
           v => /^[0-9]*$/.test(v) || '숫자만 입력 가능합니다.',
        ],
        costNoteRules:[
          v => !( v && v.length > 30 ) || '메모 입력은 최대 30글자까지 가능합니다.( ' + v.length + '글자 )',
        ],
    }
  },
  methods:{
    costModify(){
      this.isEditing = true;
    },
    costShow(){

      this.privateCosts = [];
      this.privateCostSum = 0;

      const tripCost = {
        tripProjectNo: this.tripProjectNo,
        tripCompanionNo: this.tripCompanionNo,
      };

      this.$axios.get('/trip/cost/private/info/list', {
        params: tripCost
      }).then(response=>{
         let CostSum = 0;
          if (response.data && response.data.length > 0 && response.data[0].tripPrivateCostList) {
            this.privateCosts = response.data[0].tripPrivateCostList.map(item => {
              CostSum += parseInt(item.tripPrivateCost);
              return{
                "costNo": item.tripPrivateCostNo,
                "costOrder": item.tripPrivateCostOrder,
                "costUse" : item.tripPrivateCostUse,
                "cost" : item.tripPrivateCost,
                "costNote" : item.tripPrivateCostNote,
              }
            });
            this.privateCostSum = CostSum.toLocaleString();
          }

      }).catch(error => {
          console.log(error);
      }).finally(()=>{
        console.log("costShow this.privateCosts", this.privateCosts)
        this.isEditing = false;
      })
    },

    async deleteCost() {
      try {
        console.log('this.tripDeleteCost', this.tripDeleteCost);
        await this.$axios.delete('/trip/cost/private/info', {
          data: this.tripDeleteCost
        });
        console.log('Delete request completed.');
      } catch (error) {
        console.log('Error during delete request:', error);
      } finally {
        this.tripDeleteCost = [];
        this.costShow();
      }
    },

    async updateCost() {
      try {
        await this.$axios.put('/trip/cost/private/info/list', this.tripUdpateCost);
        console.log('Update request completed.');
      } catch (error) {
        console.log('Error during update request:', error);
      } finally {
        this.tripUdpateCost = [];
        this.costShow();
      }
    },

    async saveCost() {
      try {
        await this.$axios.post('/trip/cost/private/info', this.tripSaveCost);
        console.log('Save request completed.');
      } catch (error) {
        console.log('Error during save request:', error);
      } finally {
        this.tripSaveCost = [];
        this.costShow();
      }
    },

    async executeRequests() {
      // 먼저 delete 요청 수행
      await this.deleteCost();
      
      // 다음으로 update 요청 수행
      await this.updateCost();
      
      // 마지막으로 save 요청 수행
      await this.saveCost();

       // 다른 컴포넌트의 메서드 호출을 위한 이벤트 호출부 
      // (TripPrivateCostDetail.vue로 전달) 
      this.emitter.emit('callPrivateCostCostShow');
    },

    costSave(){

      let tripPrivateCostOrder = '';

      this.privateCosts.forEach(e => {
      
      tripPrivateCostOrder = e.costOrder != '' ? e.costOrder : '' ; 
      console.log('tripPrivateCostOrder', tripPrivateCostOrder);
      console.log('e.cost', e.cost);

      console.log('tripCompanionNo', this.tripCompanionNo);
        if(tripPrivateCostOrder != ''){ // Update Cost
          this.tripUdpateCost.push({
            tripProjectNo: this.tripProjectNo,
            tripPrivateCost: {
              tripPrivateCostNo: e.costNo,
              tripCompanionNo: this.tripCompanionNo,
              tripPrivateCostOrder : tripPrivateCostOrder,
              tripPrivateCostUse: e.costUse,
              tripPrivateCost: e.cost,
              tripPrivateCostNote: e.costNote,
            },
          });
        }else{  // Save Cost
          this.tripSaveCost.push({
            tripProjectNo: this.tripProjectNo,
            tripPrivateCost: {
              tripPrivateCostNo: e.costNo,
              tripCompanionNo: this.tripCompanionNo,
              tripPrivateCostUse: e.costUse,
              tripPrivateCost: e.cost,
              tripPrivateCostNote: e.costNote,
            },
          });
        }

      });

      this.executeRequests();
      
      // this.$refs.privateCostMethod.costShow();


      this.isEditing = false;
    },
    costReset(){
      this.privateCosts.forEach(e => {
        this.tripDeleteCost.push({
            tripProjectNo: this.tripProjectNo,
            tripPrivateCostNo: e.costNo,
            tripPrivateCostOrder: e.costOrder,
        });
      });

      this.privateCosts = [];

    },
    addRow() {
      // + 버튼 클릭 시 행 추가 로직
      this.privateCosts.push({
        costNo: '',
        costOrder: '',
        companionNo: this.companionNo,
        companionNm: this.companionNm,
        costUse: '',
        cost: '',
        costNote: '',
      });
    },
    removeRow() {
      // - 버튼 클릭 시 행 삭제 로직
      if (this.privateCosts.length > 1) {

        const removedRow = this.privateCosts.slice(-1)[0]; // 마지막 요소의 복사본을 가져옴
        // removedRow를 활용하여 원하는 작업 수행

        // costOrder가 없을 때만 받는 체크로직을 추가해야함(기존데이터가 아닌 데이터 삭제 시)
        const costOrder = removedRow.costOrder;
        if(costOrder != ''){
          this.tripDeleteCost.push({
            tripProjectNo: this.tripProjectNo,
            tripPrivateCostNo: removedRow.costNo,
            tripPrivateCostOrder: costOrder,
          });

           console.log('this.tripDeleteCost',this.tripDeleteCost);
        }

        this.privateCosts.pop();
      }
    },
    // viewCost(){
    //   // this.isModalVisible = true 모달 표시
    //   console.log("viewCost isModalVisible", this.isModalVisible);
    //   this.isModalVisible = true;
    // },
    allReset(){

    },
    closeDialog(){
        this.$emit("closeDialog");
    },
  },
}
</script>

<style scoped>
   /* .custom-container {
         width: 80%; 
    } */

    .custom-dialog {
        width: 70%;
        height: 75%;
        background: white;
        background-color: rgba(0, 0, 0, 0.5);
        border-radius: 20px; /* 모서리 둥글게 설정 */
    }
    .custom-table{
        width: 100%;
        border-radius: 20px; /* 모서리 둥글게 설정 */

    }
    
    .subtitle-detail {
        margin-top: -100px;
        color: white;
        font-size: 17px;
        font-weight: bold;
    }

    .button-style{
        background-color: #333;
        color: white;
        font-size: 13px;
        margin-left: 5px;
        margin-top: 10px; /* 적용 버튼을 조금 아래로 이동 */
    }

    .close-button {
        margin-left: auto; /* 오른쪽으로 이동 */
        background: white;
        color: #333;
        margin-top: -10px;
        margin-right: -15px;
        width: 25px; /* 너비 조절 */
        height: 25px; /* 높이 조절 */
        border-radius: 4px; /* 모서리 둥글게 설정 */
        cursor: pointer;
        padding-left: 1px; /* 내부 여백 설정 */
    }

    /* v-text-field의 간격을 좁게 조절하는 부분 */
    .v-text-field{
        width: 100%; /* 100%로 변경 */
        margin-top: 10px;
    }

    /* .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    } */

</style>