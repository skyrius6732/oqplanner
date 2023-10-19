<template>
  <v-container class="custom-container">
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
        <v-data-table
        :headers="tableHeaders"
        :items="selectedScheduleDetails"
        class="elevation-1"
        height="400"
        item-value=""
        >
        <template v-slot:body="{ items }">
            <tr v-for="(schedule, index) in items" :key="index">
              <!-- 편집 모드 -->
              <template v-if="isEditing">
                <td
                  v-for="(value, key) in schedule.columns"
                  :key="key"
                  :class="{}"
                >
                  <template v-if="key == 'time'">
                  <time-range-picker
                    :modelValue="times[index]"
                    @update:modelValue="val => times[key] = val"
                    :hide-whole-day-checkbox="hideWholeDayCheckbox"
                    input-label=""
                    step="30"
                    class="margin"
                  />
                  </template>
                  <template v-else>
                    <v-text-field 
                      :modelValue="schedule.columns[key]" 
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
                <template v-if="value != ''">
                  {{ value }}
                </template>
                <template v-else>
                  -
                </template>
                  
                </td>
              </template>
            </tr>
            <!-- <time-range-picker
            v-model="time"
            variant="underlined"
            step="60"
            :max-duration="120"
            :hide-whole-day-checkbox="hideWholeDayCheckbox"
            color="blue"
          /> -->
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

// import { reactive, toRaw  } from 'vue';
import {  toRaw  } from 'vue';
import TimeRangePicker from 'vuetify3-time-range-picker';
import 'vuetify3-time-range-picker/dist/style.css'
import {
  VDataTable,
  // VDataTableServer,
  // VDataTableVirtual,
} from "vuetify/labs/VDataTable";


export default {
  
  components: {
    VDataTable,
    TimeRangePicker,
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
        {title: '시간', align: "center", sortable: false, key: 'time',  width: '14%'},
        {title: '장소(출발지)', align: "center", sortable: false, key: 'departure',  width: '12%'},
        {title: '장소(목적지)', align: "center", sortable: false, key: 'destination', width: '12%'},
        {title: '이동수단', align: "center", sortable: false, key: 'transportation',  width: '6%'},
        {title: '비용', align: "center", sortable: false, key: 'cost', width: '10%'},
        {title: '비고', align: "center", sortable: false, key: 'remarks',  width: '35%'},
      ],

      isEditing: false, // 수정 중인지 여부를 나타내는 플래그
      editedSchedule: [], // 수정 중인 일정의 데이터를 담을 객체
      times: [
        // { start: '00:00', end: '05:00', duration: '1439' },
        // { start: '05:00', end: '13:00', duration: '1439' },
        // { start: '13:00', end: '23:00', duration: '1439' },
        // { start: '13:00', end: '23:00', duration: '1439' },
      ],        
      
      
       // { start: '00:00', end: '23:59', duration: 1439 } (duration is in minutes)
      hideWholeDayCheckbox: true,

    };
  },
  methods: {
    modifySchedule(){

    },
    viewSchedule(index) {

      // 선택한 일정의 상세 정보 가져오기
      this.selectedSchedule = this.schedules[index];
      this.getDetailSchedule(index + 1);
    },
    startEditing() {
      // 수정 상태로 전환
      this.isEditing = true;

      this.editedSchedule = [];
      // this.$set을 사용하지 않고 각 인덱스에 해당하는 항목을 추가
      this.selectedScheduleDetails.forEach((schedule) => {
        
        console.log(toRaw(schedule));
        this.editedSchedule.push(toRaw(schedule));
        // this.editedSchedule = toRaw(schedule);
        // schedule은 proxy객체로 일반객체로 변환하기 위한 코드
        
        // this.editedSchedule[index] = { ...schedule };
        // this.editedSchedule = { ...this.editedSchedule }; // schedule은 proxy객체로 일반객체로 변환하기 위한 코드

        // console.log(this.editedSchedule);
        // console.log(this.isEditing);
        
        // schedule을 일반 객체로 변환
        // const scheduleObject = { ...schedule };
        // Vue.set을 사용해 직접적인 할당
        // Vue.set(this.editedSchedule, index, scheduleObject);

      });
    

      console.log("editedSchedule", this.editedSchedule);
      console.log("schedules", this.schedules);
    },
    saveSchedule() {
      // 수정된 일정을 서버에 전송하는 로직을 추가하세요.
      // this.editedSchedule 객체에는 수정된 내용이 담겨 있습니다.
      // 성공적으로 서버에 전송하면 다시 일정 목록을 가져오는 등의 작업을 수행하세요.

       // 서버 전송 후 수정 상태 종료
      this.isEditing = false;
    },
    getDetailSchedule(scheduleDay){

      this.selectedScheduleDetails = []; //초기화

      const tripProject = {
        tripProjectNo: this.tripProjectNo,
        tripScheduleDay: scheduleDay,
      }

      this.$axios.get('/trip/schedule/info',{
        params: tripProject 
      }).then(response => {

        const detailScheduleList = response.data;

        // detailScheduleList.forEach(e=>{

        //   const time= e.tripScheduleStTime.slice(0,2) + ':' + e.tripScheduleStTime.slice(2,4)
        //               + "~" + 
        //               e.tripScheduleEdTime.slice(0,2) + ':' + e.tripScheduleEdTime.slice(2,4);
        
        //   const departure = e.tripScheduleArrive ? e.tripScheduleArrive : "-";
        //   const destination = e.tripScheduleDepart ? e.tripScheduleDepart : "-";
        //   const transportation = e.tripScheduleTransport ? e.tripScheduleTransport : "-";
        //   const cost = e.tripScheduleCost ? e.tripScheduleCost+"원" : "-";

        //   let remarks = e.tripScheduleNote ? e.tripScheduleNote : "-";
        //   remarks = remarks != '-' && remarks.length > 80 ? remarks.slice(0, 80) + ' ...' : remarks.trim();
          

        //   this.selectedScheduleDetails.push({ 
        //       time: time, 
        //       departure: departure, 
        //       destination: destination,
        //       transportation: transportation,
        //       cost: cost,
        //       remarks: remarks,
        //   })
        // })

         this.selectedScheduleDetails = detailScheduleList.map(e => {

          const startTime = e.tripScheduleStTime.slice(0, 2) + ':' + e.tripScheduleStTime.slice(2, 4);
          const endTime =  e.tripScheduleEdTime.slice(0, 2) + ':' + e.tripScheduleEdTime.slice(2, 4);
          const time = startTime + '~' + endTime;

          const departure = e.tripScheduleArrive ? e.tripScheduleArrive : "";
          const destination = e.tripScheduleDepart ? e.tripScheduleDepart : "";
          const transportation = e.tripScheduleTransport ? e.tripScheduleTransport : "";
          const cost = e.tripScheduleCost ? e.tripScheduleCost : "";

          let remarks = e.tripScheduleNote ? e.tripScheduleNote : "";
          remarks = remarks != "" && remarks.length > 80 ? remarks.slice(0, 80) + ' ...' : remarks.trim();

          this.times.push({
            start: startTime,
            end: endTime,
          })

          return {
            time: time,
            departure: departure,
            destination: destination,
            transportation: transportation,
            cost: cost,
            remarks: remarks,
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

/* .plus-button-card{
  height: 360px;
} */


</style>
