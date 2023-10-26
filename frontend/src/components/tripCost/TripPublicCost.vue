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
            <v-btn @click="costSave" class="button-style">비용 저장</v-btn>
          </v-col>

          <!-- 행의 높이를 50px로 가정 -->
          <v-table
              fixed-header
              :height="'{{(publicCosts.length * 50) + 20}}px'"
              style="width: 100%"
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
                  <td class="text-center">{{ item.cost }}</td>
                  <td>{{ item.costNote }}</td>
              </template>
              <template v-else>
                <td class="text-center">
                  {{ index+1 }}
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
              <tr>
                <td class="text-center">합계</td>
                <td></td>
                <td class="text-center">
                  <strong> 123,123</strong>
                </td>
              </tr>
              </tbody>
          </v-table>
      </v-row>
</template>

<script>
export default {
  components: {

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

  },
  mounted(){
    
  },
  data(){
    return {
        // modalVisible: this.isModalVisible,
        isEditing: false, // 수정 중인지 여부를 나타내는 플래그
        publicCosts: [],
        costUseRules:[
           v => !( v && v.length > 10 ) || '사용처 입력은 최대 30글자까지 가능합니다.( ' + v.length + '글자 )',
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
    costSave(){
      const tripCost = [];

        // tripProjectNo: this.tripProjectNo,
      

      // const tripPublicCost =  this.publicCosts.map(item=> {
      //     console.log("item", item);
      //     return {
      //       tripPublicCostUse: item.costUse,
      //       tripPublicCost: item.cost,
      //       tripPublicCostNote: item.costNote,
      //     }
      // });

      this.publicCosts.forEach(e => {
        tripCost.push({
          tripProjectNo: this.tripProjectNo,
          tripPublicCost: {
            tripPublicCostUse: e.costUse,
            tripPublicCost: e.cost,
            tripPublicCostNote: e.costNote,
          },
        });
      });


      this.$axios.post('/trip/cost/public/info', tripCost)
      .then(response=>{
          console.log(response);
      }).catch(error => {
          console.log(error);
      })
     
      this.isEditing = false;
    },
    costReset(){
      this.publicCosts = [
      ];
    },
    addRow() {
      // + 버튼 클릭 시 행 추가 로직
      this.publicCosts.push({
        costOrder: '',
        costUse: '',
        cost: '',
        costNote: '',
      });
    },
    removeRow() {
      // - 버튼 클릭 시 행 삭제 로직
      if (this.publicCosts.length > 1) {
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