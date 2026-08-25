<template>
    个人资料
    <div>
        <p>用户名: {{ $store.state.user.username }}</p>
        <p>JWT 令牌: {{ $store.state.user.Jwttoken }}</p>
        <p>头像:</p>
        <img src="https://c-ssl.duitang.com/uploads/blog/202208/01/20220801163120_35a33.jpeg">
        <p>用户 ID: {{ $store.state.user.id }}</p>
        <p>登陆状态：{{ $store.state.user.isLogin }}</p>
    </div>
</template>
<script>
import { useStore } from 'vuex';
export default{
    setup(){
        const store=useStore()
        return{
            store
        }
    }
}
</script>