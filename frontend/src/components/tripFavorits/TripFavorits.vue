<template>
    <v-container class="custom-container">
    <v-row>
    <!-- 왼쪽 영역 -->
    <v-col cols="10">
      <v-row>
        <v-col>
          <div class="title">지역 선택</div>
        </v-col>
      </v-row>
      
      <v-form ref="form" lazy-validation>
      <v-row>
        <v-col>
          <v-select
            v-model="selectedSi"
            :items="regionSi"
            item-title="name"
            item-value="code"
            label="지역(시) 선택"
            :rules="regionSiRules"
          ></v-select>
        </v-col>
        <v-col>
          <v-select
            v-model="selectedGu"
            :items="regionGu"
            item-title="name"
            item-value="code"
            :disabled="!selectedSi"
            label="지역(구) 선택"
          ></v-select>
        </v-col>
        <!-- <v-col>
          <v-select
            v-model="selectedDong"
            :items="regionDong"
            label="지역(동) 선택"
          ></v-select>
        </v-col> -->
      </v-row>

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
      </v-form>

      <v-row>
        <v-col>
        <!-- 검색 결과 표시 -->
        <v-list style="height: 470px;"  v-if="searchResults && searchResults.length > 0">
             <!-- v-list-item에 직접 슬롯 적용 -->
            <v-list-item v-for="(result, index) in searchResults" :key="index" class="search-item">
                  <v-row>
                    <v-col cols="2">
                        <v-img :src="result.imageUrl" 
                        max-height="100px"
                        max-width="200px"
                        @error="replaceImg"
                        cover
                        >
                        </v-img>
                    </v-col>
                    <v-col cols="10">
                        <v-row class="search-title">
                           <!-- 북마크 아이콘 추가 -->
                            <v-icon @click="toggleBookmark(result, index)" :color="result.bookmarked ? 'yellow' : 'grey'">
                                {{ result.bookmarked ? 'mdi-star' : 'mdi-star-outline' }}
                            </v-icon>
                            <div class="search-title-text"> 
                                <!-- <span v-html="highlightSpecialText(result.searchTitle, searchKeyword)"></span> -->
                                <span v-html="result.searchTitle" ></span>
                            </div> 
                            <div class="search-addr-text"> ({{ result.addr }})</div>
                        </v-row>
                        <v-row>
                            <div class="search-text">{{ truncateText(result.overview, 260) }}</div>
                        </v-row>
                    </v-col>
                  </v-row>
            </v-list-item>
        </v-list>
        <div v-else class="no-results-container">
            <div class="no-results-overlay">
                 <span v-if="!isLoading && enterFavorits == false" class="no-results-text">검색 결과가 없습니다.</span>
                 <span v-if="isLoading">
                      <!-- 로딩 중일 때 보여질 내용 -->
                      <v-progress-circular
                          indeterminate
                          color="black"
                      ></v-progress-circular>
                  </span>
            </div>
        </div>
        </v-col>
      </v-row>
      

    </v-col>

    <!-- 오른쪽 영역 -->
    <v-col cols="2">
      <v-row>
        <v-col>
          <div class="title">즐겨찾기</div>
        </v-col>
      </v-row>

      <v-row>
          <v-col class="overlay-content">
            <v-list style="height: 675px; overflow-y: auto;">
                <v-list-item v-for="(favorit, index) in favorits" :key="index" class="search-item">
                    <row>
                     <v-img :src="favorit.imageUrl" 
                      width="200px"
                      max-height="120px"
                      @error="replaceImg"
                      cover
                      ></v-img></row>
                       <!-- 북마크 아이콘 추가 -->
                      <v-icon @click="toggleBookmark(favorit, index)" :color="favorit.bookmarked ? 'yellow' : 'grey'">
                                {{ favorit.bookmarked ? 'mdi-star' : 'mdi-star-outline' }}
                      </v-icon>
                     <row class="favorit-title-text">
                      [{{ favorit.searchTitle }}]
                     </row>
                </v-list-item>
            </v-list>
          </v-col>
        </v-row>
        
    </v-col>

    </v-row>
    </v-container>

</template>
<script>

export default {
    name: 'TripFavortis',
    components: {
      
    },
    watch:{
      selectedSi: function(v){
        this.changeSi(v);
      },
      selectedGu: function(v){
        this.changeGu(v);
      }
    },
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
        this.searchMetaRegion();
        this.getFavorites();
    },
    data(){
         return {
            tripProjectNo: "",
            tripUserNo: "",
            isLoading: false,
            enterFavorits: true,
            // selectedSi: {rnum: 1, code: '1', name: '서울'},
            selectedSi: '',
            selectedGu: '',
            // selectedDong: null,
            siCode: '',
            guCode: '',
            regionSi: [],
            regionDong: [],
            regionGu: [],
            searchKeyword: "",
            cacheHtmlContent: null,
            searchResults: [],
            searchContents: [],
            favorits: [],
            regionSiRules:[
              // v => this.checkRegionSi(v),
              v => !!v || '지역(시)를 선택해 주시길 바랍니다.',
            ],
            keywordRules: [
              v => !!v || '검색어를 입력해 주시길 바랍니다.',
            ],
          };
        },
    methods:{
        // isSpecialTextIncluded(title) {
        //   // 특정 글자가 포함되어 있는지 여부를 확인하고 true 또는 false를 반환
        //   return title.includes(this.searchKeyword);
        // },

        // highlightSpecialText(title, keyword) {
        //   // console.log('title', title);
        //   // console.log('keyword', keyword);
        //   if (!this.cachedHtmlContent) {
        //     // 특정 키워드를 빨간색으로 강조하는 함수
        //     if (this.isSpecialTextIncluded(title)) {
        //         this.cachedHtmlContent = title.split(keyword).join('<span style="color: red;">' + keyword + '</span>');
        //         console.log('include TRUE', this.cachedHtmlContent);
        //     } else {
        //         this.cachedHtmlContent = title;
        //         console.log('include FALSE', this.cachedHtmlContent);
        //     }
        //   }

        //   return this.cachedHtmlContent;
        // },

        truncateText(text, maxLength) {
          if(text != null){
            if (text.length > maxLength) {
              return text.slice(0, maxLength) + '...';
            } else {
              return text;
            }
          }
        },

        replaceImg(e){
          console.log('나오긴하냐??');
           e.target.src = require('@/assets/images/readyImage.png');
        },

        changeGu(v){
          this.guCode = v;
        },

        changeSi(v){
          this.regionGu = [];
          this.siCode = v;
          this.searchMetaRegion(v);
          console.log('selected', v);
        },

        searchMetaRegion(areaCode){

            // let baseUrl = 'https://apis.data.go.kr/B551011/KorService1/areaCode1';
            let url = 'https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D&numOfRows=100&MobileOS=ETC&MobileApp=AppTest&_type=json'
            if(areaCode != null){
               url = 'https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D&numOfRows=100&MobileOS=ETC&MobileApp=AppTest&_type=json&areaCode='+areaCode;
            }
            
            this.$axios.get(url, {
                // params: {
                //     serviceKey: 'LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D',
                //     numOfRows: '10',
                //     MobileOS: 'ETC',
                //     MobileApp: 'OQPlanner',
                //     _type: 'json'
                // }
            }).then(response=>{
                
                console.log("response", response.data.response.body.items.item)
                let array = response.data.response.body.items.item;
                // if(areaCode == null){

                // array.forEach((element)=>{
                //       areaCode == null ?  this.regionSi.push(element) : this.regionGu.push(element);
                // });

                 if (areaCode == null) {
                        // areaCode가 null이면 전체 키워드를 넣기
                        this.regionSi = [...this.regionSi, ...array];
                 } else {
                        // 아니면 해당 키워드만 넣기
                        this.regionGu = [{rnum: 0, code: '', name: '전체'}, ...this.regionGu, ...array];
                 }

            }).catch(error => {
                console.log(error);
            });
        },

        checkRegionSi(value){
          console.log('value', value);
          if(value=='' || value == null){
            return "지역(시)를 선택해 주시길 바랍니다.";
          }
        },

        async search(keyword) {
             
            await this.$refs.form.validate().then(async result => {
              if (result.valid) {
                 this.enterFavorits = true;
                 this.isLoading = true; // API 요청이 시작될 때 로딩 화면을 보여줍니다.
                  // if(value=='' || value == null){
                  //   return "지역(시)를 선택해 주시길 바랍니다.";
                  // }

                  // 검색 로직 구현
                  // this.searchResults에 결과 할당
                  // let baseUrl = 'https://apis.data.go.kr/B551011/KorService1/areaCode1';
            
                  this.searchResults = [];
                  this.cachedHtmlContent = null; // 초기화

                  let url = 'https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D&numOfRows=100&MobileOS=ETC&MobileApp=OQPlanner&_type=json&listYN=Y&arrange=A&contentTypeId=12&keyword='+keyword;

                  try {
                    let response = await this.$axios.get(url);
                    let array = response.data.response.body.items.item;

                    // this.searchResults = array.map(e => ({
                    //     contentid: e.contentid,
                    //     bookmarked: false,
                    //     imageUrl: e.firstimage || 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbnOSHZ%2FbtrLTB8V5DQ%2FnlaUCKg7kzbp7PbVKy63Qk%2Fimg.png',
                    //     searchTitle: e.title,
                    //     addr: e.addr1,
                    //     siCode: this.siCode,
                    //     guCode: this.guCode,
                    //     overview: '',
                    // }));

                    console.log('array', array);

                    // 검색 결과의 contentid와 areacode를 뽑아냄
                    let midArray = []
                    midArray = array.map(e => ({
                      contentid: e.contentid,
                      areacode: e.areacode,
                    }));

                    console.log("midArray", midArray);

                    let traffic = false;
                    for (let i = 0; i < midArray.length; i++) {

                      let detailUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D&MobileOS=ETC&MobileApp=OQPlanner&_type=json&defaultYN=Y&overviewYN=Y&firstImageYN=Y&areacodeYN=Y&addrinfoYN=Y&contentId=" + midArray[i].contentid;
                      let detailResponse = await this.$axios.get(detailUrl);

                      console.log('detailResponse', detailResponse);
     
                      if(!Array.isArray(detailResponse.data)){
                        traffic = true;
                        break;
                      }

                      // 트래픽 초과 안되었을 때 이전 활용 로직
                      let detailArray = detailResponse.data.response.body.items.item; // contentid로 인한 1건씩 response됨
                        detailArray.forEach((e) =>{

                          // console.log('e.sigungucode',e.sigungucode);
                          // console.log('e.areacode',e.areacode);
                          let bookmark = false;
                          if(this.siCode === e.areacode && (this.guCode ? this.guCode === e.sigungucode : true)){

                            this.favorits.forEach(fe => {
                              if(fe.contentid === e.contentid){
                                bookmark= true
                              }
                            });
                            
                            this.searchResults.push({
                              contentid: e.contentid,
                              bookmarked: bookmark,
                              imageUrl: e.firstimage || 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbnOSHZ%2FbtrLTB8V5DQ%2FnlaUCKg7kzbp7PbVKy63Qk%2Fimg.png',
                              searchTitle: e.title,
                              addr: e.addr1,
                              siCode: e.areacode,
                              guCode: e.sigungucode,
                              overview: e.overview,
                            })
                          }
                        })

                    }

                    if(traffic){  // 트래픽 초과 되었을때
                      array.forEach((e) =>{

                          // console.log('e.sigungucode',e.sigungucode);
                          // console.log('e.areacode',e.areacode);
                          let bookmark = false;
                          if(this.siCode === e.areacode && (this.guCode ? this.guCode === e.sigungucode : true)){
                            
                            this.favorits.forEach(fe => {
                              if(fe.contentid === e.contentid){
                                bookmark= true
                              }
                            });

                            this.searchResults.push({
                              contentid: e.contentid,
                              bookmarked: bookmark,
                              imageUrl: e.firstimage || 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbnOSHZ%2FbtrLTB8V5DQ%2FnlaUCKg7kzbp7PbVKy63Qk%2Fimg.png',
                              searchTitle: e.title,
                              addr: e.addr1,
                              siCode: e.areacode,
                              guCode: e.sigungucode,
                              overview: e.overview,
                            })
                          }
                        })
                    }


                    
                        

                    console.log('this.searchResults', this.searchResults);

                    // 첫 번째 axios 요청이 완료된 후에 두 번째 axios 요청을 수행
                    // for (let i = 0; i < array.length; i++) {
                    //     // console.log('array[i].contentid', array[i].contentid);
                        
                    //     let contentUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D&MobileOS=ETC&MobileApp=OQPlanner&_type=json&overviewYN=Y&areacodeYN=Y&contentId=" + array[i].contentid;

                    //     let detailResponse = await this.$axios.get(contentUrl);
                    //     console.log('detailResponse',detailResponse);

                    //     let detailArray = detailResponse.data.response.body.items.item;

                    //     // searchResults 배열을 순회하면서 각 객체에 overview 속성을 추가
                    //     this.searchResults.forEach((element, index) => {
                    //         // contentId가 일치하는 경우에만 overview 속성 추가
                    //         if(element.siCode === detailArray[0].areacode && element.guCode === detailArray[0].sigungucode){
                    //           if (element.contentid === detailArray[0].contentid) {
                    //               this.searchResults[index].overview = detailArray[0].overview;
                    //           }
                    //         }else{
                    //           // 여기부분 봐야할것
                    //           this.searchResults.splice(index, 1);
                    //         }
                    //     });
                    // }
                } catch (error) {
                    console.log(error);
                } finally {
                    this.enterFavorits = false;
                    this.isLoading = false; // API 요청 완료 후 로딩 화면을 숨깁니다.
                }
              }
          })
        },


        getFavorites(){
          this.favorits = [] // 조회 시 초기화
      
            let tripFavorite = {
              tripProjectNo: this.tripProjectNo,
            }

          this.$axios.get('/trip/favorites/info', {
            params: tripFavorite
          }).then(response => {
                  // 즐겨찾기 조회
                  console.log('response', response)
                  response.data.forEach((e) =>
                            this.favorits.push({
                              bookmarked: true,
                              contentid: e.tripFavoritesOutId,
                              imageUrl: e.tripFavoritesImgUrl || 'https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbnOSHZ%2FbtrLTB8V5DQ%2FnlaUCKg7kzbp7PbVKy63Qk%2Fimg.png',
                              searchTitle: e.tripFavoritesNm,
                            }))
            }).catch(error => {
                  console.error(error);
            })
        },
        toggleBookmark(result) {

            console.log('result', result);
            let tripFavorite = {
              tripProjectNo: this.tripProjectNo,
              tripFavoritesNm: result.searchTitle,
              tripFavoritesExplan: result.overview == null ? "" : result.overview,
              tripFavoritesOutId: result.contentid,
              tripFavoritesImgUrl: result.imageUrl,
            }
        
            if(result.bookmarked){  // 즐겨찾기 삭제
                this.$axios.delete('/trip/favorites/info', {data: tripFavorite})
                .then(() => {

                  // 화면 단 즐겨찾기 삭제
                  for(let i=0; i<this.favorits.length; i++){
                      console.log('this.favorits[i].contentid', this.favorits[i].contentid);
                      console.log('result.contentid', result.contentid)
                      if(this.favorits[i].contentid == result.contentid){
                          this.favorits.splice(i, 1);
                      }
                  }

                }).catch(()=>{

                })
            }else{  // 즐겨찾기 추가
                this.$axios.post('/trip/favorites/info', tripFavorite)
                .then(() => {
                  // 화면 단 즐겨찾기 조회
                  this.favorits.push(result);
                  // this.getFavorites();
                }).catch(error => {
                  console.error(error);
                })
                // console.log('result', result)
            }

            result.bookmarked = !result.bookmarked;
            
        },
    }
}
</script>

<style scoped>

.v-list{
    border-radius: 20px;
}

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

.title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.search-title{
    margin-top: 5px;
}

.container {
  width: 100px;
  height: 100px;
  overflow: hidden;
  position: relative;
}

.search-image {
  width: "200px"; 
  height: "200px"; 
}

/* .search-item{
  height: 200px;
} */


.search-title-text {
    font-size: 16px;
    font-weight: bold;
  }

.search-addr-text{
   font-size: 12px;
   font-weight: bold;
}

  .search-text {
    font-size: 14px;
    color: #333;
  }

  .bookmark-icon {
    margin-left: 5px;
  }

.overlay-content {
    width: 100%;
    height: 700px;
    background-color: rgba(0, 0, 0, 0.5); /* overlay 배경 색상 및 투명도 조절 */
    color: #fff; /* overlay 텍스트 색상 */
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    font-size: 18px; /* 텍스트 크기 */
    border-radius: 20px; /* 모서리 둥글게 설정 */
  }

.favorit-title-text{
    margin-top: -20px;
    font-size: 10px;
    font-weight: bold;
}

.search-title-text span.red-text {
  color: red ;   /* 빨간색 텍스트로 변경 */
}

.no-results-container {
    position: relative;
    height: 495px;
}

.no-results-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 10px;
}

.no-results-text {
    color: white; /* 흰색 글씨 */
    font-size: 18px;
    font-weight: bold;
}

</style>