import React, { ReactNode } from "react";
import { selectedChildInfo, userInfo } from "../../store/userStore";
import { useNavigate } from 'react-router-dom';

interface TabProps {
  children: ReactNode;
}

const Tab: React.FC<TabProps> = ({ children }) => {
  const {user, setUser} = userInfo()
  const {selectedChild, setSelectedChild} = selectedChildInfo()

  const navigate = useNavigate()
  const goChildRegisterPage = () => {
    navigate('/main/child-register')
  }

  const changeSelectedChild = (newUserId: number) => {

    const foundChild = user?.children?.find((child) => child.userId === newUserId);

    if (foundChild) {
      setSelectedChild({
        selectedChildUserId: foundChild.userId,
        selectedChildRelationId: foundChild.relationId,
        selectedChildUserName: foundChild.userName,
      });
    }
  }

  return (
    <div className="w-full h-4/5">
      <div className="mt-6 overflow-x-auto whitespace-nowrap scrollbar-hide border-b border-gray-300">

        <div className="flex space-x-4">
          {user?.children?.map((child) => (
            <button
              key={child.userName}
              // onClick={() => handleTabClick(child)}
              className={`px-4 py-2 text-sm ${
                selectedChild?.selectedChildUserId === child.userId ? 'border-b-2 border-black font-bold' : 'text-gray-500'
              }`}
              onClick={() => changeSelectedChild(child.userId)}
            >
              {child.userName}
            </button>
          ))}
          <button
            // key={child.userName}
            // onClick={() => handleTabClick(child)}
            className= "px-4 py-2 text-sm text-gray-500"
            onClick={goChildRegisterPage}
          >
            <span className="material-symbols-outlined">
              add
            </span>
          </button>
        </div>

      </div>
      <div>{ children }</div>
    </div>
  );
};

export default Tab;