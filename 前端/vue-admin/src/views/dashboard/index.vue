<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      星星博客管理系统
    </div>
    <div class="dashboard-about">
      <div class="dashboard-image">
        <img src="./introduction.jpg" style="width:60%">
      </div>
      <div class="dashboard-info">
        <div class="dashboard-title">
          <span>关于我</span>
        </div>
        <div class="dashboard-info-item">姓名：邓林莎</div>
        <div class="dashboard-info-item">性别：女</div>
        <div class="dashboard-info-item">联系方式：13989177561</div>
        <div class="dashboard-info-item">QQ号码：3510624919@qq.com</div>
        <div class="dashboard-info-item">兴趣爱好：看剧</div>
        <div class="dashboard-info-item">热爱生活</div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 0; /* 去掉外边距 */
    height: 100vh; /* 铺满整个页面 */
    background-color: #f8e7c3; /* 设置背景颜色为米色 */
    display: flex;
    flex-direction: column; /* 垂直布局 */
    align-items: center; /* 水平居中 */
    /*justify-content: center;  垂直居中 */
  }
  &-text {
    font-size: 36px; /* 调整字体大小 */
    color: #8b4513; /* 设置特别样式为棕色 */
    font-weight: bold; /* 添加粗体 */
    line-height: 1; /* 调整行距 */
    margin-bottom: 20px; /* 添加下边距 */
    margin-top: 20px; /* 添加上边距 */
  }
  &-about {
    display: flex;
    align-items: center; /* 垂直居中 */
    width: 80%; /* 设置宽度为父元素的80% */
    margin-top: 30px; /* 添加上边距 */
  }
  &-title {
    font-size: 24px; /* 调整字体大小 */
    font-weight: bold; /* 添加粗体 */
    color: #8b4513; /* 设置特别样式为棕色 */
    margin-right: 20px; /* 添加右边距 */
    margin-bottom: 10px;
  }
  &-image {
    flex: 1;
  }
  &-info {
    flex: 1;
  }
  &-info-item {
    font-size: 18px; /* 调整字体大小 */
    line-height: 30px; /* 调整行距 */
  }
}
</style>
