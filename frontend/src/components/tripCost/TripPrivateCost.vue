<template>
    <v-row>
        <v-col>
          <div>
            <span v-if="privateCosts.length > 0" class="subtitle-detail">개인 비용</span>
          </div>
        </v-col>
      </v-row>

      <v-row>
        <v-col v-for="(costs, index) in privateCosts" :key="index" :cols="4">
          <v-card class="custom-card">
            <v-card-title class="title margin-bottom">
              여행자 {{ index+1 }}
              ( {{ costs.companionNm }} )
            </v-card-title>
            <v-card-text class="schedule-contents">
              <v-row class="icon-row">
                <v-col :cols="1">
                  <v-icon class="icon">mdi-currency-krw</v-icon>
                </v-col>
                <v-col :cols="11">
                  <h3 class="subtitle">여행 비용</h3>
                    <!-- <template v-for="(info, index) in privateCosts[index].tripPrivatCostList" :key="index"> -->
                    {{ getTotalCost(privateCosts[index].tripPrivatCostList) }}
                    <!-- </template> -->
                </v-col>
              </v-row>

              <v-row class="icon-row">
                <v-col :cols="1">
                  <v-icon class="icon">mdi-comment-outline</v-icon>
                </v-col>
                <v-col :cols="11">
                  <h3 class="subtitle">여행 비용 리스트</h3>
                    <ul>
                        <li v-for="(info, index) in privateCosts[index].tripPrivatCostList.filter((e) => e.costNote != '').slice(0,7)" :key="index">
                            {{ info.costNote }}
                        </li>
                    </ul>
                </v-col>
              </v-row>
            </v-card-text>
            <v-row class="icon-row">
              <v-col :cols="12" class="d-flex justify-end">
                  <v-btn class="button-margin button-style" @click="viewCost(privateCosts[index])">비용 보기</v-btn>
              </v-col>
            </v-row>
          </v-card>
        </v-col>
      </v-row>    
</template>

<script>
export default {
    components: {

    },
    name: 'TripPrivateCost',
    props:{
        companions: {
            type: Array,
            default: () => []
        }
    },
    created(){
        
        this.tripProjectNo = sessionStorage.getItem("projectNoSession");
        this.tripUserNo = sessionStorage.getItem("userNoSession");

     
      //   // 임시코드 (빌드없이 프론트단 사용을 위한...)
      //   // 추후에 지워야함
      //   if(!sessionStorage.getItem("projectNoSession")){
      //   this.tripProjectNo = "c5bf464bf576";
      //   }else{
      //   this.tripProjectNo = sessionStorage.getItem("projectNoSession")
      //   }

      //   if(!sessionStorage.getItem("userNoSession")){
      //   this.tripUserNo = "3bb8aff388ab";
      //   }else{
      //   this.tripUserNo = sessionStorage.getItem("userNoSession")
      //   }
      // console.log('created');

      // // this.$emit("companionSelect");

      // console.log('created this.companions',this.companions)

      console.log('projectNoSession', sessionStorage.getItem("projectNoSession"));
      console.log('userNoSession',sessionStorage.getItem("userNoSession"));



      this.privateCosts = this.companions.map( item => {
            // this.tripCompanionNm = item.tripCompanionNm;
            // this.tripCompanionNo = item.tripCompanionNo;
            return {
                companionNo: item.tripCompanionNo,
                companionNm: item.tripCompanionNm,
                tripPrivatCostList: [],
            }
        });


    },
    beforeMount(){
      // TripCost.vue의 companionSelect()먼저 호출해야함
      console.log('beforeMount')
           
    },
    mounted(){
      // TripPrivateCostDetail.vue에서 TripPrivateCost.vue의 costShow() 호출을 위한
       // 이벤트 구현부
       this.emitter.on('callPrivateCostCostShow', this.handleCallPrivateCostCostShow);
       console.log('Mount')
      //  console.log('TripPrivateCost mounted this.companions', this.companions);

      
       this.costShow();

       
        //     this.emitter.on('callPrivateCostCostShow', ()=>{
        //     console.log('callPrivateCostCostShow???');
        //     this.costShow();
        // });

        //  this.$root.eventBus.on('data-updated', () => {
        //     // 여기에서 조회 메서드 또는 원하는 동작을 수행합니다.
        //     console.log('Data updated. Calling the query method.');
        //     this.costShow();
        //     });

    },

    data(){
        return {
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
        }
    },
    methods:{
        handleCallPrivateCostCostShow() {
            this.costShow();
        },
        
        viewCost(privateCosts){
            // console.log('selected privateCosts', privateCosts);

            // 상위(부모) TripCost.vue의 viewCost 메서드 호출
            this.$emit("viewCost");

            // 다른 컴포넌트로 인자값 전달 (TripPrivateCostDetail.vue로 전달) 
            this.emitter.emit('submitPrivateDetail', privateCosts);
        },
       async costShow(){

            console.log('costShow privateCosts', this.privateCosts);
            // 모든 비용 정보를 담을 배열
            const allCosts = await Promise.all(this.privateCosts.map(async (element) => {
                console.log('element.companionNo', element.companionNo);

                const tripCost = {
                    tripProjectNo: this.tripProjectNo,
                    tripCompanionNo: element.companionNo,
                };

                try {
                    const response = await this.$axios.get('/trip/cost/private/info/list', {
                        params: tripCost
                    });
                    console.log
                    if (response.data && response.data.length > 0 && response.data[0].tripPrivateCostList) {
                        let CostSum = 0;
                        element.tripPrivatCostList = response.data[0].tripPrivateCostList.map((item) => {
                            CostSum += parseInt(item.tripPrivateCost);
                            return{
                                "costNo": item.tripPrivateCostNo,
                                "costOrder": item.tripPrivateCostOrder,
                                "costUse" : item.tripPrivateCostUse,
                                "cost" : item.tripPrivateCost,
                                "costNote" : item.tripPrivateCostNote,
                            };
                        });

                        this.privateCostSum = CostSum.toLocaleString();
                    } else {
                        element.tripPrivatCostList = [];
                    }
                } catch (error) {
                    console.log(error);
                }

                return element;
            }));

            this.privateCosts = allCosts;

            // 정보 재구성하기
            // this.reConfig();

            console.log('this.privateCosts', this.privateCosts);   
        },
        getTotalCost(costList) {
            // reduce 함수를 사용하여 cost 합산
            const totalCost = costList.reduce((sum, info) => sum + parseInt(info.cost), 0);
            return totalCost.toLocaleString() + ' 원';
        },
        // reConfig(){
        //     this.privateCosts.map((element)=>{

        //     });
        // }
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

.custom-card {
  height: 100%;
}

.margin-bottom{
  margin-bottom: 20px;
}

.schedule-contents{
  height: 250px;
}

.button-style{
   background-color: #333;
   color: #fff;
   font-size: 13px;
   margin-left: 5px;
   margin-top: 10px; /* 적용 버튼을 조금 아래로 이동 */
}

.button-margin{
    margin-left: 10px;
    margin-right: 10px;
    margin-bottom: 10px;
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



</style>