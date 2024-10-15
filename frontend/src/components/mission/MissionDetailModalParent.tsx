import React, { useState } from 'react';
import { axiosCommonInstance } from '../../api/axiosInstance';
import { useNavigate } from 'react-router-dom';

interface MissionDetailModalParentProps {
  missionId: number;
  missionTitle: string;
  missionContent: string;
  missionAmount: number;
  missionImg: string;
  onEdit: () => void;
  onDelete: (missionId: number) => void; // 삭제 완료 시 호출할 콜백
  onClose: () => void;
}

const MissionDetailModalParent: React.FC<MissionDetailModalParentProps> = ({
  missionId,
  missionTitle,
  missionContent,
  missionAmount,
  missionImg,
  onEdit,
  onDelete,
  onClose
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState(missionTitle);
  const [content, setContent] = useState(missionContent);
  const [amount, setAmount] = useState(missionAmount);
  const [img, setImg] = useState(missionImg);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const navigate = useNavigate()

  // 미션 수정 함수
  const handleUpdateMission = async () => {
    try {
      const response = await axiosCommonInstance.put('quest/mission/modify', {
        missionId: missionId,
        title: title,
        content: content,
        amount: amount,
      });

      if (response.status === 200) {
        alert('미션이 성공적으로 수정되었습니다.');
        setIsEditing(false);  // 수정 모드 종료
        onEdit();  // 부모 컴포넌트로 수정 완료 알림
      } else {
        setErrorMessage('미션 수정에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      console.error('미션 수정 오류:', error);
      setErrorMessage('미션 수정에 실패했습니다. 다시 시도해주세요.');
    }
  };

  // 미션 이미지 확인
  const handlecheckMission = async () => {
    try {
      const response = await axiosCommonInstance.put('quest/mission/check', {
        missionId: missionId
      });

      if (response.status === 200) {
        alert('미션 승인하였습니다.');
        navigate(0)
        // onDelete(missionId);
      } else {
        setErrorMessage('미션 승인에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      console.error('미션 승인 오류:', error);
      setErrorMessage('미션 승인에 실패했습니다. 다시 시도해주세요.');
    }
  };

  // 미션 삭제 함수
  const handleDeleteMission = async () => {
    try {
      const response = await axiosCommonInstance.put('quest/mission/cancel', {
        missionId: missionId
      });

      if (response.status === 200) {
        alert('미션이 삭제되었습니다.');
        onDelete(missionId); // 삭제 후 부모에게 알림
      } else {
        setErrorMessage('미션 삭제에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      console.error('미션 삭제 오류:', error);
      setErrorMessage('미션 삭제에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white rounded-lg shadow-lg p-6 w-3/4 max-w-md relative">
        {isEditing ? (
          <>
            <h2 className="text-2xl font-bold mb-4">미션 수정</h2>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">제목</label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">내용</label>
              <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">금액</label>
              <input
                type="number"
                value={amount}
                onChange={(e) => setAmount(Number(e.target.value))}
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <button
              onClick={handleUpdateMission}
              className="bg-green-500 text-white font-bold py-2 px-4 rounded w-full mb-4"
            >
              저장하기
            </button>
            <button
              onClick={() => setIsEditing(false)}
              className="bg-gray-500 text-white font-bold py-2 px-4 rounded w-full"
            >
              취소
            </button>
          </>
        ) : (
          <>
            <h2 className="text-2xl font-bold mb-4">{missionTitle}</h2>
            <p className="mb-4">{missionContent}</p>
            <p className="mb-4">{missionAmount.toLocaleString()}원</p>
            <img src={img} alt="" />

            {errorMessage && <p className="text-red-500 mb-4">{errorMessage}</p>}

            {img ? (
              <button
              onClick={handlecheckMission}
              className="bg-blue-500 text-white font-bold py-2 px-4 rounded w-full mb-4">
              확인하기
            </button>
            ):(
              <>
                <button
                  onClick={() => setIsEditing(true)}
                  className="bg-yellow-500 text-white font-bold py-2 px-4 rounded w-full mb-4"
                >
                  수정하기
                </button>
                <button
                  onClick={handleDeleteMission}
                  className="bg-red-500 text-white font-bold py-2 px-4 rounded w-full mb-4"
                >
                  삭제하기
                </button>
            </>
            )}
          </>
        )}
        <span className="material-symbols-outlined absolute right-4 top-4 text-2xl"
        onClick={onClose}>
        close
        </span>
      </div>
    </div>
  );
};

export default MissionDetailModalParent;
