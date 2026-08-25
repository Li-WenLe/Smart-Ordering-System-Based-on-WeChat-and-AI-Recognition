<template>
    <el-form :model="formDate" label-width="120px" class="demo-ruleForm">
  <el-form-item label="请输入原密码" prop="old_pwd">
    <el-input v-model="formDate.old_pwd"></el-input>
  </el-form-item>
  <el-form-item label="请输入新密码" prop="new_pwd">
    <el-input v-model="formDate.new_pwd"></el-input>
  </el-form-item>
  <el-form-item label="请再次输入密码" prop="re_pwd">
    <el-input v-model="formDate.re_pwd"></el-input>
  </el-form-item>
  <el-form-item>
    <el-button @click="updatePwd">提交</el-button>
    <el-button @click="reset">重置</el-button>
  </el-form-item>
</el-form>
</template>
<script>
import {ref} from 'vue';
import $ from 'jquery';
import { useStore } from 'vuex';
export default{
    setup(){
        const store=useStore();
        const formDate=ref({
            old_pwd:'',
            new_pwd:'',
            re_pwd:'',
        })
        const reset=()=>{
            formDate.value.old_pwd='';
            formDate.value.new_pwd='';
            formDate.value.re_pwd='';
        }
        const updatePwd=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/user/updatePwd",
                type:"patch",
                data:{
                    oldPwd:formDate.value.old_pwd,
                    newPwd:formDate.value.new_pwd,
                    rePwd:formDate.value.re_pwd,
                    username:store.state.user.username,
                },
                success(resp){
                    console.log(resp)
                },
                error(){
                    console.log("出现未知错误");
                }
            })
        }
        return{
            formDate,
            reset,
            updatePwd,
        }
    }

}
</script>
<style>
</style>