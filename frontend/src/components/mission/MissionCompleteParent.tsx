import React, { useEffect, useState } from 'react';
import { axiosCommonInstance } from '../../api/axiosInstance';
import { selectedChildInfo, userInfo } from '../../store/userStore';


interface Mission {
  missionId: number;
  title: string;
  amount: number;
}

const MissionComplete: React.FC = () => {
  const [completedMissions, setCompletedMissions] = useState<Mission[]>([]);
  const [selectedMission, setSelectedMission] = useState<Mission | null>(null);
  const {selectedChild, setSelectedChild} = selectedChildInfo((state)=>state)
  const {user} = userInfo((state) => state);
  

  const fetchCompletedMissions = async () => {
    try {
      const response = await axiosCommonInstance.post('quest/mission/listCom', {
        relationId: selectedChild?.selectedChildRelationId
      });
      setCompletedMissions(response.data.data);
    } catch (error) {
      console.error('완료한 미션 가져오기 오류:', error);
      setCompletedMissions([])
    }
  };

  useEffect(() => {

    fetchCompletedMissions();
  }, []);

  useEffect(() => {
    fetchCompletedMissions()
  }, [selectedChild])

  return (
    <div className='w-full h-full overflow-y-auto'>
      {completedMissions.map((mission) => (
        <div key={mission.missionId} className="p-4 mb-2 bg-white border rounded-lg">
          <h3 className="text-lg font-bold">{mission.title}</h3>
          <p>{mission.amount.toLocaleString()}원</p>
        </div>
      ))}
    </div>
  );
};

export default MissionComplete;
// import React, { useEffect, useState } from 'react';

// interface Mission {
//   missionId: number;
//   title: string;
//   amount: number;
// }

// const mockCompletedMissions: Mission[] = [
// ];

// const MissionComplete: React.FC = () => {
//   const [completedMissions, setCompletedMissions] = useState<Mission[]>([]);

//   useEffect(() => {
//     // 목업 데이터 설정
//     setCompletedMissions(mockCompletedMissions);
//   }, []);

//   return (
//     <div>
//       {completedMissions.map((mission) => (
//         <div key={mission.missionId} className="p-4 mb-2 bg-white border rounded-lg">
//           <h3 className="text-lg font-bold">{mission.title}</h3>
//           <p>{mission.amount.toLocaleString()}원</p>
//         </div>
//       ))}
//     </div>
//   );
// };

// export default MissionComplete;
