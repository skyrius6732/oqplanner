<template>
  <v-container class="custom-container">
    <!-- <div class="d-flex justify-space-between mb-2"> -->
          <div class="subtitle-detail">
           <span >일정 간략히 보기</span>
          </div>
    <!-- </div> -->
    <v-row>
      <v-col v-for="(schedule, index) in schedules" :key="index" cols="12" md="4">
        <v-card class="custom-card">
          <v-card-title class="title text-left">
            {{ schedule.date }}
          </v-card-title>
          <v-card-text class="schedule-contents">
            <v-row class="icon-row">
              <v-col cols="2" md="1">
                <v-icon class="icon">mdi-map-marker</v-icon>
              </v-col>
              <v-col cols="10" md="11">
                <h3 class="subtitle">여행 경로</h3>
                {{ schedule.route.length >= 20 ? schedule.route.slice(0, 20) + ' ...' : schedule.route }}
              </v-col>
            </v-row>

            <v-row class="icon-row">
              <v-col cols="2" md="1">
                <v-icon class="icon">mdi-currency-krw</v-icon>
              </v-col>
              <v-col cols="10" md="11">
                <h3 class="subtitle">여행 비용</h3>
                <p>{{ schedule.cost }}</p>
              </v-col>
            </v-row>

            <v-row class="icon-row">
               <v-col cols="2" md="1">
                <v-icon class="icon">mdi-comment-outline</v-icon>
              </v-col>
              <v-col cols="10" md="11">
                <h3 class="subtitle">메모</h3>
                <ul v-if="schedule.remarks">
                    <li v-for="(note, index) in schedule.remarks.split('/').filter(value => value.trim() !== '').slice(0,4)" :key="index">
                        {{ note.length > 20 ? note.slice(0, 20) + ' ...' : note.trim() }}
                    </li>
                </ul>
              </v-col>
            </v-row>
          </v-card-text>
          <v-row class="icon-row">
            <v-col :cols="12" class="d-flex justify-end">
                <v-btn v-if="index === schedules.length - 1" class="button-style" @click="startDeleteDialog(index)">일정 삭제</v-btn>
                <v-btn class="button-margin button-style" @click="viewSchedule(index)">일정 보기</v-btn>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
      <v-col :cols="12" md="4">
        <v-card v-if="schedules.length < tripPlanAllNum" class="plus-button-card" @click="addSchedule">
          <v-card-text class="text-center">
              <v-icon size="55">mdi-plus-box</v-icon>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

   <!-- 밑줄 -->
    <v-row id="schedule-table" class="underline-row">
      <v-col>
        <v-divider></v-divider>
      </v-col>
    </v-row>

     <!-- 일정 자세히 보기 -->
    <v-form ref="form" lazy-validation>
      <v-row class="d-flex justify-space-between mb-2" v-if="selectedSchedule"> 
          <div>
           <span class="subtitle-detail">일정 자세히 보기</span>
           <span class="subtitle-detail-date" >  {{ selectedSchedule && selectedSchedule.date }} </span>
          </div>
      </v-row>
      <v-row v-if="selectedSchedule">
          <v-col :cols="12" class="d-flex justify-end">
             <!-- 수정 상태인지 확인하고, 수정 상태라면 저장 버튼을 표시 -->
            <template v-if="isEditing">
              <v-btn class="button-style" @click="saveSchedule">일정 저장</v-btn>
              <v-btn class="button-style" @click="startShowConfirmDialog">일정 보기</v-btn>
            </template>
            <template v-else>
              <v-btn class="button-style" @click="startEditing">일정 수정</v-btn>
            </template>
          </v-col>
      </v-row>
    <v-row v-if="selectedSchedule">
 
      <v-col>
        <!-- height selectedScheduleDetails 개수에 따라 동적변경 진행 -->
        <v-data-table
        :headers="tableHeaders"
        :items="selectedScheduleDetails"
        :items-per-page="60"
        height="800px"
        mobile-breakpoint="1000px"
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
                        <v-row 
                        class="edit-schedule-date">  
                        {{ items[index].raw.startDate.slice(0,4) }}년 
                        {{ items[index].raw.startDate.slice(4,6) }}월 
                        {{ items[index].raw.startDate.slice(6,8) }}일 </v-row>
                        <vue-timepicker 
                          v-model="selectedScheduleDetails[index].time[index].start" 
                          class="start-timpicker"
                          @change="checkTimes(value,index,items,'startTime')"/>
                        <v-row 
                        class="edit-schedule-date"> 
                        {{ items[index].raw.endDate.slice(0,4) }}년 
                        {{ items[index].raw.endDate.slice(4,6) }}월 
                        {{ items[index].raw.endDate.slice(6,8) }}일</v-row>  
                        <vue-timepicker 
                          v-model="selectedScheduleDetails[index].time[index].end" 
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
                      v-model="selectedScheduleDetails[index][key]"
                      :items="transPortations"
                      item-title="value"
                      class="margin"
                    ></v-select>
                  </template>
                  <template v-else-if="key == 'cost'">
                    <v-text-field 
                      v-model="selectedScheduleDetails[index][key]" 
                      @update:modelValue="val => selectedScheduleDetails[index][key] = val"
                      @keyup.enter="saveSchedule"
                      :rules="costRules"
                      class="margin"
                      >
                    </v-text-field>
                  </template>
                  <template v-else-if="key == 'remarks'">
                    <v-text-field 
                      v-model="selectedScheduleDetails[index][key]" 
                      @update:modelValue="val => selectedScheduleDetails[index][key] = val"
                       @keyup.enter="saveSchedule"
                      :rules="remarkRules"
                      class="margin">
                    </v-text-field>
                  </template>
                  <template v-else>
                    <v-row>
                      <v-col cols="7">
                        <v-text-field 
                          v-model="selectedScheduleDetails[index][key]" 
                          @update:modelValue="val => selectedScheduleDetails[index][key] = val"
                          class="margin"
                          readonly>
                        </v-text-field>
                      </v-col>
                      <v-col cols="3">  
                        <v-btn class="spot-button-style" size="x-large" @click="showModal(index, key)">선택</v-btn>
                      </v-col>
                    </v-row>
                  </template>

                </td>
              </template>
              <!-- 보기 모드 -->
              <template v-else>
                <td
                  v-for="(value, key) in schedule.columns"
                  :key="key"
                  :class="{ 'left-align': key === 'remarks', 'center-align': key !== 'remarks' || value === '' }"
                >
                
                <template v-if="value != '' && key === 'time'">
                  <v-row 
                        class="show-schedule-date">  
                        {{ items[index].raw.startDate.slice(0,4) }}년 
                        {{ items[index].raw.startDate.slice(4,6) }}월 
                        {{ items[index].raw.startDate.slice(6,8) }}일 
                        </v-row>{{ value[index].start }}
                  <v-row 
                        class="show-schedule-date">  
                        {{ items[index].raw.startDate.slice(0,4) }}년 
                        {{ items[index].raw.startDate.slice(4,6) }}월 
                        {{ items[index].raw.startDate.slice(6,8) }}일
                        </v-row> {{ value[index].end }}
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
            <TripScheduleModal 
                        v-model="isModalVisible"
                        @closeModal="closeModal"
                        @submitTitle="submitTitle"
                        ref="modal"/>
        </template>
        <template v-slot:bottom></template>
        </v-data-table>

      </v-col>

    </v-row>
    </v-form>
  </v-container>

  <v-dialog
      v-model="showDialog"
      persistent
      width="auto"
    >
      <v-card>
        <v-card-title class="text-h5">
          이전 데이터를 보시겠습니까?
        </v-card-title>
        <v-card-text>지금까지 수정 하신 데이터가 저장되지 않습니다.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            variant="text"
            @click="showDialog = false"
            class="button-style"
          >
            취소
          </v-btn>
          <v-btn
            variant="text"
            @click="startShowing"
            class="button-style"
          >
            확인
          </v-btn>
        </v-card-actions>
      </v-card>
  </v-dialog>

  <v-dialog
      v-model="deleteDialog"
      persistent
      width="auto"
    >
      <v-card>
        <v-card-title class="text-h5">
          선택한 일정을 삭제 하시겠습니까?
        </v-card-title>
        <v-card-text>선택한 일정의 모든 데이터가 삭제 됩니다.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            variant="text"
            @click="deleteDialog = false"
            class="button-style"
          >
            취소
          </v-btn>
          <v-btn
            variant="text"
            @click="deleteSchedule(this.scheduleDeleteIdx)"
            class="button-style"
          >
            확인
          </v-btn>
        </v-card-actions>
      </v-card>
  </v-dialog>

</template>


<script>
// import TimeRangePicker from 'vuetify3-time-range-picker';


// import { toRaw  } from 'vue';
// import { ref, onMounted  } from 'vue';
import VueTimepicker from 'vue3-timepicker'
import 'vue3-timepicker/dist/VueTimepicker.css'
import { reactive } from 'vue';
// import 'vuetify3-time-range-picker/dist/style.css'
import {
  VDataTable,
  // VDataTableServer,
  // VDataTableVirtual,
} from "vuetify/labs/VDataTable";

// import { VDataTableVirtual } 
// from 'vuetify/labs/VDataTable/VDataTableVirtual';

import TripScheduleModal from './TripScheduleModal.vue';

export default {
  
  components: {
    VDataTable,
    VueTimepicker,
    // VDataTableVirtual,
    // TimeRangePicker,
    // VDataTableServer,
    // VDataTableVirtual,
    TripScheduleModal,
  },
  created() {

    this.tripProjectNo = sessionStorage.getItem("projectNoSession");
    this.tripUserNo = sessionStorage.getItem("userNoSession");

     console.log('projectNoSession', sessionStorage.getItem("projectNoSession"));
      console.log('userNoSession',sessionStorage.getItem("userNoSession"));
    
    // 임시코드 (빌드없이 프론트단 사용을 위한...)
    // 추후에 지워야함
    if(sessionStorage.getItem("projectNoSession")){
      console.log('강제로 넣어줌 안타?');
      this.tripProjectNo = "64c9505cf55f";
    }else{
      console.log('강제로 넣어줌 안타?2222222');
      this.tripProjectNo = sessionStorage.getItem("projectNoSession")
    }

    if(sessionStorage.getItem("userNoSession")){
      this.tripUserNo = "966e78ad4ea1";
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
      scheduleDeleteIdx: '',
      showDialog: false,
      deleteDialog: false,
      schedules: [],
      tripProjectNo: "",
      tripUserNo: "",
      tripPlanAllNum: "",
      selectedSchedule: null,
      selectedScheduleDetails: reactive({
        // 데이터 속성들...
      }),
      selectedScheduleDetailsBak: [],
      // selectedScheduleDetails: reactive({}),
      // 표 헤더
      tableHeaders: [
        {title: '시간', align: "center", sortable: false, key: 'time',  width: '5%'},
        {title: '장소(출발지)', align: "center", sortable: false, key: 'departure',  width: '12%'},
        {title: '장소(목적지)', align: "center", sortable: false, key: 'destination', width: '12%'},
        {title: '이동수단', align: "center", sortable: false, key: 'transportation',  width: '8%'},
        {title: '비용', align: "center", sortable: false, key: 'cost', width: '12%'},
        {title: '메모', align: "center", sortable: false, key: 'remarks',  width: '20%'},
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
      saveFlag: false,
      scheduleDate: "",
      alertFlag: false,
      costRules: [
        v => /^[0-9]*$/.test(v) || '숫자만 입력 가능합니다.',
      ],
      remarkRules: [
        v => !( v && v.length > 30 ) || '메모 입력은 최대 30글자까지 가능합니다.( ' + v.length + '글자 )',
      ],
      isModalVisible: false,

    };
  },
  methods: {

    submitTitle(value, currentIdx, currentKey){

      console.log('submitTitle value', value);
      console.log('submitTitle currentIdx', currentIdx);
      console.log('submitTitle currentKey', currentKey);
      this.selectedScheduleDetails[currentIdx][currentKey] = value;

      this.closeModal();

    },

    showModal(index, key){
      
      console.log('this.isModalVisible', this.isModalVisible);
       // 모달을 열 때 현재 행의 index를 함께 전달
       console.log('this.$refs', this.$refs.modal); 

      this.$refs.modal.showModal(index, key);

        this.isModalVisible = true;
      
    },
    closeModal(){
      // this.$refs.privateCostMethod.costShow();
      this.isModalVisible = false;
    },
    checkTimes(value, index, items, flag){

      const arrayLength = items.length;
      const regex = /[a-zA-Z]/;
      const nextIdx = index+1;
      const beforeIdx = index-1;
      
      const currentStartDate = items[index].raw.startDate;
      const currentEndDate = items[index].raw.endDate;

      const currnetStartTime = value[index].start.replace(':','');
      const currnetEndTime = value[index].end.replace(':','');
      
      let nextStartDate = "";
      let beforeEndDate = "";
      // let nextEndDate = "";

      let nextStartTime = "";
      let beforeEndTime = "";
      // let nextEndTime = "";

      let nextStartFullStr = "";
      let beforeEndFullStr = "";
      // let nextEndFullStr = "";

      if(nextIdx < arrayLength){
        nextStartDate = items[nextIdx].raw.startDate;
        nextStartTime = value[nextIdx].start.replace(':','');
        nextStartFullStr = nextStartDate + nextStartTime;
      }

      if(beforeIdx >= 0){
         beforeEndDate = items[beforeIdx].raw.endDate;
         beforeEndTime = value[beforeIdx].end.replace(':','');
         beforeEndFullStr = beforeEndDate + beforeEndTime;
      }
    
      

      const currentStartFullStr = currentStartDate + currnetStartTime;
      const currentEndFullStr = currentEndDate + currnetEndTime;
      
      console.log("currentStartFullStr",  currentStartFullStr);
      console.log("currentEndFullStr",  currentEndFullStr);
      console.log("nextStartFullStr",  nextStartFullStr);
      console.log("beforeEndFullStr",  beforeEndFullStr);
      console.log("beforeIdx", beforeIdx);


      // 정규식 이용하여 HH:mm 있거나 ''인 경우 제외
      if(regex.test(currentStartFullStr) || regex.test(currentEndFullStr) 
          || currnetStartTime == '' || currnetEndTime == ''){
        value[index].alertFlag = true;
        value[index].alertMessage = "시간 선택은 필수 입니다.";
        this.saveFlag = false;
        return;
      }else if(currentStartFullStr > currentEndFullStr){
        //startTime과 endTime비교하여 startTime이 큰 경우 제외
        value[index].alertFlag = true;
        value[index].alertMessage = "현재 종료시간 > 현재 시작시간";
        console.log("현재 스케쥴 비교");
        this.saveFlag = false;
        return;
  
      }else if(nextIdx < arrayLength && currentEndFullStr > nextStartFullStr){
        value[index].alertFlag = true;
        value[index].alertMessage = "현재 종료시간 < 다음 시작시간";
        console.log("after 비교")
        this.saveFlag = false;
        return;
      }else if(beforeIdx >= 0 && currentStartFullStr < beforeEndFullStr){
        value[index].alertFlag = true;
        value[index].alertMessage = "현재 시작시간 > 이전 종료시간";
        console.log("before 비교")
        this.saveFlag = false;
        return;
      }else{
        value[index].alertFlag = false;
        value[index].alertMessage = "";
        this.saveFlag = true;
      }
      
      
      console.log(value)
      console.log(value[index+1]);
      console.log(flag);
      console.log(arrayLength)
    },
    modifySchedule(){

    },
    viewSchedule(index) {

      // 선택한 일정의 상세 정보 가져오기
      this.selectedSchedule = this.schedules[index];
      this.selectedIndex = index+1
      this.getDetailSchedule(this.selectedIndex);
    },
    startShowConfirmDialog(){
      this.showDialog = true;
    },
    startDeleteDialog(index){
      this.deleteDialog = true;
      this.scheduleDeleteIdx = index;
    },
    startShowing(){
      // dialog 닫기
      this.showDialog = false;

      // 보기 상태로 전환
      this.isEditing = false;
      this.getDetailSchedule(this.selectedIndex);
      // selectedScheduleDetails을 선택된 일정 데이터로 설정
      // this.selectedScheduleDetails = [...this.selectedScheduleDetailsBak];
      console.log("this.selectedScheduleDetails", this.selectedScheduleDetails);
    },
    startEditing() {
      // 수정 상태로 전환
      this.isEditing = true;

      // 현재 선택한 일정의 데이터를 selectedScheduleDetailsBak 복사
      // this.selectedScheduleDetailsBak = [...this.selectedScheduleDetails];
      console.log("this.selectedScheduleDetailsBak", this.selectedScheduleDetailsBak);
    },

    async saveSchedule() {
      // 수정된 일정을 서버에 전송하는 로직을 추가하세요.
      // 성공적으로 서버에 전송하면 다시 일정 목록을 가져오는 등의 작업을 수행하세요.

      if(this.saveFlag == true){

        await this.$refs.form.validate().then(async result => {
          if (result.valid) {
          console.log("saveSchedule", this.selectedScheduleDetails);
          // 데이터 정제
          console.log("tripUserNo", this.tripUserNo);
          console.log("tripProjectNo", this.tripProjectNo);

          let tripProject = {}
          const tripScheduleList = []
          this.selectedScheduleDetails.forEach(e => {

            const scheduleOrder = e.scheduleOrder;
            
            tripScheduleList.push({
                tripProjectNo: this.tripProjectNo,
                tripScheduleStDt: e.startDate,
                tripScheduleEdDt: e.endDate,
                tripScheduleStTime: e.time[scheduleOrder].start.replace(":",""),
                tripScheduleEdTime: e.time[scheduleOrder].end.replace(":",""),
                tripScheduleDepart: e.departure,
                tripScheduleArrive: e.destination,
                tripScheduleTransport: e.transportation,
                tripScheduleCost: e.cost,
                tripScheduleNote: e.remarks,
                tripScheduleOrder: scheduleOrder,
                tripScheduleDay: this.selectedIndex,  // 선택일자
            })
          });

          tripProject = {
              tripProjectNo: this.tripProjectNo,
              tripUserNo: this.tripUserNo,
              tripScheduleList: tripScheduleList
          }

          console.log("tripProject", tripProject)
          

          this.$axios.put('/trip/schedule/info/list', tripProject).then(response => {
                      // 성공적으로 전송된 경우의 처리
                      console.log(response.data);
                      // return this.$axios.post(`/trip/plan/info`, tripPlan);
              })
              .catch(error => {
                      // 전송 중 오류가 발생한 경우의 처리
                      console.error(error);
              }).finally(()=>{
                this.getSchedule();
              });
                
          // 서버 전송 후 수정 상태 종료
          this.isEditing = false;
          }
        })
      }
    },


    getDetailSchedule(scheduleDay){

      this.selectedScheduleDetails = []; //다시 조회 하기에 선택된 스케쥴 배열 초기화(시간 제외)
      this.times = [];  // 다시 조회 하기에 선택된 시간 배열 초기화

      const tripProject = {
        tripProjectNo: this.tripProjectNo,
        tripScheduleDay: scheduleDay,
      }

      // const time = "";
      this.$axios.get('/trip/schedule/info',{
        params: tripProject 
      }).then(response => {

        const detailScheduleList = response.data;

      this.$nextTick(() => {
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

          const scheduleOrder = e.tripScheduleOrder;

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
            scheduleOrder: scheduleOrder,
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

      })
        
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
      
      // deleteDialog 닫기
      this.deleteDialog = false

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

            let tripRoute = splitRouteArr.length ? splitRouteArr.filter(item=> item !== '').join(' → ') : '';

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
     
      }).finally(() => {
         // 이 부분에서 선택한 일정의 상세 정보를 가져오는 비동기 로직을 추가
        // 예시로 setTimeout을 사용해 0초 후에 데이터를 갱신하도록 함
        setTimeout(() => {
          const tableElement = document.getElementById('schedule-table');
          if (tableElement) {
            tableElement.scrollIntoView({ behavior: 'smooth' });
          }
        }, 0);
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
  color: #333;
  font-size: 17px;
  font-weight: bold;
}

.subtitle-detail-date{
  font-size: 13px;
  /* margin-left: 10px; */
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
   background-color: #333 !important; 
   color: #fff;
   font-size: 13px;
   margin-left: 5px;
}

.spot-button-style{
   margin-left: -10px;
   margin-top: 12px;
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
    margin-bottom: 10px;
}

.end-timpicker {
    margin-top: 10px;
    margin-bottom: 10px;
}

.alert-message {
   color:#b00020;
   font-weight: bold;
   height: 12px;
   font-size: 9px;
   margin-top: -6px;
   margin-left: 5px;
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

.edit-schedule-date {
   color:#000000;
   font-weight: bold;
   font-size: 8px;
   margin-left: 5px;
   margin-top: 5px;
}

.show-schedule-date{
  color:#000000;
   font-weight: bold;
   font-size: 8px;
   margin-left: 5px;
   margin-bottom: 5px;
}

.tr-style{
  height: 150px;
}

/** 모바일, pc */
/* Styles for mobile devices (adjust font sizes and paddings as needed) */
  @media (max-width: 768px) {
    .subtitle-detail{
       font-size: 0.8rem; /* Adjust font size */
    }
    .subtitle-detail-date{
       font-size: 0.6rem; /* Adjust font size */
    }
    .v-btn {
      font-size: 0.8rem; /* Adjust font size */
      /* padding: 4px 8px; Adjust padding */
    }
    .button-style{
      font-size: 0.8rem; /* Adjust font size */
    }
    .spot-button-style{
      padding: 0px
    }
    .v-data-table td {
      /* transform: rotate(0deg); */
      width:300px;
    }
  }

/* .plus-button-card{
  height: 360px;
} */


</style>
