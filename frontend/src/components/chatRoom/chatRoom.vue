<template>
  <div class="chat-room">
    <div>
      <div class="chat-toggle" v-if="!showChat" @click="enterChat($event)">{{ chatRoomText }}</div>  <!-- 채팅방입장 -->
    </div>
    <div class="chat-toggle"  v-if="showChatter" @click="toggleChatList($event)">
      채팅자
    </div>
    <div v-if="showChatterList" class="chat-list-window">
      <ul>
        <li v-for="chatter in chatterTotalList" :key="chatter.userNo">
          <span class="emoticon">😊</span>
          <span class="user-name">{{chatter.userName}}</span>
          <span class="date"> ( {{chatter.date}} )</span>
        </li>
      </ul>
    </div>
    <div class="chat-toggle"  v-if="showChat" @click="toggleChat($event)">                           <!-- 채팅방 -->
      {{ chatRoomText }}
       <button class="exit-button" v-if="showChat" @click="exitChat($event)">나가기</button>
    </div>
   
    <div v-if="showChat" class="chat-window">
      <!-- 채팅 내용 표시 -->
      <ul class="chat-container">
         <!-- 이전 메시지 -->
        <li v-for="previousChat in previousChatMessages" :key="previousChat.date" 
          :class="{ 'user-chat': this.userNo === previousChat.userNo, 
                    'other-chat': this.userNo !== previousChat.userNo, 
                    'system-chat': previousChat.systemMessage,
                  }"
          class="chat">
          <div>
            <v-container>
            <v-row>
              <v-col>
              <!-- <span v-if="this.userNo === previousChat.userNo" class="other-person-name">{{ previousChat.userName }}</span> -->
              <span v-if="!previousChat.systemMessage" class="other-person-name">{{ previousChat.userName }}</span>
              <span v-if="!previousChat.systemMessage" class="chat-date">  [{{ previousChat.date }}]  </span>
              <div class="message-content">
                {{ previousChat.text }}
              </div>
              </v-col>
            </v-row>
            </v-container>
          </div>
        </li>
        <li v-for="chat in chatTotalMessages" :key="chat.date" 
          :class="{ 'user-chat': this.userNo === chat.userNo, 
                    'other-chat': this.userNo !== chat.userNo, 
                    'system-chat': chat.systemMessage,
                  }"
          class="chat"
        >
        <div class="scroll-to-bottom" v-if="!isScrolledToBottom" @click="scrollToBottom">
           ⏬최신 메세지 이동⏬
        </div>
        <div>
          <v-container>
            <v-row>
              <v-col>
              <!-- <span v-if="this.userNo === chat.userNo" class="other-person-name">{{ chat.userName }}</span> -->
              <span v-if="!chat.systemMessage" class="other-person-name">{{ chat.userName }}</span>
              <span v-if="!chat.systemMessage" class="chat-date">  [{{ chat.date }}]  </span>
              <div class="message-content">
                {{ chat.text }}
              </div>
              </v-col>
            </v-row>
          </v-container>
        </div>
        </li>
      </ul>
    </div>
    <div v-if="showChat">
        <!-- 채팅 입력 폼 -->
      <div class="input-container">
        <input type="text" v-model="newMessage" @keyup.enter="sendMessage" placeholder="메세지를 입력하세요.">
        <button @click="sendMessage">전송</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  mounted(){
    // window.addEventListener('wheel', this.handleScroll);
    window.addEventListener('scroll', this.handleScroll, true);

    // setInterval(this.getChatters(), 5000);
  },
  created(){
    this.userNo = sessionStorage.getItem("userNoSession");
    // this.userNo = '66f21fb2b35c'; // 임시
    this.projectNo = sessionStorage.getItem("projectNoSession");
    this.getTripUserInfo();
    // this.connect();
    
  },

  data() {
    return {
      showChat: false,
      showChatterList: false,
      showChatter: false,
      chatRoomText: '채팅방 입장',
      chatMessages: [], // 채팅 메세지 (send 전달 용)
      chatTotalMessages: [], // 채팅 전체 내역
      previousChatMessages: [], // 이전 내역 불러와서 보여주기
      // chatterList: [],  // 채팅자 리스트 (send 전달 용)
      chatterTotalList: [],  // 채팅자 전체 리스트
      newMessage: '',
      lastReadIndex: -1, // 초기에는 마지막으로 읽은 채팅 없음을 나타냄
      chatEnterFlag: false,
      userNo: '',
      userName: '',
      projectNo: '',
      websocket: '',
      isScrolledToBottom: true, // 스크롤이 제일 아래로 이동했는지 여부를 추적
      // address: "ws://localhost:8081/oqplanner/ws/chat", // 임시
      address: 'ws://13.209.197.38:8081/oqplanner/ws/chat',
    };
  },
  methods: {

    // handleWebSocketClose(event) {
    //   // WebSocket이 닫힐 때 수행할 작업을 여기에 작성
    //   console.log('WebSocket이 닫혔습니다.', event);
    //   this.getChatters();
    //   // 예를 들어, 재연결을 시도할 수 있습니다.
    //   // this.reconnectWebSocket();
    // },


    connect(){
      this.websocket = new WebSocket(this.address);

      // WebSocket 연결이 닫힐 때의 이벤트 핸들러


      if(!this.chatEnterFlag){  // 첫 입장
        this.websocket.onopen=()=>{
            console.log('Connection opened');
              this.websocket.send(JSON.stringify(this.chatMessages[0]));
              this.chatMessages = [];
        }
      }

      this.websocket.onclose=(event)=>{
        // WebSocket이 닫힐 때 수행할 작업을 여기에 작성
        console.log('WebSocket이 닫혔습니다.', event);
        this.getChatters();
        // 예를 들어, 재연결을 시도할 수 있습니다.
        // this.reconnectWebSocket();
      }

      

  
      this.websocket.onmessage = (event)=>{
          console.log('take a message');
          console.log("onmessage", event.data);
          try{
            const message = JSON.parse(event.data);

            // 받은 메시지를 chatTotalMessages 배열에 추가하여 화면에 표시
            const currentDate = new Date();
            const formattedDate = `${currentDate.getFullYear().toString().substr(-2)}-${(currentDate.getMonth() + 1).toString().padStart(2, '0')}-${currentDate.getDate().toString().padStart(2, '0')} ${currentDate.getHours().toString().padStart(2, '0')}:${currentDate.getMinutes().toString().padStart(2, '0')}:${currentDate.getSeconds().toString().padStart(2, '0')}`;
            this.chatTotalMessages.push({
              userNo: message.userNo,
              userName: message.userName,
              text: message.text,
              date: formattedDate,
              systemMessage: message.systemMessage,
            });

            // 현재 채팅자 목록 get (정상적으로 enter/exit 시)
            if(message.flag=="enter" || message.flag=="exit"){
              this.getChatters();
            }

            if(this.userNo == message.userNo){
              this.moveScroll(); 
            }
          }catch(error){
            console.error('JSON 파싱 오류:', error);
          }  
      }

    },
    
    onClose() {
        const currentDate = new Date();
        const formattedDate = `${currentDate.getFullYear().toString().substr(-2)}-${(currentDate.getMonth() + 1).toString().padStart(2, '0')}-${currentDate.getDate().toString().padStart(2, '0')} ${currentDate.getHours().toString().padStart(2, '0')}:${currentDate.getMinutes().toString().padStart(2, '0')}:${currentDate.getSeconds().toString().padStart(2, '0')}`;
        this.chatMessages.push({
            flag: "exit",
            userNo: this.userNo,
            userName: this.userName,
            text: ` [알림] "${this.userName}"님이 퇴장 하셨습니다.`,
            date: formattedDate,
            systemMessage: true,
        });
        
        this.websocket.send(JSON.stringify(this.chatMessages[0]));
        this.websocket.close();
        this.chatMessages=[];
    },

    getChatters(){
      this.chatterTotalList=[]; // 초기화
      this.$axios.get('/trip/chat/chatter')
      .then(response => {
        console.log('getChatter response', response)
          response.data.forEach(item => {
              console.log("getChatter", item);
              this.chatterTotalList.push(item);
          });
      }).finally(()=>{
        // this.moveScroll();
      });
    },

    getBeforMessages(){
      this.previousChatMessages=[];  // 초기화
      const value = {
            date: "",   // date 입력시 해당 날짜에 대한 key값을 가져오도록 함 (2024 *) 형식으로 가져오기에
                        // date yymmddhhmmss 다 안넣어도 상관없음
        };
      this.$axios.get('/trip/chat/message' ,{
          params: value,
      }).then(response => {

          response.data.forEach(item => {
              this.previousChatMessages.push(item);
          });
      }).finally(()=>{
        this.moveScroll();
      });
    },

    getTripUserInfo(){
        console.log("getTripUserInfo");
        const tripUser = {
            tripUserNo: this.userNo,
        };
        this.$axios.get('/trip/user/info', {
          params:tripUser
        }).then(userResponse => {
          this.userName = userResponse.data.tripUserName;
        });
         
    },
    enterChat(event){
      console.log("enterChat!!")
      event.stopPropagation();

      this.showChat = !this.showChat;
      this.showChatterList = this.showChat;
      this.showChatter = this.showChat;

      if(!this.chatEnterFlag){  // 첫입장
          this.chatRoomText = "채팅방";

    
          (async ()=> {
             // 기존 채팅 내역 get
            await this.getBeforMessages();

            // 현재 입장의 알림 push
            const currentDate = new Date();
            const formattedDate = `${currentDate.getFullYear().toString().substr(-2)}-${(currentDate.getMonth() + 1).toString().padStart(2, '0')}-${currentDate.getDate().toString().padStart(2, '0')} ${currentDate.getHours().toString().padStart(2, '0')}:${currentDate.getMinutes().toString().padStart(2, '0')}:${currentDate.getSeconds().toString().padStart(2, '0')}`;
            console.log("formattedDate 입장 date:: " + formattedDate);
           
            this.chatMessages.push({   // 자신과 타인의 클라이언트에 보여지는 chatTotalMessages에 입장 추가  
                                      // 이후 webSocket.send로 전달되어 다른 클라이언트에게 보여짐
              flag: "enter",
              userNo: this.userNo,
              userName: this.userName,
              text: ` [알림] "${this.userName}"님이 입장 하셨습니다.`,
              // date: new Date().toString(),
              date: formattedDate,
              systemMessage: true,
            });



          })();
          this.connect();  
      }

      if (this.showChat) {
          // 채팅창이 열릴 때마다 마지막으로 읽은 채팅 위치로 스크롤 이동
          console.log("showChat!!");
          this.moveScroll();
      }

      this.chatEnterFlag = true;
    },
    exitChat(event){
      console.log("exitChat!!")
      event.stopPropagation();
      this.onClose();
      this.chatEnterFlag = false;
      this.chatRoomText = "채팅방 입장";  // 채팅방 밖의 문구 변경하여 입장도록 유도
      this.showChat = !this.showChat;
      this.showChatterList = this.showChat;
      this.showChatter = this.showChat;
      this.chatMessages = [];
      this.chatTotalMessages=[];
    },
    toggleChatList(event){
      event.stopPropagation();
      this.showChatterList = !this.showChatterList;
      // if (this.showChatterList) {
      //     // 채팅창이 열릴 때마다 마지막으로 읽은 채팅 위치로 스크롤 이동
      //     // this.moveScroll();
      // }
    },

    toggleChat(event) {
      console.log("toggleChat chatEnterFlag :: " + this.chatEnterFlag);
      event.stopPropagation();
      this.showChat = !this.showChat;
      this.showChatterList = this.showChat;
      this.showChatter = this.showChat;
      
      if (this.showChat) {
          // 채팅창이 열릴 때마다 마지막으로 읽은 채팅 위치로 스크롤 이동
          this.moveScroll();
      }
    },
    sendMessage() {
      if (this.newMessage.trim() !== '') {
        const currentDate = new Date();
        const formattedDate = `${currentDate.getFullYear().toString().substr(-2)}-${(currentDate.getMonth() + 1).toString().padStart(2, '0')}-${currentDate.getDate().toString().padStart(2, '0')} ${currentDate.getHours().toString().padStart(2, '0')}:${currentDate.getMinutes().toString().padStart(2, '0')}:${currentDate.getSeconds().toString().padStart(2, '0')}`;
        this.chatMessages.push({
          flag: "message",
          userNo: this.userNo,
          userName: this.userName,
          date: formattedDate, 
          text: this.newMessage,
          systemMessage: false,
          });
        this.newMessage = '';
        this.websocket.send(JSON.stringify(this.chatMessages[0]));
        this.chatMessages = [];
      }
      // this.moveScroll(); // 스크롤클릭시아래로 주석
     
    },
    moveScroll() {
      // 스크롤을 맨 아래로 이동
      this.$nextTick(() => {
        let chatWindow = this.$el.querySelector('.chat-window');
        if (chatWindow) {
          chatWindow.scrollTop = chatWindow.scrollHeight-50;
          this.isScrolledToBottom = true;
        }
      });
    },
    scrollToBottom() {
      this.moveScroll(); // 스크롤이 제일 아래로 이동
      this.isScrolledToBottom = true; // 스크롤이 제일 아래로 이동했음을 표시
    },
    handleScroll(event) {
      // 스크롤이 위로 올라갔는지 여부 확인
      if (event.target.scrollTop <= 8000) {
        console.log('scroll moving');
        this.isScrolledToBottom = false; // 스크롤이 위로 올라갔을 때 isScrolledToBottom을 false로 설정
      }
    },
  },
  beforeUnmount() {
    // window.removeEventListener('wheel', this.handleScroll);
    window.removeEventListener('scroll', this.handleScroll);
  },

};
</script>

<style scoped>
.chat-room {
  position: fixed;
  bottom: 60px;
  right: 5px;
  z-index: 9999; /* 다른 요소보다 위에 위치하도록 높은 값으로 설정 */
}

.chat-toggle {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  background-color: #333;
  color: white;
  padding: 7px;
  border-radius: 5px;
  border: 1px solid gray;
}

.toggle-text {
  flex: 1;
}

.exit-button {
  background-color: #333;
  color: white;
  border: none;
  padding: 1px;
  /* padding: 5px 10px; */
  border-radius: 3px;
  cursor: pointer;
}
.exit-button:hover {
  background-color: gray;
}

.chat-window {
  background-color: white;
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
  max-height: 500px; /* 높이를 최대 높이로 변경 */
  overflow-y: auto;
  width: 500px; /* 채팅창의 너비를 고정시킵니다 */
  display: flex;
  flex-direction: column; /* 요소들을 세로 방향으로 정렬합니다. */
}


.chat-window ul {
  list-style-type: none;
  padding: 0;
  margin: 0; /* 내부 간격을 없앱니다 */
}

.input-container {
  display: flex;
  align-items: center;
  border-top: 1px solid #ccc;
  background-color: white;
}

input[type="text"] {
  flex: 1;
  padding: 5px;
  border-radius: 3px;
}

button {
  background-color: #333;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 3px;
  cursor: pointer;
  margin-left: 10px;
}


/* 채팅자 목록의  스타일 */

.chat-list-window {
  background-color: #333;
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
  max-height: 200px; /* 높이를 최대 높이로 변경 */
  overflow-y: auto;
  width: 500px; /* 채팅창의 너비를 고정시킵니다 */
  display: flex;
  flex-direction: column; /* 요소들을 세로 방향으로 정렬합니다. */
}


.chat-list-window ul {
  list-style-type: none;
  padding: 0;
}

.chat-list-window ul li {
  border-bottom: 2px solid white;
  border-top: 2px solid white;
  margin-top: -2px; /* 수정된 부분 */
  padding: 10px 0;
  color: white;
  font-size: 12px;
}

.chat-list-window ul li span.date {
  font-size: 9px;
}

.chat-list-window ul li .emoticon {
  font-size: 12px;
  margin-right: 5px;
}



/* 채팅창 내부의 채팅 스타일 */

.chat-window li {
  display: flex;
  align-items: center;
  text-align: left;
  margin-bottom: 10px;
}

.chat-window li span {
  font-size: smaller;
}

.chat-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 요소들을 세로 방향으로 시작점에 정렬합니다. */
}

.user-chat {
  align-self: flex-end;
  justify-content: flex-end;
  background-color: #E5E5EA; /* 사용자 채팅 창 배경색 */
}

.message-content {
  word-break: break-all;
}

.other-chat {
  align-self: flex-start;
  justify-content: flex-start;
  background-color: #E5E5EA; /* 상대방 채팅 창 배경색 */
}

.system-chat {
  align-self: center !important;
  justify-content: center !important;
  /* background-color: transparent !important; 배경색을 투명으로 설정 */
  background-color: #333 !important;
  color: white !important;
}

.chat {
  max-width: 80%;
  /* margin-bottom: 10px; */
  /* padding: 10px; */
  border-radius: 15px; /* 둥근 모서리 */
}

.other-person-name {
  margin-bottom: 5px; /* 이름과 텍스트 사이의 간격 설정 */
  font-weight: bold; /* 이름의 글꼴을 두꺼운체로 설정 */
  color: #333;
  font-size: 15px !important;
}

.chat-date {
  font-size: 10px !important;
}

.scroll-to-bottom {
  position: absolute; /* 절대 위치 지정 */
  bottom: 25px; /* 하단으로부터의 거리 설정 */
  left: 50%; /* 가로 중앙으로 이동 */
  transform: translateX(-50%); /* 중앙 정렬을 위한 변형(transform) 설정 */
  background-color: #333; /* 배경색 지정 */
  color: white; /* 글자색 지정 */
  padding: 10px; /* 내부 여백 설정 */
  border-radius: 5px; /* 모서리 둥글게 설정 */
  cursor: pointer; /* 마우스 커서를 포인터로 변경하여 클릭 가능함을 나타냄 */
}

.scroll-to-bottom:hover {
  background-color: #555; /* 마우스를 올렸을 때의 배경색 지정 */
}

 @media (max-width: 768px) {
  .chat-room{
    /* width: 100%; */
  }

   .chat-window{
    width: 100%;
    max-height: 450px; 
  }

  .chat-list-window{
    width: 100%;
    max-height: 150px; 
  }

  .scroll-to-bottom{
    font-size: 8px;
  }

 
 }






</style>
