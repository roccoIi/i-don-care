import React, { useEffect, useState } from "react";
import Tab from "../../components/common/Tab";
import { useLocation, useNavigate } from "react-router-dom";
import BankKeyboard from "../../components/bank/BankKeyboard";
import { selectedChildInfo } from "../../store/userStore";
import { fetchAllowanceRegist } from "../../api/bankingApi";
import { fetchAllowanceModify } from "../../api/bankingApi";
import Swal from "sweetalert2";

interface AllowanceProps {}

const Allowance: React.FC<AllowanceProps> = function Allowance() {
  const navigate = useNavigate()
  const location = useLocation()
  const accountState = location?.state
  const [amount, setAmount] = useState<string>('')
  const [type, setType] = useState<number>(0)
  const [day, setDay] = useState<number>(0)
  const [selectedDay, setSelectedDay] = useState<number>(0)
  const {selectedChild} = selectedChildInfo()

  const weekDay = ['일', '월', '화', '수', '목', '금', '토']
  const monthDay = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23, 24, 25, 26, 27, 28
  ]

  const showSuccessModal = () => {
    Swal.fire({
      title: '용돈 등록 완료!',
      text: '이제 아이 돈 케어가 용돈을 관리할게요!',
      icon: 'success',
      confirmButtonText: '확인'
    });
    navigate("/main")
  };

  useEffect(() => {
    if (accountState?.state === "update") {
      setType(accountState?.allowance?.type)
      setAmount(accountState?.allowance?.amount)
      if (accountState?.allowance?.type === 1) {
        setDay(accountState?.allowance?.day)
      } else {
        setSelectedDay(accountState?.allowance?.day-1)
      }
    }
  },[])

  useEffect(() => {
    setDay(selectedDay+1);
  }, [selectedDay])

  function handleSelectChange (event: React.ChangeEvent<HTMLSelectElement>) {
    if (type === 2) {
      setSelectedDay(parseInt(event.target.value))
    } else if (type === 1) {
      setDay(parseInt(event.target.value))
    }
  };

  function handleClick () {
    if (selectedChild) {
      if (accountState?.state === "update") {
        fetchAllowanceModify({
          relationId: selectedChild?.selectedChildRelationId,
          type: type,
          day: day,
          amount: parseInt(amount)
        })
        .then((data) => {
          if (data?.message === "SUCCESS") {
            showSuccessModal()
          }
        })
        .catch((error) => {
          console.log(error)
        })
      } else{
        fetchAllowanceRegist({
          relationId: selectedChild?.selectedChildRelationId,
          type: type,
          day: day,
          amount: parseInt(amount)
        })
        .then((data) => {
          if (data?.message === "SUCCESS") {
            showSuccessModal()
          }
        })
        .catch((error) => {
          console.log(error)
        })
      }
    }
  }

  return (
    <div className="w-full h-[92vh] py-12 px-8 flex flex-col items-center">
      <h1 className="text-2xl font-bold text-green-600">용돈 설정 페이지</h1>
      <Tab>
        <h2 className="mt-4 mb-1 text-lg text-green-600">지급 날짜</h2>
        <div className="flex items-center mb-6">
          <button className={`w-20 h-8 text-sm rounded mr-2 ${type === 2 ? "bg-green-600 text-white" : "bg-white text-gray-400"}`}
          onClick={() => setType(2)}>
            주에 1번
          </button>
          <button className={`w-20 h-8 text-sm rounded mr-4 ${type === 1 ? "bg-green-600 text-white" : "bg-white text-gray-400"}`}
          onClick={() => setType(1)}>
            달에 1번
          </button>
          {type === 2? (
            <>
            <select id="weekSelect" value={selectedDay} onChange={handleSelectChange}
            className="w-14 h-8 outline-none border border-gray-400 rounded focus:ring-2 focus:border-none focus:ring-green-400 text-sm max-h-10 overflow-y-auto">
              {weekDay.map((item, index) => (
                <option value={index} key={index}>
                  {item}
                </option>
              ))}
            </select>
            <p className="mx-2 text-gray-600">요일</p>
            </>
          ) : type === 1 ?(
            <>
            <select id="monthSelect" value={day} onChange={handleSelectChange}
            className="w-14 h-8 outline-none border border-gray-400 rounded focus:ring-2 focus:border-none focus:ring-green-400 text-sm max-h-10 overflow-y-auto">
              {monthDay.map((item, index) => (
                <option value={item} key={index}>
                  {item}
                </option>
              ))}
            </select>
            <p className="mx-2 text-gray-600">일</p>
            </>
          ) : null}
        </div>
        {type > 0 && (
          <BankKeyboard amount={amount} setAmount={setAmount} accountBalance={accountState?.accountBalance}>
            <p className={`text-lg my-2 ${!amount && "text-gray-400"}`}>
              {amount ? amount+'원' : '얼마를 보낼까요?'}
            </p>
          </BankKeyboard>
        )}
      </Tab>
      <button className={`relative -top-6 w-full h-10 text-lg rounded ${amount? "text-white bg-green-600": "text-gray-500 bg-gray-300"}`}
      disabled={!amount}
      onClick={handleClick}>
        용돈 설정하기
      </button>
    </div>
  )
}

export default Allowance;