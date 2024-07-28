<template>
  <!-- 404页面的模板 -->
    <div style="overflow: hidden; height: 100vh">
        <!-- 图片元素，使用相对路径引用404图片，并设置宽高为100% -->
        <img src="../assets/img/404.png" alt="" style="width: 100%; height: 100%">
    </div>
</template>

<script>
export default {
    // 组件名为NotFound
    name: "NotFound"
}
</script>

<style>
.bgImg {
    /* 使用404图片作为背景，不重复，铺满整个容器 */
    background: url("../assets/img/404.png") no-repeat;
    background-size: 100% 100vh;
}
</style>
