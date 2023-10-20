<template>
  <v-container class="custom-container">
    <div class="d-flex justify-space-between mb-2">
          <div>
           <span class="subtitle-detail">일정 간략히 보기</span>
          </div>
    </div>
    <v-row>
      <v-col v-for="(schedule, index) in schedules" :key="index" :cols="4">
        <v-card class="custom-card">
          <v-card-title class="title text-left">
            {{ schedule.date }}
          </v-card-title>
          <v-card-text class="schedule-contents">
            <v-row class="icon-row">
              <v-col :cols="1">
                <v-icon class="icon">mdi-map-marker</v-icon>
              </v-col>
              <v-col :cols="11">
                <h3 class="subtitle">여행 경로</h3>
                {{ schedule.route.length >= 20 ? schedule.route.slice(0, 20) + ' ...' : schedule.route }}
              </v-col>
            </v-row>

            <v-row class="icon-row">
              <v-col :cols="1">
                <v-icon class="icon">mdi-currency-krw</v-icon>
              </v-col>
              <v-col :cols="11">
                <h3 class="subtitle">여행 비용</h3>
                <p>{{ schedule.cost }}</p>
              </v-col>
            </v-row>

            <v-row class="icon-row">
               <v-col :cols="1">
                <v-icon class="icon">mdi-comment-outline</v-icon>
              </v-col>
              <v-col :cols="11">
                <h3 class="subtitle">비고</h3>
                <ul v-if="schedule.remarks">
                    <li v-for="(note, index) in Array.from({ length: 3 }).map((_, i) => schedule.remarks ? schedule.remarks.split('/')[i] || ' - ' : '')" :key="index">
                      {{ note.length > 20 ? note.slice(0, 20) + ' ...' : note.trim() }}
                    </li>
                </ul>
              </v-col>
            </v-row>
          </v-card-text>
          <v-row class="icon-row">
            <v-col :cols="12" class="d-flex justify-end">
                <v-btn v-if="index === schedules.length - 1" class="button-style" @click="deleteSchedule(index)">일정 삭제</v-btn>
                <v-btn class="button-margin button-style" @click="viewSchedule(index)">일정 보기</v-btn>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
      <v-col :cols="4">
        <v-card v-if="schedules.length < tripPlanAllNum" class="plus-button-card" @click="addSchedule">
          <v-card-text class="text-center">
              <v-icon size="55">mdi-plus-box</v-icon>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- 밑줄 -->
    <v-row class="underline-row">
      <v-col>
        <v-divider></v-divider>
      </v-col>
    </v-row>

     <!-- 일정 자세히 보기 -->
    <v-row id="schedule-table" v-if="selectedSchedule">
      <v-col>
        <div class="d-flex justify-space-between mb-2">
          <div>
           <span class="subtitle-detail">일정 자세히 보기</span>
           <span class="subtitle-detail-date" > {{ selectedSchedule && selectedSchedule.date }} </span>
          </div>
          <div>
             <!-- 수정 상태인지 확인하고, 수정 상태라면 저장 버튼을 표시 -->
            <template v-if="isEditing">
              <v-btn class="button-style" @click="saveSchedule">저장</v-btn>
            </template>
            <template v-else>
              <v-btn class="button-style" @click="startEditing">일정 수정</v-btn>
            </template>
              </div>
        </div>
        <!-- height selectedScheduleDetails 개수에 따라 동적변경 진행 -->
        <v-data-table
        :headers="tableHeaders"
        :items="selectedScheduleDetails"
        class="elevation-1"
        height="1000px"   
        item-value=""
        >
        <template v-slot:body="{ items }">
            <tr v-for="(schedule, index) in items" :key="index" class="tr-style">
              <!-- 편집 모드 -->
              <template v-if="isEditing">
                <td
                  v-for="(value, key) in schedule.columns"
                  :key="key"
                  :class="{}"
                >
                  <!-- 시간 형식 time-range-picker로 보여주기 -->
                  <template v-if="key == 'time'">
                      <div>
                        <vue-timepicker 
                          v-model="value[index].start" 
                          class="start-timpicker" 
                          @change="checkTimes(value,index,items,'startTime')"/>
                        <vue-timepicker 
                          v-model="value[index].end" 
                          class="end-timpicker"
                          @change="checkTimes(value,index,items,'endTime')" />
                      
                      <v-row 
                        v-show="value[index].alertFlag"
                        class="alert-message"
                      > {{ value[index].alertMessage }}</v-row>
                      </div>
                  </template>
                  <template v-else-if="key == 'transportation'">
                    <v-select
                      v-model="schedule.columns[key]"
                      :items="transPortations"
                      item-title="value"
                      class="margin"
                    ></v-select>
                  </template>
                  <template v-else-if="key == 'cost'">
                    <v-text-field 
                      v-model="schedule.columns[key]" 
                      @update:modelValue="val => schedule.columns[key] = val"
                      :rules="costRules"
                      class="margin">
                    </v-text-field>
                  </template>
                  <template v-else-if="key == 'remarks'">
                    <v-text-field 
                      v-model="schedule.columns[key]" 
                      @update:modelValue="val => schedule.columns[key] = val"
                      :rules="remarkRules"
                      class="margin">
                    </v-text-field>
                  </template>
                  <template v-else>
                    <v-text-field 
                      v-model="schedule.columns[key]" 
                      @update:modelValue="val => schedule.columns[key] = val"
                      class="margin">
                    </v-text-field>
                  </template>

                </td>
              </template>
              <!-- 보기 모드 -->
              <template v-else>
                <!-- <v-btn @click="toggleEditMode(index)">Edit</v-btn> -->
                <td
                  v-for="(value, key) in schedule.columns"
                  :key="key"
                  :class="{ 'left-align': key === 'remarks', 'center-align': key !== 'remarks' || value === '' }"
                >
                
                <template v-if="value != '' && key === 'time'">
                  <!-- {{ index }} -->
                  {{ value[index].start }} ~ {{ value[index].end }}
                </template>
                <template v-else-if="key == 'transportation'">
                  {{ value }}
                </template>
                <template v-else-if="value != ''">
                  {{ value }}
                </template>
                <template v-else>
                  -
                </template>
                  
                </td>
              </template>
            </tr>
        </template>
        </v-data-table>

      
        <!-- 수정 버튼과 저장 버튼 -->
        <!-- <v-btn v-if="!isEditing" class="button-style" @click="startEditing">일정 수정</v-btn>
        <v-btn v-if="isEditing" class="button-style" @click="saveSchedule">저장</v-btn> -->

      </v-col>
    </v-row>


  </v-container>
    
</template>

<script>
// import TimeRangePicker from 'vuetify3-time-range-picker';


// import { toRaw  } from 'vue';
// import { ref, onMounted  } from 'vue';
import VueTimepicker from 'vue3-timepicker'
import 'vue3-timepicker/dist/VueTimepicker.css'
// import 'vuetify3-time-range-picker/dist/style.css'
import {
  VDataTable,
  // VDataTableServer,
  // VDataTableVirtual,
} from "vuetify/labs/VDataTable";


export default {
  
  components: {
    VDataTable,
    VueTimepicker,
    // TimeRangePicker,
    // VDataTableServer,
    // VDataTableVirtual,
  },
  created() {
    console.log(sessionStorage.getItem("projectNoSession"));
    console.log(sessionStorage.getItem("userNoSession"));
    
    //임시코드
    if(!sessionStorage.getItem("projectNoSession")){
      this.tripProjectNo = "99362bdccb54";
    }else{
      this.tripProjectNo = sessionStorage.getItem("projectNoSession")
    }

    if(!sessionStorage.getItem("userNoSession")){
      this.tripUserNo = "c65a23c9f07a";
    }else{
      this.tripUserNo = sessionStorage.getItem("userNoSession")
    }

    this.getSchedule();
  },
  mounted() {
    
    
  },
 
  watch: {
  },
  data() {
    return {
      schedules: [],
      tripProjectNo: "",
      tripUserNo: "",
      tripPlanAllNum: "",
      selectedSchedule: null,
      selectedScheduleDetails: [],
      // selectedScheduleDetails: reactive({}),
      // 표 헤더
      tableHeaders: [
        {title: '시간', align: "center", sortable: false, key: 'time',  width: '8%'},
        {title: '장소(출발지)', align: "center", sortable: false, key: 'departure',  width: '12%'},
        {title: '장소(목적지)', align: "center", sortable: false, key: 'destination', width: '12%'},
        {title: '이동수단', align: "center", sortable: false, key: 'transportation',  width: '8%'},
        {title: '비용', align: "center", sortable: false, key: 'cost', width: '12%'},
        {title: '메모', align: "center", sortable: false, key: 'remarks',  width: '35%'},
      ],

      isEditing: false, // 수정 중인지 여부를 나타내는 플래그
      editedSchedule: [], // 수정 중인 일정의 데이터를 담을 객체
      times: [
        // { start: '00:00', end: '05:00', duration: '1439' },
      ],
      transPortation: "",
      transPortations: [
        {value : "기차"},
        {value : "도보"},
        {value : "배"},
        {value : "버스"},
        {value : "비행기"},
        {value : "자가용"},
        {value : "지하철"},
        {value : "택시"},
      ],
      // hideWholeDayCheckbox: true,
      selectedIndex: "",
      alertMessage: "",
      alertFlag: false,
      costRules: [
        v => /^[0-9]*$/.test(v) || '숫자만 입력 가능합니다.',
      ],
      remarkRules: [
        v => !( v && v.length > 30 ) || '메모란은 최대 30글자까지 가능합니다.( ' + v.length + '글자 )',
      ]

    };
  },
  methods: {
    checkTimes(value, index, items, flag){

      const arrayLength = items.length;
      const regex = /[a-zA-Z]/;

      
       const currentStartDate = items[index].raw.startDate;
       const currentEndDate = items[index].raw.endDate;

       const currnetStartTime = value[index].start.replace(':','');
       const currnetEndTime = value[index].end.replace(':','');
      
      // const nextStartDate = items[index+1].raw.startDate;
      // const nextEndDate = items[index+1].raw.endDate;

      // const nextStartTime = value[index+1].start.replace(':','');
      // const nextEndTime = value[index+1].end.replace(':','');
      

       const currentStartFullStr = currentStartDate + currnetStartTime;
       const currentEndFullStr = currentEndDate + currnetEndTime;
      // const currentEndFullStr = currentEndDate + currnetEndTime;
      // const nextStartFullStr = nextStartDate + nextStartTime;
      // const nextEndFullStr = nextEndDate + nextEndTime;



      // 정규식 이용하여 HH:mm 있거나 ''인 경우 return
      if(regex.test(currentStartFullStr) || regex.test(currentEndFullStr) 
          || currnetStartTime == '' || currnetEndTime == ''){
        value[index].alertFlag = true;
        value[index].alertMessage = "시간 선택은 필수 입니다.";
      }else{
         value[index].alertFlag = true;
         value[index].alertMessage = "";
      }

      // startTime과 endTime비교하여 startTime이 큰 경우 제외

      // if(flag === "startTime"){

      // }
      
      console.log(value)
      console.log(value[index+1]);
      console.log(flag);
      console.log(arrayLength)
    


  
    },
    // updateScheduleTime(index, updatedValue) {
    //   // Vue 3에서는 $set을 사용하지 않고 직접 프로퍼티에 접근하여 업데이트
    //   console.log("updatedValue", updatedValue);
    //   this.selectedScheduleDetails[index].time = updatedValue;
    // },
    // handleTimeRangeUpdate(index){
    //   console.log("handleTimeRangeUpdate", index);
    //   // value[index]의 새로운 복사본을 만들어서 변경
    // // this.$set(this.value, index, Object.assign({}, updatedValue));
    // },
    modifySchedule(){

    },
    viewSchedule(index) {

      // 선택한 일정의 상세 정보 가져오기
      this.selectedSchedule = this.schedules[index];
      this.selectedIndex = index+1
      this.getDetailSchedule(this.selectedIndex);
    },
    startEditing() {
      // 수정 상태로 전환
      this.isEditing = true;

      // // this.editedSchedule = [];
      // // this.$set을 사용하지 않고 각 인덱스에 해당하는 항목을 추가
      // this.selectedScheduleDetails.forEach((schedule) => {
        
      //   console.log(toRaw(schedule));
      //   this.editedSchedule.push(toRaw(schedule));
      //   // this.editedSchedule = toRaw(schedule);
      //   // schedule은 proxy객체로 일반객체로 변환하기 위한 코드
        
      //   // this.editedSchedule[index] = { ...schedule };
      //   // this.editedSchedule = { ...this.editedSchedule }; // schedule은 proxy객체로 일반객체로 변환하기 위한 코드

      //   // console.log(this.editedSchedule);
      //   // console.log(this.isEditing);
        
      //   // schedule을 일반 객체로 변환
      //   // const scheduleObject = { ...schedule };
      //   // Vue.set을 사용해 직접적인 할당
      //   // Vue.set(this.editedSchedule, index, scheduleObject);

      // });
    

      // // console.log("editedSchedule", this.editedSchedule);
      // // console.log("schedules", this.schedules);
    },
    saveSchedule() {
      // 수정된 일정을 서버에 전송하는 로직을 추가하세요.
      // this.editedSchedule 객체에는 수정된 내용이 담겨 있습니다.
      // 성공적으로 서버에 전송하면 다시 일정 목록을 가져오는 등의 작업을 수행하세요.

       // 서버 전송 후 수정 상태 종료
      this.isEditing = false;
    },
    getTransportationString(transportationValue) {

      const transportationObject = this.transPortations.find(
        (item) => item.value === transportationValue
      );
      return transportationObject ? transportationObject.string : "";
    },
    getDetailSchedule(scheduleDay){

      this.selectedScheduleDetails = []; //초기화

      const tripProject = {
        tripProjectNo: this.tripProjectNo,
        tripScheduleDay: scheduleDay,
      }

      // const time = "";
      this.$axios.get('/trip/schedule/info',{
        params: tripProject 
      }).then(response => {

        const detailScheduleList = response.data;


         this.selectedScheduleDetails = detailScheduleList.map(e => {

          const startTime = e.tripScheduleStTime.slice(0, 2) + ':' + e.tripScheduleStTime.slice(2, 4);
          const endTime =  e.tripScheduleEdTime.slice(0, 2) + ':' + e.tripScheduleEdTime.slice(2, 4);
          

          // const transportation = e.tripScheduleTransport? this.getTransportationString(e.tripScheduleTransport): "";

          const departure = e.tripScheduleArrive ? e.tripScheduleArrive : "";
          const destination = e.tripScheduleDepart ? e.tripScheduleDepart : "";
          const transportation = e.tripScheduleTransport? e.tripScheduleTransport : "";

          const cost = e.tripScheduleCost ? e.tripScheduleCost : "";

          let remarks = e.tripScheduleNote ? e.tripScheduleNote : "";
          remarks = remarks != "" && remarks.length > 80 ? remarks.slice(0, 80) + ' ...' : remarks.trim();

          this.times.push({
            start: startTime,
            end: endTime,
            // displayTime: displayTime,
          })
          
          // 스케쥴 한 행의 시작날짜
          const startDate = e.tripScheduleStDt;

          // 스케쥴 한 행의 종료날짜
          const endDate = e.tripScheduleEdDt;

          return {
            time: this.times,
            departure: departure,
            destination: destination,
            transportation: transportation,
            cost: cost,
            remarks: remarks,
            startDate: startDate,
            endDate: endDate,
            alertFlag: false,
            alertMessage: "",
          };
        });

        
        // 이 부분에서 선택한 일정의 상세 정보를 가져오는 비동기 로직을 추가
        // 예시로 setTimeout을 사용해 0초 후에 데이터를 갱신하도록 함
        setTimeout(() => {
          const tableElement = document.getElementById('schedule-table');
          if (tableElement) {
            tableElement.scrollIntoView({ behavior: 'smooth' });
          }
        }, 0);
        
        console.log(response);
      }).catch(error => {
        console.log(error);
      }).finally(()=>{
            // this.editedSchedule = { ...this.selectedScheduleDetails }; // 수정 중인 일정 초기화
            console.log("this.times", this.times);
            
            
            console.log("this.selectedScheduleDetails", this.selectedScheduleDetails);
            
      })
    },

    deleteSchedule(idx){
      
      let tripScheduleDay = idx+1

      console.log(this.tripProjectNo)
      const paramMap = {
        tripProjectNo: this.tripProjectNo,
        tripScheduleDay: tripScheduleDay,
      }
      this.$axios.delete('/trip/schedule/info', {data: paramMap})
      .then(() => {
        
        
          this.getSchedule();
          this.selectedSchedule = ""; // 삭제 후 선택된 일정 초기화
          // this.this.schedules.pop();
        
      }).catch(()=>{

      })
    },
    getSchedule(){

      // 스케쥴 초기화
      this.schedules = [];

      const tripProject = {
        tripProjectNo: this.tripProjectNo,
      }

      this.$axios.get('/trip/schedule/info/list/date',{
        params: tripProject 
      }).then(response => {
        console.log("response", response);
        const scheduleList = response.data.scheduleList;
        this.tripPlanAllNum = response.data.TRIP_PLAN_ALL_NUM;

        
        scheduleList.forEach(e => {

          if(!e.deletedDate){
            const { TRIP_CURRENT_DAY, TRIP_ROUTE, TRIP_COST, TRIP_NOTE } = e;

            const splitDateArr =  TRIP_CURRENT_DAY.split('-')
            let tripCurentDay = splitDateArr[0]+"년 "+splitDateArr[1]+"월 "+splitDateArr[2]+"일 ( "+ e.TRIP_SCHEDULE_DAY + "일차 )";


            // 모든 요소를 ' → '로 연결
            const splitRouteArr = TRIP_ROUTE ? e.TRIP_ROUTE.split('/') : [];
            let tripRoute = splitRouteArr.length ? splitRouteArr.join(' → ') : '';

            // 원 단위 나누기
            let tirpCost = TRIP_COST ? TRIP_COST.toLocaleString()+"원" : '';
            let tripNote = TRIP_NOTE;
            
            
            this.schedules.push({ 
              date: tripCurentDay, 
              route: tripRoute, 
              cost: tirpCost,
              remarks: tripNote,
            })
          }
        });
        
      }).catch(() =>{
     
      })

    },

    addSchedule() {
      // this.$router.push('/add-schedule');

      const tripProject = {
        tripProjectNo: this.tripProjectNo,
      } 

      // eslint-disable-next-line no-unused-vars
      this.$axios.post('/trip/schedule/info', tripProject)
      .then(() => {

        // 조회
        this.getSchedule();

      }).catch(error => {
        console.error(error);
      })
    },
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

.no-cursor {
  caret-color: transparent; /* 커서를 투명하게 설정하여 감춤 */
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

.icon-row {
  margin-top: 10px;
}

.icon {
  color: #333;
  margin-right: 10px;
}

.subtitle {
  color: #333;
  margin-bottom: 10px;
}

.subtitle-detail {
  margin-top: -50px;
  color: #333;
  font-size: 17px;
  font-weight: bold;
}

.subtitle-detail-date{
  font-size: 13px;
  margin-left: 10px;
}

.mr-2 {
  margin-right: 8px;
}

.button-margin{
    margin-left: 10px;
    margin-right: 10px;
    margin-bottom: 10px;
}

.button-style{
   background-color: #333;
   color: #fff;
   font-size: 13px;
}

.schedule-contents{
  height: 250px;
}

.left-align{
  text-align: left;
}

.center-align{
  text-align: center;
}

.margin{
  margin-top: 10px;
  margin-bottom: -10px;
  margin-left: -10px;
  margin-right: -10px;
}

/* .timepicker-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.timepicker-container p {
    margin-bottom: 0;
} */

.start-timpicker {
    margin-top: 10px;
    margin-bottom: 3px;
}

.end-timpicker {
    margin-bottom: 10px;
}

.alert-message {
   color:#b00020;
   height: 12px;
   font-size: 10px;
   margin-top: -6px;
   margin-left: 25px;
   text-align: center;
   line-height: 12px;
   vertical-align: baseline;
   letter-spacing: 0.4px;
   word-spacing: 0px;
   font-family: Roboto, sans-serif;
   font-weight: 400;
   font-style: normal;
   font-variant: normal;
   text-transform: none;
   text-decoration: none solid rgb(176, 0, 32);
   text-align: start;
   text-indent: 0px;

}

.tr-style{
  height: 110px;
}

/* .plus-button-card{
  height: 360px;
} */


</style>
