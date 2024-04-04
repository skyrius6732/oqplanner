<template>
    <v-dialog class="custom-dialog">
    <v-container class="custom-container">
        <v-row>
            <div class="close-button" @click="closeModal"><v-icon>mdi-close</v-icon></div>
        </v-row>
        <v-row>
            <v-col>
                <div class="title">검색 결과</div>
            </v-col>
        </v-row>
        <v-row>
            <v-col>
                <div class="title sfont">
                    검색 결과가 정확하지 않다면 '검색어'를 정확하게 입력해보세요.<br>
                    ex '해운대', '해운대 후기', '해운대 조개구이 후기' 등에 따라 정확도과 향상됩니다.
                </div>
            </v-col>
        </v-row>
        <v-row>
             <!-- <v-data-table
            :headers="tableHeaders"
            :items="selectedScheduleDetails"
            :items-per-page="60"
            height="800px"
            >
            <template v-if="key == 'searchSite'">

            </template>
            <template v-else-if="key == 'searchContent'">
                
            </template>
            </v-data-table> -->
             <v-table
                fixed-header
                :height="450"
                style="width: 100%"
                class="custom-table"
            >
            <thead>
                    <tr>
                        <th class="text-center" style="width: 15%">검색 사이트</th>
                        <th class="text-center" style="width: 10%">후기 날짜</th>
                        <th class="text-center" style="width: 60%">후기 제목</th>
                    </tr>
            </thead>
            <tbody>
                <tr
                    v-for="(item) in tripSearchList"
                    :key="item.name"
                >
                <td class="text-left">
                    <img :src="item.tripLogoImg" class="logo-image">{{ item.tripSite }}
                </td>
                <td class="text-center">{{ item.tripDate }}</td>
                <td class="text-left">
                    <a :href="item.tripUrl" target="_blank">
                         {{ item.tripSubject.split('https')[0] }}
                    </a>
                </td>
                </tr>
            </tbody>
            </v-table>

        </v-row>
    </v-container>
    </v-dialog>
</template>

 <script>

// import {
//   VDataTable,
// } from "vuetify/labs/VDataTable";

 export default {
    name: 'TripSearchModal',
    components: {
    //   VDataTable,
    },
    created(){
        this.tripProjectNo = sessionStorage.getItem("projectNoSession");
        this.tripUserNo = sessionStorage.getItem("userNoSession");
        console.log('TripSearchModal created')
    },
    mounted(){
        console.log('TripSearchModal mounted');

        this.emitter.off('submitSearchKeyword', this.searchHandler);
        // 새로운 이벤트 리스너 등록
        this.searchHandler = (data) => {
            this.searchShow(data);
        };
        this.emitter.on('submitSearchKeyword', this.searchHandler);
    },
    data(){
        return {
            // tableHeaders: [
            //     {title: '검색 사이트', align: "center", sortable: false, key: 'site',  width: '50%'},
            //     {title: '검색 내용', align: "center", sortable: false, key: 'content',  width: '50%'}
            // ],
            tripSearchList: [{
                tripLogoImg: '',
                tripSite: '',
                tripDate: '',
                tripUrl: '',
                tripSubject: '',
            }],
            
        }
    },
    methods:{
      closeModal(){
            this.$emit("closeModal");
      },
      async searchShow(searchKeyword){

        this.tripSearchList = [];
        const keyword = {
          keyword: searchKeyword,
        };
        this.$axios.get('/trip/rank/search', {
            params: keyword
        }).then(response => {
                  // 구글링 검색
                console.log('responsesss', response.data);
                 this.tripSearchList=response.data.map(item=>{
                    //'@/assets/images/readyImage.png'
                    return{
                        "tripLogoImg": item.tripLogoImg !== "" ? item.tripLogoImg : require('@/assets/images/readyImage.png'),
                        "tripSite": item.tripSite,
                        "tripDate": item.tripDate,
                        "tripUrl": item.tripUrl,
                        "tripSubject": item.tripSubject,
                    }
                 });
                  

                  // response.data.forEach((e) =>
                  //           this.favorits.push({
                  //             bookmarked: true,
                  //             contentid: e.tripFavoritesOutId,
                  //             imageUrl: e.tripFavoritesImgUrl || 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbnOSHZ%2FbtrLTB8V5DQ%2FnlaUCKg7kzbp7PbVKy63Qk%2Fimg.png',
                  //             searchTitle: e.tripFavoritesNm,
                  //           }))


            }).catch(error => {
                  console.error(error);
            })
      },
    }
 }
 </script>

 <style scoped>
    .custom-dialog {
        width: 70%;
        height: 75%;
        background: white;
        background-color: rgba(0, 0, 0, 0.5);
        border-radius: 20px; /* 모서리 둥글게 설정 */
    }

    .close-button {
        margin-left: auto; /* 오른쪽으로 이동 */
        background: white;
        color: #333;
        /* margin-top: -280px; */
        margin-right: -15px;
        width: 25px; /* 너비 조절 */
        height: 25px; /* 높이 조절 */
        border-radius: 4px; /* 모서리 둥글게 설정 */
        cursor: pointer;
        padding-left: 1px; /* 내부 여백 설정 */
    }

    .title {
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 10px;
        color: white;
    }

    .title.sfont {
        font-size: 15px;
        font-weight: bold;
        margin-bottom: 10px;
        color: white;
    }

     .logo-image {
        width: 30px; /* 원하는 이미지 폭 */
        height: 30px; /* 원하는 이미지 높이 */
        object-fit: contain; /* 이미지가 지정된 크기에 맞게 조정되되, 이미지의 비율을 유지함 */
        object-position: center; /* 이미지를 가운데 정렬함 */
        vertical-align: middle; /* 로고 이미지를 수직 가운데로 정렬함 */
        margin-right: 5px; /* 이미지와 텍스트 사이의 오른쪽 여백을 조정함 */
    }

    .bold-text {
        font-weight: bold;
    }


 </style>