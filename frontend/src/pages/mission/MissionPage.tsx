import React from "react";
import MissionParent from "../../components/mission/MissionParent";
import MissionChild from "../../components/mission/MissionChild";
import { userInfo } from "../../store/userStore";
import Tab from "../../components/common/Tab";

const MissionPage: React.FC = function MissionPage() {
  const { user } = userInfo();

  return (
    <div className="flex justify-center bg-gray-100">
      <div className="w-full pt-4 h-[92vh]">
        {user?.role === 'PARENT' ? 
          <Tab>
            <MissionParent />
          </Tab>
        :
          <MissionChild />
        }
      </div>
    
    </div>
  );
};

export default MissionPage;