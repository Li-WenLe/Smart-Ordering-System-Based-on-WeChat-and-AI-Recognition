<template>
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside width="200px">
      <div class="el-aside__logo"></div>
      <el-menu
        active-text-color="#ffd04b"
        background-color="#232323"
        text-color="#fff"
        router
      >
        <el-menu-item index="/data/show">
          <el-icon>
            <Management />
          </el-icon>
          <span>数据统计</span>
        </el-menu-item>
        <el-menu-item index="/banner/manager">
          <el-icon>
            <Management />
          </el-icon>
          <span>轮播图管理</span>
        </el-menu-item>
        <el-menu-item index="/dish/type">
          <el-icon>
            <Management />
          </el-icon>
          <span>菜品分类</span>
        </el-menu-item>
        <el-menu-item index="/dish/manager">
          <el-icon>
            <Promotion />
          </el-icon>
          <span>菜品管理</span>
        </el-menu-item>
        <el-menu-item index="/setmeal/manager">
          <el-icon>
            <Promotion />
          </el-icon>
          <span>套餐管理</span>
        </el-menu-item>
        <el-menu-item index="/voucher/manager">
          <el-icon>
            <Promotion />
          </el-icon>
          <span>优惠券管理</span>
        </el-menu-item>
        <el-menu-item index="/order/manager">
          <el-icon>
            <Promotion />
          </el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/chat/chat">
          <el-icon>
            <Promotion />
          </el-icon>
          <span>用户会话</span>
        </el-menu-item>
        <!-- <el-sub-menu> 
          <template #title>
            <el-icon>
              <UserFilled />
            </el-icon>
            <span>个人中心</span>
          </template>
          <el-menu-item index="/user/info">
            <el-icon>
              <User />
            </el-icon>
            <span>基本资料</span>
          </el-menu-item>
          <el-menu-item index="/user/changephoto">
            <el-icon>
              <Crop />
            </el-icon>
            <span>更换头像</span>
          </el-menu-item>
          <el-menu-item index="/user/resetpassword">
            <el-icon>
              <EditPen />
            </el-icon>
            <span>重置密码</span>
          </el-menu-item>
        </el-sub-menu>-->
      </el-menu>
    </el-aside>
    <!-- 右侧主区域 -->
    <el-container>
      <!-- 头部区域 -->
      <el-header>
        <!--下拉菜单-->
        <el-dropdown placement="bottom-end">
          <span class="el-dropdown__box">
            <!--<el-avatar v-if="$store.state.user.isLogin"  src={{ store.state.user.photo }} />-->
            <div style="display: flex; justify-content: center; align-items: center;">
              <div>
                <img class="headphoto" :src="avatar" />
              </div>
              <div style="margin-left: 15px;">{{ store.state.admin.username }}</div>
            </div>
            <el-icon>
              <CaretBottom />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile" :icon="User">基本资料</el-dropdown-item>
              <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
              <el-dropdown-item command="password" :icon="EditPen">重置密码</el-dropdown-item>
              <el-dropdown-item command="logout" :icon="SwitchButton" @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <!-- 中间区域 -->
      <el-main>
        <router-view></router-view>
      </el-main>
      <!-- 底部区域 -->
      <el-footer>点餐小程序管理端 ©2025 Created by lwl</el-footer>
    </el-container>
  </el-container>
</template>

<script>
import {
  Management,
  Promotion,
  UserFilled,
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom
} from '@element-plus/icons-vue';
import avatar from '@/assets/default.png';
import { useStore } from 'vuex';
import { ElNotification } from 'element-plus';

let webSocket; // 声明在外部，以便在组件卸载时访问

export default {
  setup() {
    const store = useStore();
    const logout = () => {
      store.dispatch('logout');
    };

    const initwebsocket = () => {
      if (!webSocket || webSocket.readyState === WebSocket.CLOSED) {
        webSocket = new WebSocket('ws://localhost:3000/websocket');
        webSocket.onopen = () => {
          console.log('websocket连接成功');
        };
        webSocket.onmessage = (event) => {
          const message = event.data;
          console.log('收到消息：', message);
          // 使用 ElNotification 显示消息
          ElNotification({
            title: '新消息',
            message: message,
            type: 'info',
            position: 'top-right',
            duration: 5000
          });
          speak("你有新的订单待处理");
        };
        webSocket.onclose = () => {
          console.log('websocket连接关闭');
        };
        webSocket.onerror = (error) => {
          console.log('websocket连接错误：', error);
        };
      }
    };

    const speak=(text)=> {
      if ('speechSynthesis' in window) {
        const utterance = new SpeechSynthesisUtterance(text);
        utterance.lang = 'zh-CN'; // 设置中文
        utterance.rate = 0.7;    // 语速 (0.1-10)
        utterance.pitch = 3.0;   // 音高 (0-2)
        utterance.volume = 7.0;  // 音量 (0-1)
        speechSynthesis.speak(utterance);
      } else {
        console.log('浏览器不支持语音合成');
      }
    }

    initwebsocket();

    return {
      store,
      Management,
      Promotion,
      UserFilled,
      User,
      Crop,
      EditPen,
      SwitchButton,
      CaretBottom,
      avatar,
      logout,
      initwebsocket
    };
  },
  beforeUnmount() {
    if (webSocket && webSocket.readyState === WebSocket.OPEN) {
      webSocket.close();
    }
  }
};
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;

 .el-aside {
    background-color: #232323;

    &__logo {
      height: 120px;
      background: url('@/assets/login_title.png') no-repeat center / 120px auto;
    }

   .el-menu {
      border-right: none;
    }
  }

 .el-header {
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;

   .el-dropdown__box {
      display: flex;
      align-items: center;

     .el-icon {
        color: #999;
        margin-left: 10px;
      }

      &:active,
      &:focus {
        outline: none;
      }
    }
  }

 .el-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
  }

 .headphoto {
    height: 50px;
    width: 50px;
    border-radius: 50%;
  }
}
</style>