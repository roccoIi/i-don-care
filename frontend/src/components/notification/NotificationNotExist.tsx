import React, { useEffect } from "react";
import { fetchNotification } from "../../api/notificationApi";


const NotificationNotExist: React.FC = () => {
  return (
    <div className="h-4/6">
      <div className="flex justify-center items-center h-full text-3xl font-bold">
        <p>알림이 없습니다.</p>
      </div>
    </div>
  )
}

export default NotificationNotExist