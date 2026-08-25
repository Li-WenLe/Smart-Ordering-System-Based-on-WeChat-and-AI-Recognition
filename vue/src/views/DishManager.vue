<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>菜品管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer=true">添加菜品</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="菜品分类：">
                <el-select class="selectbox" placeholder="请选择" v-model="dishtypeId">
                    <el-option v-for="c in DishTypes" :key="c.id" :label="c.type" :value="c.id"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item  label="启售状态：">
                <el-select class="selectbox" placeholder="请选择" v-model="status">
                    <el-option label="启售" value="0"></el-option>
                    <el-option label="停售" value="1"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="findDish">搜索</el-button>
                <el-button @click="reset">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 菜品列表 -->
        <el-table :data="dishList" style="width: 100%">
            <el-table-column label="菜品名称" width="200" prop="name"></el-table-column>
            <el-table-column label="菜品封面图" prop="image">
                <template #default="scope">
                    <img :src="scope.row.image" style="width: 100px; height: 80px;">
                </template>
            </el-table-column>
            <el-table-column label="菜品描述" prop="description"></el-table-column>
            <el-table-column label="菜品价格" prop="price">
                <template #default="{row}">
                    ¥ {{ row.price }}
                </template>
            </el-table-column>
            <el-table-column label="菜品分类" prop="typeName"></el-table-column>
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
            <el-table-column label="是否推荐" prom="recommend">
                <template #default="scope">
                    <span>{{ scope.row.recommend === 0 ?  '是':'否'}}</span>
                </template>
            </el-table-column>
            <el-table-column label="修改时间" prop="updateTime"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="scope">
                    <el-button :icon="Edit" circle plain type="primary" @click="openEditDialog(scope.row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="openDeleteDialog(scope.row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
         <!--添加编辑弹窗---->
         <el-dialog v-model="updateVisible" title="修改菜品" width="30%">
            <el-form :model="updateDishModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="菜品名称" prop="name">
                   <el-input v-model="updateDishModel.name" minlength="1" maxlength="10" ></el-input>
                </el-form-item>
                <el-form-item label="菜品价格" prop="price">
                   <el-input v-model="updateDishModel.price" minlength="1" maxlength="10" >
                    <template #default="{row}">
                        ¥ {{ row.price }}
                    </template>
                   </el-input>
                </el-form-item>
                <el-form-item label="菜品分类" prop="status">
                    <el-select placeholder="请选择" v-model="updateDishModel.type">
                        <el-option v-for="c in DishTypes" :key="c.id" :label="c.type" :value="c.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="菜品图片" prop="price">
                    <img :src="updateDishModel.image" style="width: 100px; height: 50px;">
                    <el-upload
                        action="http://121.43.139.67:3000/upload"
                        :on-success="updatehandleUploadSuccess"
                        :before-upload="beforeUpload"
                        :limit="1"
                        :on-exceed="handleExceed"
                        :file-list="fileList"
                    >

                        <el-button type="primary" style="margin-left: 50px;">点击修改</el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select placeholder="请选择" v-model="updateDishModel.status">
                        <el-option label="启售中" value="0"></el-option>
                        <el-option label="已停售" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否推荐" prop="recommend">
                    <el-select placeholder="请选择" v-model="updateDishModel.recommend">
                        <el-option label="是" value="0"></el-option>
                        <el-option label="否" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="菜品描述" prop="description">
                    <el-input v-model="updateDishModel.description" minlength="1" type="textarea">{{ updateCategoryModel.content }}</el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="updateVisible = false">取消</el-button>
                    <el-button type="primary" @click="updateDish"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
        <el-dialog v-model="deleteVisible" title="删除分类" width="30%">
           <el-alert>是否确定删除此菜品？</el-alert>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="deleteVisible = false">取消</el-button>
                    <el-button type="primary" @click="deleteDish"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
        <!--添加菜品表单-->
        <el-drawer v-model="visibleDrawer" title="添加菜品" direction="rtl" size="50%">
            <el-form :model="addDishModel" label-width="100px" >
                <el-form-item label="菜品名称" >
                    <el-input v-model="addDishModel.name" placeholder="请输入菜品名称"></el-input>
                </el-form-item>
                <el-form-item label="菜品分类">
                    <el-select placeholder="请选择" v-model="addDishModel.type">
                        <el-option v-for="c in DishTypes" :key="c.id" :label="c.type" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="菜品价格" >
                    <el-input v-model="addDishModel.price" placeholder="请输入菜品价格">
                    <template #default="{row}">
                        ¥ {{ row.price }}
                    </template>
                    </el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select placeholder="请选择" v-model="addDishModel.status">
                        <el-option label="启售" value="0"></el-option>
                        <el-option label="停售" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否推荐" prop="recommend">
                    <el-select placeholder="请选择" v-model="addDishModel.recommend">
                        <el-option label="是" value="0"></el-option>
                        <el-option label="否" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="上传菜品封面" prop="image">
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
                    <div v-if="addDishModel.image">
                        <img :src="addDishModel.image" style="width: 100px; height: 50px; object-fit: cover;" />
                    </div>
                </el-form-item>
                <el-form-item label="菜品描述">
                    <el-input v-model="addDishModel.description" minlength="1"  type="textarea">{{ updateCategoryModel.content }}</el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="insert">添加</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5 ,10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>
</template>

<script>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref} from 'vue'
import $ from 'jquery'
import {Plus} from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { ElMessage } from 'element-plus'
export default{
    setup(){
        const deleteVisible=ref(false)
        const deletDishId=ref(0)
        //定义修改表单是否显示
        const updateVisible=ref(false)
        //控制抽屉是否显示
        const visibleDrawer = ref(false)
        //添加表单数据模型
        const articleModel = ref({
            title: '',
            categoryId: '',
            coverImg: '',
            content:'',
            state:'',
            categoryName:'',
        })
        //菜品分类数据模型
        const DishTypes = ref([
        ])

        //用户搜索时选中的分类id
        const dishtypeId=ref('')
        const categoryId=ref('')

        //用户搜索时选中的菜品状态
        const status=ref('')

        //文章列表数据模型
        const articles = ref([
        ])

        const dishList=ref([

        ])

        //分页条数据模型
        const pageNum = ref(1)//当前页
        const total = ref(0)//总条数
        const pageSize = ref(5)//每页条数

        //当每页条数发生了变化，调用此函数
        const onSizeChange = (size) => {
            pageSize.value = size
            loadData();

        }
        //当前页码发生变化，调用此函数
        const onCurrentChange = (num) => {
            pageNum.value = num
            loadData();
        }
        //定义修改菜品的表单数据
        const updateDishModel=ref({
            id:'',
            name: '',
            status:"0",
            description:'',
            image:'',
            price:'',
            type:'',
            recommend:'0',
        })
         //定义添加菜品的表单数据
         const addDishModel=ref({
            id:'',
            name: '',
            status:"0",
            description:'',
            image:'',
            price:'',
            type:'',
            recommend:'0',
        })
        //获取菜品分类
        const getAllDishType = () => {
            $.ajax({
                url: "http://121.43.139.67:3000/dishtype",
                type: "post",
                success(resp) {
                    ElMessage.success("获取菜品分类成功")
                    console.log(resp.data);
                    DishTypes.value = resp.data;
                    console.log(DishTypes.value);
                },
                error(){
                    ElMessage.error("获取菜品分类失败")
                }
            });
        };
        
        // 统一加载数据方法
        const loadData = () => {
            if (dishtypeId.value || status.value) {
                findDish();
            } else {
                getAllDish();
            }
        };
        //获取菜品列表
        const getAllDish = () => {
            $.ajax({
                url: "http://121.43.139.67:3000/dish",
                type: "get",
                data:{
                    pageSize:pageSize.value,
                    pageNum:pageNum.value
                },
                success(resp) {
                    console.log(resp.data);
                    //ElMessage.success("获取菜品信息成功");
                    // 添加分类名称到每个菜品对象
                     //根据商品的dishtypeid获取对应的type
                    dishList.value = resp.data.items.map(dish => {
                        const typeObj = DishTypes.value.find(type => type.id === dish.dishTypeId);
                        return {
                            ...dish,
                            typeName: typeObj ? typeObj.type : '未知分类'
                        };
                    });
                    total.value=resp.data.total;
                    console.log(dishList.value);
                },
                error() {
                    ElMessage.error("获取菜品信息失败");
                }
            });
        };
        //搜索框搜索菜品
        const findDish=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/dish/getdish",
                type:"get",
                data:{
                    dishTypeId:dishtypeId.value,
                    status:status.value
                },
                success(resp){
                    console.log(resp);
                    //ElMessage.success("查询成功");
                    //更新dishList
                    dishList.value = resp.data.map(dish => {
                        const typeObj = DishTypes.value.find(type => type.id === dish.dishTypeId);
                        return {
                            ...dish,
                            typeName: typeObj ? typeObj.type : '未知分类'
                        };
                    });
                },
                error(){
                    ElMessage.error("查询失败");
                }
            })
        }
        //添加发布函数
        const insert=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/dish",
                type:"put",
                contentType:"application/json",
                data:JSON.stringify({
                    name: addDishModel.value.name,
                    price: addDishModel.value.price,
                    image: addDishModel.value.image,
                    description:addDishModel.value.description,
                    dishTypeId:addDishModel.value.type,
                    status:addDishModel.value.status,
                }),
                success(resp){
                    console.log(resp);
                    ElMessage.success("添加菜品成功")
                    visibleDrawer.value=false
                    getAllDishType()
                    getAllDish()
                },
                error(){
                    ElMessage.error("添加菜品失败")
                }
            })
        }
        const reset=()=>{
            dishtypeId.value='';
            status.value='';
        }
        const openEditDialog = (row) => {
            updateDishModel.value = {
                id: row.id,
                name: row.name,
                description:row.description,
                //status:row.status,
                image:row.image,
                price:row.price,
                type:row.typeName,
            };
            updateVisible.value = true;
        };
        const openDeleteDialog=(row)=>{
            deletDishId.value=row.id;
            deleteVisible.value=true;
        }
        const updateDish=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/dish/update",
                type:"post",
                contentType:"application/json",
                data:JSON.stringify({
                    id:updateDishModel.value.id,
                    status:updateDishModel.value.status,
                    description:updateDishModel.value.description,
                    price:updateDishModel.value.price,
                    dishTypeId:updateDishModel.value.type,
                    image:updateDishModel.value.image,
                    name:updateDishModel.value.name,
                    recommend:updateDishModel.value.recommend
                }),
                 success(resp){
                    console.log(resp);
                    ElMessage.success("菜品修改成功")
                    updateVisible.value=false
                    getAllDishType()
                    getAllDish()
                },
                error(){
                    ElMessage.error("菜品修改失败")
                }
                
            })
        }
        const deleteDish=()=>{
            $.ajax({
                url:"http://121.43.139.67:3000/dish/delete",
                type:"post",
                data:{
                    id:deletDishId.value
                },
                success(){
                    ElMessage.success("删除菜品成功")
                    deleteVisible.value=false;
                    getAllDish();
                    getAllDishType();
                },
                error(){
                    ElMessage.error("删除分类失败")
                }
            })
        }
        const beforeUpload = (file) => {
            const isJPGPNG = file.type === 'image/jpeg' || file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 5;

            if (!isJPGPNG) {
                ElMessage.error('上传图片只能是 JPG/PNG 格式!');
            }
            if (!isLt2M) {
                ElMessage.error('上传图片大小不能超过 5MB!');
            }
            return isJPGPNG && isLt2M;
        };

        const handleExceed = (files) => {
            ElMessage.warning(`只能上传 1 个文件，当前上传了 ${files.length} 个文件`);
        };
        const handleUploadSuccess = (resp) => {
           console.log(resp);
           addDishModel.value.image=resp;
           console.log(addDishModel.value.image);
        };
        const updatehandleUploadSuccess = (resp) => {
           console.log(resp);
           updateDishModel.value.image=resp;
           console.log(updateDishModel.value.image);
        };
        getAllDishType()
        getAllDish()

        return{
            Edit,
            Delete, 
            DishTypes,
            categoryId,
            status, 
            articles, 
            total, 
            onSizeChange,
            onCurrentChange,
            getAllDishType,
            visibleDrawer,
            Plus,
            articleModel,
            getAllDish,
            insert,
            QuillEditor,
            reset,
            updateVisible,
            updateDishModel,
            openEditDialog,
            updateDish,
            openDeleteDialog,
            deleteVisible,
            dishtypeId,
            dishList,
            findDish,
            addDishModel,
            handleExceed,
            handleUploadSuccess,
            beforeUpload,
            updatehandleUploadSuccess,
            deletDishId,
            deleteDish
        }
    }
}

</script>

<style scoped lang="scss">
.page-container {
    min-height: 100%;
    box-sizing: border-box;
    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}
.editor {
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
.selectbox{
    width: 150px;
}
.status-dot {
    display: inline-block;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    margin-right: 6px;
}

.el-tag {
    display: inline-flex;
    align-items: center;
    padding: 0 8px;
}
</style>