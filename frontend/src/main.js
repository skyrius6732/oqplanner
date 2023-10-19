import { createApp } from 'vue'

import App from './App.vue'

import vuetify from './plugins/vuetify'

import { loadFonts } from './plugins/webfontloader'
import router from './router'
import axios from 'axios';
import qs from 'qs'; 




loadFonts()

// mitt 라이브러리 셋팅(vue2의 EventBus와 같은 개념)
import mitt from 'mitt'

let emitter = mitt();
let app = createApp(App);

axios.defaults.baseURL = 'http://localhost:8080';

// axios 설정
let instance = axios.create({
    paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),
});

app.config.globalProperties.emitter = emitter;
app.config.globalProperties.$axios = instance;
// app.config.compilerOptions.isCustomElement = (tag) => tag.startsWith('v-');

app.use(router).use(vuetify).mount('#app')
