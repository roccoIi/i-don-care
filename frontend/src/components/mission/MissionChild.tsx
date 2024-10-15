import React, { useState } from 'react';
import MissionList from './MissionList';
import MissionCompleteChild from './MissionCompleteChild';
import { userInfo } from '../../store/userStore';
import { axiosCommonInstance } from '../../api/axiosInstance';
import { bankDetail } from '../../store/bankStore';
import missionImg from '../../assets/childRegister/mission.png'
import { useNavigate } from 'react-router-dom';

const MissionChild: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'ongoing' | 'completed'>('ongoing');
  const [showForm, setShowForm] = useState<boolean>(false);
  const [title, setTitle] = useState<string>('');
  const [content, setContent] = useState<string>('');
  const [amount, setAmount] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const { user } = userInfo((state) => state);
  const selectedChild = user?.children?.[0];
  const {bank} = bankDetail()
  const navigate = useNavigate()

  const toggleForm = () => {
    setShowForm(!showForm);
  };

  const handleTabChange = (tab: 'ongoing' | 'completed') => {
    setActiveTab(tab);
  };

  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage('');

    if (!title || !content || !amount || !selectedChild) {
      setErrorMessage('모든 필드를 입력해주세요.');
      return;
    }

    try {
      const response = await axiosCommonInstance.post('/quest/mission/regist', {
        relationId: selectedChild.relationId,
        title,
        content,
        amount: parseInt(amount, 10),
      });

      if (response.status === 200) {
        alert('미션이 성공적으로 등록되었습니다.');
        setShowForm(false);
        setTitle('');
        setContent('');
        setAmount('');
      }
    } catch (error) {
      console.error('미션 등록 오류:', error);
      setErrorMessage('미션 등록에 실패했습니다. 다시 시도해주세요.');
    }
  };

  if (bank)
  return (
    <div className="h-5/6 p-6 bg-gray-100">
      {!showForm && (
        <div className="flex justify-center mb-6">
          <button
            className={`px-4 py-2 rounded-l-lg ${activeTab === 'ongoing' ? 'bg-green-500 text-white' : 'bg-gray-300'}`}
            onClick={() => handleTabChange('ongoing')}
          >
            진행중인 미션
          </button>
          <button
            className={`px-4 py-2 rounded-r-lg ${activeTab === 'completed' ? 'bg-green-500 text-white' : 'bg-gray-300'}`}
            onClick={() => handleTabChange('completed')}
          >
            완료한 미션
          </button>
        </div>
      )}

      {user?.role === 'PARENT' && (
        <>
          {!showForm && (
            <button
              className="bg-green-500 text-white font-bold py-2 px-4 mt-6 rounded-lg w-full"
              onClick={toggleForm}
            >
              등록하기
            </button>
          )}

          {showForm && (
            <form onSubmit={handleFormSubmit} className="bg-white p-6 rounded-lg shadow-md mt-6">
              {errorMessage && <p className="text-red-500 mb-4">{errorMessage}</p>}
              
              <div className="mb-4">
                <label className="block text-gray-700 text-sm font-bold mb-2">제목</label>
                <input
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className="w-full px-3 py-2 border rounded"
                  placeholder="미션 제목을 입력하세요"
                />
              </div>

              <div className="mb-4">
                <label className="block text-gray-700 text-sm font-bold mb-2">내용</label>
                <textarea
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  className="w-full px-3 py-2 border rounded"
                  placeholder="미션 내용을 입력하세요"
                />
              </div>

              <div className="mb-4">
                <label className="block text-gray-700 text-sm font-bold mb-2">금액</label>
                <input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  className="w-full px-3 py-2 border rounded"
                  placeholder="미션 금액을 입력하세요"
                />
              </div>

              <button
                type="submit"
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded w-full"
              >
                등록하기
              </button>

              <button
                type="button"
                className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded w-full mt-4"
                onClick={toggleForm}
              >
                취소
              </button>
            </form>
          )}
        </>
      )}

      
      {!showForm && activeTab === 'ongoing' && <MissionList />}
      {!showForm && activeTab === 'completed' && <MissionCompleteChild />}
    </div>
  );

  else
  return (
    <div className='h-[70vh] pt-20 bg-gray-100 flex flex-col items-center'>
      <img src={missionImg} alt="child_not_registered_missionImg" className='w-2/3 mb-4' />
      <h1 className='my-4 text-xl font-bold text-green-600'>미션 수행: 노동의 가치를 배우자!</h1>
      <p className='text-base text-gray-600'>부모님이 설정한 미션을 수행하고,</p>
      <p className='text-base text-gray-600'>추가 용돈을 획득해 보세요!</p>
      <button className='my-4 w-4/5 h-12 border border-green-600 rounded text-green-600 text-xl font-bold bg-white flex items-center justify-center'
      onClick={() => navigate('/account/link')}>
        계좌 연결하러 가기
        <span className="material-symbols-outlined text-2xl">
        chevron_right
        </span>
      </button>
    </div>
  )
};

export default MissionChild;