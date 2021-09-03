import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import 'reset.css'
if (process.env.NODE_ENV === 'development') {
    axios.defaults.baseURL = '/xhr'
  }

createApp(App).use(store).use(router).mount('#app')
