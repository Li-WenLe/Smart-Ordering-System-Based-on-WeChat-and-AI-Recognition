<template>
  <el-form :model="setMealModel" :rules="rules" label-width="100px">
    <el-form-item label="套餐名称" prop="name" :required="isCreate">
      <el-input v-model="setMealModel.name"></el-input>
    </el-form-item>
    <el-form-item label="套餐价格" prop="price" :required="isCreate">
      <el-input v-model="setMealModel.price" type="number"></el-input>
    </el-form-item>
    <el-form-item label="套餐菜品">
      <el-button type="primary" @click="openAddDishDialog">+ 添加菜品</el-button>
      <el-table :data="DishList" style="width: 100%">
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="价格" prop="price"></el-table-column>
        <el-table-column label="封面图" prop="cover">
          <template #default="scope">
            <img :src="scope.row.cover" style="width: 50px; height: 50px;">
          </template>
        </el-table-column>
        <el-table-column label="份数">
          <template #default="scope">
            <el-input v-model="scope.row.acount" style="width: 50px; text-align: center;" type="number"  @change="updateTotalPrice"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="text" @click="deleteDish(scope.row)" style="color: red;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form-item>
    <el-form-item label="修改套餐图片" prop="cover">
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
              <div v-if="setMealModel.cover" style="text-align: center;">
                  <img :src="setMealModel.cover" style="width: 100px; height: 100px; margin-right: 15px; object-fit: cover;" />
              </div>
          </el-col>
      </el-row>
  </el-form-item>
    <el-form-item label="套餐描述">
      <el-input v-model="setMealModel.description" type="textarea"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="cancel">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </el-form-item>
  </el-form>
  <el-dialog v-model="addDishDialogVisible" title="添加菜品">
    <AddDish :packageForm="packageForm" @close="addDishDialogVisible = false"></AddDish>
  </el-dialog>
  <!--添加菜品弹窗-->
  <el-dialog
    v-model="visible"
    title="添加菜品"
    width="60%"
  >
    <div class="add-dish-container">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="category-filter" style="overflow-y: auto;">
            <div 
              v-for="dishtype in DishType"
              :key="dishtype.id"
              @click="selectDishByTypeId(dishtype.id)"
              :class="{'active-category': activeTypeId === dishtype.id}"
              style="
                padding: 6px 12px;
                margin-bottom: 6px;
                cursor: pointer;
                border-radius: 15px;
                background: #f5f5f5;
                color: #666;
                transition: all 0.3s ease;
                width: fit-content; /* 关键修改：宽度适应内容 */
                min-width: 80px; /* 可选：设置最小宽度保持一致性 */
                text-align: center; /* 文字居中 */
              "
            >
              {{ dishtype.type }}
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="dish-list">
            <el-table
              :data="TypeDishList"
              style="width: 100%"
              @selection-change="handleSelectionChange"
            >
              <el-table-column
                type="selection"
                width="55"
              />
              <el-table-column
                label="名称"
                prop="name"
              />
              <el-table-column
                label="图片"
              >
              <template #default="scope">
                <img 
                  v-if="scope.row.image"
                  :src="scope.row.image" 
                  style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px;"
                  alt="菜品图片"
                >
              </template>
              </el-table-column>
              <el-table-column
                label="状态"
                prop="status"
              >
                <template #default="scope">
                  <span>{{ scope.row.status === 0? '在售' : '停售' }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="价格"
                prop="price"
              />
            </el-table>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="selected-dish-list">
            <!---<div>已选菜品({{ selectedDishes.length }})</div>-->
            <el-table
              :data="selectedDishes"
              style="width: 100%"
            >
              <el-table-column
                label="名称"
                prop="name"
              />
              <el-table-column
                label="价格"
                prop="price"
              />
              <el-table-column
                label="操作"
              >
                <template #default="scope">
                  <el-button
                    type="text"
                    @click="removeSelectedDish(scope.row)"
                    style="color: red;"
                  >×</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
      <div class="action-buttons">
        <el-button
          type="danger"
          @click="visible = false"
        >取消</el-button>
        <el-button
          type="primary"
          @click="confirmAddDishes"
        >添加</el-button>
      </div>
    </div>
  </el-dialog>
</template>


<script>
import { ref } from 'vue';
import { useRoute} from 'vue-router';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import $ from 'jquery'
export default {
  setup() {
    const router=useRouter();
    const activeTypeId = ref(null);
    const route = useRoute();
   // const router = useRouter();
    const id = route.params.id;
    const visible=ref(false);
    // 套餐表单数据
    const setMealModel = ref({
      name: '',
      price: '',
      description: '',
      cover: ''
    });
    //套餐菜品列表
    const DishList=ref([])
    //分类列表
    const DishType=ref([]);
    const TypeDishList=ref([])
    //勾选的菜品
    const selectedDishes = ref([]);
    // 表单验证规则
    const rules = {
      name: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }],
      price: [{ required: true, message: '请输入套餐价格', trigger: 'blur' }]
    };
    const updatehandleUploadSuccess = (resp) => {
      console.log(resp);
      setMealModel.value.cover=resp;
      console.log(setMealModel.value.cover);
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

    // 获取套餐数据
      const getSetMealById=()=>{
          $.ajax({
              url:"http://121.43.139.67:3000/setmeal/getbyid",
              data:{
                id:id
              },
              type:"post",
              success(resp){
                  setMealModel.value=resp.data;
                  console.log(resp.data);
              },
              error(){
                  ElMessage.error("获取套餐失败")
              }
          })
        }
    //获取所有的菜品分类
    const getAllDishType = () => {
            $.ajax({
                url: "http://121.43.139.67:3000/dishtype",
                type: "post",
                success(resp) {
                    DishType.value = resp.data;
                    console.log(resp.data);
                },
                error() {
                    ElMessage.error('获取分类列表失败');
                }
            });
        };
    const openAddDishDialog=()=>{
      visible.value=true;
    }
    //选中菜品
    /*const handleSelectionChange = (selection) => {
      selectedDishes.value = selection;
    };*/
    const handleSelectionChange = (selection) => {
      const newSelections = [...selectedDishes.value];
      selection.forEach(item => {
        if (!newSelections.some(selected => selected.id === item.id)) {
          newSelections.push(item); // 添加新选中的
        }
      });
      selectedDishes.value = newSelections;
    };
    //获取选中商品
    const showSelectedDishes = () => {
      console.log('已选菜品:', selectedDishes.value);
      ElMessage.success(`已选择 ${selectedDishes.value.length} 个菜品`);
    };

    //获取该套餐下的菜品
    const getDishBySetMealId=()=>{
      $.ajax({
        url:"http://121.43.139.67:3000/setmeal/getdish",
        type:"post",
        data:{
          id:id
        },
        success(resp){
          DishList.value=resp.data;
          updateTotalPrice()
          console.log(DishList.value)
          ElMessage.success("获取菜品列表成功");
        }
      })
    }
    //删除选中菜品
    const removeSelectedDish = (dish) => {
      const index = selectedDishes.value.findIndex(item => item.id === dish.id);
      if (index !== -1) {
        selectedDishes.value.splice(index, 1);
        ElMessage.success('已移除该菜品');
        updateTotalPrice();
      }
    };
    //删除菜品
     const deleteDish = (dish) => {
      const index = DishList.value.findIndex(item => item.id === dish.id);
      if (index!== -1) {
        DishList.value.splice(index, 1);
        ElMessage.success('删除菜品成功');
        updateTotalPrice()
      }
    };
    const updateTotalPrice = () => {
      let total = 0;
      DishList.value.forEach(item => {
        total += item.price * item.acount; // 单价 × 份数
      });
      setMealModel.value.price = total; // 更新套餐价格
      console.log('当前总价:', total);
    };
    const selectDishByTypeId=(id)=>{
      activeTypeId.value = id;
      $.ajax({
        url:"http://121.43.139.67:3000/dish/getdishbytypeid",
        type:"post",
        data:{
          dishTypeId:id,
        },
        success(resp){
          console.log(resp);
          TypeDishList.value=resp.data
          ElMessage.success("获取该分类下商品成功")
        }
      })
    }

    //提交
   /* const confirmAddDishes = () => {
      /*if (selectedDishes.value.length === 0) {
        ElMessage.warning('请至少选择一个菜品');
        return;
      }
      console.log('当前selectedDishes:', JSON.parse(JSON.stringify(selectedDishes.value)));
      console.log('当前DishList:', JSON.parse(JSON.stringify(DishList.value)));
      selectedDishes.value.forEach(dish => {
        if (!DishList.value.some(item => item.name === dish.name)) {
          DishList.value.push({
            id: dish.id,
            name: dish.name,
            price: dish.price,
            cover: dish.image,
            status: dish.status,
            acount: 1,
            originalPrice: dish.price
          });
        }
      });
      let total=0
      DishList.value.forEach(item => {
        total += item.value.price * item.value.acount; // Multiply by quantity if needed
      });
      setMealModel.value.price=total
      visible.value = false;
      selectedDishes.value = [];
      console.log('当前DishList:', JSON.parse(JSON.stringify(DishList.value)));
    };*/
      const confirmAddDishes = () => {
        if (selectedDishes.value.length === 0) {
          ElMessage.warning('请至少选择一个菜品');
          return;
        }
        selectedDishes.value.forEach(dish => {
          if (!DishList.value.some(item => item.name === dish.name)) {
            DishList.value.push({
              id: dish.id,
              name: dish.name,
              price: dish.price,
              cover: dish.image,
              status: dish.status,
              acount: 1, // 默认份数为1
              originalPrice: dish.price
            });
          }
        });

        // 动态计算总价
        updateTotalPrice();
        visible.value = false;
        selectedDishes.value = [];
      };

    /*// 图片上传处理
    const handleImageChange = (file) => {
      const reader = new FileReader();
      reader.onload = (e) => {
        setMealModel.value.cover = e.target.result;
      };
      reader.readAsDataURL(file.raw);
    };*/

    // 保存套餐
  const save = async () => {
  console.log(setMealModel.value.name)
  console.log(setMealModel.value.description)
    const dishesData = DishList.value.map(item => ({
      name: item.name,
      cover: item.cover,
      price: item.price,
      acount: item.acount,
      setmealId:id
    }));
    
    await $.ajax({
      url: `http://121.43.139.67:3000/setmealdish/update?setmealId=${id}`,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(dishesData),
      success() {
        ElMessage.success("套餐修改成功");
        router.push('/setmeal/manager');
      },
      error(xhr) {
        ElMessage.error(xhr.responseJSON?.message || "套餐修改失败");
      }
    });

    await $.ajax({
      url:"http://121.43.139.67:3000/setmeal/update",
      type:"post",
      contentType:"application/json",
      data:JSON.stringify({
        id:id,
        price:setMealModel.value.price,
        cover:setMealModel.value.cover,
        description:setMealModel.value.description,
        name:setMealModel.value.name,
      }),
      success(){
        ElMessage.success("修改套餐成功")
      },
      error(){
        ElMessage.error("套餐修改失败")
      }
    })
};

    // 初始化获取数据
    getSetMealById();
    getAllDishType();
    getDishBySetMealId();
    return {
      setMealModel,
      rules,
      /*handleImageChange,
      save,
      cancel*/
      visible,
      openAddDishDialog,
      getSetMealById,
      getDishBySetMealId,
      DishType,
      DishList,
      deleteDish,
      selectDishByTypeId,
      TypeDishList,
      activeTypeId,
      handleSelectionChange,
      showSelectedDishes,
      selectedDishes,
      removeSelectedDish,
      confirmAddDishes,
      save,
      updateTotalPrice,
      beforeUpload,
      handleExceed,
      updatehandleUploadSuccess
    };
  }
};
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}
.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.active-category {
  background: #ffd700 !important;
  color: #333 !important;
  font-weight: bold;
}
</style>