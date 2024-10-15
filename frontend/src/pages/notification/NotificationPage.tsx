import React, { useState, useEffect } from "react";
import { fetchNotification } from "../../api/notificationApi";
import NotificationExist from "../../components/notification/NotificationExist";
import NotificationNotExist from "../../components/notification/NotificationNotExist";

export type notificationData = {
  notificationId: number,
  senderId: number,
  senderName:string,
  type: string,
}

const NotificationPage: React.FC = function NotificationPage() {

  const getNotificationData = async () => {
    try {
      const response = await fetchNotification()
      const data = await response.data
      if (data) {
        setNotificationsData(data)
      }
      console.log("Notification Data: ", data)
    } catch (error) {
      console.log("Notification Error: ", error)
    }
  }

  const [notificationsData, setNotificationsData] = useState<notificationData[]>([])

  // const mockupData : notificationData[] = [
  //   {
  //     notificationId: 1,
  //     senderId: 1,
  //     senderName: '강신구',
  //     type: 'REQUEST_RELATION',
  //   },
  //   {
  //     notificationId: 2,
  //     senderId: 2,
  //     senderName: '황정현',
  //     type: 'COINBOX_START',
  //   },
  //   {
  //     notificationId: 3,
  //     senderId: 3,
  //     senderName: '최소현',
  //     type: 'MISSION_SUCCESS',
  //   },
  //   {
  //     notificationId: 4,
  //     senderId: 4,
  //     senderName: '이지흔',
  //     type: 'COINBOX_SUCCESS',
  //   },
  //   {
  //     notificationId: 5,
  //     senderId: 5,
  //     senderName: '주소영',
  //     type: 'QUIZ_SUCCESS',
  //   },
  //   {
  //     notificationId: 5,
  //     senderId: 5,
  //     senderName: '주소영',
  //     type: 'LESS_BALANCE',
  //   },
  //   {
  //     notificationId: 5,
  //     senderId: 5,
  //     senderName: '주소영',
  //     type: 'LESS_BALANCE',
  //   },
  // ]

  useEffect(() => {
    getNotificationData()
    // setNotificationsData(mockupData)
  }, [])

  return (
    <div className="flex justify-center bg-gray-100">
      <div className="w-3/4 pt-9 h-[92vh]">
      
        <div className="text-4xl font-semibold border-b border-[#0BAE58] py-3 text-[#0BAE58]">
          <p>알림</p>
        </div>
        {notificationsData?.length !== 0 ? (<NotificationExist notifications={notificationsData} />) : (<NotificationNotExist />)}

      </div>
    </div>
  )
}
export default NotificationPage