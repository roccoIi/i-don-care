import React, { useEffect, useState } from "react";
import { selectedChildInfo } from "../../store/userStore";
import { fetchAllowanceDetail, fetchBankDetail } from "../../api/bankingApi";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

interface ChildAllowanceProps {}

interface AllowanceInfo {
  amount: number,
  day: number,
  relationId: number,
  type: number
}

interface weekdayInfo {
  pk: number,
  day: string
}

const ChildAllowance: React.FC<ChildAllowanceProps> = function ChildAllowance() {
  const {selectedChild} = selectedChildInfo()
  const [isAllowance, setIsAllowance] = useState(false)
  const navigate = useNavigate()
  const [allowance, setAllowance] = useState<AllowanceInfo>()
  const [day, setDay] = useState<weekdayInfo>()
  const [isAccount, setIsAccount] = useState<boolean>(false)
  const [accountBalance, setAccountBalance] = useState<number>(0)

  const showWarningModal = () => {
    Swal.fire({
      title: '계좌 미등록 사용자',
      text: '계좌 연동 후 사용 가능한 기능입니다.',
      icon: 'warning',
      confirmButtonText: '확인'
    });
  };

  const weekday: weekdayInfo[] = [
    { pk: 1, day: "일" },
    { pk: 2, day: "월" },
    { pk: 3, day: "화" },
    { pk: 4, day: "수" },
    { pk: 5, day: "목" },
    { pk: 6, day: "금" },
    { pk: 7, day: "토" },
  ]

  useEffect(() => {
    if (selectedChild) {
      fetchAllowanceDetail({relationId: selectedChild?.selectedChildRelationId})
      .then((data) => {
        setAllowance(data?.data)
        setIsAllowance(true)
        
      })
      .catch((error) => {
        if (error?.response?.data?.code === "Q008") {
          setIsAllowance(false)
        }
      })
    }
    fetchBankDetail()
    .then((data) => {
      setAccountBalance(data?.data?.accountBalance)
      setIsAccount(true)
    })
    .catch((error) => {
      setIsAccount(false)
    })
  }, [])

  useEffect(() => {
    if (allowance) {
      setDay(weekday.find(item => item.pk === allowance?.day))
    }
  },[allowance])

  function handleClick () {
    if (isAllowance) {
      if (isAccount) {
        navigate('/main/allowance', {state: {state: "update", accountBalance: accountBalance, allowance: allowance}})
      } else {
        showWarningModal()
      }
    } else {
      if (isAccount) {
        navigate('/main/allowance', {state: {state: "create", accountBalance: accountBalance}})
      } else {
        showWarningModal()
      }
    }
  }

  if (isAllowance)
  return (
    <div className="my-4 w-full h-20 bg-white shadow-md text-sm rounded-lg p-4 relative">
      <h2 className="text-sm font-bold">{selectedChild?.selectedChildUserName}님의 용돈 💸</h2>
      <p className="text-sm text-gray-600 my-1">
        {allowance?.type === 1 ? "매달" : allowance?.type === 2 ? "매주" : null} {allowance?.type === 1 ? allowance?.day : allowance?.type === 2 ? day?.day : null}
        {allowance?.type === 1 ? "일" : allowance?.type === 2 ? "요일" : null},&nbsp;
        <span className="text-green-600">{allowance?.amount}원씩 지급 중!</span>
      </p>
      <button className="absolute top-4 right-4 bg-gray-400 w-12 text-xs h-5 text-white rounded flex items-center justify-center"
      onClick={handleClick}>
        수정
        <span className="material-symbols-outlined text-sm">
        edit
        </span>
      </button>
    </div>
  )

  else
  return (
    <div className="my-4 w-full h-12 bg-white shadow-md text-sm text-gray-700 rounded-lg flex items-center justify-between px-6"
    onClick={handleClick}>
      {selectedChild?.selectedChildUserName}님에게 용돈 설정하기
      <span className="material-symbols-outlined text-2xl">
      chevron_right
      </span>
    </div>
  )
}

export default ChildAllowance;