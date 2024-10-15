import React, { useState, useEffect } from 'react';
import ProgressBar from '../common/Progressbar';
import { axiosCommonInstance } from '../../api/axiosInstance';
import Modal from './SavingsConfirmModal';
import { useNavigate } from 'react-router-dom';
import { userInfo } from '../../store/userStore';
import Swal from 'sweetalert2';

interface SavingsPageList_ChildProps {

}

const SavingsPageList_Child: React.FC<SavingsPageList_ChildProps> = () => {
  const [showForm, setShowForm] = useState<boolean>(false);  // 폼 전환 상태 관리
  const [chargeAmount, setChargeAmount] = useState<number>(0);
  const [goalTitle, setGoalTitle] = useState<string>('');  // 목표 제목 상태
  const [currentAmount, setCurrentAmount] = useState<number>(0);  // 현재 모은 금액 상태
  const [goalAmount, setGoalAmount] = useState<number>(0);  // 목표 금액 상태
  const [errorMessage, setErrorMessage] = useState<string>('');
  // const [successMessage, setSuccessMessage] = useState<string>('');
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [isErrorModalOpen, setIsErrorModalOpen] = useState<boolean>(false);
  const [coinboxId, setCoinboxId] = useState<number>(-1);
  const navigate = useNavigate();
  const {user} = userInfo();

  useEffect(() => {
    // 백엔드에서 저장된 목표 정보 불러오기
    const fetchSavingsGoal = async () => {
      try {
        // const response = await axiosCommonInstance.post('savings/childDetail', {
        const response = await axiosCommonInstance.post('quest/savings/childDetail', {
          relationId: user?.parent?.relationId
        });
        const data = response.data.data;
        console.log("Data===============",data); // 데이터 확인

        setGoalTitle(data?.goalTitle);  // 목표 제목 설정
        setCurrentAmount(data?.amount);  // 현재 모은 금액 설정
        setGoalAmount(data?.goalAmount);  // 목표 금액 설정
        setCoinboxId(data?.coinboxId);
        // console.log("goalTitle============",goalTitle);
        // console.log("currentAmount============",currentAmount);
        // console.log("goalAmount============",goalAmount);
        // console.log("coinboxId============",coinboxId);
      } catch (error) {
        console.error('목표 정보 불러오기 오류:', error);
      }
    };

    fetchSavingsGoal();
  }, []);

  useEffect(() => {
    if (coinboxId > 0) {

      console.log('찐찐막')
      console.log("goalTitle============",goalTitle);
      console.log("currentAmount============",currentAmount);
      console.log("goalAmount============",goalAmount);
      console.log("coinboxId============",coinboxId);
    }
  }, [coinboxId])

  //저금 목표 성공
  useEffect(() => {
    
    if (currentAmount >= goalAmount && goalAmount !== 0){
      showSuccessModal()
    }
  }, [currentAmount])

  const toggleForm = () => {
    setShowForm(true);
  };

  const openModal = () => {
    setIsModalOpen(true);
  }

  const closeModal = () => {
    setIsModalOpen(false);
  }

  const goSavings = () => {
    navigate('/main')
  }


  const handleCancel = async () => {
    try {
      // console.log(coinboxId);
      // const response = await axiosCommonInstance.put('savings/cancel', {
      const response = await axiosCommonInstance.put('quest/savings/cancel', {
        coinboxId: coinboxId });  // 목표 취소 API 호출
      console.log("목표 취소 성공:", response.data);
      // setGoalTitle("");
      // setCurrentAmount(0);
      // setGoalAmount(0);
      closeModal();
      // setTimeout(() => {
      //   // goSavings();  // 모달이 닫힌 후 페이지 이동
      window.location.reload()
      // }, 300);
      
    } catch (error) {
      console.error("목표 취소 오류:", error);
      setErrorMessage("목표 취소 중 오류가 발생했습니다. 다시 시도해주세요.");
      setIsErrorModalOpen(true);
      closeModal();
      // alert("목표 취소 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (chargeAmount <= 0) {
      setErrorMessage("충전 금액을 입력해주세요.");
      setIsErrorModalOpen(true);
    } else {
      axiosCommonInstance.put('quest/savings/addA', {
          coinboxId: coinboxId,
          addAmount: chargeAmount
        })
        .then((data)=>{
          console.log("충전 성공:", data);
          setErrorMessage("");  // 오류 메시지 초기화
          setCurrentAmount((prev) => prev + chargeAmount);
          setShowForm(false);
        })
        .catch ((error) => {
        console.error("충전 오류:", error);
        if (error.response.data.code === "Q007") {
          setErrorMessage("잔액이 부족하여 충전에 실패하였습니다.");
        } else {
          setErrorMessage("충전 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
        setIsErrorModalOpen(true);
      })
    }
  };

  // const handleSubmit = async (e: React.FormEvent) => {
  //   e.preventDefault();

  //   if (chargeAmount <= 0) {
  //     setErrorMessage("충전 금액을 입력해주세요.");
  //     setIsErrorModalOpen(true);
  //   } else {
  //     try {
  //       // const response = await axiosCommonInstance.put('savings/addA', {
  //       const response = await axiosCommonInstance.put('quest/savings/addA', {
  //         coinboxId: coinboxId,
  //         addAmount: chargeAmount
  //       });

  //       console.log("충전 성공:", response.data);
  //       setErrorMessage("");  // 오류 메시지 초기화
  //       setCurrentAmount((prev) => prev + chargeAmount);
  //       setShowForm(false);
  //     } catch (error) {
  //       console.error("충전 오류:", error);
  //       setErrorMessage("충전 중 오류가 발생했습니다. 다시 시도해주세요.");
  //       setIsErrorModalOpen(true);
  //     }
  //   }
  // };

  const closeErrorModal = () => {
    setIsErrorModalOpen(false);
  }

  const showSuccessModal = () => {
    Swal.fire({
      title: '저금통 완성!',
      text: '목표를 달성했어요. 달성금을 드릴게요!',
      icon: 'success',
      confirmButtonText: '확인'
    }).then((result) => {
      if (result.isConfirmed) {
        navigate(0)
      }
    });
  };

  return (
    <div className="bg-gray-100 min-h-screen flex flex-col items-center">
      <div className='w-3/4 max-w-md text-center'>
        <h1 className="text-2xl font-bold text-green-500 mb-4">{goalTitle}</h1>
      </div>

      <div className="border-2 border-green-500 bg-white shadow-lg rounded-lg p-8 w-3/4 max-w-md">
        {/* 충전하기 버튼 누르기 전 */}
        {!showForm && (
          <div>
            <p className="text-center text-lg mb-4">
              지금까지 <span className="font-bold text-green-500">{currentAmount}원</span> 모았어요.
            </p>

            {/* 진행률 표시 (Progress Bar) */}
            <ProgressBar currentAmount={currentAmount} targetAmount={goalAmount} />

            <button 
              className = "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded w-full my-4"
              onClick={openModal}  // 목표 취소 핸들러

            >
              그만하기
            </button>
            <button
              className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded w-full"
              onClick={toggleForm}  // 충전 폼으로 전환
            >
              충전하기
            </button>
          </div>
        )}

        {/* 충전하기 폼 (충전하기 버튼을 누르면 보여짐) */}
        {showForm && (
          <form onSubmit={handleSubmit}>
            <h2 className="text-l font-bold text-green-500 mb-4 text-center">얼마나 충전하시겠습니까?</h2>
            <input
              type="number"
              value={chargeAmount === 0 ? '' : chargeAmount}
              onChange={(e) => setChargeAmount(e.target.value ? parseInt(e.target.value) : 0)}
              placeholder="충전 금액을 입력하세요"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline mb-4"
              min="0"
            />

            {errorMessage && (
              <p className="text-red-500 text-sm text-center mb-4">{errorMessage}</p>
              )}

            <p className="text-gray-500 text-xs text-center">
              목표를 달성하지 못하면 이자를 못 받고 모았던 원금을 돌려받아요.
            </p>
            <button
              type="submit"
              className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded w-full mt-4"
            >
              넣기
            </button>
          </form>
        )}
      </div>
      {/* 목표 취소 확인 모달 */}
      <Modal
        isOpen={isModalOpen}
        onClose={closeModal}
        message="정말 그만하시겠습니까?"
      >
        <button
          className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mt-4"
          onClick={handleCancel}  // 목표 취소 함수 실행
        >
          확인
        </button>
      </Modal>

      {/* 에러 메시지 모달 */}
      <Modal
        isOpen={isErrorModalOpen}
        onClose={() => setIsErrorModalOpen(false)}
        message={errorMessage}
      />
    </div>
  );
};

export default SavingsPageList_Child;