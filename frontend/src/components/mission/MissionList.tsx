import React, { useEffect, useState } from 'react';
import { axiosCommonInstance } from '../../api/axiosInstance';
import { userInfo } from '../../store/userStore';
import { selectedChildInfo } from '../../store/userStore';
import MissionDetailModalChild from './MissionDetailModalChild';
import MissionDetailModalParent from './MissionDetailModalParent';
import { fetchMissionList } from '../../api/missionApi';

interface Mission {
  missionId: number;
  title: string;
  content: string;
  amount: number;
  proofPictureUrl: string;
}

const MissionList: React.FC = () => {
  const [missions, setMissions] = useState<Mission[]>([]);
  const [selectedMission, setSelectedMission] = useState<Mission | null>(null);
  const { user } = userInfo((state) => state);
  const {selectedChild} = selectedChildInfo();

  const fetchMissions = async () => {
    
      try {
        if (user && user.children && user.children.length > 0) {
          if (selectedChild) {
                fetchMissionList({relationId: selectedChild?.selectedChildRelationId})
                .then((data) => {
                  setMissions(data?.data);
                })
                .catch((error) => {
                  console.log(error)
                })
          }
        } else {
          if (user && user.parent) {
            fetchMissionList({relationId: user.parent.relationId})
            .then((data) => {
              setMissions(data?.data);
            })
            .catch((error) => {
              console.log(error)
            })
        }}
      } catch (error) {
        console.error('진행중인 미션 가져오기 오류:', error);
      }
    
  };

  useEffect(() => {
    fetchMissions();
  }, [selectedChild]);

  const handleMissionClick = (mission: Mission) => {
    console.log('선택된미션', mission);
    setSelectedMission(mission);
  };

  const handleCloseModal = () => {
    setSelectedMission(null);
  };

  const handleEditMission = async (missionId: number) => {
    // 수정 버튼을 클릭했을 때
    console.log(`Editing mission: ${missionId}`);
    // 이곳에 mission 수정 로직을 추가하거나 별도 화면으로 이동할 수 있음
  };
  
  const handleDeleteMission = async (missionId: number) => {
    try {
      // 미션 삭제 API 호출
      const response = await axiosCommonInstance.put('quest/mission/cancel', {
        missionId: missionId,
      });
  
      if (response.status === 200) {
        alert('미션이 삭제되었습니다.');
        // 미션 목록 새로고침
        setMissions((prevMissions) =>
          prevMissions.filter((mission) => mission.missionId !== missionId)
        );
        setSelectedMission(null); // 모달 닫기
      } else {
        alert('미션 삭제에 실패했습니다.');
      }
    } catch (error) {
      console.error('미션 삭제 오류:', error);
    }
  };


  

  return (
    <div className='w-full h-full overflow-y-auto'>
      {missions.map((mission) => (
        <div
          key={mission.missionId}
          className={`p-4 mb-2 border rounded-lg flex justify-between items-center cursor-pointer ${mission?.proofPictureUrl ? "bg-green-200" : "bg-white"}`}
          onClick={() => handleMissionClick(mission)}
        >
          <div>
            <h3 className="text-lg font-bold">{mission.title}</h3>
            <p>{mission.amount}원</p>
          </div>
          <span>&gt;</span>
        </div>
      ))}

      {selectedMission && user?.role === 'CHILD' && (
        <MissionDetailModalChild
        missionId={selectedMission.missionId} // missionId 추가
        missionTitle={selectedMission.title}
        missionContent={selectedMission.content}
        missionAmount={selectedMission.amount}
        
        onSuccess={() => {
          setSelectedMission(null); // 성공 후 모달 닫기
          fetchMissions(); // 성공 후 목록 갱신
        }}
        onClose={handleCloseModal}
        />
      )}

      {selectedMission && user?.role === 'PARENT' && (
        <MissionDetailModalParent
          missionId={selectedMission?.missionId}
          missionTitle={selectedMission?.title}
          missionContent={selectedMission?.content}
          missionAmount={selectedMission?.amount}
          missionImg={selectedMission?.proofPictureUrl}
          onEdit={() => handleEditMission(selectedMission?.missionId)}
          onDelete={() => handleDeleteMission(selectedMission?.missionId)}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
};

export default MissionList;
