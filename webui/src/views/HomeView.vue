<template>
  <div class="main" :style="{'background': 'url('+background+')'}">
      <div class="play-main" v-if="loginStatus" @click="play">
          <div class="play-main-container">
              <img src="@/assets/logo.png" alt="">
              <div><span>点击进入游戏</span></div>
          </div>
      </div>
      <div class="auth" v-if="!loginStatus">
          <el-form
          label-position="top"
          label-width="100px"
          :model="loginForm"
          style="max-width: 350px;margin: 0 auto;padding-left: 10px;padding-right: 10px;"
          >
              <el-form-item label="用户名">
                  <el-input v-model="loginForm.name" />
              </el-form-item>
              <el-form-item label="邮箱" v-if="!formStatus.isLogin">
                  <el-input v-model="loginForm.email" />
              </el-form-item>
              <el-form-item label="密码">
                  <el-input type="password" v-model="loginForm.password" />
              </el-form-item>
              <el-form-item label="重复密码" v-if="!formStatus.isLogin">
                  <el-input type="password" v-model="loginForm.passwordRepeat" />
              </el-form-item>
              <el-form-item>
                  <el-button round @click="switchFormStatus">{{ formStatus.actionText }}</el-button>
                  <div style="flex: 1;"></div>
                  <el-button type="primary" round @click="loginStatus = !loginStatus">{{ formStatus.submitText }}</el-button>
              </el-form-item>
              <!-- 如需重置密码功能，完成此部分 -->
              <!-- <el-form-item>
                  <div style="flex: 1;text-align: center;">
                      <el-link type="info" @click="resetpwd">重置密码</el-link>
                  </div>
              </el-form-item> -->
          </el-form>
      </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import router from '@/router'

// static data
const background = new URL('@/assets/bg.png', import.meta.url)

const loginStatus = ref(false)

const loginForm = ref({
  name: '',
  email: '',
  password: '',
  passwordRepeat: ''
})

const formStatus = ref({
  isLogin: true,
  actionText: '注册',
  submitText: '登录'
})

const switchFormStatus = () => {
  formStatus.value.isLogin = !formStatus.value.isLogin;
  formStatus.value.actionText = formStatus.value.isLogin ? '注册' : '回到登录';
  formStatus.value.submitText = formStatus.value.isLogin ? '登录' : '注册';
}

// 功能暂定
// const resetpwd = () => {
//     ElMessageBox.prompt('请输入注册时的email', '重置密码', {
//         confirmButtonText: '确认',
//         cancelButtonText: '取消',
//         inputPattern:
//         /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
//         inputErrorMessage: '无效输入',
//     })
//     .then(({ value }) => {
//       ElMessage({
//         type: 'success',
//         message: `Your email is:${value}`,
//       })
//     })
//     .catch(() => {})
// }

const play = () => {
  router.push('/playroom')
}
</script>

<style lang="less" scoped>
.main{
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  .auth{
      width: 430px;
      border-radius: 15px;
      padding-top: 40px;
      padding-bottom: 40px;
      background-color: rgba(255, 255, 255, 0.092);
      backdrop-filter: blur(10px);
      box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.2);
  }
  .play-main{
      height: 100%;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      .play-main-container{
          width: 500px;
          height: 60%;
          padding: 20px;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          align-items: center;
          animation: enter-scale 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
          img{
              width: 100%;
          }
          div{
              span{
                  font-size: 20px;
                  font-weight: bold;
              }
          }
      }
  }
}

@keyframes enter-scale {
  0% {
      transform: scale(0.6);
  }
  100% {
      transform: scale(1);
  }
}
</style>