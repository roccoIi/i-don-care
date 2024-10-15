import { axiosCommonInstance } from "./axiosInstance";


interface ApiResponse<T> {
  status: number
  message: string
  code: string
  data: T
}

// interface