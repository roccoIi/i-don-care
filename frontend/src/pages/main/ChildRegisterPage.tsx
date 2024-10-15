import React, { useEffect, useState } from "react";
import { registerChildRequest } from "../../api/childRegisterApi";
import ErrorToast from "../../components/common/ErrorToast";
import Modal from "../../components/common/ContentModal";
import familyImg from "../../assets/user/rabbit_family.png"
import { useNavigate } from "react-router-dom";


interface ChildRegisterPageProps {}

const ChildRegisterPage: React.FC<ChildRegisterPageProps> = function ChildRegisterPage() {

  const [phoneNumber, setPhoneNumber] = useState<string>('')
  const [errorMessage, setErrorMessage] = useState<string>('')
  const [toast, setToast] = useState<boolean>(false)
  const [isRequestModalOpen, setIsRequestModalOpen] = useState<boolean>(false)
  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState<boolean>(false)
  const [isErrortModalOpen, setIsErrorModalOpen] = useState<boolean>(false)
  const [isDisable, setIsDisable] = useState<boolean>(true)
  const navigate = useNavigate()

  const openRequestModal = () => setIsRequestModalOpen(true)
  const closeRequestModal = () => setIsRequestModalOpen(false)
  const openSuccessModal = () => setIsSuccessModalOpen(true)
  const closeSuccessModal = () => {navigate(0)} //setIsSuccessModalOpen(false) 
  const openErrorModal = () => setIsErrorModalOpen(true)
  const closeErrorModal = () => setIsErrorModalOpen(false)

  // input 내용 저장
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value
    const isvalid = /^[0-9]*$/.test(value)

    // 숫자 말고 다른 문자 들어가면 오류 메세지 출력
    if (isvalid) {

      //12자 이상 입력시 에러 메세지
      if(value.length > 11) {
        setErrorMessage('전화번호는 11자리를 입력해주세요')
        setToast(true)
        setIsDisable(true)
      } else if(value.length == 11) {
        setToast(false)
        setIsDisable(false)
        setPhoneNumber(value)
        setErrorMessage('')
      } else {
        //에러 메시지 없어짐
        setToast(false)
        //번호 저장
        setPhoneNumber(value)
        //에러메시지 초기화
        setErrorMessage('')
        setIsDisable(true)
      }
    } else {
      // 현재 다른 문자가 들어가있음
      setErrorMessage('숫자만 입력해주세요')
      setToast(true)
      setIsDisable(true)
    } 
  }
  
  // 자녀 등록 요청
  const registerChild = async () => {

    registerChildRequest({
      tel: phoneNumber
    })
    .then((data) => {
      console.log(data)
      openSuccessModal()
    })
    .catch((error) => {
      console.log(error)
      if (error.response) {
        if(error.response.data.code === 'U004') {
          console.log("error status: U004")

          setErrorMessage('부모로 등록된 회원입니다.')
          openErrorModal()
        } else if(error.response.data.code === 'U006') {
          console.log("error status: U006")

          setErrorMessage('자녀는 자녀등록이 불가능합니다.')
          openErrorModal()
        } else if(error.response.data.code === 'U003') {
          console.log("error status: U003")

          setErrorMessage('이미 등록된 회원입니다.')
          openErrorModal()
        }
      }
    })
  }

  return (
    <div className="flex flex-col items-center">
      <div className="mt-10 text-green-500 text-2xl font-bold">
        <p>자녀 등록</p>
      </div>
      <div className="w-4/5 mt-9 h-[60vh]">
        <div className="flex flex-col items-center p-4 w-full h-full border-2 border-green-500 bg-white shadow-lg rounded-lg relative">

          {/* 자녀 번호 입력 창 */}
          <form action="" className="w-full">
            <p className="text-center my-2 text-base text-green-600">자녀의 휴대폰 번호 입력</p>
            <input 
              className="w-full text-center py-2 bg-white shadow-[0_0_15px_-5px_rgba(0,0,0,0.3)] focus:shadow-[0_0_20px_-5px_rgba(0,150,0,0.3)] text-gray-700 ring-1 ring-gray-300 focus:ring-green-600 rounded-lg outline-none" 
              placeholder="'-' 없이 입력해 주세요." inputMode="numeric"
              onChange={handleInputChange}
            />
          </form>

          {/* 오류 메시지 */}
          {/* {errorMessage && <p>{errorMessage}</p>} */}
          <div className="mt-5 w-3/4">
            {toast && (
              <ErrorToast
                setToast={setToast}
                message={errorMessage}
              />
            )}
          </div>

          <img src={familyImg} alt="family_img" className="w-40 my-4" />
          <p className="text-gray-500 text-sm">전화번호로 아이를 연결해서</p>
          <p className="text-gray-500 text-sm">우리 가족의 경제 활동을 관리해요.</p>

          {/* 등록하기 버튼 */}
          <button
            type="submit"
            className={`${
              isDisable ? 
              'bg-gray-400 cursor-not-allowed' : "bg-green-500 hover:bg-green-700"
            } text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-3/4 absolute bottom-10`}
            onClick={openRequestModal}
            disabled ={isDisable}
          >
            등록하기
          </button>
        </div>
      </div>
      {/* 요청 모달 */}
      <Modal isOpen={isRequestModalOpen} onClose={closeRequestModal}>
        <div className="w-full h-full relative">
          <div className="text-center font-bold absolute top-40 inset-x-0">
            <p>{phoneNumber}</p>
            <p>자녀 요청하시겠습니까?</p>
          </div>
          <div className="flex justify-center">
            <button
              type="submit"
              className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-3/4 absolute bottom-16"
              onClick={async () => {
                await registerChild(); // 자녀 등록 요청
                closeRequestModal();   // 요청 성공 시 모달 닫기
              }}>

              확인
            </button>
          </div>
        </div>
      </Modal>

      {/* 요청 성공 모달 */}
      <Modal isOpen={isSuccessModalOpen} onClose={closeSuccessModal}>
        <div className="w-full h-full relative">
          <div className="text-center absolute top-40 inset-x-0">
            <p className="font-bold">성공적으로 요청하였습니다</p>
          </div>
          <div className="flex justify-center">
            <button
              type="submit"
              className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-3/4 absolute bottom-16"
              onClick={closeSuccessModal}>
              확인
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
  )
}

export default ChildRegisterPage