<script>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import $ from 'jquery'
import { useRouter } from 'vue-router';
import { jwtDecode } from 'jwt-decode';
import { useStore } from 'vuex';
export default{
    setup(){
        const isRegister = ref(false)
        const errorMessage=ref('')
        const router=useRouter()
        const store=useStore()
        const registData=ref({
            username:'',
            password:'',
            repassword:'',
        })
        const loginData=ref({
            username:'',
            password:'',
        })
        const checkRegister=()=>{
            if (registData.value.username === '') {
                errorMessage.value = "用户名不为空";
            } else if (registData.value.username.length < 5 || registData.value.username.length > 16) {
                errorMessage.value = "用户名不合法，请输入5-16位非空字符串";
            } else if(registData.value.password === ''){
                errorMessage.value = '密码不为空';
            }else if(registData.value.password.length < 5 || registData.value.password.length > 16){
                errorMessage.value = "密码不合法，请输入5-16位非空字符串";
            }else if(registData.value.repassword!==registData.value.password){
                errorMessage.value = "两次输入的密码不一致";
            }else{
                errorMessage.value=""
                $.ajax({
                    url:"http://121.43.139.67:3000/admin/regist",
                    type:"post",
                    dataType: "json", 
                    data:{
                        username:registData.value.username,
                        password:registData.value.password,
                    },
                    success(resp){
                        console.log(resp);
                    },
                    error(){
                        console.log("注册失败")
                    }
                })
            }
        }
        const checkLogin=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/admin/login",
                type:"post",
                data:{
                    username:loginData.value.username,
                    password:loginData.value.password,
                },
                success(resp){
                    console.log(resp);
                    const token=resp.data
                    console.log(token)
                    localStorage.setItem('token', token)
                    const tokenlock=jwtDecode(token)
                    console.log(tokenlock)
                    const username=tokenlock.claims.username
                    const id=tokenlock.claims.id
                    console.log(username)
                    console.log(id)
                    store.commit("setUser",{
                        username:username,
                        id:id,
                        Jwttoken:token,
                        isLogin:true,
                    })
                    alert("登录成功");
                    router.push('/index')
                },
                error(){
                    console.log("登陆失败");
                }
            })
        }
        return{
            registData,
            isRegister,
            User,
            Lock,
            checkRegister,
            checkLogin,
            errorMessage,
            loginData
        }
    },  
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form"  size="large" autocomplete="off" v-if="isRegister"  :rules="rules" v-model="registData">
                <el-form-item>
                    <h1>管理员注册</h1>
                </el-form-item>
                <el-form-item>
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registData.username"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registData.password"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码" v-model="registData.repassword"></el-input>
                </el-form-item>
                <p style="color: red; font-size: small;">{{ errorMessage }}</p>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" @click="checkRegister" auto-insert-space>
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else>
                <el-form-item>
                    <h1>管理员登录</h1>
                </el-form-item>
                <el-form-item>
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="loginData.username"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="loginData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" @click="checkLogin" auto-insert-space>登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/back.png') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
        width: 50%;
        height: 100%;
    }
    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>