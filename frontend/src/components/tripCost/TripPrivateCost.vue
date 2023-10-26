<template>
    <v-dialog class="custom-dialog">
    <v-container class="custom-container">
        <v-row>
            <div class="close-button" @click="closeDialog"><v-icon>mdi-close</v-icon></div>
        </v-row>
        <v-row>
            <v-col>
                <div>
                    <span class="subtitle-detail">개인 비용</span>
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
                        <th class="text-center">NO.</th>
                        <th class="text-center">사용처</th>
                        <th class="text-center">비용(원)</th>
                        <th class="text-center">메모</th>
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
                    <td class="text-center">{{ item.cost }}</td>
                    <td>{{ item.costNote }}</td>
                </template>
                <template v-else>
                <td>
                    {{ index+1 }}
                </td>
                <td>
                    <v-text-field 
                    outlined class="gray-text-field"
                    :rules="costUseRules"
                    v-model="item.costUse" >
                    </v-text-field>
                </td>
                <td>
                    <v-text-field 
                    outlined class="gray-text-field"
                    :rules="costRules">
                    </v-text-field>
                </td>
                <td>
                    <v-text-field 
                    outlined class="gray-text-field"
                    :rules="costNoteRules">
                    </v-text-field>
                </td>
                </template>   
                </tr>
                <tr>
                    <td>합계</td>
                    <td></td>
                    <td>
                        <strong> 123,123</strong>
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
  //   props: ['isModalVisible'],
  created(){
    console.log("create privateCost", this.isModalVisible);
  },
  mounted(){
    console.log("mounted privateCost", this.isModalVisible);
  },
  data(){
    return {
        // modalVisible: this.isModalVisible,
        isEditing: false, // 수정 중인지 여부를 나타내는 플래그
        privateCosts: [
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창서비스왕창집/왕창왕창', companionName: "홍길동"/*임시 값임*/},
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집/맛있엉', companionName: "박길동"/*임시 값임*/},
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집/숙소가는데왤케멀어', companionName: "이길동"/*임시 값임*/},
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집/아몰라몰라집갈래몰라몰라몰라아서비스왕창집/아몰라몰라집갈래몰라몰라몰라아/서비스왕창집/아몰라몰라집갈래몰라몰라몰라아서비스왕창집/아몰라몰라집갈래몰라몰라몰라아', companionName: "황길동"/*임시 값임*/},
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집', companionName: "최길동"/*임시 값임*/},
          { costOrder: '1', companionNo: '33', costUse: '해운대개미집', cost: 30000, costNote: '서비스왕창집', companionName: "고길동"/*임시 값임*/},
        ],
    }
  },
  methods:{
    costModify(){
      this.isEditing = true;
    },
    costSave(){
      this.isEditing = false;
    },
    costReset(){
      this.privateCosts = [
      ];
    },
    addRow() {
      // + 버튼 클릭 시 행 추가 로직
      this.privateCosts.push({
        costOrder: this.privateCosts.length + 1,
        costUse: '',
        cost: '',
        costNote: '',
      });
    },
    removeRow() {
      // - 버튼 클릭 시 행 삭제 로직
      if (this.privateCosts.length > 1) {
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

    /* .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    } */

</style>