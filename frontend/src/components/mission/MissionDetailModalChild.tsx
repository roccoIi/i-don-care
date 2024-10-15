import React, { useState } from 'react';
import { axiosCommonInstance } from '../../api/axiosInstance';

interface MissionDetailModalChildProps {
  missionId: number;
  missionTitle: string;
  missionContent: string;
  missionAmount: number;
  onClose: () => void;
  onSuccess: () => void;
}

const MissionDetailModalChild: React.FC<MissionDetailModalChildProps> = ({
  missionId,
  missionTitle,
  missionContent,
  missionAmount,
  onSuccess,
  onClose,
}) => {
  const [image, setImage] = useState<File | null>(null); // 이미지 파일 상태
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  // 이미지 업로드 핸들러
  // const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  //   if (e.target.files && e.target.files[0]) {
  //     setImage(e.target.files[0]);
  //   }
  // };
  const handleImageChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      
      // 파일을 Blob으로 변환하는 과정 (이 부분이 첫 번째 코드와 유사하게 변경된 부분입니다)
      const blob = await fetch(URL.createObjectURL(file)).then(response => response.blob());
      const newFile = new File([blob], file.name, { type: blob.type });

      setImage(newFile); // 변환된 파일을 상태로 저장
    }
  };

  // 성공 요청 API 호출
  const handleSuccessRequest = async () => {
    if (!image) {
      setErrorMessage('이미지를 업로드해주세요.');
      return;
    }

    const formData = new FormData();
    formData.append('missionId', missionId.toString());
    formData.append('image', image);

    try {
      const response = await axiosCommonInstance.post('quest/mission/review', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      if (response.status === 200) {
        alert('성공 요청이 전송되었습니다.');
        onSuccess(); 
        onClose();
      } else {
        setErrorMessage('성공 요청에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      console.error('성공 요청 오류:', error);
      setErrorMessage('성공 요청에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 className="text-2xl font-bold mb-4">{missionTitle}</h2>
        <p className="mb-4">{missionContent}</p>
        <p className="mb-4">{missionAmount.toLocaleString()}원</p>

        <input
          type="file"
          accept="image/*"
          onChange={handleImageChange}
          className="mb-4"
        />
        {errorMessage && <p className="text-red-500 mb-4">{errorMessage}</p>}

        <button
          onClick={handleSuccessRequest}
          className="bg-green-500 text-white font-bold py-2 px-4 rounded w-full"
        >
          성공 요청하기
        </button>
        <button
          onClick={onClose}
          className="mt-4 bg-gray-500 text-white font-bold py-2 px-4 rounded w-full"
        >
          닫기
        </button>
      </div>
    </div>
  );
};

export default MissionDetailModalChild;
