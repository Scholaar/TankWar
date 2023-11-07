<template>
    <main>
        <h1>404</h1>
        <p>页面不存在，将在 {{ count }} 秒后返回首页</p>
    </main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import router from '@/router'

const count = ref(5)
onMounted(() => {
    const timer = setInterval(() => {
        count.value--
        if (count.value === 0) {
            clearInterval(timer)
            router.push('/')
        }
    }, 1000)
})

</script>

<style lang="less" scoped>
main{
    height: 100vh;
    width: 100vw;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
}
</style>