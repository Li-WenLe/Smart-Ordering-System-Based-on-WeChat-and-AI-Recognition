// pages/taste-select/taste-select.js
Page({
  data: {
    tasteList: [
      { id: 1, name: '麻辣', selected: false },
      { id: 2, name: '酸甜', selected: false },
      { id: 3, name: '清淡', selected: false },
      { id: 4, name: '香辣', selected: false },
      { id: 5, name: '五香', selected: false },
      { id: 6, name: '咖喱', selected: false },
      { id: 7, name: '蒜香', selected: false },
      { id: 8, name: '孜然', selected: false },
      { id: 9, name: '黑椒', selected: false },
      { id: 10, name: '酱香', selected: false },
    ]
  },

  // 切换标签选中状态
  toggleTag(e) {
    const id = e.currentTarget.dataset.id
    this.setData({
      tasteList: this.data.tasteList.map(item => {
        if (item.id === id) {
          return { ...item, selected: !item.selected }
        }
        return item
      })
    })
  },

  // 保存选择
  saveSelection() {
    const selectedTastes = this.data.tasteList
      .filter(item => item.selected)
      .map(item => item.name)
    
    if (selectedTastes.length === 0) {
      wx.showToast({ title: '请至少选择一个口味', icon: 'none' })
      return
    }
    
    wx.showToast({
      title: '保存成功',
      icon: 'success',
      success() {
        setTimeout(() => wx.navigateBack(), 1500)
      }
    })
    
    // 实际使用时可在此处调用接口保存数据
    console.log('已选择的口味:', selectedTastes)
  }
})