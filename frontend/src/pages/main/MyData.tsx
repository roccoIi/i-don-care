import React, { useEffect, useState } from "react";
import SavingsGraph from "../../components/mydata/SavingsGraph";
import SavingsMBTI from "../../components/mydata/SavingsMBTI";
import { bankDetail } from "../../store/bankStore";
import { fetchSumCard, fetchScore, fetchSimilar } from "../../api/MyDataApi";
import { fetchHolder } from '../../api/bankingApi';
import { userInfo, selectedChildInfo } from "../../store/userStore";
import { fetchSavingInfo } from "../../api/savingsApi";
import { useNavigate } from "react-router-dom";

interface MyDataProps {}

interface savingsInfo {
  amount: number,
  coinboxId: number,
  goalTitle: string,
  goalAmount: number,
  interestAcount: number
}

const MyData:React.FC<MyDataProps> = function MyData () {
  const [score, setScore] = useState<number>(0)
  const {user} = userInfo()
  const {selectedChild} = selectedChildInfo()
  const {bank} = bankDetail()
  const [name, setName] = useState<string>('')
  const [consume, setConsume] = useState<number>(0)
  const [relationId, setRelationId] = useState<number>(0)
  const [saving, setSaving] = useState<savingsInfo>()
  const navigate = useNavigate()

  useEffect(() => {
    if (bank && user?.role === "CHILD" && user?.parent?.relationId) {
      fetchSumCard({relation_id: user?.parent?.relationId})
      .then((data) => {
        setConsume(data?.total_balance)
      })
      .catch((error) => {
        console.log(error)
      })
      console.log(user)
      fetchHolder({accountNum: bank?.accountNo})
      .then((data) => {
        setName(data?.data)
      })
      .catch((error) => {
        console.log(error)
      })
      fetchScore({relation_id: user?.parent?.relationId})
      .then((data) => {
        setScore(data)
      })
      .catch((error) => {
        console.log(error)
      })
      fetchSimilar({relation_id: user?.parent?.relationId})
      .then((data) => {
        setRelationId(data)
      })
      .catch((error) => {
        console.log(error)
      })
    } else if (bank && user?.role === "PARENT" && selectedChild) {
      setName(selectedChild?.selectedChildUserName)
      fetchSumCard({relation_id: selectedChild?.selectedChildRelationId})
      .then((data) => {
        setConsume(data?.total_balance)
      })
      .catch((error) => {
        console.log(error)
      })
      fetchScore({relation_id: selectedChild?.selectedChildRelationId})
      .then((data) => {
        setScore(data)
      })
      .catch((error) => {
        console.log(error)
      })
      fetchSimilar({relation_id: selectedChild?.selectedChildRelationId})
      .then((data) => {
        setRelationId(data)
      })
      .catch((error) => {
        console.log(error)
      })
    }
  },[])

  useEffect(() => {
    if (relationId > 0) {
      fetchSavingInfo({relationId: relationId})
      .then((data) => {
        setSaving(data?.data?.data)
      })
      .catch((error) => {
        console.log(error)
      })
    }
  }, [relationId])

  return (
    <div className="w-full h-[92vh] flex flex-col items-center overflow-y-auto">
      <div className="w-full h-1/4 bg-green-600 flex flex-col items-center justify-center">
        <SavingsGraph score={score} />
        <h1 className="text-lg text-white">{name}님의 소비 점수</h1>
      </div>
      <div className="w-full h-3/4 flex flex-col items-center">
        <div className="w-5/6 h-[100px] bg-white mt-4 rounded-lg shadow-[0_0_8px_-2px_rgba(0,0,0,0.3)] p-4">
          <p className="text-gray-700">한 달 용돈 소비금액</p>
          <div className="flex items-center w-full relative my-3">
            <hr className="w-[250px] border-4 rounded absolute" />
            <hr className="border-4 rounded absolute border-green-600"
            style={{'width': consume/300000 >= 1? "250px" :`${(consume/300000*100).toFixed(0) * 2.5}px`}} />
          </div>
          <p className="text-gray-400 text-sm">{consume.toLocaleString()} / 300,000원 <span className="text-green-600">({(consume/300000*100).toFixed(1)}%)</span></p>
        </div>
        <div className="w-5/6 h-1/2 bg-white mt-4 rounded-lg shadow-[0_0_8px_-2px_rgba(0,0,0,0.3)] p-4">
          <h1 className="text-gray-700">{name}님의 소비 MBTI</h1>
          <SavingsMBTI />
          <div className="w-full border-2 border-green-600 h-3/4 bg-white p-4 my-2 rounded-lg shadow-[0_0_8px_-2px_rgba(0,0,0,0.3)">
            <h1 className="text-base text-green-600">{name}님의 맞춤 소비계획💪</h1>
            <hr className="my-2 border-gray-400" />
            <h1>제목: {saving?.goalTitle}</h1>
            <p>목표 금액: {saving?.goalAmount}원💸</p>
            <button className="mt-1 w-full h-10 bg-green-600 text-white rounded"
            onClick={() => navigate('/main/savings')}>
              내 취향 소비계획 만들기
            </button>
          </div>
          
        <div className="w-5/6 h-28"></div>
        </div>
      </div>
    </div>
  )
}

export default MyData;