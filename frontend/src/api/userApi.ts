import { axiosCommonInstance } from "./axiosInstance";

interface ApiResponse<T> {
  status: number
  message: string
  code: string
  data: T
}

export const fetchUserInfo = async (): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.get('user/relation')
  return data;
}

interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
}

interface SignupRequest {
  tel: string;
  birth: string;
  userName: string;
}

interface SignupRoleRequest {
  role: string;
  userId: number;
}

interface IntegrateSendRequest {
  tel: string;
}

interface IntegrateVerifyRequest {
  tel: string;
  code: string;
}

interface SimpleRequest {
  password: string;
  userId: number;
}

interface SimpleLoginRequest {
  password: string;
}

export const fetchSignupForParentTest = async (): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('user/integrate/signup/parent-test')
  return data;
}

export const fetchSignupForChildTest = async (): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('user/integrate/signup/child-test')
  return data;
}

export const fetchSignup = async (signupRequest:SignupRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('user/integrate/signup', signupRequest)
  return data;
}

export const fetchSignupRole = async (signupRoleRequest:SignupRoleRequest): Promise<{ status: number; data: ApiResponse<any> }> => {
  const response = await axiosCommonInstance.post('user/integrate/signup/role', signupRoleRequest)
  return { status: response.status, data: response.data };
}

export const fetchIntegrateSend = async (integrateSendRequest: IntegrateSendRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('user/integrate/send', integrateSendRequest)
  return data;
}

export const fetchIntegrateVerify = async (integrateVerifyRequest: IntegrateVerifyRequest): Promise<{ status: number; data: ApiResponse<any> }> => {
  const response = await axiosCommonInstance.post('user/integrate/verify', integrateVerifyRequest)
  return { status: response.status, data: response.data };
}

export const fetchSimple = async (SimpleRequest: SimpleRequest): Promise<{ status: number; data: ApiResponse<any> }> => {
  const response = await axiosCommonInstance.post('user/simple', SimpleRequest)
  return { status: response.status, data: response.data };
}

export const updateSimple = async (SimpleRequest: SimpleRequest): Promise<{ status: number; data: ApiResponse<any> }> => {
  const response = await axiosCommonInstance.patch('user/simple', SimpleRequest)
  return { status: response.status, data: response.data };
}

// export const fetchSimpleLogin = async (SimpleLoginRequest: SimpleLoginRequest): Promise<ApiResponse<any>> => {
//   const {data} = await axiosCommonInstance.post('user/simple/signin', SimpleLoginRequest)
//   return data;
// }

export const fetchSimpleLogin = async (SimpleLoginRequest: SimpleLoginRequest) => {
  const response = await axiosCommonInstance.post('user/simple/signin', SimpleLoginRequest);
  return response;
};

export const fetchLogout = async (): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('user/integrate/logout')
  return data;
};
