import React, { useState, useEffect } from 'react';
import Modal from './NoRegisterModal';
import { axiosCommonInstance } from "../../api/axiosInstance";
import { useNavigate } from 'react-router-dom';
import { fetchAddInterest } from '../../api/savingsApi';
import { selectedChildInfo, userInfo } from '../../store/userStore';
import ProgressBar from '../common/Progressbar';
import savingsImg from '../../assets/childRegister/savings.png'
import { fetchSavingInfo } from '../../api/savingsApi';
import noneImg from '../../assets/childRegister/sad_pig.png'

const SavingsPage_Parents: React.FC = () => {
  const [childName, setChildName] = useState<string>(''); 
  const [goalTitle, setGoalTitle] = useState<string>('');  
  const [goalAmount, setGoalAmount] = useState<number>(0);
  const [coinboxId, setCoinboxId] = useState<number>(0); 
  const [currentAmount, setCurrentAmount] = useState<number>(0);  
  const [progressPercentage, setProgressPercentage] = useState<number>(0);  
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);  
  const [showInterestForm, setShowInterestForm] = useState<boolean>(false); 
  const [showFinalConfirmation, setShowFinalConfirmation] = useState<boolean>(false);  
  const [interestAmount, setInterestAmount] = useState<number>(0);
  const [interestAmountPlus, setInterestAmountPlus] = useState<number>(0);
  const [totalInterestAmount, setTotalInterestAmount] = useState<number>(0);
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [isSaving, setIsSaving] = useState<boolean>(false)

  // userstore에서 user 정보 가져오기
  const { user } = userInfo((state) => state);  // user 정보를 가져옴
  const {selectedChild, setSelectedChild} = selectedChildInfo()
  const navigate = useNavigate();

 

  useEffect(() => {
    if (totalInterestAmount > 0) {
      console.log('누적된 이자 금액:', totalInterestAmount);
    }
  }, [totalInterestAmount]);


  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
  
    
    if (/^\d*$/.test(value)) {  
      setInterestAmountPlus(value === '' ? 0 : parseInt(value));  
      setErrorMessage('');  
    } else {
      setErrorMessage('숫자만 입력하세요.');  
      console.log(value);
    }
  };
  
  useEffect(() => {
    if (selectedChild) {
      fetchSavingInfo({relationId: selectedChild.selectedChildRelationId})
      .then((response) => {
        console.log(response)
        setCoinboxId(response?.data?.data?.coinboxId);
        setGoalTitle(response?.data?.data?.goalTitle);
        setGoalAmount(response?.data?.data?.goalAmount);
        setCurrentAmount(response?.data?.data?.amount);
        setInterestAmount(response?.data?.data?.interestAmount);
        setIsSaving(true)
        const progress = response?.data?.data?.goalAmount ? (response?.data?.data?.amount / response?.data?.data?.goalAmount) * 100 : 0;
        setProgressPercentage(progress);
      })
      .catch((error) => {
        console.log(error?.status)
        setIsSaving(false)
      })
    }
  }, [selectedChild?.selectedChildRelationId]);


  const handleAddInterest = () => {
    setShowInterestForm(true);
  };

  const handleSubmitInterest = () => {
    console.log("======asf",interestAmountPlus);
    if (interestAmountPlus === 0 || interestAmountPlus === null) {
      setErrorMessage('이자 금액을 입력해주세요.');
      return;  
    }
  
    
    setShowInterestForm(false);
    setShowFinalConfirmation(true);  
  };

  const handleFinalConfirmation = async () => {
    try {
      console.log("이자 등록 누른 후",interestAmountPlus);
      console.log("코인박스아이디:",coinboxId);
      const response = await fetchAddInterest({
        coinboxId: coinboxId, 
        addAmount: interestAmountPlus,
      });
      console.log("Data",response.data);
      console.log('이자 추가 성공', response.data);
      console.log('누적된 이자 금액:', totalInterestAmount + interestAmount);
      localStorage.setItem('selectedChild', JSON.stringify(selectedChild));

      // 페이지 전환 후 새로고침
      navigate('/main/savings', { replace: true });
      window.location.reload();
    } catch (error) {
      console.error('이자 추가 오류:', error);
    }
  };

  const closeModalAndNavigate = () => {
    setIsModalOpen(false);  
    navigate('/main');
  };
  
  if (selectedChild && isSaving)
  return (
    <div className="bg-gray-100 p-6 min-h-3/4 flex flex-col items-center">
      {!showInterestForm && !showFinalConfirmation && (
        <>          
          <div className="border-2 border-green-500 bg-white shadow-lg rounded-lg p-6 w-full max-w-md">
            <p className="text-lg font-bold text-center">목적 : {goalTitle}</p>
            <p className="text-lg font-bold text-center">목표 금액 : {goalAmount}원</p>
          </div>

          
          <div className="border-2 border-green-500 bg-white shadow-lg rounded-lg p-6 w-full max-w-md mt-6">
            <p className="text-center text-lg mb-4">
              {selectedChild?.selectedChildUserName}님은 지금까지<br />
              <span className="font-bold text-green-500">{currentAmount}원</span>을 모았어요.
            </p>
  
            
            <div className="w-full max-w-md mb-6">
              <div className="bg-gray-200 h-4 rounded">
                <div
                  className="bg-green-500 h-4 rounded"
                  style={{ width: `${progressPercentage}%` }} 
                ></div>
              </div>
              <p className="text-center mt-2">현재 진행률 {progressPercentage.toFixed(2)}%</p>
              <p className="text-center mt-2">
                이자 <span className="text-green-500">{interestAmount}원</span>을 추가했습니다.
              </p>
            </div>
          </div>
          <button
            onClick={handleAddInterest}
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded mt-4 w-full"
          >
            이자 넣기
          </button>
        </>
      )}

      
      {showInterestForm && (
        <div className="border-2 border-green-500 bg-white shadow-lg rounded-lg p-6 w-full max-w-md mt-6">
          <h2 className="text-l font-bold text-green-500 mb-4 text-center">이자를 얼마나 넣을까요?</h2>
          <input
            type="text"
            value={interestAmountPlus === 0 ? '' : interestAmountPlus}
            onChange={handleChange}
            placeholder="금액 입력"
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline mb-4"
            min="0"
          />
          {errorMessage && (
            <p className="text-red-500 text-sm text-center">{errorMessage}</p>  
          )}
          <p className="text-gray-500 text-sm text-center">추후 수정 시, 초기 이자에 누적 금액으로 적용됩니다.</p>
          <button
            onClick={handleSubmitInterest}
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded mt-4 w-full"
          >
            다음
          </button>
        </div>
      )}

      {/* 최종 확인 폼 */}
      {showFinalConfirmation && (
        <div className="border-2 border-green-500 bg-white shadow-lg rounded-lg p-6 w-full max-w-md mt-6">
          <h2 className="text-xl font-bold text-green-500 mb-4 text-center">저금통 요청 확인</h2>
          <p className="text-center text-lg">
            <span className="font-bold">{goalTitle}</span>을 위해 <br />
            <span className="font-bold">{goalAmount}원</span>을 모을 거예요.<br /><br />
            최종 이자를 붙이면 <br />
            <span className="font-bold">{(goalAmount + interestAmount + interestAmountPlus).toLocaleString()}원</span>이 {childName}님 계좌로 입금됩니다.
          </p>
          <p className="text-gray-500 text-sm text-center mt-4">
            만약 목표를 달성하지 못하면,<br /> 이자를 못 받고 모았던 원금을 돌려받아요.
          </p>
          <button
            onClick={handleFinalConfirmation}
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded mt-4 w-full"
          >
            등록하기
          </button>
        </div>
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={closeModalAndNavigate}
        message="등록된 아이가 없습니다. 아이에게 요청하세요."
      />
    </div>
  ); else if (selectedChild) return (
    <div className='bg-gray-100 p-6 min-h-3/4 flex flex-col items-center'>
      <img src={noneImg} alt="none_savings_img"
      className='opacity-60 w-5/6 my-6' />
      <p className='text-lg text-gray-600'>{selectedChild?.selectedChildUserName}님이 설정한 목표가 없어요.</p>
    </div>
  );

  else return (
    <div className='bg-gray-100 p-6 min-h-3/4 flex flex-col items-center'>
      <img src={savingsImg} alt="child_not_registered_savingImg" className='w-5/6 mb-4' />
      <h1 className='my-4 text-xl font-bold text-green-600'>저금통: 적금 학습하기</h1>
      <p className='text-base text-gray-600'>우리 아이의 저금 현황을 조회하고,</p>
      <p className='text-base text-gray-600'>이자를 통해 목표 달성을 응원해 주세요!</p>
      <button className='my-4 w-5/6 h-12 border border-green-600 rounded text-green-600 text-xl font-bold bg-white flex items-center justify-center'
      onClick={() => navigate('/main/child-register')}>
        아이 등록하러 가기
        <span className="material-symbols-outlined text-2xl">
        chevron_right
        </span>
      </button>
    </div>
  )
};

export default SavingsPage_Parents;
