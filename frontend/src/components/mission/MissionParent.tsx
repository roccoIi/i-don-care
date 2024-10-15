import React, { useState, useEffect } from 'react';
import MissionList from './MissionList';
import MissionCompleteParent from './MissionCompleteParent';
import { axiosCommonInstance } from '../../api/axiosInstance';
import { selectedChildInfo, userInfo } from '../../store/userStore';
import { useNavigate } from 'react-router-dom';
import missionImg from '../../assets/childRegister/mission.png'
import { fetchMissionList } from '../../api/missionApi';

export interface missionInfo {
  amount: number,
  content: string,
  missionId: number,
  proofPictureUrl: string | null,
  relationId: number,
  state: string,
  title: string
}

const MissionParent: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'ongoing' | 'completed'>('ongoing');
  const [showForm, setShowForm] = useState<boolean>(false);
  const [title, setTitle] = useState<string>('');
  const [content, setContent] = useState<string>('');
  const [amount, setAmount] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [missionList, setMissionList] = useState<missionInfo[]>([])
  
  const { user } = userInfo((state) => state);
  const { selectedChild, setSelectedChild } = selectedChildInfo((state) => state);
  const navigate = useNavigate();

  useEffect(() => {
    if (!selectedChild) return;

    console.log('현재 선택된 아이 1111:', selectedChild.selectedChildUserName);

  }, [selectedChild]);

  // useEffect(()=>{
  //   fe
  //   console.log("리로딩 됨", selectedChild)
  // },[])

  // 진행 중인 미션이나 완료된 미션을 불러오는 함수
  const fetchMissions = async () => {
    try {
      
      console.log('현재 선택된 아이22222:', selectedChild?.selectedChildRelationId);
      const response = await axiosCommonInstance.post('quest/mission/listIn', {
        relationId: selectedChild?.selectedChildRelationId
      });
      console.log('가져온 미션 데이터:', response.data);
    } catch (error) {
      console.error('미션 데이터를 가져오는 중 오류 발생:', error);
    }
  };

  const toggleForm = () => {
    setShowForm(!showForm);
  };

  
  const handleTabChange = (tab: 'ongoing' | 'completed') => {
    setActiveTab(tab);
  };

  // 미션 등록
  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage('');

    if (!title || !content || !amount) {
      setErrorMessage('모든 필드를 입력해주세요.');
      return;
    }

    try {
      const response = await axiosCommonInstance.post('quest/mission/regist', {
        relationId: selectedChild?.selectedChildRelationId, 
        title,
        content,
        amount: parseInt(amount, 10), // 금액을 숫자로 변환
      });
      const data = response.data
      console.log("DATA================",data)

      if (response.status === 200) {
        alert('미션이 성공적으로 등록되었습니다.');
        setShowForm(false);
        setTitle('');
        setContent('');
        setAmount('');
        fetchMissions(); 
      }
    } catch (error) {
      console.error('미션 등록 오류:', error);
      setErrorMessage('미션 등록에 실패했습니다. 다시 시도해주세요.');
    }
  };
  if (selectedChild)
  return (
    <div className="h-[70vh] p-4 bg-gray-100">
      {/* 탭 메뉴 */}
      {!showForm && (
        <div className="flex justify-center mb-4">
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

      {/* 미션 리스트 및 등록 버튼 */}
      <div className="space-y-4 h-5/6">
        <div className={`overflow-y-auto ${activeTab === 'ongoing' ? (showForm ? "h-0" : "h-5/6") : "h-full"}`}>
          {!showForm && activeTab === 'ongoing' && <MissionList />}
          {!showForm && activeTab === 'completed' && <MissionCompleteParent />}
        </div>
        
        {(user?.role === 'PARENT' && activeTab ==='ongoing') && (
          <>
            {!showForm && (
              <button
                className="bg-green-500 text-white font-bold py-2 px-4 mt-0 rounded-lg w-full"
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
      </div>
    </div>
  );
  else return (
    <div className='h-[70vh] p-6 bg-gray-100 flex flex-col items-center'>
      <img src={missionImg} alt="child_not_registered_missionImg" className='w-5/6 mb-4' />
      <h1 className='my-4 text-xl font-bold text-green-600'>미션 수행: 노동의 가치를 배우자!</h1>
      <p className='text-base text-gray-600'>우리 아이가 수행할 미션을 정해 주고,</p>
      <p className='text-base text-gray-600'>아이가 스스로 돈을 벌 수 있게 도와 주세요!</p>
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

export default MissionParent;
