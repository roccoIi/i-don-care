// import React, { useEffect, useState } from 'react';
// import { useParams, useNavigate } from 'react-router-dom';
// import { axiosCommonInstance } from '../../api/axiosInstance';
// import { userInfo } from '../../store/userStore';

// interface MissionDetailProps {
//   missionId: number;
//   title: string;
//   content: string;
//   amount: number;
// }

// const MissionDetail: React.FC<MissionDetailProps> = ({missionId, title, content, amount}) => {
//   const { id } = useParams<{ id: string }>();
// //   const [mission, setMission] = useState<MissionDetailProps | null>(null);
//   const { user } = userInfo((state) => state);
//   const navigate = useNavigate();

// //   useEffect(() => {
// //     const fetchMissionDetail = async () => {
// //       try {
// //         const response = await axiosCommonInstance.get(`/quest/mission/${id}`);
// //         setMission(response.data.data);
// //       } catch (error) {
// //         console.error('미션 상세 정보 가져오기 오류:', error);
// //       }
// //     };

// //     fetchMissionDetail();
// //   }, [id]);

//   const handleEdit = () => {
//     navigate(`/mission/edit/${id}`);
//   };

//   const handleDelete = async () => {
//     try {
//       await axiosCommonInstance.delete(`/quest/mission/delete/${id}`);
//       alert('미션이 삭제되었습니다.');
//       navigate('/mission');
//     } catch (error) {
//       console.error('미션 삭제 오류:', error);
//     }
//   };

//   const handleRequestSuccess = () => {
//     alert('성공 요청이 전송되었습니다.');
//   };

// //   if (!mission) return <p>로딩중...</p>;

//   return (
//     <div className="p-6">
//       <h1 className="text-2xl font-bold">{title}</h1>
//       <p>{content}</p>
//       <p>{amount.toLocaleString()}원</p>

//       {/* 부모면 수정/삭제 버튼, 자녀면 성공 요청 버튼 */}
//       {user?.role === 'PARENT' ? (
//         <div className="flex space-x-4">
//           <button className="bg-yellow-500 hover:bg-yellow-700 text-white py-2 px-4 rounded" onClick={handleEdit}>
//             수정하기
//           </button>
//           <button className="bg-red-500 hover:bg-red-700 text-white py-2 px-4 rounded" onClick={handleDelete}>
//             삭제하기
//           </button>
//         </div>
//       ) : (
//         <button className="bg-green-500 hover:bg-green-700 text-white py-2 px-4 rounded" onClick={handleRequestSuccess}>
//           성공 요청하기
//         </button>
//       )}
//     </div>
//   );
// };

// export default MissionDetail;
import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { axiosCommonInstance } from '../../api/axiosInstance';
import { userInfo } from '../../store/userStore';

interface MissionDetailProps {
  missionId: number;
  title: string;
  content: string;
  amount: number;
}

const MissionDetail: React.FC<MissionDetailProps> = ({ missionId, title, content, amount }) => {
  const { id } = useParams<{ id: string }>();
  const { user } = userInfo((state) => state);
  const navigate = useNavigate();

  const handleEdit = () => {
    navigate(`/mission/edit/${id}`);
  };

  const handleDelete = async () => {
    try {
      await axiosCommonInstance.delete(`/quest/mission/cancel`);
      alert('미션이 삭제되었습니다.');
      navigate('/main/mission');  // 삭제 후 미션 목록 페이지로 이동
    } catch (error) {
      console.error('미션 삭제 오류:', error);
    }
  };

  const handleRequestSuccess = () => {
    alert('성공 요청이 전송되었습니다.');
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold">{title}</h1>
      <p>{content}</p>
      <p>{amount.toLocaleString()}원</p>

      {/* 부모면 수정/삭제 버튼, 자녀면 성공 요청 버튼 */}
      {user?.role === 'PARENT' ? (
        <div className="flex space-x-4">
          <button className="bg-yellow-500 hover:bg-yellow-700 text-white py-2 px-4 rounded" onClick={handleEdit}>
            수정하기
          </button>
          <button className="bg-red-500 hover:bg-red-700 text-white py-2 px-4 rounded" onClick={handleDelete}>
            삭제하기
          </button>
        </div>
      ) : (
        <button className="bg-green-500 hover:bg-green-700 text-white py-2 px-4 rounded" onClick={handleRequestSuccess}>
          성공 요청하기
        </button>
      )}
    </div>
  );
};

export default MissionDetail;
