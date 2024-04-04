<template>
  <v-container class="custom-container">

     <v-row>
        <v-col cols="11">
          <v-text-field 
            v-model="searchKeyword" 
            label="검색어"
            :rules="keywordRules"
            @keyup.enter="search(searchKeyword)"></v-text-field>
        </v-col>
        <v-col cols="1"> 
          <v-btn @click="search(searchKeyword)" size="x-large" class="button-style center-button">검색</v-btn>
        </v-col>
      </v-row>
    
    <v-row>
        <v-col>
          <div class="subtitle-detail">일간 여행지 추천</div>
        </v-col>
    </v-row>
    <v-row>
      <v-col v-for="(destination, index) in dayDestinations" :key="index" cols="12" md="3">
        <v-card class="pa-3" elevation="12">
            <v-card-title class="rank-title">{{ destination.rank }}</v-card-title>
          <v-img 
            :src="destination.image" 
            height="200">
        </v-img>
          <v-card-title class="card-title">{{ destination.name }}</v-card-title>
          <v-card-subtitle>{{ destination.description }}</v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>


    <v-row>
        <v-col>
          <div class="subtitle-week-detail">주간 여행지 추천</div>
        </v-col>
    </v-row>
    <v-row>
      <v-col v-for="(destination, index) in weekDestinations" :key="index" cols="12" md="3">
        <v-card class="pa-3" elevation="12">
            <v-card-title class="rank-title">{{ destination.rank }}</v-card-title>
          <v-img 
            :src="destination.image" 
            height="200">
        </v-img>
          <v-card-title class="card-title">{{ destination.name }}</v-card-title>
          <v-card-subtitle>{{ destination.description }}</v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
        <v-col>
          <div class="subtitle-month-detail">월간 여행지 추천</div>
        </v-col>
    </v-row>
    <v-row>
      <v-col v-for="(destination, index) in monthDestinations" :key="index" cols="12" md="3">
        <v-card class="pa-3" elevation="12">
            <v-card-title class="rank-title">{{ destination.rank }}</v-card-title>
          <v-img 
            :src="destination.image" 
            height="200">
        </v-img>
          <v-card-title class="card-title">{{ destination.name }}</v-card-title>
          <v-card-subtitle>{{ destination.description }}</v-card-subtitle>
        </v-card>
      </v-col>


    </v-row>
    
  </v-container>
  <TripSearchModal 
    v-model="isModalVisible"
    @closeModal="closeModal"
    @submitKeyword="submitKeyword"
    ref="modal"/>
</template>

<script>

import TripSearchModal from './TripSearchModal.vue';

export default{

    components: {
      TripSearchModal,
    },
    created(){
        this.tripProjectNo = sessionStorage.getItem("projectNoSession");
        this.tripUserNo = sessionStorage.getItem("userNoSession");
    },
    mounted(){
        
        this.getDayRanking()
    
    },
    data(){
        return{
            dayDestinations: [],
            weekDestinations: [],
            monthDestinations: [],
            searchKeyword: "",
            keywordRules: [
              v => !!v || '검색어를 입력해 주시길 바랍니다.',
            ],
            isModalVisible: false,
        } 
    },
    methods:{
      closeModal(){
        // this.$refs.privateCostMethod.costShow();
        this.isModalVisible = false;
      },
      search(searchKeyword) {
        // let searchKeyword = searchKeyword;
        this.isModalVisible = true;

        // 다른 컴포넌트로 인자값 전달 (TripSearchModal.vue로 전달) 
        this.emitter.emit('submitSearchKeyword', searchKeyword);
      },

      getDayRanking(){
            this.$axios.get('/trip/rank/info',{
            }).then(response=>{
                const data = response.data;
                data.forEach(element => {
                    const descriptionText = `여행자중 ${element.tripRankingFavoritesNum}건이 즐겨찾기 되어 있습니다.`;
                    let descriptionRank = '';
                    
                    if(element.tripRankingFlag == 'DAY'){
                        descriptionRank = `일간 가고싶은 ${element.tripRankingOrder}번째 여행지 `;

                        this.dayDestinations.push({
                            name: element.tripFavoritesNm,
                            rank: descriptionRank,
                            image: element.tripFavoritesImgUrl,
                            description: descriptionText
                        })
                    }else if (element.tripRankingFlag == 'WEEK'){
                        descriptionRank = `주간 가고싶은 ${element.tripRankingOrder}번째 여행지 `;
                        this.weekDestinations.push({
                            name: element.tripFavoritesNm,
                            rank: descriptionRank,
                            image: element.tripFavoritesImgUrl,
                            description: descriptionText
                        })

                    }else if (element.tripRankingFlag == 'MONTH'){
                        descriptionRank = `월간 가고싶은 ${element.tripRankingOrder}번째 여행지 `;
                        this.monthDestinations.push({
                            name: element.tripFavoritesNm,
                            rank: descriptionRank,
                            image: element.tripFavoritesImgUrl,
                            description: descriptionText
                        })

                    }

                });
            }).catch((e)=>{
                console.log(e)
            }).finally(()=>{
                
            })
        }
    }
    
}
</script>

<style scoped>
    .custom-container {
        width: 80%;
    }

    .button-style{
      background-color: #333;
      color: #fff;
      font-size: 15px;
      margin-left: 5px;
      margin-top: 3px; /* 적용 버튼을 조금 아래로 이동 */
    }

    .center-button {
      margin-left: -10px; 
    }
    .subtitle-week-detail, .subtitle-month-detail{
        margin-top: 20px;
        color: #333;
        font-size: 17px;
        font-weight: bold;
    }

    .subtitle-detail {
        color: #333;
        font-size: 17px;
        font-weight: bold;
    }

    .card-title{
        font-size: 16px;
        color: black; /* 예시 색상, 원하는 색상으로 변경하세요 */
        font-weight: bold;
    }

    .rank-title{
        font-size: 13px;
        color: black; /* 예시 색상, 원하는 색상으로 변경하세요 */
        font-weight: bold;
    }
  

   
</style>