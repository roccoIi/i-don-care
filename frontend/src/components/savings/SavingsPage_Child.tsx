import React, { useEffect, useState } from "react";
import SavingsPageRegist_Child from "./SavingsPageRegist_Child";
import SavingsPageList_Child from "./SavingsPageList_Child";
import { axiosCommonInstance } from "../../api/axiosInstance";
import { userInfo } from "../../store/userStore";
import { bankDetail } from "../../store/bankStore";
import savingsImg from "../../assets/childRegister/savings.png"
import { useNavigate } from "react-router-dom";

const SavingsPage_Child: React.FC = () => {
  const [hasSavingsGoal, setHasSavingsGoal] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const { user } = userInfo();
  const {bank} = bankDetail()
  const navigate = useNavigate()
  
  useEffect(() => {
    const fetchSavingsGoal = async () => {
      try {
        const response = await axiosCommonInstance.post('quest/savings/childDetail', {
          relationId: user?.parent?.relationId
        });
        const data = response.data;
        const hasGoal = data?.data?.goalAmount;

        console.log(hasSavingsGoal)
        console.log(data);
        console.log('goalAmount 값:', hasGoal);
        setHasSavingsGoal(true);
      } catch (error) {
        setHasSavingsGoal(false);
      } finally {
        setIsLoading(false);
      }
    };

    fetchSavingsGoal();
  }, []);

  if (isLoading) {
    return null;
  }

  if (bank)
  return (
    <div className="h-full">
      {hasSavingsGoal ? <SavingsPageList_Child /> : <SavingsPageRegist_Child />}
    </div>
  );

  else
  return (
    <div className="w-full flex flex-col items-center pt-10">
      <img src={savingsImg} alt="child_not_registered_savingImg" className='w-5/6 mb-4' />
      <h1 className='my-4 text-xl font-bold text-green-600'>저금통: 적금 학습하기</h1>
      <p className='text-base text-gray-600'>나의 저금 목표를 설정하고,</p>
      <p className='text-base text-gray-600'>목표 달성 시 부모님의 이자를 추가로 받아요!</p>
      <button className='my-4 w-5/6 h-12 border border-green-600 rounded text-green-600 text-xl font-bold bg-white flex items-center justify-center'
      onClick={() => navigate('/account/link')}>
        계좌 연결하러 가기
        <span className="material-symbols-outlined text-2xl">
        chevron_right
        </span>
      </button>
    </div>
  )
};

export default SavingsPage_Child;