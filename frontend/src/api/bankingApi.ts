import { axiosCommonInstance } from "./axiosInstance";

interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
}

interface ReadBankStatementRequest {
  accountNum: string;
}

interface SendAccountRequest {
  sendAccountNum: string;
  receiveAccountNum: string;
  amount: string;
}

interface AccountAuthRequest {
  accountNum: string;
}

interface AccountCheckRequest {
  accountNum: string;
  authNum: string;
}

interface AllowanceRegistRequest {
  relationId: number,
  type: number,
  day: number,
  amount: number
}

interface AllowanceDetailRequest {
  relationId: number
}

export const fetchReadBankStatement = async (readBankStatementRequest:ReadBankStatementRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('bank/statement', readBankStatementRequest)
  return data;
}

export const fetchBankDetail = async (): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.get('bank/detail')
  return data;
}

export const fetchSendCommon = async (sendAccountRequest: SendAccountRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.put('bank/send/comm', sendAccountRequest)
  return data;
}

export const fetchSendResent = async (): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.get('bank/send/recent')
  return data;
}

export const fetchAccountAuth = async (accountAuthRequest:AccountAuthRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('bank/auth', accountAuthRequest)
  return data;
}

export const fetchAccountCheck = async (accountCheckRequest:AccountCheckRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('bank/auth/check', accountCheckRequest)
  return data;
}

export const fetchCanDoRegisterAccount = async (): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.get('bank/inner/canRegister')
  return data;
}

export const fetchAccountAuthNumber = async (accountAuthRequest: AccountAuthRequest): Promise<ApiResponse<any>> => {
  const { data } = await axiosCommonInstance.post('bank/inner/accountHistory', accountAuthRequest);
  return data;
};

export const fetchRegistCard = async (): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('bank/regist/card')
  return data;
}

export const fetchHolder = async (holderRequest:AccountAuthRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('bank/holder', holderRequest)
  return data;
}

export const fetchAllowanceRegist = async(allowanceRegistRequest: AllowanceRegistRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('quest/allowance/regist', allowanceRegistRequest)
  return data;
}

export const fetchAllowanceDetail = async(allowanceDetailRequest: AllowanceDetailRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('quest/allowance/detail', allowanceDetailRequest)
  return data;
}

export const fetchAllowanceModify = async(allowanceModifyRequest: AllowanceRegistRequest): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.post('quest/allowance/modify', allowanceModifyRequest)
  return data;
}