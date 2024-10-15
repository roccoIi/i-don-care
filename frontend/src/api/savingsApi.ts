import { axiosCommonInstance } from "./axiosInstance";

interface ApiResponse<T> {
    success: boolean;
    data: T;
    message: string;
}

interface AddInterestRequest {
    coinboxId: number;
    addAmount: number;
}

interface SavingsInfoRequest {
    relationId: number
}

export const fetchAddInterest = async (addInterestRequest:AddInterestRequest): Promise<ApiResponse<any>> => {
    const {data} = await axiosCommonInstance.put('quest/savings/addI', addInterestRequest)
    return data;
}

export const fetchSavingInfo = async (savingsInfoRequest:SavingsInfoRequest): Promise<{ status: number; data: ApiResponse<any> }> => {
    const response = await  axiosCommonInstance.post('quest/savings/childDetail', savingsInfoRequest)
    return { status: response.status, data: response.data }
}