import React, { useState } from "react";
import ChildNotRegistered from "./ChildNotRegistered";
import ChildRegistered from "./ChildRegistered";
import { userInfo } from "../../store/userStore";

const ParentMain : React.FC = () => {
  const {user, setUser} = userInfo()
  // 자녀가 등록되어있는지 여부
  const [isRegisteredChild, setIsRegisteredChild] = useState<boolean>(!!(user?.children))

  return (
    <div className="w-full h-3/5">
      {isRegisteredChild ? <ChildRegistered /> : <ChildNotRegistered />}
    </div>
  )
}

export default ParentMain