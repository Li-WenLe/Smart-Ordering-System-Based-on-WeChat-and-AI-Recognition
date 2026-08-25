<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>套餐管理</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true">添加套餐</el-button>
                </div>
            </div>
        </template>
        <el-table :data="SetMeal" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="套餐名称" prop="name"></el-table-column>
            <el-table-column label="套餐图片" prop="cover">
                <template #default="scope">
                    <img :src="scope.row.cover" style="width: 50px; height: 50px; object-fit: cover;" />
                </template>
            </el-table-column>
            <el-table-column label="套餐价格" prop="price">
                <template #default="{row}">
                    ¥ {{ row.price }}
                </template>
            </el-table-column>
            <el-table-column label="套餐描述" prop="description"></el-table-column>
            <el-table-column label="状态" prop="status" width="120">
                <template #default="scope">
                    <el-tag :type="scope.row.status == 0 ? 'success' : 'danger'" size="small">
                        <span v-show="scope.row.status == 0" style="display: inline-flex; align-items: center;">
                            <i class="status-dot" style="background-color: #67C23A"></i>
                            <span>启售中</span>
                        </span>
                        <span v-show="scope.row.status == 1" style="display: inline-flex; align-items: center;">
                            <i class="status-dot" style="background-color: #F56C6C"></i>
                            <span>已停售</span>
                        </span>
                    </el-tag>
                </template>
            </el-table-column>
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
        <el-dialog v-model="dialogVisible" title="添加套餐" width="45%"  >
            <el-form :model="addSetMealModel" :rules="rules" label-width="100px" style="padding-right: 15px;margin-left: 5px;">
                <el-form-item label="套餐名称" prop="name">
                    <el-input v-model="addSetMealModel.name" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="套餐价格" prop="price">
                    <el-input v-model="addSetMealModel.price" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="套餐描述" prop="description">
                    <el-input v-model="addSetMealModel.description"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-input v-model="addSetMealModel.status" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="上传封面图片" prop="cover">
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
                    <div v-if="addSetMealModel.cover">
                        <img :src="addSetMealModel.cover" style="width: 50px; height: 50px; object-fit: cover;" />
                    </div>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="addSetMeal"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
          <!--添加编辑弹窗---->
          <el-dialog v-model="updateVisible" title="修改套餐" width="50%">
            <el-form :model="updateSetMealModel" :rules="rules" label-width="120px" style="padding-right: 30px" label-position="left" >
                <el-form-item label="套餐名称" prop="name">
                    <el-input v-model="updateSetMealModel.name" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="套餐价格" prop="price">
                    <el-input v-model="updateSetMealModel.price" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="套餐描述" prop="description">
                    <el-input v-model="updateSetMealModel.description" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select placeholder="请选择" v-model="updateSetMealModel.status">
                        <el-option label="启售" value="0"></el-option>
                        <el-option label="停售" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <!-- 修改图片 -->
                <el-form-item label="修改封面图" prop="cover">
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
                            <div v-if="updateSetMealModel.cover" style="text-align: center;">
                                <img :src="updateSetMealModel.cover" style="width: 100px; height: 100px; margin-right: 15px; object-fit: cover;" />
                            </div>
                        </el-col>
                    </el-row>
                </el-form-item>

            </el-form>
    <!-- 弹窗底部按钮 -->
    <template #footer>
        <span class="dialog-footer">
            <el-button @click="updateVisible = false">取消</el-button>
            <el-button type="primary" @click="updateSetMeal">确认</el-button>
        </span>
    </template>
</el-dialog>
        <!--添加删除弹窗---->
        <el-dialog v-model="deleteVisible" title="删除套餐" width="30%">
           <el-alert>是否确定删除此套餐？</el-alert>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="deleteVisible = false">取消</el-button>
                    <el-button type="primary" @click="deleteSetMeal"> 确认 </el-button>
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
import { useRouter } from 'vue-router';
export default {
    setup() {
        const router=useRouter();
        const store = useStore();
        let SetMeal = ref([]);
        const dialogVisible = ref(false);
        const updateVisible=ref(false);
        const deleteVisible=ref(false);
        const fileList = ref([]); // 定义 fileList
        const updateSetMealModel=ref({
            name:'',
            cover:'',
            price:'',
            description:'',
            status:"0",
        })
        const deleteSetMealModel=ref({
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
        const addSetMealModel=ref({
            name:'',
            description:'',
            cover:'',
            price:'',
            status:'',
        })
        const getAllSetMeal=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/setmeal",
                type:"get",
                success(resp){
                    SetMeal.value=resp.data;
                    console.log(resp.data);
                },
                error(){
                    ElMessage.error("获取套餐失败")
                }
            })
        }
        const addSetMeal = () => {
            $.ajax({
                url: "http://121.43.139.67:3000/setmeal/add",
                type: "post",
                contentType: "application/json",
                data: JSON.stringify({
                    name: addSetMealModel.value.name,
                    price: addSetMealModel.value.price,
                    cover: addSetMealModel.value.cover,
                    status:addSetMealModel.value.usedTime,
                    description:addSetMealModel.value.description
                }),
                success(resp) {
                    console.log(resp);
                    dialogVisible.value = false;
                    getAllSetMeal();
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
           /* updateSetMealModel.value.name=row.name;
            updateSetMealModel.value.price=row.price;
            updateSetMealModel.value.cover=row.cover;
            updateSetMealModel.value.id=row.id;
            updateSetMealModel.value.description=row.description;
            updateVisible.value=true;*/
            router.push({ name: 'SetMealDetail', params: { id: row.id } });
        }
        const openDeleteDialog=(row)=>{
            deleteSetMealModel.value.id=row.id;
            deleteVisible.value=true;
        };
        const handleUploadSuccess = (resp) => {
           console.log(resp);
           addSetMealModel.value.cover=resp;
           console.log(addSetMealModel.value.cover);
        };
        const updatehandleUploadSuccess = (resp) => {
           console.log(resp);
           updateSetMealModel.value.cover=resp;
           console.log( updateSetMealModel.value.cover);
        };
        const deleteSetMeal=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/setmeal/delete",
                type:"post",
                data:{
                    setmealId:deleteSetMealModel.value.id,
                },
                success(){
                    ElMessage.success("删除套餐成功");
                    deleteVisible.value=false;
                    getAllSetMeal()
                },
                error(error){
                    console.log(error)
                    ElMessage.error("删除套餐失败");
                }
            })
        }
        const updateSetMeal=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/setmeal",
                type:"put",
                contentType:"application/json",
                data:JSON.stringify({
                    id: updateSetMealModel.value.id,
                    name: updateSetMealModel.value.name,
                    status: updateSetMealModel.value.status,
                    cover: updateSetMealModel.value.cover,
                    description:updateSetMealModel.value.description,
                    price:updateSetMealModel.value.price
                }),
                success(resp){
                    console.log(resp);
                    updateVisible.value=false;
                    ElMessage.success("修改套餐成功");
                    //更新数据
                    getAllSetMeal()
                },
                error(){
                    ElMessage.error("修改套餐失败")
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

        getAllSetMeal();

        return {
            store,
            dialogVisible,
            Edit,
            Delete,
            addVoucherModel,
            addSetMeal,
            handleUploadSuccess,
            rules,
            beforeUpload,
            handleExceed,
            fileList,
            updateVisible,
            deleteVisible,
            openEditDialog,
            updatehandleUploadSuccess,
            openDeleteDialog,
            getAllSetMeal,
            SetMeal,
            addSetMealModel,
            updateSetMealModel,
            updateSetMeal,
            deleteSetMeal,
            deleteSetMealModel
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
.status-dot {
    display: inline-block;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    margin-right: 6px;
}

</style>