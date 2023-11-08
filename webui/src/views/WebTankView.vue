<template>
    <div class="main" @keydown="handleKeydown" tabindex="0">
        <div v-for="(tank, index) in tankPosition" :key="tank.name" :class="['tank', index === 0 ? 'friendly' : 'enemy']" :style="{left: `${tank.x}px`, top: `${tank.y}px`}">
            {{ tank.name }}
        </div>
        <div v-for="wall in walls" :key="wall.id" class="wall" :style="{left: `${wall.x}px`, top: `${wall.y}px`}"></div>
        <div v-for="grass in grasses" :key="grass.id" class="grass" :style="{left: `${grass.x}px`, top: `${grass.y}px`}"></div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const tankPosition = ref([
    {"x": 0, "y": 0, "name": 'tank1'},
    {"x": 50, "y": 50, "name": 'tank2'},
    {"x": 100, "y": 100, "name": 'tank3'}
])

const walls = ref([
    {
        x: 200,
        y: 200,
    }
])

const grasses = ref([
    {x: 500, y: 500, id: 1},
    {x: 500, y: 550, id: 2},
])
</script>

<style lang="less" scoped>
.tank {
    position: absolute;
    width: 50px;
    height: 50px;
    text-align: center;
    line-height: 50px;
}

.friendly {
    background-color: blue;
}

.enemy {
    background-color: red;
}

.wall {
    position: absolute;
    width: 50px;
    height: 50px;
    background-color: gray;
}

.grass {
    position: absolute;
    width: 50px;
    height: 50px;
    background-color: green;
}
</style>