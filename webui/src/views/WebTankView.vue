<template>
    <div class="playground">
        <div id="rectangle"
        class="rectangle"
        :style="{width: resolution.width + 'px', height: resolution.height + 'px'}"
        >
            <div
            id="square"
            class="square"
            style="background: url('src/assets/tank.png');background-size: cover;"
            :style="{
                top: postionConvertY(tankPosition.myTank.y)+'px',
                left: postionConvertX(tankPosition.myTank.x)+'px',
                transform: directionHandler(tankPosition.myTank.direction),
            }"
            ></div>

            <div
            id="square"
            class="square"
            style="background: url('src/assets/tank.png');background-size: cover;background-color: red;"
            v-for="enemyTank in tankPosition.enemyTank"
            :style="{
                top: postionConvertY(enemyTank.y)+'px',
                left: postionConvertX(enemyTank.x)+'px',
                transform: directionHandler(enemyTank.direction),
            }"
            ></div>

            <div
            id="square"
            class="square"
            style="background: url('src/assets/tank.png');background-size: cover;background-color: aqua;"
            v-for="bullet in bullets"
            :style="{
                top: postionConvertY(bullet.y)+'px',
                left: postionConvertX(bullet.x)+'px',
                transform: directionHandler(bullet.direction),
            }"
            ></div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'

const tankPosition = ref({
    "myTank": {
        "x": 100,
        "y": 300,
        "direction": "right"
    },
    "enemyTank": [
        {
            "x": 200,
            "y": 200,
            "direction": "up"
        },
        {
            "x": 400,
            "y": 400,
            "direction": "down"
        },
        {
            "x": 600,
            "y": 600,
            "direction": "left"
        },
        {
            "x": 300,
            "y": 200,
            "direction": "right"
        }
    ]
})

const bullets = ref([])

const resolution = ref({
    width: 1280,
    height: 720
})

const directionHandler = (direction) => {
    switch (direction) {
        case 'up':
            return 'rotate(' + 0 + 'deg)'
        case 'down':
            return 'rotate(' + 180 + 'deg)'
        case 'left':
            return 'rotate(' + 270 + 'deg)'
        case 'right':
            return 'rotate(' + 90 + 'deg)'
    }
}

const postionConvertX = (x) => {
    // 将坐标转换为方格坐标,同时适应屏幕缩放，坐标为1920*1080
    return x * resolution.value.width / 1920
}

const postionConvertY = (y) => {
    // 将坐标转换为方格坐标,同时适应屏幕缩放，坐标为1920*1080
    return y * resolution.value.height / 1080
}

const createBullet = (x,y,direction) => {
    bullets.value.push({
        "x": x,
        "y": y,
        "direction": direction
    })
}

// 上下左右移动
window.addEventListener('keydown', (e) => {
    switch (e.key) {
        case 'ArrowUp':
            if(tankPosition.value.myTank.y <= 0) return;
            tankPosition.value.myTank.y -= 10
            tankPosition.value.myTank.direction = 'up'
            break
        case 'ArrowDown':
            if(tankPosition.value.myTank.y >= 1080-80) return;
            tankPosition.value.myTank.y += 10
            tankPosition.value.myTank.direction = 'down'
            break
        case 'ArrowLeft':
            if(tankPosition.value.myTank.x <= 0) return;
            tankPosition.value.myTank.x -= 10
            tankPosition.value.myTank.direction = 'left'
            break
        case 'ArrowRight':
            if(tankPosition.value.myTank.x >= 1920-80) return;
            tankPosition.value.myTank.x += 10
            tankPosition.value.myTank.direction = 'right'
            break
        case ' ':
            createBullet(tankPosition.value.myTank.x,tankPosition.value.myTank.y,tankPosition.value.myTank.direction)
            break
    }
})

onMounted(() => {
    // 获取窗口宽度和高度
    var w = window.innerWidth*0.9;
    var h = window.innerHeight*0.9;
    if(w/h > 16/9){
        resolution.value.width = h * 16/9
        resolution.value.height = h
    }else{
        resolution.value.width = w
        resolution.value.height = w * 9/16
    }
})

// 监听窗口变化
window.addEventListener('resize', () => {
    var w = window.innerWidth*0.9;
    var h = window.innerHeight*0.9;
    if(w/h > 16/9){
        resolution.value.width = h * 16/9
        resolution.value.height = h
    }else{
        resolution.value.width = w
        resolution.value.height = w * 9/16
    }
})

setInterval(() => {
    bullets.value.forEach((bullet,index) => {
        switch (bullet.direction) {
            case 'up':
                bullet.y -= 10
                break
            case 'down':
                bullet.y += 10
                break
            case 'left':
                bullet.x -= 10
                break
            case 'right':
                bullet.x += 10
                break
        }
        if(bullet.x < 0 || bullet.x > 1920-80 || bullet.y < 0 || bullet.y > 1080-80){
            bullets.value.splice(index,1)
        }
    })
}, 50)

// 创建websocket连接，对坦克位置和子弹位置进行赋值
// const ws = new WebSocket('ws://localhost:8080')
// ws.onopen = () => {
//     console.log('ws open')
// }
// ws.onmessage = (e) => {
//     console.log(e.data)
//     let data = JSON.parse(e.data)
//     tankPosition.value = data.tankPosition
//     bullets.value = data.bullets
// }
// ws.onclose = () => {
//     console.log('ws close')
// }
watch(tankPosition.value.myTank, (newValue, oldValue) => {
    // ws.send(JSON.stringify({
    //     "tankPosition": newValue,
    //     "bullets": bullets.value
    // }))
    // console.log(newValue)
})
</script>

<style lang="less" scoped>
.playground{
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100vw;
    height: 100vh;
    .rectangle {
        position: relative;
        background-color: #d5d5d5;
        border: 5px solid rgb(72, 255, 0);
        .square {
            position: absolute;
            width: calc(100% / 24);
            height: calc(100% / 13.5);
            box-sizing: border-box;
            border: 1px solid red;
        }
    }
}
</style>