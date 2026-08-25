<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>菜品分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true">添加菜品分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="DishType" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="type"></el-table-column>
            <el-table-column label="是否首页展示" prop="ishow">
                <template #default="scope">
                    <span>{{ scope.row.ishow === 0 ? '是' : '否' }}</span>
                </template>
            </el-table-column>
            <el-table-column label="分类图片" prop="cover">
                <template #default="scope">
                    <img :src="scope.row.cover" style="width: 50px; height: 50px; object-fit: cover;" />
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
        <el-dialog v-model="dialogVisible" title="添加分类" width="45%"  >
            <el-form :model="addTypeModel" :rules="rules" label-width="100px" style="padding-right: 15px;margin-left: 5px;">
                <el-form-item label="分类名称" prop="type">
                    <el-input v-model="addTypeModel.type" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="是否首页展示" prop="ishow">
                    <el-select class="selectbox" placeholder="请选择" v-model="addTypeModel.ishow">
                        <el-option label="是" value="0"></el-option>
                        <el-option label="否" value="1"></el-option>
                    </el-select>
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
                    <div v-if="addTypeModel.cover">
                        <img :src="addTypeModel.cover" style="width: 50px; height: 50px; object-fit: cover;" />
                    </div>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="addDishType"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
          <!--添加编辑弹窗---->
          <el-dialog v-model="updateVisible" title="修改分类" width="50%">
            <el-form :model="updateTypeModel" :rules="rules" label-width="120px" style="padding-right: 30px" label-position="left" >
                <el-form-item label="分类名称" prop="type">
                    <el-input v-model="updateTypeModel.type" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <!-- 是否首页展示 -->
                <el-form-item label="是否首页展示" prop="ishow">
                    <el-select class="selectbox" placeholder="请选择" v-model="updateTypeModel.ishow">
                        <el-option label="是" value="0"></el-option>
                        <el-option label="否" value="1"></el-option>
                    </el-select>
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
                            <div v-if="updateTypeModel.cover" style="text-align: center;">
                                <img :src="updateTypeModel.cover" style="width: 100px; height: 100px; margin-right: 15px; object-fit: cover;" />
                            </div>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>
    <!-- 弹窗底部按钮 -->
    <template #footer>
        <span class="dialog-footer">
            <el-button @click="updateVisible = false">取消</el-button>
            <el-button type="primary" @click="updateDishType">确认</el-button>
        </span>
    </template>
</el-dialog>
        <!--添加删除弹窗---->
        <el-dialog v-model="deleteVisible" title="删除分类" width="30%">
           <el-alert>是否确定删除此分类？</el-alert>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="deleteVisible = false">取消</el-button>
                    <el-button type="primary" @click="deleteDishType"> 确认 </el-button>
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
        let DishType = ref([]);
        const dialogVisible = ref(false);
        const updateVisible=ref(false);
        const deleteVisible=ref(false);
        const fileList = ref([]); // 定义 fileList
        const updateTypeModel=ref({
            type: '',
            ishow: '',
            cover: '',
            id:'',
        })
        const deleteTypeModel=ref({
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

        const addTypeModel = ref({
            type: '',
            ishow: '',
            cover: ''
        });

        const getAllDishType = () => {
            $.ajax({
                url: "http://121.43.139.67:3000/dishtype",
                type: "post",
                success(resp) {
                    DishType.value = resp.data;
                    console.log(resp)
                },
                error() {
                    ElMessage.error('获取分类列表失败');
                }
            });
        };

        const addDishType = () => {
            $.ajax({
                url: "http://121.43.139.67:3000/dishtype/addtype",
                type: "post",
                contentType: "application/json",
                data: JSON.stringify({
                    type: addTypeModel.value.type,
                    ishow: addTypeModel.value.ishow,
                    cover: addTypeModel.value.cover
                }),
                success(resp) {
                    console.log(resp);
                    dialogVisible.value = false;
                    getAllDishType();
                    ElMessage.success('添加分类成功');
                    addTypeModel.value = {
                        type: '',
                        ishow: '',
                        cover: ''
                    };
                },
                error() {
                    ElMessage.error('添加分类失败');
                }
            });
        };
        const openEditDialog=(row)=>{
            //updateTypeModel.value.ishow=row.ishow;
            updateTypeModel.value.type=row.type;
            updateTypeModel.value.cover=row.cover;
            updateTypeModel.value.id=row.id;
            updateVisible.value=true;
        }
        const openDeleteDialog=(row)=>{
            deleteTypeModel.value.id=row.id;
            deleteVisible.value=true;
        };
        const deleteDishType=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/dishtype/delete",
                type:"post",
                data:{
                    id:deleteTypeModel.value.id
                },
                success(resp){
                    console.log(resp);
                    ElMessage.success("删除分类成功");
                    deleteVisible.value=false;
                    getAllDishType();
                },
                error(){
                    ElMessage.error("删除分类失败");
                }
            })
        }
        const handleUploadSuccess = (resp) => {
           console.log(resp);
           addTypeModel.value.cover=resp;
           console.log(addTypeModel.value.cover);
        };
        const updatehandleUploadSuccess = (resp) => {
           console.log(resp);
           updateTypeModel.value.cover=resp;
           console.log(updateTypeModel.value.cover);
        };
        const updateDishType=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/dishtype/update",
                type:"post",
                contentType:"application/json",
                data:JSON.stringify({
                    id:updateTypeModel.value.id,
                    type:updateTypeModel.value.type,
                    ishow:updateTypeModel.value.ishow,
                    cover:updateTypeModel.value.cover,
                }),
                success(resp){
                    console.log(resp);
                    updateVisible.value=false;
                    ElMessage.success("修改分类成功");
                    //更新数据
                    getAllDishType()
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

        getAllDishType();

        return {
            store,
            dialogVisible,
            Edit,
            Delete,
            getAllDishType,
            DishType,
            addTypeModel,
            addDishType,
            handleUploadSuccess,
            rules,
            beforeUpload,
            handleExceed,
            fileList,
            updateVisible,
            deleteVisible,
            updateTypeModel,
            openEditDialog,
            updatehandleUploadSuccess,
            updateDishType,
            deleteTypeModel,
            openDeleteDialog,
            deleteDishType
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