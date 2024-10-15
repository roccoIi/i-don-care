import { axiosCommonInstance } from "./axiosInstance";


interface ApiResponse<T> {
  status: number
  message: string
  code: string
  data: T
}

interface RegisterChildRequest {
  tel: string,
}

interface RegisterChildResponse {
  notificationId: number,
  isAccept: boolean,
}


export const registerChildRequest = async (registerChildRequest: RegisterChildRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('user/relation/register', registerChildRequest)
  return data;
}

export const registerChildResponse = async (registerChildResponse: RegisterChildResponse): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('user/relation/response', registerChildResponse)
  return data;
}