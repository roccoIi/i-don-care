import { axiosCommonInstance } from "./axiosInstance";

interface ApiResponse<T> {
  status: number
  message: string
  code: string
  data: T
}

interface NotificationId {
  notificationId: number
}

export const fetchNotification = async(): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.get('user/notification')
  return data;
}

export const fetchNotificationCount = async(): Promise<ApiResponse<any>> => {
  const {data} = await axiosCommonInstance.get('user/notification/count')
  return data;
}

export const setNotificationAsRead = async(notificationId:NotificationId): Promise<ApiResponse<any>>  => {
  try {
    const { data } = await axiosCommonInstance.post('user/notification', notificationId);
    return data;
  } catch (error) {
    console.error('Error marking notification as read:', error);
    throw error
  }
}