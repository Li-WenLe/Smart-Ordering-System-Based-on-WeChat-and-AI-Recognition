<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>优惠券管理</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true">添加优惠券</el-button>
                </div>
            </div>
        </template>
        <el-table :data="Voucher" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="优惠券名称" prop="title"></el-table-column>
            <el-table-column label="优惠券图片" prop="cover">
                <template #default="scope">
                    <img :src="scope.row.cover" style="width: 100px; height: 50px; object-fit: cover;" />
                </template>
            </el-table-column>
            <el-table-column label="使用规则" prop="ruler"></el-table-column>
            <el-table-column label="截至生效时间" prop="usedTime"></el-table-column>
            <el-table-column label="使用规则" prop="ruler"></el-table-column>
            <el-table-column label="发放数量" prop="number"></el-table-column>
            <el-table-column label="剩余数量" prop="remain"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="scope">
                    <el-button :icon="Edit" circle plain type="primary"  @click="openEditDialog(scope.row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="openDeleteDialog(scope.row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!--添加弹窗-->
        <el-dialog v-model="dialogVisible" title="添加优惠券" width="45%"  >
            <el-form :model="addVoucherModel" :rules="rules" label-width="100px" style="padding-right: 15px;margin-left: 5px;">
                <el-form-item label="优惠券名称" prop="title">
                    <el-input v-model="addVoucherModel.title" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="使用规则" prop="ruler">
                    <el-input v-model="addVoucherModel.ruler" minlength="1" type="textarea"></el-input>
                </el-form-item>
                <el-form-item label="截至生效时间" prop="usedTime">
                    <el-input v-model="addVoucherModel.usedTime" minlength="1"></el-input>
                </el-form-item>
                <el-form-item label="发放数量" prop="number">
                    <el-input v-model="addVoucherModel.number" minlength="1"></el-input>
                </el-form-item>
                <el-form-item label="剩余数量" prop="remain">
                    <el-input v-model="addVoucherModel.remain" minlength="1"></el-input>
                </el-form-item>
                <el-form-item label="上传图片" prop="cover">
                    <el-upload
                        action="http://121.43.139.67:3000/upload"
                        :on-success="handleUploadSuccess"
                        :before-upload="beforeUpload"
                        :limit="1"
                        :on-exceed="handleExceed"
                        :file-list="fileList"
                    >
                        <el-button type="primary">点击上传</el-button>
                        <template #tip>
                            <div class="el-upload__tip">请上传 JPG/PNG 格式的图片，且不超过 2MB</div>
                        </template>
                    </el-upload>
                    <!-- 图片回显区域 -->
                    <div v-if="addVoucherModel.cover">
                        <img :src="addVoucherModel.cover" style="width: 100px; height: 50px; object-fit: cover;" />
                    </div>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="addVoucher"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
          <!--添加编辑弹窗---->
          <el-dialog v-model="updateVisible" title="修改优惠券" width="50%">
            <el-form :model="updateVoucherModel" :rules="rules" label-width="120px" style="padding-right: 30px" label-position="left" >
                <el-form-item label="优惠券名称" prop="title">
                    <el-input v-model="updateVoucherModel.title" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="使用规则" prop="ruler">
                    <el-input v-model="updateVoucherModel.ruler" minlength="1"  type="textarea"></el-input>
                </el-form-item>
                <el-form-item label="截至生效时间" prop="usedTime">
                    <el-input v-model="updateVoucherModel.usedTime" minlength="1"></el-input>
                </el-form-item>
                <el-form-item label="发放数量" prop="number">
                    <el-input v-model="updateVoucherModel.number" minlength="1"></el-input>
                </el-form-item>
                <el-form-item label="剩余数量" prop="remain">
                    <el-input v-model="updateVoucherModel.remain" minlength="1"></el-input>
                </el-form-item>
                <!-- 修改图片 -->
                <el-form-item label="修改图片" prop="cover">
                    <el-row :gutter="20">
                        <!-- 图片上传区域 -->
                        <el-col :span="12">
                            <el-upload
                                action="http://121.43.139.67:3000/upload"
                                :on-success="updatehandleUploadSuccess"
                                :before-upload="beforeUpload"
                                :limit="1"
                                :on-exceed="handleExceed"
                                :file-list="fileList"
                            >
                                <el-button type="primary">点击修改</el-button>
                                <template #tip>
                                    <div class="el-upload__tip" style="margin-top: 10px; margin-left: 15px;">
                                        请上传 JPG/PNG 格式的图片，且不超过 2MB
                                    </div>
                                </template>
                            </el-upload>
                        </el-col>
                        <!-- 图片回显区域 -->
                        <el-col :span="12">
                            <div v-if="updateVoucherModel.cover" style="text-align: center;">
                                <img :src="updateVoucherModel.cover" style="width: 100px; height: 100px; margin-right: 15px; object-fit: cover;" />
                            </div>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>
    <!-- 弹窗底部按钮 -->
    <template #footer>
        <span class="dialog-footer">
            <el-button @click="updateVisible = false">取消</el-button>
            <el-button type="primary" @click="updateVoucher">确认</el-button>
        </span>
    </template>
</el-dialog>
        <!--添加删除弹窗---->
        <el-dialog v-model="deleteVisible" title="删除优惠券" width="30%">
           <el-alert>是否确定删除此优惠券？</el-alert>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="deleteVisible = false">取消</el-button>
                    <el-button type="primary" @click="deleteV"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>

<script>
import { Edit, Delete } from '@element-plus/icons-vue';
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';
import { ElMessage } from 'element-plus';

export default {
    setup() {
        const store = useStore();
        let Voucher = ref([]);
        const dialogVisible = ref(false);
        const updateVisible=ref(false);
        const deleteVisible=ref(false);
        const fileList = ref([]); // 定义 fileList
        const updateVoucherModel=ref({
            title: '',
            ruler: '',
            cover: '',
            usedTime:'',
            id:''
        })
        const deleteVoucherModel=ref({
            id:'',
        })
        const rules = ref({
            type: [
                { required: true, message: '分类名称不能为空', trigger: 'blur' },
                { min: 1, max: 10, message: '分类名称长度在1到10个字符之间', trigger: 'blur' },
            ],
            ishow: [
                { required: true, message: '是否首页展示不能为空', trigger: 'blur' },
                { min: 1, max: 15, message: '是否首页展示长度在1到15个字符之间', trigger: 'blur' },
            ],
            cover: [
                { required: true, message: '请上传分类图片', trigger: 'change' },
            ],
        });

        const addVoucherModel = ref({
            title: '',
            ruler: '',
            cover: '',
            usedTime:'',
            id:''
        });

        const getAllVoucher = () => {
            $.ajax({
                url: "http://121.43.139.67:3000/voucher",
                type: "get",
                success(resp) {
                    Voucher.value = resp.data;
                    console.log(resp.data)
                },
                error() {
                    ElMessage.error('获取优惠券失败');
                }
            });
        };

        const addVoucher = () => {
            $.ajax({
                url: "http://121.43.139.67:3000/voucher/add",
                type: "post",
                contentType: "application/json",
                data: JSON.stringify({
                    title: addVoucherModel.value.title,
                    ruler: addVoucherModel.value.ruler,
                    cover: addVoucherModel.value.cover,
                    usedTime:addVoucherModel.value.usedTime,
                    number:addVoucherModel.value.number,
                    remain:addVoucher.value.remain
                }),
                success(resp) {
                    console.log(resp);
                    dialogVisible.value = false;
                    getAllVoucher();
                    ElMessage.success('添加优惠券成功');
                    addVoucherModel.value = {
                        name: '',
                        ruler: '',
                        cover: '',
                        usedTime:''
                    };
                },
                error() {
                    ElMessage.error('添加优惠券失败');
                }
            });
        };
        const openEditDialog=(row)=>{
            updateVoucherModel.value.title=row.title;
            updateVoucherModel.value.ruler=row.ruler;
            updateVoucherModel.value.cover=row.cover;
            updateVoucherModel.value.id=row.id;
            updateVoucherModel.value.usedTime=row.usedTime
            updateVoucherModel.value.number=row.number
            updateVoucherModel.value.remain=row.remain
            updateVisible.value=true;
        }
        const openDeleteDialog=(row)=>{
            deleteVoucherModel.value.id=row.id;
            deleteVisible.value=true;
        };
        const deleteVouchcer=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/voucher/delete",
                type:"post",
                data:{
                    id:deleteVoucherModel.value.id
                },
                success(resp){
                    console.log(resp);
                    ElMessage.success("删除优惠券成功");
                    deleteVisible.value=false;
                    getAllVoucher();
                },
                error(){
                    ElMessage.error("删除优惠券失败失败");
                }
            })
        }
        const handleUploadSuccess = (resp) => {
           console.log(resp);
           addVoucherModel.value.cover=resp;
           console.log(addVoucherModel.value.cover);
        };
        const updatehandleUploadSuccess = (resp) => {
           console.log(resp);
           updateVoucherModel.value.cover=resp;
           console.log( updateVoucherModel.value.cover);
        };
        const updateVoucher=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/voucher/update",
                type:"post",
                contentType:"application/json",
                data:JSON.stringify({
                    id: updateVoucherModel.value.id,
                    title: updateVoucherModel.value.title,
                    ruler: updateVoucherModel.value.ruler,
                    cover: updateVoucherModel.value.cover,
                }),
                success(resp){
                    console.log(resp);
                    updateVisible.value=false;
                    ElMessage.success("修改分类成功");
                    //更新数据
                    getAllVoucher()
                },
                error(){
                    ElMessage.error("修改分类失败")
                }
            })
        };
        const beforeUpload = (file) => {
            const isJPGPNG = file.type === 'image/jpeg' || file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPGPNG) {
                ElMessage.error('上传图片只能是 JPG/PNG 格式!');
            }
            if (!isLt2M) {
                ElMessage.error('上传图片大小不能超过 2MB!');
            }
            return isJPGPNG && isLt2M;
        };

        const handleExceed = (files) => {
            ElMessage.warning(`只能上传 1 个文件，当前上传了 ${files.length} 个文件`);
        };

        getAllVoucher();

        return {
            store,
            dialogVisible,
            Edit,
            Delete,
            getAllVoucher,
            Voucher,
            addVoucherModel,
            addVoucher,
            handleUploadSuccess,
            rules,
            beforeUpload,
            handleExceed,
            fileList,
            updateVisible,
            deleteVisible,
            updateVoucherModel,
            openEditDialog,
            updatehandleUploadSuccess,
            updateVoucher,
            deleteVoucherModel,
            openDeleteDialog,
            deleteVouchcer
        };
    }
};
</script>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>