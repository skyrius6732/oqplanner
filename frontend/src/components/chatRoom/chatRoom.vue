<template>
  <div class="chat-room">
    <div>
      <div class="chat-toggle" v-if="!showChat" @click="enterChat($event)">{{ chatRoomText }}</div>  <!-- 채팅방입장 -->
    </div>  
    <div class="chat-toggle"  v-if="showChat" @click="toggleChat($event)">                           <!-- 채팅방 -->
      {{ chatRoomText }}
    <button class="exit-button" v-if="showChat" @click="exitChat($event)">나가기</button>
    </div>
    <div v-if="showChat" class="chat-window">
      <!-- 채팅 내용 표시 -->

      <ul class="chat-container">
        <li v-for="chat in chatTotalMessages" :key="chat.date" 
          :class="{ 'user-chat': this.userNo === chat.userNo, 
                    'other-chat': this.userNo !== chat.userNo, 
                    'system-chat': chat.systemMessage,
                  }"
          class="chat"
        >
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

      <!-- 채팅 입력 폼 -->
      <div class="input-container">
        <input type="text" v-model="newMessage" @keyup.enter="sendMessage" placeholder="메시지를 입력하세요.">
        <button @click="sendMessage">전송</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  mounted(){

  },
  created(){
    this.userNo = sessionStorage.getItem("userNoSession");
    // this.userNo = '8367b2193356'; // 임시
    this.projectNo = sessionStorage.getItem("projectNoSession");
    this.getTripUserInfo();
    
  },
  data() {
    return {
      showChat: false,
      chatRoomText: '채팅방 입장',
      chatMessages: [],
      chatTotalMessages: [],
      newMessage: '',
      lastReadIndex: -1, // 초기에는 마지막으로 읽은 채팅 없음을 나타냄
      chatEnterFlag: false,
      userNo: '',
      userName: '',
      projectNo: '',
      websocket: '',
      address: 'ws://localhost:8081/oqplanner/ws/chat',
    };
  },
  methods: {
    connect(){
      console.log("onOpen chatEnterFlag :: " + this.chatEnterFlag);
      if(!this.chatEnterFlag){  // 첫입장
          this.chatMessages.push({
            userName: "",
            userNo: "system",
            text: ` [알림] "${this.userName}"님이 입장 하셨습니다.`,
            // date: new Date().toString(),
            date: "",
            systemMessage: true,
          });
      }

      this.websocket = new WebSocket(this.address);

      if(!this.chatEnterFlag){  // 첫 입장
        this.websocket.onopen=()=>{
            console.log('Connection opened');
              this.websocket.send(JSON.stringify(this.chatMessages[0]));
              this.chatMessages = [];
        }
      }

      this.websocket.onmessage = (event)=>{
          console.log('take a message');

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
            this.moveScroll();
          }catch(error){
            console.error('JSON 파싱 오류:', error);
          }  
      }

    },
    

    onClose() {
        this.chatMessages.push({
            userNo: "system",
            userName: "",
            text: ` [알림] "${this.userName}"님이 퇴장 하셨습니다.`,
            // date: new Date().toString(),
            date: "",
            systemMessage: true,
        });
        
        this.websocket.send(JSON.stringify(this.chatMessages[0]));
        this.websocket.close();
        this.chatMessages=[];
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
      if(!this.chatEnterFlag){  // 첫입장
          this.chatRoomText = "채팅방";
          this.connect();  
      }
      if (this.showChat) {
          // 채팅창이 열릴 때마다 마지막으로 읽은 채팅 위치로 스크롤 이동
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
      this.chatMessages = [];
      this.chatTotalMessages=[];
    },
    toggleChat(event) {
      console.log("toggleChat chatEnterFlag :: " + this.chatEnterFlag);
      event.stopPropagation();
      this.showChat = !this.showChat;
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
          userName: this.userName,
          userNo: this.userNo,
          date: formattedDate, 
          text: this.newMessage,
          systemMessage: false,
          });
        this.newMessage = '';
      }
      this.moveScroll();
      this.websocket.send(JSON.stringify(this.chatMessages[0]));
      this.chatMessages = [];
    },
    moveScroll() {
      // 스크롤을 맨 아래로 이동
      this.$nextTick(() => {
        let chatWindow = this.$el.querySelector('.chat-window');
        if (chatWindow) {
          chatWindow.scrollTop = chatWindow.scrollHeight-50;
        }
      });
    },
  }
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
  padding: 10px;
  border-radius: 5px;
}

.toggle-text {
  flex: 1;
}

.exit-button {
  background-color: #333;
  color: white;
  border: none;
  padding: 5px 10px;
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
  padding-top: 10px;
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

/** 새로운 css */

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





</style>
