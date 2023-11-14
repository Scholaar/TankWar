import axios from 'axios';

import useTokenStore from '@/stores/tokenStore'

const token = () => {
    const tokenStore = useTokenStore()
    if(tokenStore.token === '') {
        return ''
    } else {
        return 'Bearer ' + tokenStore.token
    }
}

let request = axios.create({
    // baseURL: 'https://mock.apifox.com/m1/3545050-0-default',
    baseURL: 'http://localhost:8080',
    timeout: 8000,
});

// 请求拦截器
request.interceptors.request.use(function (config) {
    config.headers['Authorization'] = token();
    return config;
}, function (error) {
    return Promise.reject(error);
});

// 响应拦截器
request.interceptors.response.use(function (response) {
    if(response.data.mesg !== "") {
        ElMessage({
            message: response.data.mesg,
            type: 'success',
        })
    }
    return response.data.data
}, function (error) {
    if(error.response.data.mesg !== "") {
        ElMessage({
            message: error.response.data.mesg,
            type: 'error',
        })
    }
    return Promise.reject(error);
});

export default request;