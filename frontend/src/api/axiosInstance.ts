import axios from 'axios';
// import { encryptDataWithRSA } from '../utils/cryptoUtils';

const BASE_URL = import.meta.env.VITE_BASE_URL;

const axiosRequestConfig = {
  baseURL: BASE_URL,
  withCredentials: true,
  credentials: 'include'
};

console.log(BASE_URL)

export const axiosCommonInstance = axios.create(axiosRequestConfig);

export const refreshAccessToken = async () => {
  try {
    const response = await axios.post(`${BASE_URL}user/reissue`, {}, {
      withCredentials: true,
    });
    console.log(response)
    const newAccessToken = response.data.accessToken;
    localStorage.setItem("ACCESS_TOKEN", newAccessToken);
    return newAccessToken;
  } catch (error) {
    console.log(error)
    console.error("Failed to refresh access token:", error);
    localStorage.removeItem("ACCESS_TOKEN");
    document.cookie = "refresh=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    throw error;
  }
};

//엑세스 토큰 체크 로직
axiosCommonInstance.interceptors.request.use(
  async (config) => {
    const token = localStorage.getItem("ACCESS_TOKEN");
    if (token) {
      config.headers.Authorization = token;
    }

    if (!config.headers['Content-Type']) {
      config.headers['Content-Type'] = 'application/json';
    }

    // config.method가 정의되어 있는지 확인
    // if (config.data && config.method && ['POST', 'PUT', 'PATCH'].includes(config.method.toUpperCase())) {
    //   const encryptedData = await encryptDataWithRSA(JSON.stringify(config.data));
    //   config.data = { encrypted: encryptedData };
    // }
    console.log(config.data)
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axiosCommonInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      window.location.href = '/';
      const newAccessToken = await refreshAccessToken();
      
      if (newAccessToken) {
        console.log("accessToken 재발급 받음")
        console.log("새 accessToken")
        console.log(newAccessToken)
        localStorage.setItem("ACCESS_TOKEN",newAccessToken);
        originalRequest.headers.Authorization = newAccessToken;
        return axiosCommonInstance(originalRequest);
      }
    }
    
    return Promise.reject(error);
  }
);
