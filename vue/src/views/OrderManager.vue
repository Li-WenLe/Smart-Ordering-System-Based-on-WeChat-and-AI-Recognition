<template>
  <div class="order-management">
    <!-- 查询工具栏 -->
    <div class="toolbar">
      <div class="search-panel">
        <div class="search-item">
          <label>订单号：</label>
          <el-input v-model="searchParams.orderId" placeholder="请输入订单号" clearable></el-input>
        </div>
        <div class="search-item">
          <label>手机号：</label>
          <el-input v-model="searchParams.phone" placeholder="请输入手机号" clearable></el-input>
        </div>
        <div class="search-item">
          <label>订单状态：</label>
          <el-select class="selectbox" placeholder="请选择订单状态" v-model="searchParams.status">
            <el-option label="已完成" value='0'></el-option>
            <el-option label="待付款" value='1'></el-option>
            <el-option label="待取餐" value='2'></el-option>
          </el-select>
        </div>
        <el-button type="primary" @click="handleSearch">
          <i class="el-icon-search"></i> 查询
        </el-button>
      </div>
    </div>

    <!-- 订单列表表格 -->
    <div class="order-table">
      <el-table :data="orderList" borderstyle="width: 120%">
        <el-table-column prop="orderId" label="订单号" width="180"></el-table-column>
        <el-table-column prop="status" label="订单状态" width="130">
          <template #default="{ row }">
            <el-tag 
              :type="row.status === 0? 'success' : 
                    row.status === 3? 'danger' : 
                    'warning'">
              {{ row.status === 0? '已完成' : 
                row.status === 1? '待付款' : 
                row.status ===2? '待接单' : 
                row.status ===4? '待送达' :
                 row.status ===3? '已取消' :'未知状态' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="用户名" width="120"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="170"></el-table-column>
        <el-table-column prop="address" label="地址" width="170" show-overflow-tooltip></el-table-column>
        <el-table-column prop="total" label="应付金额" width="120">
          <template #default="{ row }">￥{{ row.total }}</template>
        </el-table-column>
        <el-table-column prop="total" label="实付金额" width="120">
          <template #default="{ row }">￥{{ row.payedTotal }}</template>
        </el-table-column>
        <el-table-column prop="orderTime" label="下单时间" width="180"></el-table-column>
        <el-table-column prop="updateTime" label="订单结束时间" width="180"></el-table-column>
        <el-table-column label="操作" width="230">
          <template #default="{ row }">
            <el-button 
              size="mini" 
              :disabled="row.status !== 2" 
              @click="handleput(row)">
              接单
            </el-button>
            <el-button 
              size="mini" 
              :disabled="row.status !== 4" 
              @click="handleputon(row)">
              送达
            </el-button>
            <el-button size="mini" type="primary" @click="handleView(row)">
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
  <el-dialog 
    title="订单详情" 
    v-model="dialogVisible"
    width="50%">
    
    <el-table :data="orderDetail">
      <el-table-column prop="image" label="商品封面" width="120">
        <template #default="{row}">
          <img :src="row.image" style="width:80px;height:80px;object-fit:cover">
        </template>
      </el-table-column>
      <el-table-column prop="dishname" label="商品名称"></el-table-column>
      <el-table-column prop="number" label="数量" width="80"></el-table-column>
      <el-table-column prop="acount" label="单价" width="100">
        <template #default="{row}">￥{{row.acount}}</template>
      </el-table-column>
      <el-table-column prop="total" label="小计" width="100">
        <template #default="{row}">￥{{row.acount * row.number}}</template>
      </el-table-column>
    </el-table>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import axios from 'axios';
import $ from 'jquery'
export default {
  name: 'OrderManagement',
  data() {
    return {
      dialogVisible: false, 
      total: 0,
      orderDetail:[],
      searchParams: {
        orderId: '',
        phone: '',
        status: '0',
      },
      orderList: [],
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
    }
  },
  methods: {
    getAllOrder() {
      axios.get("http://121.43.139.67:3000/admin/order/getall", {
        params: {
          pageSize: this.pagination.pageSize,
          pageNum: this.pagination.pageNum
        }
      })
      .then(response => {
        this.orderList = response.data.items;
        console.log(response.data.total);
        console.log(response.data.items)
        this.total = response.data.total
      })
      .catch(error => {
        console.error("Error fetching orders:", error);
      });
    },
    handleSearch() {
      console.log(this.searchParams.status)
      const vm = this;
      $.ajax({
        url: "http://121.43.139.67:3000/admin/order/search",
        type: "post",
        data: {
          pageSize: this.pagination.pageSize,
          pageNum: this.pagination.pageNum,
          orderId: this.searchParams.orderId,
          phone: this.searchParams.phone,
          status: parseInt(this.searchParams.status) 
        },
        success(resp) {
          console.log(resp)
          vm.orderList = resp.items
          vm.total = resp.total
        },
        error(error) {
          console.log(error)
        }
      })
    },
    handleput(row) {
      let orderId = row.orderId;
      console.log(orderId);
      this.$confirm('确定要接单吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(() => {
        this.$message({
          type: 'success',
          message: '接单成功!'
        });
        //修改订单状态
        axios({
          url: "http://121.43.139.67:3000/admin/order/updatestatus",
          method: "post",
          data:  {  // 使用data发送请求体
            orderId: orderId,
            status: 4
          },
        }).then(resp => {
          console.log(resp)
          this.$message.success('接单成功');
          this.getAllOrder()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        });          
      });
    },
    handleputon(row) {
      if (row.status!== 4) {
        this.$message({
          type: 'info',
          message: '请先接单！'
        });
        return;
      }
      let orderId = row.orderId;
      console.log(orderId);
      this.$confirm('确定已送达吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(() => {
        this.$message({
          type: 'success',
          message: '已送达!'
        });
        //修改订单状态
        axios({
          url: "http://121.43.139.67:3000/admin/order/updatestatus",
          method: "post",
          data:  {  // 使用data发送请求体
            orderId: orderId,
            status: 0
          },
        }).then(resp => {
          console.log(resp)
          this.$message.success('接单成功');
          this.getAllOrder()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        });          
      });
    },
    handleView(row) {
      let orderId=row.orderId;
      this.dialogVisible=true;
      console.log(orderId);
      axios({
        url:"http://121.43.139.67:3000/admin/order/getdetail",
        method:"post",
        data:{
          orderId:orderId,
        }
      }).then(resp=>{
        console.log(resp.data.data);
        this.orderDetail=resp.data.data; // 修正属性名拼写错误
      }).catch(error=>{
        console.log(error)
        this.$message.error("获取订单详情失败");
      })
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val;
      this.getAllOrder()
      // this.fetchOrders()
    },
    handleCurrentChange(val) {
      this.pagination.pageNum = val;
      this.getAllOrder()
    },
    fetchOrders() {
      // 实际项目中这里应该调用API获取订单数据
      // 示例:
      // api.getOrders({
      //   ...this.searchParams,
      //   page: this.pagination.currentPage,
      //   size: this.pagination.pageSize
      // }).then(response => {
      //   this.orderList = response.data.list
      //   this.pagination.total = response.data.total
      // })
    }
  },
  created() {
    this.getAllOrder();
  }
}
</script>

<style scoped>
.order-management {
  padding: 20px;
  background-color: #f5f5f5;
}

.toolbar {
  background-color: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.search-panel {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.search-item {
  margin-right: 20px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

.search-item label {
  width: 80px;
  text-align: right;
  margin-right: 10px;
  color: #333;
  font-weight: 500;
}

.search-item .el-input,
.search-item .el-select {
  width: 200px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  transition: border-color 0.3s;
}

.search-item .el-input:hover,
.search-item .el-select:hover {
  border-color: #409eff;
}

.search-item .el-input:focus,
.search-item .el-select:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.toolbar .el-button {
  background-color: #409eff;
  border-color: #409eff;
  border-radius: 4px;
  padding: 10px 20px;
  transition: background-color 0.3s;
}

.toolbar .el-button:hover {
  background-color: #3a8ee6;
  border-color: #3a8ee6;
}

.toolbar .el-button i {
  margin-right: 5px;
}

.order-table {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

/* 新增容器样式 */
.order-management-container {
  display: flex;
  justify-content: flex-end; /* 使内容靠右 */
  padding-top: 20px; /* 顶部间距 */
}

.order-management {
  width: 95%; /* 控制整体宽度 */
  margin-right: 20px; /* 右侧间距 */
}

/* 调整表格样式 */
.order-table {
  background-color: #fff;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

/* 调整分页和页脚样式 */
.footer-section {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-end; /* 使内容靠右 */
}

.pagination {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.pagination .total,
.pagination .page-size,
.pagination .page-jumper {
  margin: 0 10px;
  font-size: 14px;
  color: #606266;
}

.copyright {
  text-align: right;
  font-size: 12px;
  color: #909399;
  margin-top: 10px;
}

.copyright div {
  margin-top: 5px;
}

/* 调整表格单元格间距 */
.el-table {
  margin: 0;
}

.el-table td, .el-table th {
  padding: 2px 0 !important; /* 将原来的 8px 改为 4px，让列之间更紧凑 */
}

/* 调整按钮间距 */
.el-button {
  margin-left: 5px;
}

.el-table--border {
  border-spacing: 0; /* 移除单元格之间的默认间距 */
}

/* 更紧凑的表格样式 */
.order-table .el-table {
  margin: 0;
  border-collapse: collapse; /* 让边框合并 */
}

.order-table .el-table td, 
.order-table .el-table th {
  padding: 2px 0 !important; /* 更小的内边距 */
  border: none; /* 可选：移除边框 */
}

.order-table .el-table--border th, 
.order-table .el-table--border td {
  border-right: 1px solid #EBEEF5; /* 保留列边框 */
}

.order-table .el-table--border::after, 
.order-table .el-table--group::after {
  display: none; /* 移除表格底部边框 */
}

/* 调整按钮大小和间距 */
.order-table .el-button {
  padding: 5px 8px !important; /* 更小的按钮 */
  margin-left: 3px !important; /* 更小的按钮间距 */
}
/* 商品图片样式 */
.el-table img {
  border-radius: 4px;
  border: 1px solid #eee;
}

/* 弹窗标题样式 */
.el-dialog__title {
  font-weight: bold;
  color: #333;
}

/* 弹窗内容区域样式 */
.el-dialog__body {
  padding: 20px;
}
</style>