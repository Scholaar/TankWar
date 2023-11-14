<template>
    <div class="main">
        <div class="container">
            <div class="logo">
                <Loading />
            </div>
            <el-progress :percentage="percentage" style="width: 300px;">
                {{ wsData }} / {{ maxNum }}
            </el-progress>
        </div>
    </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import Loading from '../components/Loading.vue';
import router from '../router';
import useTokenStore from '@/stores/tokenStore'

const tokenStore = useTokenStore()
const wsData = ref(0)
const maxNum = ref(5)
const percentage = ref(0)

// 在 ws 变量被实例化后执行的操作
onMounted(() => {
  // 调用某个方法，例如 initWebSocket 方法
  isReady();
});


const ws = new WebSocket('ws://localhost:8080/websocket/'+tokenStore.userName)
ws.onmessage = (event) => {
    wsData.value = JSON.parse(event.data).num
}

watch(wsData, (newVal) => {
    percentage.value = newVal / 5 * 100
    if(newVal == maxNum.value){
        router.push('/play')
    }
})

function isReady() {
    ws.onopen = () => {
        // 构造包含 type 属性的 JSON 对象
        const jsonData = { action: '-1' };
        ws.send(JSON.stringify(jsonData));
    }
}

</script>

<style lang="less" scoped>
.main{
    width: 100vw;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    .container{
        width: 80%;
        height: 60%;
        display: flex;
        flex-direction: column;
        align-items: center;
        .logo{
            width: 100%;
            height: 60%;
        }
    }
}
</style>