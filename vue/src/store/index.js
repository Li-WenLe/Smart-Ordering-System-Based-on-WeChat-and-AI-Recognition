import { createStore } from 'vuex'
export default createStore({
  state: {
    admin: {
      isLogin: false,
      username: '',
      Jwttoken: '',
      id: 0,
    }
  },
  getters: {
  },
  mutations: {
    setUser(state, data) {
      state.admin.username = data.username;
      state.admin.id = data.id;
      state.admin.Jwttoken = data.Jwttoken;
      console.log(state.admin)
      state.admin.isLogin = true;
    },
    logout(state) {
      state.admin.isLogin = false;
      state.admin.username = '';
      state.admin.Jwttoken = '';
      state.admin.id = 0;
      localStorage.removeItem('token');
    }
  },
  actions: {
  },
  modules: {
  }
})
