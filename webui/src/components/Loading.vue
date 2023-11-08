<template>
    <div id="main"></div>
</template>

<script setup>
import * as echarts from "echarts";
import { onMounted } from "vue";

//声明周期函数，自动执行初始化
onMounted(() => {
  init();
});
//初始化函数
function init() {
  // 基于准备好的dom，初始化echarts实例
  let Chart = echarts.init(document.getElementById("main"));
  // 绘制图表
  let options = {
    graphic: {
        elements: [
        {
            type: 'text',
            left: 'center',
            top: 'center',
            style: {
            text: 'Battle City',
            fontSize: 80,
            fontWeight: 'bold',
            lineDash: [0, 200],
            lineDashOffset: 0,
            fill: 'transparent',
            stroke: '#000',
            lineWidth: 1
            },
            keyframeAnimation: {
            duration: 3000,
            loop: true,
            keyframes: [
                {
                percent: 0.7,
                style: {
                    fill: 'transparent',
                    lineDashOffset: 200,
                    lineDash: [200, 0]
                }
                },
                {
                // Stop for a while.
                percent: 0.8,
                style: {
                    fill: 'transparent'
                }
                },
                {
                percent: 1,
                style: {
                    fill: 'black'
                }
                }
            ]
            }
        }
        ]
    }
    };

    // 渲染图表
    Chart.setOption(options);
}

</script>

<style lang="less" scoped>
#main{
    width: 100%;
    height: 100%;
}
</style>