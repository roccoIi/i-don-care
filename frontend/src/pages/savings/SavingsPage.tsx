// import React, { useState } from "react";
// import SavingsPage_Child from "../../components/savings/SavingsPage_Child";
// import SavingsPage_Parents from "../../components/savings/SavingsPage_Parents";
// import { userInfo } from "../../store/userStore";
// import Tab from "../../components/common/Tab";

// interface SavingsPageProps {}

// const SavingsPage: React.FC<SavingsPageProps> = function SavingsPage() {

//   const {user, setUser} = userInfo()
//     return (
//     <div className="flex justify-center bg-gray-100">
//       <div className="w-3/4 mt-9 h-screen">
//           {user?.role === 'PARENT' ? 
//           <Tab>
//             <SavingsPage_Parents />
//           </Tab>
//           :
//           <SavingsPage_Child />
//           }
//       </div>
//     </div>
//   )
// }


// export default SavingsPage;

import React from "react";
import SavingsPage_Child from "../../components/savings/SavingsPage_Child";
import SavingsPage_Parents from "../../components/savings/SavingsPage_Parents";
import { userInfo } from "../../store/userStore";
import Tab from "../../components/common/Tab";
import { useEffect } from "react";

const SavingsPage: React.FC = function SavingsPage() {
  const { user } = userInfo();

  useEffect(()=>{
    console.log("aaaaa: ", user)
  }, [])

  return (
    <div className="flex justify-center bg-gray-100">
      <div className="w-full pt-4 h-[92vh]">
        {user?.role === 'PARENT' ? 
          <Tab>
            <SavingsPage_Parents />
          </Tab>
        :
          <SavingsPage_Child />
        }
      </div>
    </div>
  );
};

export default SavingsPage;