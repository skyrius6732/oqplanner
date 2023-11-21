<template>
        <v-row>
          <v-col>
              <div>
                  <span class="subtitle-detail">공통 비용</span>
              </div>
          </v-col>
          <v-col style="width: 80px; text-align: right;" v-if="!isEditing">
            <v-btn @click="costModify" class="button-style">비용 수정</v-btn>
          </v-col>
          <v-col style="width: 80px; text-align: right;" v-if="isEditing">
            <v-btn @click="addRow" class="button-style"><v-icon>mdi-plus</v-icon></v-btn>
            <v-btn @click="removeRow" class="button-style"><v-icon>mdi-minus</v-icon></v-btn>
            <v-btn @click="costReset" class="button-style">비용 초기화</v-btn>
            <v-btn @click="costShow" class="button-style">비용 보기</v-btn>
            <v-btn @click="costSave" class="button-style">비용 저장</v-btn>
          </v-col>
        </v-row>
          <v-row>
          <v-form 
            ref='publicForm'
            style="width: 100%" 
            lazy-validation>
          <!-- 행의 높이를 50px로 가정 -->
          <v-table
              fixed-header
              :height="'{{(publicCosts.length * 50) + 20}}px'"
          >
         
              <thead>
                  <tr>
                      <th class="text-center">NO.</th>
                      <th class="text-center">사용처</th>
                      <th class="text-center">비용(원)</th>
                      <th class="text-center">메모</th>
                  </tr>
              </thead>
              <tbody>
              <tr
                  v-for="(item, index) in publicCosts"
                  :key="item.costOrder"
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
                    v-model="publicCosts[index].costUse" >
                  </v-text-field>
                </td>
                <td>
                  <v-text-field 
                    outlined class="gray-text-field"
                    :rules="costRules"
                    v-model="publicCosts[index].cost">
                  </v-text-field>
                </td>
                <td>
                  <v-text-field 
                    outlined class="gray-text-field"
                    :rules="costNoteRules"
                    v-model="publicCosts[index].costNote">
                  </v-text-field>
                </td>
              </template>
              
              </tr>
              <tr v-if='publicCosts.length > 0'>
                <td class="text-center">합계</td>
                <td></td>
                <td class="text-center">
                  <strong> {{ publicCostSum }}</strong>
                </td>
              </tr>
              </tbody>

          </v-table>
        </v-form>
        </v-row>
      
</template>

<script>
export default {
  components: {

  },
  created(){
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

  },
  mounted(){
    this.costShow();
  },
  data(){
    return {
        // modalVisible: this.isModalVisible,
        isEditing: false, // 수정 중인지 여부를 나타내는 플래그
        publicCosts: [],
        publicCostSum: 0,
        costUseRules:[
           v => !( v && v.length > 10 ) || '사용처 입력은 최대 10글자까지 가능합니다.( ' + v.length + '글자 )',
        ],
        costRules:[
           v => /^[0-9]*$/.test(v) || '숫자만 입력 가능합니다.',
        ],
        costNoteRules:[
          v => !( v && v.length > 30 ) || '메모 입력은 최대 30글자까지 가능합니다.( ' + v.length + '글자 )',
        ],
        tripSaveCost: [],
        tripUdpateCost: [],
        tripDeleteCost: [],
    }
  },
  methods:{
    costShow(){
      const tripCost = {
        tripProjectNo: this.tripProjectNo,
      };

      this.$axios.get('/trip/cost/public/info/list', {
        params: tripCost
      }).then(response=>{
          console.log('costShow', response);
          let CostSum = 0;
          this.publicCosts = response.data.map(item => {
            CostSum += parseInt(item.tripPublicCost);
            return{
              "costNo": item.tripPublicCostNo,
              "costOrder": item.tripPublicCostOrder,
              "costUse" : item.tripPublicCostUse,
              "cost" : item.tripPublicCost,
              "costNote" : item.tripPublicCostNote,
            }
          });
          this.publicCostSum = CostSum.toLocaleString();

      }).catch(error => {
          console.log(error);
      }).finally(()=>{
        this.isEditing = false;
      })
    },
    costModify(){
      this.isEditing = true;
    },

    async deleteCost() {
      try {
        console.log('this.tripDeleteCost', this.tripDeleteCost);
        await this.$axios.delete('/trip/cost/public/info', {
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
        await this.$axios.put('/trip/cost/public/info/list', this.tripUdpateCost);
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
        await this.$axios.post('/trip/cost/public/info', this.tripSaveCost);
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
    },

    async costSave(){

      let tripPublicCostOrder = '';

      await this.$refs.publicForm.validate().then(result => {
          if (result.valid) {
              this.publicCosts.forEach(e => {
              
              tripPublicCostOrder = e.costOrder != '' ? e.costOrder : '' ; 
              console.log('tripPublicCostOrder', tripPublicCostOrder);
              console.log('e.cost', e.cost);


                if(tripPublicCostOrder != ''){ // Update Cost
                  this.tripUdpateCost.push({
                    tripProjectNo: this.tripProjectNo,
                    tripPublicCost: {
                      tripPublicCostNo: e.costNo,
                      tripPublicCostOrder : tripPublicCostOrder,
                      tripPublicCostUse: e.costUse,
                      tripPublicCost: e.cost,
                      tripPublicCostNote: e.costNote,
                    },
                  });
                }else{  // Save Cost
                  this.tripSaveCost.push({
                    tripProjectNo: this.tripProjectNo,
                    tripPublicCost: {
                      tripPublicCostNo: e.costNo,
                      tripPublicCostUse: e.costUse,
                      tripPublicCost: e.cost,
                      tripPublicCostNote: e.costNote,
                    },
                  });
                }

              });


              this.executeRequests();
              this.isEditing = false;
          }
      })
    },
    costReset(){
      this.publicCosts = [
      ];
    },
    addRow() {
      console.log('add row 왜 안보여?')
      // + 버튼 클릭 시 행 추가 로직
      this.publicCosts.push({
        costNo: '',
        costOrder: '',
        costUse: '',
        cost: '',
        costNote: '',
      });
    },
    removeRow() {
      console.log('remove row 왜 안보여?')
      // - 버튼 클릭 시 행 삭제 로직
      if (this.publicCosts.length > 0) {

        const removedRow = this.publicCosts.slice(-1)[0]; // 마지막 요소의 복사본을 가져옴
        // removedRow를 활용하여 원하는 작업 수행

        // costOrder가 없을 때만 받는 체크로직을 추가해야함(기존데이터가 아닌 데이터 삭제 시)
        const costOrder = removedRow.costOrder;
        if(costOrder != ''){
          this.tripDeleteCost.push({
            tripProjectNo: this.tripProjectNo,
            tripPublicCostNo: removedRow.costNo,
            tripPublicCostOrder: costOrder,
          });

           console.log('this.tripDeleteCost',this.tripDeleteCost);
        }

       
        this.publicCosts.pop();
    
      }
    },
  },
}
</script>

<style scoped>

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
        margin-top: 10px;
    }


</style>