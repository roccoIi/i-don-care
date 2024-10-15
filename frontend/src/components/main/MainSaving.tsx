import React, { useState, useEffect } from "react";
import ProgressBar from "../common/Progressbar";
import { useNavigate } from "react-router-dom";
import { fetchSavingInfo } from "../../api/savingsApi";
import { userInfo } from "../../store/userStore";
import { selectedChildInfo } from "../../store/userStore";

type SavingData = {
  goalTitle: string,
  amount: number,
  goalAmount: number,
}

const MainSaving: React.FC = () => {
  const navigate = useNavigate()

  const goSavingsPage = () => {
    navigate('/main/savings')
  }

  const [data, setData] = useState<SavingData>()
  const { user, setUser} = userInfo()
  const { selectedChild, setSelectedChild } = selectedChildInfo()

  useEffect(() => {
    if (user?.parent) {
      fetchSavingInfo({relationId: user?.parent?.relationId})
      .then((response) => {
        console.log("MainSaving Data: ", response)
        setData(response?.data?.data)
      })
      .catch((error) => {
        console.log(error)
      })} else if (selectedChild) {
        fetchSavingInfo({relationId: selectedChild?.selectedChildRelationId})
        .then((response) => {
          console.log(response)
          setData(response?.data?.data)
        })
        .catch((error) => {
          console.log(error)
          setData(undefined)
        })
      }
  },[selectedChild])

  return (
    // 가장 바깥쪽
    <div className={`box-border bg-[#D5ED9F] rounded-lg w-full shadow-md mt-4 ${data? 'p-4' : 'p-6'}`} onClick={goSavingsPage}>
      {/* 저금통 데이터가 있다면 그래프 */}
      {data ? (
        <div>
          <div className="flex justify-between"> 
            <p>{data?.goalTitle}</p>
            <span className="material-symbols-outlined text-sm">
              arrow_forward_ios
            </span>
          </div> 

          <div className="mt-4">
            <ProgressBar currentAmount={data?.amount ?? 0} targetAmount={data?.goalAmount ?? 0} />
          </div>
        </div>
      ) : (
          <div className="flex justify-between">
            <p className="font-bold">저금을 시작해보세요</p>
            <span className="material-symbols-outlined text-sm flex items-center">
              arrow_forward_ios
            </span>
          </div>
      )}
    </div>
  )
}

export default MainSaving