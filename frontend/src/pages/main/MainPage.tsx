import React, { useState, useEffect } from "react";
import BankAccount from "../../components/main/BankAccount";
import ChildMain from "../../components/main/ChildMain.tsx";
import ParentMain from "../../components/main/ParentMain.tsx";
import { userInfo } from "../../store/userStore";
import { selectedChildInfo } from "../../store/userStore";
import { fetchUserInfo } from "../../api/userApi";
import { useNavigate } from "react-router-dom";
import { fetchBankDetail } from "../../api/bankingApi.ts";
import { bankDetail } from "../../store/bankStore.ts";


interface MainPageProps {}

const MainPage: React.FC<MainPageProps> = function MainPage() {
  const {user, setUser} = userInfo()
  const {selectedChild, setSelectedChild} = selectedChildInfo()
  const navigate = useNavigate()
  const {setBank} = bankDetail()

  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchUserInfo()
    .then((data) => {
      console.log("User Data: ",data)
      setUser(data?.data)
    })
    .catch((error) => {
      console.log('User Error: ', error)
    })
    fetchBankDetail()
    .then((data) => {
      setBank(data?.data)
    })
    .catch((error) => {
      console.log('User Error: ', error)
    })
  },[])

  useEffect(() => {
    setLoading(false)
    if (user?.children) {
      setSelectedChild({
        selectedChildUserId: user.children[0].userId,
        selectedChildRelationId: user.children[0].relationId,
        selectedChildUserName: user.children[0].userName,
      });
    }
  },[user])

  if (loading) {
    return <div></div>;
  } else if (user) {
    return (
      <div className="flex flex-col items-center bg-gray-100 overflow-y-auto">
        <div className="w-4/5 pt-9 h-[92vh]">
            <BankAccount />
            {user?.role === "PARENT" ? <ParentMain /> : <ChildMain />}
        </div>
      </div>
    )
  }

    
}


export default MainPage