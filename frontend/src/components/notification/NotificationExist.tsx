import React, { useEffect } from "react";
import { notificationData } from "../../pages/notification/NotificationPage";
import Notification from "./Notification";

interface NotificationExistProps {
  notifications: notificationData[]
}

const NotificationExist: React.FC<NotificationExistProps> = ({ notifications }) => {
  return (
    <div className="h-3/4 overflow-y-auto">
      {notifications.map((notification) => 
        <Notification 
          key={notification.notificationId}
          notificationId={notification.notificationId}
          senderId={notification.senderId}
          senderName={notification.senderName}
          type={notification.type} />
      )}
    </div>
  )
}

export default NotificationExist
