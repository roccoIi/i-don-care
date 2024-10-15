import { axiosCommonInstance } from "./axiosInstance";

interface ApiResponse<T> {
    status: number
    message: string
    code: string
    data: T
}

interface MissionListResponse {
    relationId: number
}
export const fetchMissionList = async(missionListResponse:MissionListResponse): Promise<ApiResponse<any>> => {
    const {data} = await axiosCommonInstance.post('quest/mission/listIn', missionListResponse);
    return data;
} 