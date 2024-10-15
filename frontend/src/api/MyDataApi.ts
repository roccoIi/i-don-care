import axios from 'axios';

const axiosRequestConfig = {
  baseURL: 'https://j11a603.p.ssafy.io/data/',
  withCredentials: true,
  credentials: 'include'
};

const axiosDataInstance = axios.create(axiosRequestConfig)

interface ApiResponse<T> {
  status: number
  message: string
  code: string
  data: T
}

interface SumCardRequest {
  relation_id: number
}

export const fetchSumCard = async (sumCardRequest:SumCardRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosDataInstance.post('sum_card', sumCardRequest)
  return data;
}

export const fetchSimilarity = async (sumCardRequest:SumCardRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosDataInstance.post('similarity', sumCardRequest)
  return data;
}

export const fetchCategory = async (sumCardRequest:SumCardRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosDataInstance.post('category_sum', sumCardRequest)
  return data;
}

export const fetchScore = async (scoreRequest:SumCardRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosDataInstance.post('saving_rate', scoreRequest)
  return data;
}

export const fetchSimilar = async (similarRequest:SumCardRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosDataInstance.post('similarity', similarRequest)
  return data;
}
