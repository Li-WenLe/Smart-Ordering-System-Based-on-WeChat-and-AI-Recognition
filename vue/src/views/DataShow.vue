<template>
  <div class="dashboard-container">
    <h1>数据统计</h1>
    
    <div class="charts-grid">
      <!-- 数据统计区块 -->
      <div class="chart-card">
        <h2>数据统计</h2>
        <div class="stats-grid">
          <div class="stat-item">
            <h3>今日总订单</h3>
            <div class="stat-value">{{ todayOrderNumber }}</div>
          </div>
          <div class="stat-item">
            <h3>今日有效订单</h3>
            <div class="stat-value">{{ todayRealOrderNumber }}</div>
          </div>
          <div class="stat-item">
            <h3>今日订单完成率</h3>
            <div class="stat-value">{{ complateRate }}</div>
          </div>
          <div class="stat-item">
            <h3>今日营业额</h3>
            <div class="stat-value">¥{{ todaytotal.toFixed(2) }}</div>
          </div>
          <div class="stat-item">
            <h3>历史总订单</h3>
            <div class="stat-value">{{ allOrderNumber }}</div>
          </div>
          <div class="stat-item">
            <h3>历史有效订单</h3>
            <div class="stat-value">{{ allRealOrderNumber }}</div>
          </div>
          <div class="stat-item">
            <h3>历史订单完成率</h3>
            <div class="stat-value">{{ allComplateRate }}</div>
          </div>
          <div class="stat-item">
            <h3>历史总营业额</h3>
            <div class="stat-value">¥{{ alldaystotal.toFixed(2) }}</div>
          </div>
        </div>
      </div>

      <div class="chart-card">
        <h2>今日订单统计</h2>
        <div class="chart-container" ref="orderChartToday"></div>
        <h2>历史订单统计</h2>
        <div class="chart-container" ref="orderChartHistory"></div>
      </div>
      <!-- 销售排名区块 -->
      <div class="chart-card">
        <h2>历史销量排名TOP10</h2>
        <div class="chart-container" ref="salesRankChart"></div>
        <h2>今日销量排名TOP10</h2>
        <div class="chart-container" ref="todaysalesRankChart"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref, computed, watch } from 'vue';
import * as echarts from 'echarts';
import $ from 'jquery';

export default {
  setup() {
    // DOM引用
    const orderChartToday = ref(null);
    const orderChartHistory = ref(null);
    const userChart = ref(null);
    const userAddChart = ref(null);
    const salesRankChart = ref(null);
    const todaysalesRankChart = ref(null);

    const loading = ref(false);
    const error = ref(null);
    // 数据状态
    const todayRealOrderList = ref([]);
    const todayOrderList = ref([]);
    const todayComplateOrderList = ref([]);
    const allOrderList = ref([]);
    const allRealOrderList = ref([]);
    const allComplateOrderList = ref([]);
    const allTopList = ref([]);
    const todayTopList = ref([]);
    const resentUserIncreate = ref([]); // 存储数据
    // 统计数据
    const todayComplateNumber = ref(0);
    const allOrderNumber = ref(0);
    const allRealOrderNumber = ref(0);
    const allComplateOrderNumber = ref(0);
    const alldaystotal = ref(0);
    const todaytotal = ref(0);
    const todayOrderNumber = ref(0);
    const todayRealOrderNumber = ref(0);
    const currentDateRange = ref('2022-09-01 至 2022-09-30');
    const topProduct = ref('人气车载计划');

    // 计算属性
    const complateRate = computed(() => {
      if (todayRealOrderNumber.value === 0) return '0%';
      return (todayComplateNumber.value / todayRealOrderNumber.value * 100).toFixed(1) + '%';
    });

    const allComplateRate = computed(() => {
      if (allRealOrderNumber.value === 0) return '0%';
      return (allComplateOrderNumber.value / allRealOrderNumber.value * 100).toFixed(1) + '%';
    });

    // 数据获取方法
    const getTotalTodyOrder = () => {
      return new Promise((resolve) => {
        $.ajax({
          url: "http://121.43.139.67:3000/admin/order/gettodayorder",
          type: "get",
          success(resp) {
            todayOrderList.value = resp.data;
            todayOrderNumber.value = todayOrderList.value.length;
            resolve();
          },
          error() {
            console.error("获取今日总订单失败");
            resolve();
          }
        });
      });
    };

    const getTodayRealOrder = () => {
      return new Promise((resolve) => {
        $.ajax({
          url: "http://121.43.139.67:3000/admin/order/gettodayrealorder",
          type: "get",
          success(resp) {
            todayRealOrderList.value = resp.data;
            todayRealOrderNumber.value = todayRealOrderList.value.length;
            todaytotal.value = todayRealOrderList.value.reduce((total, item) => {
              return total + (item.total || 0);
            }, 0);
            resolve();
          },
          error() {
            console.error("获取今日有效订单失败");
            resolve();
          }
        });
      });
    };

    const getTodayComplateOrder = () => {
      return new Promise((resolve) => {
        $.ajax({
          url: "http://121.43.139.67:3000/admin/order/gettodaycomplateorder",
          type: "get",
          success(resp) {
            todayComplateOrderList.value = resp.data;
            todayComplateNumber.value = todayComplateOrderList.value.length;
            resolve();
          },
          error() {
            console.error("获取今日完成订单失败");
            resolve();
          }
        });
      });
    };

    const getAllOrders = () => {
      return new Promise((resolve) => {
        $.ajax({
          url: "http://121.43.139.67:3000/admin/order/getallorders",
          type: "get",
          success(resp) {
            allOrderList.value = resp.data;
            allOrderNumber.value = allOrderList.value.length;
            resolve();
          },
          error() {
            console.error("获取历史总订单失败");
            resolve();
          }
        });
      });
    };

    const getAllRealOrders = () => {
      return new Promise((resolve) => {
        $.ajax({
          url: "http://121.43.139.67:3000/admin/order/getallrealorders",
          type: "get",
          success(resp) {
            allRealOrderList.value = resp.data;
            allRealOrderNumber.value = allRealOrderList.value.length;
            alldaystotal.value = allRealOrderList.value.reduce((total, item) => {
              return total + (item.total || 0);
            }, 0);
            resolve();
          },
          error() {
            console.error("获取历史有效订单失败");
            resolve();
          }
        });
      });
    };

    const getAllComplateOrders = () => {
      return new Promise((resolve) => {
        $.ajax({
          url: "http://121.43.139.67:3000/admin/order/getallcomplateorders",
          type: "get",
          success(resp) {
            allComplateOrderList.value = resp.data;
            allComplateOrderNumber.value = allComplateOrderList.value.length;
            resolve();
          },
          error() {
            console.error("获取历史完成订单失败");
            resolve();
          }
        });
      });
    };

    const historyTopInfo = () => {
      return new Promise((resolve) => {
        $.ajax({
          url: "http://121.43.139.67:3000/admin/order/alltop",
          type: "get",
          success(resp) {
            console.log(resp);
            allTopList.value = resp.data;
            resolve();
          },
          error() {
            console.error("获取销售排行数据失败");
            resolve();
          }
        });
      });
    };
    const todayTopInfo = () => {
      return new Promise((resolve) => {
        $.ajax({
          url: "http://121.43.139.67:3000/admin/order/todaytop",
          type: "get",
          success(resp) {
            console.log(resp);
            todayTopList.value = resp.data;
            resolve();
          },
          error() {
            console.error("获取销售排行数据失败");
            resolve();
          }
        });
      });
    };
    //统计近7天的新增用户数量
    const resentUserIncreatefunction = () => {
      loading.value = true;
      error.value = null;
      $.ajax({
        url: "http://121.43.139.67:3000/admin/user/weeklyregistrations",
        type: "get",
        success(resp) {
          console.log("API响应:", resp);
          if (resp && Array.isArray(resp.data)) {
            resentUserIncreate.value = resp.data;
          } else {
            resentUserIncreate.value = [];
            console.warn("返回数据格式不正确:", resp);
            // 显示错误提示
            error.value = "返回数据格式不正确";
          }
        },
        error(err) {
          console.error("请求失败:", err);
          error.value = err;
          resentUserIncreate.value = [];
        },
        complete() {
          loading.value = false;
        }
      });
    };
    // 图表初始化方法
    const initOrderTodayChart = () => {
      if (!orderChartToday.value) return;
      
      const chart = echarts.init(orderChartToday.value);
      chart.setOption({
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '订单统计',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: todayRealOrderNumber.value, name: '今日有效订单' },
              { value: todayOrderNumber.value, name: '今日总订单' },
              { value: todayComplateNumber.value, name: '今日已完成' }
            ]
          }
        ]
      });
      return chart;
    };

    const initOrderHistoryChart = () => {
      if (!orderChartHistory.value) return;
      
      const chart = echarts.init(orderChartHistory.value);
      chart.setOption({
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '订单统计',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: allRealOrderNumber.value, name: '历史有效订单' },
              { value: allOrderNumber.value, name: '历史总订单' },
              { value: allComplateOrderNumber.value, name: '历史已完成' }
            ]
          }
        ]
      });
      return chart;
    };

    const initUserChart = () => {
      if (!userChart.value) return;
      
      const chart = echarts.init(userChart.value);
      chart.setOption({
        xAxis: {
          type: 'category',
          data: ['', '', '', '', ''],
          show: false
        },
        yAxis: {
          type: 'value',
          show: false
        },
        series: [
          {
            data: [1, 0.8, 0.6, 0.4, 0.2],
            type: 'line',
            smooth: true,
            lineStyle: {
              width: 3,
              color: '#5470C6'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgba(84, 112, 198, 0.5)'
                },
                {
                  offset: 1,
                  color: 'rgba(84, 112, 198, 0.1)'
                }
              ])
            },
            symbol: 'circle',
            symbolSize: 8,
            itemStyle: {
              color: '#5470C6',
              borderColor: '#fff',
              borderWidth: 2
            }
          }
        ],
        grid: {
          top: 0,
          bottom: 0,
          left: 0,
          right: 0
        }
      });
      return chart;
    };
    const initSalesRankChart = () => {
      if (!salesRankChart.value || !allTopList.value.length) return;
      
      // 准备图表数据 - 按销量降序排序
      const sortedData = [...allTopList.value].sort((a, b) => b.totalSales - a.totalSales);
      const dishNames = sortedData.map(item => item.dishname);
      const salesData = sortedData.map(item => item.totalSales);
      
      const chart = echarts.init(salesRankChart.value);
      chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: '{b}: {c} 份'
        },
        grid: {
          left: '20%',
          right: '5%',
          bottom: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          },
          axisLabel: {
            color: '#666'
          },
          splitLine: {
            lineStyle: {
              color: '#eee'
            }
          }
        },
        yAxis: {
          type: 'category',
          data: dishNames,
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          },
          axisLabel: {
            color: '#666',
            fontSize: 12
          }
        },
        series: [
          {
            name: '销量',
            type: 'bar',
            data: salesData,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#FF9A9E' },
                { offset: 0.5, color: '#FAD0C4' },
                { offset: 1, color: '#FAD0C4' }
              ])
            },
            label: {
              show: true,
              position: 'right',
              formatter: '{c} 份',
              color: '#333'
            },
            barMaxWidth: 40
          }
        ]
      });
      return chart;
    };
    const initTodaySalesRankChart = () => {
      if (!todaysalesRankChart.value || !todayTopList.value.length) return;
      
      // 准备图表数据 - 按销量降序排序
      const sortedData = [...todayTopList.value].sort((a, b) => b.totalSales - a.totalSales);
      const dishNames = sortedData.map(item => item.dishname);
      const salesData = sortedData.map(item => item.totalSales);
      
      const chart = echarts.init(todaysalesRankChart.value);
      chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: '{b}: {c} 份'
        },
        grid: {
          left: '20%',
          right: '5%',
          bottom: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          },
          axisLabel: {
            color: '#666'
          },
          splitLine: {
            lineStyle: {
              color: '#eee'
            }
          }
        },
        yAxis: {
          type: 'category',
          data: dishNames,
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          },
          axisLabel: {
            color: '#666',
            fontSize: 12
          }
        },
        series: [
          {
            name: '销量',
            type: 'bar',
            data: salesData,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#FF9A9E' },
                { offset: 0.5, color: '#FAD0C4' },
                { offset: 1, color: '#FAD0C4' }
              ])
            },
            label: {
              show: true,
              position: 'right',
              formatter: '{c} 份',
              color: '#333'
            },
            barMaxWidth: 40
          }
        ]
      });
      return chart;
    };
    // 初始化所有图表
    const initAllCharts = () => {
      initOrderTodayChart();
      initOrderHistoryChart();
      initUserChart();
      initSalesRankChart();
      initTodaySalesRankChart()
    };

    // 监听数据变化更新图表
    watch([todayRealOrderNumber, todayOrderNumber, todayComplateNumber], () => {
      const chart = echarts.getInstanceByDom(orderChartToday.value);
      if (chart) {
        chart.setOption({
          series: [{
            data: [
              { value: todayRealOrderNumber.value, name: '今日有效订单' },
              { value: todayOrderNumber.value, name: '今日总订单' },
              { value: todayComplateNumber.value, name: '今日已完成' }
            ]
          }]
        });
      }
    });

    watch([allRealOrderNumber, allOrderNumber, allComplateOrderNumber], () => {
      const chart = echarts.getInstanceByDom(orderChartHistory.value);
      if (chart) {
        chart.setOption({
          series: [{
            data: [
              { value: allRealOrderNumber.value, name: '历史有效订单' },
              { value: allOrderNumber.value, name: '历史总订单' },
              { value: allComplateOrderNumber.value, name: '历史已完成' }
            ]
          }]
        });
      }
    });

    watch(allTopList, () => {
      initSalesRankChart();
    }, { deep: true });

    watch(todayTopList, () => {
      initTodaySalesRankChart();
    }, { deep: true });
    watch(resentUserIncreate, () => {
      const chart = echarts.getInstanceByDom(userAddChart.value);
      if (chart) {
        chart.setOption({
          xAxis: {
            data: resentUserIncreate.value.map(item => item.date)
          },
          series: [{
            data: resentUserIncreate.value.map(item => item.count)
          }]
        });
      }
    }, { deep: true });
    // 组件挂载时加载数据并初始化图表
    onMounted(async () => {
      try {
        await Promise.all([
          getTotalTodyOrder(),
          getTodayRealOrder(),
          getTodayComplateOrder(),
          getAllOrders(),
          getAllRealOrders(),
          getAllComplateOrders(),
          historyTopInfo(),
          todayTopInfo(),
          resentUserIncreatefunction()
        ]);

        initAllCharts(); 
      } catch (error) {
        console.error("初始化数据失败:", error);
      }
    });
    return {
      orderChartToday,
      orderChartHistory,
      userChart,
      userAddChart,
      salesRankChart,
      todaysalesRankChart,
      todaytotal,
      todayOrderNumber,
      todayRealOrderNumber,
      todayComplateNumber,
      complateRate,
      allComplateRate,
      allOrderNumber,
      allRealOrderNumber,
      allComplateOrderNumber,
      alldaystotal,
      currentDateRange,
      topProduct,
      resentUserIncreate,
      loading,
      error
    };
  }
};
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

h1 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.chart-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-card h2 {
  font-size: 16px;
  margin-bottom: 16px;
  color: #666;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.stat-item {
  text-align: center;
  padding: 12px;
  background-color: #f9f9f9;
  border-radius: 6px;
}

.stat-item h3 {
  font-size: 14px;
  color: #888;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.date-range {
  font-size: 16px;
  color: #333;
  text-align: center;
  margin-top: 80px;
}

.top-product {
  font-size: 18px;
  color: #1890ff;
  text-align: center;
  margin-top: 80px;
  font-weight: bold;
}
</style>