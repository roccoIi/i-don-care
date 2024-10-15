import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import Modal from './NoRegisterModal';
import { axiosCommonInstance } from "../../api/axiosInstance";
import { userInfo } from '../../store/userStore';

const SavingsPageRegist_Child: React.FC = () => {
  const [showForm, setShowForm] = useState<boolean>(false);
  const [goalTitle, setGoalTitle] = useState<string>('');
  const [goalAmount, setGoalAmount] = useState<string>(''); // 금액은 string으로 시작해서 숫자 변환
  const [message, setMessage] = useState<string>('');  // 사용자 메시지
  const [errorMessage1, setErrorMessage1] = useState<string>(''); // 에러 메시지 상태 추가
  const [errorMessage2, setErrorMessage2] = useState<string>(''); // 에러 메시지 상태 추가
  const [isSavingsRegister, setSavingsRegister] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSubmitDisabled, setIsSubmitDisabled] = useState<boolean>(true);  // 폼 제출 버튼 비활성화 여부
  const navigate = useNavigate();
  const [isComposing, setIsComposing] = useState<boolean>(false); // 한글 입력 중인지 상태 관리
  const { user }= userInfo();

  const toggleForm = () => {
    setShowForm(true);
  };


  // 목표 이름 유효성 검사 핸들러
  const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    // if (isComposing) return; // 한글 입력 중이면 유효성 검사를 하지 않음

    const value = e.target.value;

    if (/^[A-Za-zㄱ-힣0-9]*$/.test(value) || value === '') {
      setGoalTitle(value);
      setErrorMessage1(''); // 정상적인 입력은 오류 메시지 제거
    } else {
      setErrorMessage1("목표 이름은 문자만 입력할 수 있습니다.");
    }

    validateForm(value, goalAmount); // 폼 유효성 검사
  };
  

  // 목표 금액 유효성 검사 핸들러
  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.currentTarget.value;
    // Const isvalid = /^[0-9]*$/.test(value)
    // 목표 금액은 숫자만 허용
    console.log(value);
    if (/^\d*$/.test(value) || value === '') {
      setGoalAmount(value); // 빈 값이면 빈 문자열, 아니면 숫자로 변환
      setErrorMessage2(''); // 정상적인 입력은 오류 메시지 제거
    } else {
      setErrorMessage2('목표 금액은 숫자만 입력할 수 있습니다.');
    }
  
    validateForm(goalTitle, value); // 폼 유효성 검사
  };

  // 폼 유효성 검사 함수
  const validateForm = (title: string, amount: string | number) => {
    const parsedAmount = typeof amount === 'string' ? parseInt(amount, 10) : amount;

  if (title.trim() !== '' && !isNaN(parsedAmount) && parsedAmount > 0) {
    setIsSubmitDisabled(false);  // 폼이 유효하면 제출 버튼 활성화
  } else {
    setIsSubmitDisabled(true);  // 폼이 유효하지 않으면 제출 버튼 비활성화
  }
};

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(`${goalTitle}을(를) 위해 \n ${goalAmount.toLocaleString()}원을 모을 거에요. \n 꼭 끝까지 성공하세요!`);
    setSavingsRegister(true);
  };

  const handleFinalConfirmation = async () => {
      try {
        // const response = await axiosCommonInstance.put('savings/regist', {
        const response = await axiosCommonInstance.post('quest/savings/regist', {
          relationId: user?.parent?.relationId,
          goalTitle: goalTitle,
          goalAmount: goalAmount,
        });
        console.log('목표 생성 성공:', response.data);
        console.log("목표 생성 relationId: ", user?.parent?.relationId)
        alert('목표가 성공적으로 등록되었습니다.');
        navigate(0);  // 성공적으로 등록되면 화면 새로고침
      } catch (error) {
        console.error('목표 생성 오류:', error);
        console.log("목표 생성 relationId: ", user?.parent?.relationId)
        alert('목표 등록에 실패했습니다. 다시 시도해주세요.');
      }
  };

  // useEffect(() => {
  //   // API를 통해 부모 등록 여부 확인
  //   const checkParentsRegistered = async () => {
  //     try {
  //       const response = await axiosCommonInstance.get('/quest/savings/childDetail');
  //       const data = response.data;
  //       const relationship = data?.data[0]?.relation_id;
  //       if (relationship !== 1) {
  //         setSavingsRegister(false);  // 부모가 등록되지 않았으면
  //       } else {
  //         setSavingsRegister(true);  // 부모가 등록되었으면
  //       }
  //     } catch (error) {
  //       console.error('부모 등록 여부 확인 오류:', error);
  //     }
  //   };

  //   checkParentsRegistered();
  // }, []);

  const closeModalAndNavigate = () => {
    setIsModalOpen(false);
    navigate('/main');
  };

  return (
    <div className="bg-gray-100 p-6 min-h-screen flex flex-col items-center">
      
      <h1 className="text-2xl font-bold text-green-500 mb-4">저금통 목표 세우기</h1>
      <p className='mb-4 text-gray-500'>나만의 저금 목표를 세우고 실천해 보세요.</p>

      <div className="border-2 border-green-500 bg-white shadow-lg rounded-lg p-6 w-full max-w-md">
        {!showForm && (
          <div className="flex items-center justify-center w-full h-40 rounded-lg cursor-pointer" 
          onClick={toggleForm}>
            <span className="text-green-400 text-6xl">+</span>
          </div>
        )}

        {showForm && (
          <>
            {message ? (
              <div className="font-bold text-green-700 p-4 rounded-lg mb-4" style={{textAlign: 'center'}}>
                <p style={{whiteSpace: "pre-line"}}>{message}</p>
              </div>
            ) : (
              <form onSubmit={handleSubmit}>
                <div className="mb-4">
                  {errorMessage1 && <p className="text-red-500">{errorMessage2}</p>}
                  <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="goal-title">
                    목표 이름
                  </label>
                  <input
                    id="goal-title"
                    type="text"
                    placeholder="목표 이름을 입력하세요"
                    value={goalTitle}
                    onChange={handleTitleChange}
          
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                  />
                </div>

                <div className="mb-6">
                  <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="goal-amount">
                    목표 금액
                  </label>
                  <input
                    id="goal-amount"
                    type="text"
                    value={goalAmount}
                    placeholder="목표 금액을 입력하세요"
                    onInput={handleAmountChange}
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    min="0"
                    step="1"
                  />
                </div>

                <button
                  type="submit"
                  className={`bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full ${isSubmitDisabled && 'opacity-50 cursor-not-allowed'}`}
                  disabled={isSubmitDisabled}  // 폼이 유효하지 않으면 버튼 비활성화
                >
                  다음
                </button>
              </form>
            )}
          </>
        )}
      </div>

      {/* 부모가 등록되지 않은 경우 모달 */}
      <Modal
      isOpen={isModalOpen}
      onClose={closeModalAndNavigate}
      message="등록된 부모가 없습니다. 부모에게 요청하세요."
      />
      
      {/* 목표가 설정된 경우 최종 확인 버튼 */}
      {isSavingsRegister && (
        <button
          onClick={handleFinalConfirmation}  // 백엔드로 데이터 전송
          className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full mt-4 pt-2"
        >
          최종확인
        </button>
      )}
    </div>
  );
};

export default SavingsPageRegist_Child;
