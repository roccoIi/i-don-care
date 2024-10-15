import React, { useEffect, useState } from "react";
import Modal from "../common/ContentModal";
import { useNavigate } from "react-router-dom";
import { registerChildResponse } from "../../api/childRegisterApi";
import { setNotificationAsRead } from "../../api/notificationApi";
import { userInfo } from "../../store/userStore";
import { selectedChildInfo } from "../../store/userStore";

type NotificationProps = {
  notificationId: number,
  senderId: number,
  senderName: string,
  type: string,
}

const Notification: React.FC<NotificationProps> = ({notificationId, senderId, senderName, type}) => {
  const navigate = useNavigate()
  const {user, setUser} = userInfo()
  const {selectedChild, setSelectedChild} = selectedChildInfo()
  // const [senderName, setSenderName] = useState<string|null>('')
  const [message, setMessage] = useState<string>('')
  const [errorMessage, setErrorMessage] = useState<string>('')
  const [isRegisterChildModalOpen, setIsRegisterChildModalOpen] = useState<boolean>(false)
  const [isErrortModalOpen, setIsErrorModalOpen] = useState<boolean>(false)
  const [isInitialRender, setIsInitialRender] = useState(true)

  const openErrorModal = () => setIsErrorModalOpen(true)
  const closeErrorModal = () => setIsErrorModalOpen(false)

  const setSelectedChildInfo = (senderId: number) => {
    const foundChild = user && user?.children?.find((child) => senderId === child.userId)
    if(foundChild){
      setSelectedChild({
        selectedChildUserId: foundChild.userId,
        selectedChildRelationId: foundChild.relationId,
        selectedChildUserName: foundChild.userName,
      })
    }
  }

  const registerChild = async (isAccept: boolean) => {

    registerChildResponse({
      notificationId: notificationId,
      isAccept: isAccept,
    })
    .then((data) => {
      console.log(data)
    })
    .catch((error) => {
      console.log(error)
    })
  }

  const goMissionPage = () => {
    readNotification()
    setSelectedChildInfo(senderId)
    // navigate('/main/mission')
    // console.log(selectedChild)
  }


  const goSavingsPage = () => {
    readNotification()
    setSelectedChildInfo(senderId)
    // navigate('/main/savings')
    // console.log(selectedChild)
  }

  const goBankingPage = () => {
    readNotification()
    window.alert('계좌를 확인해주세요')
    navigate('/main/bank')
  }

  const openRegisterChildModal = () => setIsRegisterChildModalOpen(true)
  const closeRegisterChildModal = () => setIsRegisterChildModalOpen(false)
  const clickAccept = () => {
    registerChild(true)
    readNotification()
    closeRegisterChildModal()
    navigate(0)
  }
  const clickReject = () => {
    registerChild(false)
    readNotification()
    closeRegisterChildModal()
    navigate(0)
  }

  // const readNotification = async() => {
  //   try{
  //     const response = await setNotificationAsRead({notificationId:notificationId})
  //     const data = await response.data
  //     console.log('ReadNotification Data: ', data)
  //     navigate(0)
  //   } catch(error){
  //     console.log('Error: ', error)
  //     if (error) {
  //       if(error.response.data?.code === 'U010') {
  //         console.log("error status: U010")
  
  //         setErrorMessage('존재하지 않는 회원입니다.')
  //         openErrorModal()
  //       } else if(error.response.data.code === 'U011') {
  //         console.log("error status: U011")
  
  //         setErrorMessage('이미 등록된 자녀입니다.')
  //         openErrorModal()
  //       }
  //     }
  //   }
  // }

  const readNotification = async () => {

    setNotificationAsRead({
      notificationId: notificationId
    })
    .then((data) => {
      console.log(data)
    })
    .catch((error) => {
      console.log(error)
      if (error.response) {
        if(error.response.data.code === 'U007') {
          console.log("error status: U007")

          // 에러시 나타날 코드 작성 필요
          setErrorMessage('이미 처리된 알림입니다')
          openErrorModal()
        } else if(error.response.data.code === 'U009') {
          console.log("error status: U009")

          setErrorMessage('이미 처리된 요청입니다')
          openErrorModal()
        } else if(error.response.data.code === 'U011') {
          console.log("error status: U011")

          setErrorMessage('이미 관계가 등록된 유저입니다.')
          openErrorModal()
        }
      }
    })
  }

  const clickNotification = () => {
    if (type === 'REQUEST_RELATION') {
      openRegisterChildModal()
    } else if (type === 'COINBOX_START' || type === 'COINBOX_SUCCESS') {
      goSavingsPage()
    } else if (type === 'MISSION_SUCCESS') {
      goMissionPage()
    } else if (type === 'LESS_BALANCE') {
      goBankingPage()
    } else if (type === 'QUIZ_SUCCESS') {
      readNotification()
      navigate(0)
    }
  }
  
  useEffect(() => {
    if (type === 'REQUEST_RELATION') {
      setMessage('님이 자녀 등록 요청을 신청하였습니다')
    } else if (type === 'COINBOX_START') {
      setMessage('님의 저금통이 생성되었습니다')
    } else if (type === 'COINBOX_SUCCESS') {
      setMessage('님이 저금을 완료하였습니다')
    } else if (type === 'MISSION_SUCCESS') {
      setMessage('님이 미션에 성공하였습니다')
    } else if (type === 'QUIZ_SUCCESS') {
      setMessage('님이 퀴즈를 성공하였습니다')
    } else if (type === 'LESS_BALANCE') {
      setMessage('님 계좌에 잔액이 부족하여 \n 자녀 계좌로 이체를 실패하였습니다.')
    } else {
      setMessage('error')
    }
    // setSelectedChildInfo(senderId)
  }, [])

  useEffect(()=>{
    //처음 들어올 경우에는 실행 안되도록 설정
    if (isInitialRender) {
      setIsInitialRender(false);
      return;
    }

    if (type === 'COINBOX_START' || type === 'COINBOX_SUCCESS') {
      console.log("확인확인", selectedChild, user?.role)
      navigate('/main/savings')
    } else if (type === 'MISSION_SUCCESS') {
      console.log("확인확인", selectedChild, user?.role)
      navigate('/main/mission')
    }
  },[selectedChild])

  return (
    <div>
      <div className="py-7 border-b border-[#0FE976]" onClick={clickNotification} style={{ whiteSpace: 'pre-line' }}>
        <p>요청자: {senderName}</p>
        <p>{senderName}{message}</p>
      </div>
      <div>
        
        <Modal isOpen={isRegisterChildModalOpen} onClose={closeRegisterChildModal}>
        <div className="w-full h-full relative">
            <div className="text-center absolute top-40 inset-x-0">
              <p className="font-bold">{senderName}{message}</p>
            </div>
            <div className="flex justify-center">
              <button
                type="submit"
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-2/5 absolute bottom-16 left-5"
                onClick={clickAccept}>
                수락
              </button>

              <button
                type="submit"
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-2/5 absolute bottom-16 right-5"
                onClick={clickReject}>
                거절
              </button>
            </div>
          </div>
        </Modal>

        {/* 요청 실패 모달 */}
        <Modal isOpen={isErrortModalOpen} onClose={closeErrorModal}>
          <div className="w-full h-full relative">
            <div className="text-center absolute top-40 inset-x-0">
              <p className="font-bold">{errorMessage}</p>
            </div>
            <div className="flex justify-center">
              <button
                type="submit"
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-3/4 absolute bottom-16"
                onClick={closeErrorModal}>
                확인
              </button>
            </div>
          </div>
        </Modal>
      </div>
    </div>
  )
}

export default Notification