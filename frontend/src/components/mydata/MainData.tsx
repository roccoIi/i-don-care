import React from "react";
import { useNavigate } from "react-router-dom";
import dataImg from "../../assets/childRegister/main.png"
import Swal from "sweetalert2";
import { bankDetail } from "../../store/bankStore";
import { userInfo, selectedChildInfo } from "../../store/userStore";

interface MainDataProps {}

const MainData:React.FC<MainDataProps> = function MainData() {
  const navigate = useNavigate()
  const {bank} = bankDetail()
  const {user} = userInfo()
  const selectedChild = selectedChildInfo()

  const showWarningModal = () => {
    Swal.fire({
      title: '계좌 연동 필요',
      text: '카드를 연동하려면 계좌가 필요해요.\n계좌 연동 페이지로 이동할게요!',
      icon: 'warning',
      confirmButtonText: '확인'
    }).then((result) => {
      if (result.isConfirmed) {
        navigate("/account/link")
      }
    });
  };

  function handleClick() {
    if (bank) {
      navigate("/main/mydata")
    } else {
      showWarningModal()
    }
  }
  
  return (
    <>
      <div className="w-full relative h-[70%] my-5 bg-white rounded-lg p-4 shadow-[0_0_8px_-2px_rgba(0,0,0,0.3)]">
        <h1 className="font-bold text-lg text-gray-700">{user?.role === "PARENT"? selectedChild?.selectedChild?.selectedChildUserName + "님의 소비 점수는?" : "내 소비 점수는 몇 점일까?"}</h1>
        <div className="w-full flex justify-center">
          <img src={dataImg} alt="data_img" className="w-32 my-2" />
        </div>
        <p className="text-sm text-gray-500">카드 사용 내역을 통해, </p>
        <p className="text-sm text-gray-500">내 소비 점수와 성향을 조회해 보세요.</p>
        <button className="absolute bottom-4 w-[90%] h-10 bg-green-600 rounded-lg text-white flex items-center px-4"
        onClick={handleClick}>
          카드 연동하고 소비 분석하기
          <span className="material-symbols-outlined text-xl">
          chevron_right
          </span>
        </button>
      </div>
      <div className="w-full h-16"></div>
    </>
  )
}

export default MainData;