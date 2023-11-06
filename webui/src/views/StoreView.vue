<template>
    <div class="btn" v-if="!display">
        <p>充值前请确认你已经年满18岁</p>
        <el-button type="primary" @click="play">确定</el-button>
    </div>
    <div class="store" v-if="display">
        <div class="main">
            <div class="container">
                <div class="price item">
                    <img src="@/assets/img1.png" alt="">
                </div>
            </div>
            <div class="container"></div>
        </div>
        <img class="person" src="@/assets/416UR-Yazawa-Nico-Smile.png" alt="">
    </div>
</template>

<script setup>
import { ref } from 'vue'
const display = ref(false)


function play() {
    var musiclink = "src/assets/1.mp3";
    var audio = new Audio(musiclink);
    audio.play();
    display.value = true;
    // 30s后停止播放音乐
    setTimeout(() => {
        audio.pause();
        display.value = false;
        ElMessage({
            message: '充值超时',
            type: 'warning',
        })
    }, 30000);
}

</script>

<style lang="less">
.btn{
    width: 100vw;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.main{
    width: 100vw;
    height: 100vh;
    display: flex;
    .container{
        width: 50%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        .item{
            img{
                width: 100%;
                height: 100%;
                object-fit: cover;
            }
        }
        .price{
            animation: price-enter .6s ease-in-out forwards,
            price 1s ease-in-out 2s infinite;
        }
    }
}

.person{
    position: fixed;
    bottom: 0;
    right: 0;
    height: 100%;
    animation: person .6s ease-in-out forwards;
}

@keyframes person {
    from {
        transform: translateX(300%);
    }
    to {
        transform: translateX(0%);
    }
}

@keyframes price {
    // 无限晃动
    0% {
        transform: translate(0px, 0px);
    }
    25% {
        transform: translate(5px, 20px);
    }
    50% {
        transform: translate(-5px, -20px);
    }
    75% {
        transform: translate(-10px, 20px);
    }
    100% {
        transform: translate(0px, 0px);
    }
}

@keyframes price-enter {
    from {
        transform: translateX(-300%);
    }
    to {
        transform: translateX(0%);
    }
}
</style>