<template>
  <div class="chat-room">
    <div class="chat-toggle" @click="toggleChat">
      {{ showChat ? '채팅방' : '채팅방 입장' }}
    </div>
    <div v-if="showChat" class="chat-window">
      <!-- 채팅 내용 표시 -->
      <ul>
        <li v-for="(message) in chatMessages" :key="message.id">
          {{ message.text }}
        </li>
      </ul>
      <!-- 채팅 입력 폼 -->
      <div class="input-container">
        <input type="text" v-model="newMessage" @keyup.enter="sendMessage" placeholder="메시지를 입력하세요...">
        <button @click="sendMessage">전송</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showChat: false,
      chatMessages: [],
      newMessage: '',
      lastReadIndex: -1 // 초기에는 마지막으로 읽은 채팅 없음을 나타냄
    };
  },
  methods: {
    toggleChat() {
      this.showChat = !this.showChat;
      if (this.showChat) {
        // 채팅창이 열릴 때마다 마지막으로 읽은 채팅 위치로 스크롤 이동
        this.moveScroll();
      }
    },
    sendMessage() {
      if (this.newMessage.trim() !== '') {
        this.chatMessages.push({ id: Date.now(), text: this.newMessage });
        this.newMessage = '';
      }
      this.moveScroll();
    },
    moveScroll() {
      // 스크롤을 맨 아래로 이동
      this.$nextTick(() => {
        let chatWindow = this.$el.querySelector('.chat-window');
        if (chatWindow) {
          chatWindow.scrollTop = chatWindow.scrollHeight;
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
  cursor: pointer;
  background-color: #333;
  color: white;
  padding: 10px;
  border-radius: 5px;
}

.chat-window {
  background-color: white;
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
  max-height: 500px; /* 높이를 최대 높이로 변경 */
  overflow-y: auto;
}

.chat-window ul {
  list-style-type: none;
  padding: 0;
}

.chat-window li {
  margin-bottom: 5px;
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
</style>
