import React from 'react';
import { useNavigate } from 'react-router-dom';
import ChildNotRegiestedImage from "../../../src/assets/childRegister/main.png";

const ChildNotRegistered: React.FC = () => {
  const navigate = useNavigate()

  const goChildRegisterPage = () => {
    navigate('/main/child-register')
  }

  return (
    <div className='flex flex-col items-center box-border w-full h-full rounded-lg shadow-[0_0_15px_-5px_rgba(0,0,0,0.3)] bg-white mt-6 p-4' onClick={goChildRegisterPage}>
      <img src={ChildNotRegiestedImage} className='w-2/3 h-1/2' />
      <h1 className='my-4 text-xl font-bold text-green-600'>아이 데이터 조회하기</h1>
      <p className='text-sm text-gray-600'>우리 아이의 소비 점수와,</p>
      <p className='text-sm text-gray-600'>아이의 경제 활동 기록을 보여 드릴게요.</p>
      <button className='my-4 w-5/6 h-12 border border-green-600 rounded text-green-600 text-lg font-bold bg-white flex items-center justify-center'
      onClick={() => navigate('/main/child-register')}>
        아이 등록하러 가기
        <span className="material-symbols-outlined text-2xl">
        chevron_right
        </span>
      </button>
    </div>
  )
}

export default ChildNotRegistered;