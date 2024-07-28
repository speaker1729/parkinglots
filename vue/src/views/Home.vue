<template>
    <!-- 主页的根元素 -->
    <div class="home">
        <div class="greeting">
            <h1>欢迎使用本系统，{{ user.nickname }}！</h1>
        </div>
        <el-card class="card">
            <p>这里是主界面。</p>
            <p>您可以使用左侧的导航栏进入其他页面。</p>
            <p>如果您在使用过程中遇到任何问题，请联系我们。</p>
            <p>感谢您的使用！</p>
        </el-card>
    </div>
</template>

<script>
export default {
    name: 'Home',
    data() {
        return {
            // 从 localStorage 中获取用户信息
            user: JSON.parse(localStorage.getItem('user') || '{}'),
        };
    },
};
</script>

<style scoped>
/* 主页的样式 */
.home {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    /* 背景颜色使用渐变 */
    background-image: linear-gradient(to bottom right, #ff6699, #ffcc66);
}
/* 欢迎语的样式 */
.greeting {
    text-align: center;
    margin-bottom: 30px;
}
/* 内容的样式 */
.card {
    width: 35%; /* 卡片的宽度 */
    padding: 30px; /* 内间距 */
    text-align: center; /* 文字居中 */
    background-color: rgba(203, 187, 125, 0.98);
    border-radius: 5px; /* 圆角边框 */
}
/* 段落的样式 */
.card p {
    margin-bottom: 10px; /* 下边距 */
    font-size: 16px; /* 字体大小 */
    line-height: 1.5; /* 行高 */
}
</style>
