<template>
  <el-card class="page-container">
      <template #header>
          <div class="header">
              <span>轮播图管理</span>
              <div class="extra">
                  <el-button type="primary" @click="dialogVisible=true">添加轮播图</el-button>
              </div>
          </div>
      </template>
      <el-table :data="bannerList" style="width: 100%">
          <el-table-column label="序号" width="100" type="index"> </el-table-column>
          <el-table-column label="标题" prop="title"></el-table-column>
          <el-table-column label="是否首页展示" prop="ishow">
              <template #default="scope">
                  <span>{{ scope.row.ishow === 0 ? '是' : '否' }}</span>
              </template>
          </el-table-column>
          <el-table-column label="封面图" prop="cover">
              <template #default="scope">
                  <img :src="scope.row.cover" style="width: 100px; height: 50px; object-fit: cover;" />
              </template>
          </el-table-column>
          <el-table-column label="内容" prop="content"></el-table-column>
          <el-table-column label="内容插图" prop="image">
            <template #default="scope">
                  <img :src="scope.row.image" style="width: 100px; height: 50px; object-fit: cover;" />
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
      <el-dialog v-model="dialogVisible" title="添加轮播图" width="50%"  >
          <el-form :model="addBannerModel" :rules="rules" label-width="120px" style="padding-right: 30px" label-position="left">
              <el-form-item label="标题" prop="title">
                  <el-input v-model="addBannerModel.title" minlength="1"></el-input>
              </el-form-item>
              <el-form-item label="是否首页展示" prop="ishow">
                <el-select class="selectbox" placeholder="请选择" v-model="addBannerModel.ishow">
                <el-option label="是" value="0"></el-option>
                <el-option label="否" value="1"></el-option>
                </el-select>
            </el-form-item>
              <el-form-item label="内容" prop="content">
                  <el-input v-model="addBannerModel.content"></el-input>
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
                  <div v-if="addBannerModel.cover">
                      <img :src="addBannerModel.cover" style="width: 100px; height: 50px; object-fit: cover;" />
                  </div>
              </el-form-item>
              <el-form-item label="上传内容插图" prop="image">
                  <el-upload
                      action="http://121.43.139.67:3000/upload"
                      :on-success="handleUploadImageSuccess"
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
                  <div v-if="addBannerModel.image">
                      <img :src="addBannerModel.image" style="width: 100px; height: 50px; object-fit: cover;" />
                  </div>
              </el-form-item>
          </el-form>
          <template #footer>
              <span class="dialog-footer">
                  <el-button @click="dialogVisible = false">取消</el-button>
                  <el-button type="primary" @click="addBanner"> 确认 </el-button>
              </span>
          </template>
      </el-dialog>
        <!--添加编辑弹窗---->
        <el-dialog v-model="updateVisible" title="修改轮播图" width="50%">
          <el-form :model="updateBannerModel"  label-width="120px" style="padding-right: 30px" label-position="left" >
              <el-form-item label="标题" prop="type">
                  <el-input v-model="updateBannerModel.title" minlength="1"></el-input>
              </el-form-item>
              <!-- 是否首页展示 -->
              <el-form-item label="是否首页展示" prop="ishow">
                <el-select class="selectbox" placeholder="请选择" v-model="updateBannerModel.ishow">
                <el-option label="是" value="0"></el-option>
                <el-option label="否" value="1"></el-option>
                </el-select>
            </el-form-item>
              <el-form-item label="内容" prop="content">
                  <el-input v-model="updateBannerModel.content" minlength="1"></el-input>
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
                          <div v-if="updateBannerModel.cover" style="text-align: center;">
                              <img :src="updateBannerModel.cover" style="width: 100px; height: 100px; margin-right: 15px; object-fit: cover;" />
                          </div>
                      </el-col>
                  </el-row>
              </el-form-item>
              <el-form-item label="修改内容插图" prop="image">
                  <el-row :gutter="20">
                      <!-- 图片上传区域 -->
                      <el-col :span="12">
                          <el-upload
                              action="http://121.43.139.67:3000/upload"
                              :on-success="updatehandleUploadImageSuccess"
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
                          <div v-if="updateBannerModel.image" style="text-align: center;">
                              <img :src="updateBannerModel.image" style="width: 100px; height: 100px; margin-right: 15px; object-fit: cover;" />
                          </div>
                      </el-col>
                  </el-row>
              </el-form-item>
          </el-form>
  <!-- 弹窗底部按钮 -->
  <template #footer>
      <span class="dialog-footer">
          <el-button @click="updateVisible = false">取消</el-button>
          <el-button type="primary" @click="updateBanner">确认</el-button>
      </span>
  </template>
</el-dialog>
      <!--添加删除弹窗---->
      <el-dialog v-model="deleteVisible" title="删除轮播图" width="30%">
         <el-alert>是否确定删除此轮播图？</el-alert>
          <template #footer>
              <span class="dialog-footer">
                  <el-button @click="deleteVisible = false">取消</el-button>
                  <el-button type="primary" @click="deleteBanner"> 确认 </el-button>
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
import AutoResizer from 'element-plus/es/components/table-v2/src/components/auto-resizer';

export default {
  setup() {
      const store = useStore();
      let bannerList = ref([]);
      let DishType = ref([]);
      const dialogVisible = ref(false);
      const updateVisible=ref(false);
      const deleteVisible=ref(false);
      const fileList = ref([]); // 定义 fileList
      const updateBannerModel=ref({
          title: '',
          ishow: '',
          cover: '',
          id:'',
          content:'',
          image:'',
      })
      const deleteBannerModel=ref({
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

      const addBannerModel = ref({
          content: '',
          ishow: '',
          cover: '',
          image:'',
          title:'',
      });

      const getAllBanner = () => {
          $.ajax({
              url: "http://121.43.139.67:3000/banner",
              type: "get",
              headers:{
                Authorization: `Bearer ${store.state.token}`
              },
              success(resp) {
                  bannerList.value = resp.data;
              },
              error() {
                  ElMessage.error('获取分类列表失败');
              }
          });
      };

      const addBanner = () => {
          $.ajax({
              url: "http://121.43.139.67:3000/banner/add",
              type: "post",
              contentType: "application/json",
              data: JSON.stringify({
                  title:addBannerModel.value.title,
                  content:addBannerModel.value.content,
                  image:addBannerModel.value.image,
                  cover:addBannerModel.value.cover,
                  ishow:addBannerModel.value.ishow
              }),
              success(resp) {
                  console.log(resp);
                  dialogVisible.value = false;
                  getAllBanner();
                  ElMessage.success('添加轮播图成功');
              },
              error() {
                  ElMessage.error('添加轮播图失败');
              }
          });
      };
      const openEditDialog=(row)=>{
          //updateBannerModel.value.ishow=row.ishow;
          updateBannerModel.value.title=row.title;
          updateBannerModel.value.cover=row.cover;
          updateBannerModel.value.id=row.id;
          updateBannerModel.value.image=row.image;
          updateBannerModel.value.content=row.content;
          updateVisible.value=true;
      }
      const openDeleteDialog=(row)=>{
          deleteBannerModel.value.id=row.id;
          deleteVisible.value=true;
      };
      const deleteBanner=()=>{
          $.ajax({
              url:"http://121.43.139.67:3000/banner/delete",
              type:"post",
              data:{
                  id:deleteBannerModel.value.id
              },
              success(resp){
                  console.log(resp);
                  ElMessage.success("删除轮播图成功");
                  deleteVisible.value=false;
                  getAllBanner();
              },
              error(){
                  ElMessage.error("删除轮播图失败");
              }
          })
      }
      const handleUploadSuccess = (resp) => {
         console.log(resp);
         addBannerModel.value.cover=resp;
         console.log(addBannerModel.value.cover);
      };
      const handleUploadImageSuccess=(resp)=>{
        console.log(resp);
         addBannerModel.value.image=resp;
         console.log(addBannerModel.value.image);
      }
      const updatehandleUploadSuccess = (resp) => {
         console.log(resp);
         updateBannerModel.value.cover=resp;
         console.log(updateBannerModel.value.cover);
      };
      const updatehandleUploadImageSuccess = (resp) => {
         console.log(resp);
         updateBannerModel.value.image=resp;
         console.log(updateBannerModel.value.image);
      };
      const updateBanner=()=>{
          $.ajax({
              url:"http://121.43.139.67:3000/banner/update",
              type:"post",
              contentType:"application/json",
              data:JSON.stringify({
                  id:updateBannerModel.value.id,
                  title:updateBannerModel.value.title,
                  ishow:updateBannerModel.value.ishow,
                  cover:updateBannerModel.value.cover,
                  image:updateBannerModel.value.image,
                  content:updateBannerModel.value.content,
              }),
              success(resp){
                  console.log(resp);
                  updateVisible.value=false;
                  ElMessage.success("修改分类成功");
                  //更新数据
                  getAllBanner()
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

      getAllBanner();

      return {
          store,
          dialogVisible,
          Edit,
          Delete,
          getAllBanner,
          DishType,
          addBannerModel,
          addBanner,
          handleUploadSuccess,
          handleUploadImageSuccess,
          rules,
          beforeUpload,
          handleExceed,
          fileList,
          updateVisible,
          deleteVisible,
          updateBannerModel,
          openEditDialog,
          updatehandleUploadSuccess,
          updatehandleUploadImageSuccess,
          updateBanner,
          deleteBannerModel,
          openDeleteDialog,
          deleteBanner,
          bannerList,
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