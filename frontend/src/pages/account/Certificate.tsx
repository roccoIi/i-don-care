import React, { useState, useEffect, useRef } from "react";
import AccountImg from "../../assets/user/account_auth.png";
import { useLocation, useNavigate } from "react-router-dom";
import { AccountInfo } from "./AccountLink";
import { fetchAccountCheck, fetchAccountAuthNumber } from "../../api/bankingApi";
import ContentModal from '../../components/common/ContentModal';

interface CertificateProps {}

const Certificate: React.FC<CertificateProps> = function Certificate() {
  const location = useLocation();
  const navigate = useNavigate();
  const accountInfo = location?.state as AccountInfo;
  const [isKeyboardVisible, setIsKeyboardVisible] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const firstRef = useRef<HTMLInputElement>(null);
  const secondRef = useRef<HTMLInputElement>(null);
  const thirdRef = useRef<HTMLInputElement>(null);
  const fourthRef = useRef<HTMLInputElement>(null);
  const [firstNum, setFirstNum] = useState<string>('');
  const [secondNum, setSecondNum] = useState<string>('');
  const [thirdNum, setThirdNum] = useState<string>('');
  const [fourthNum, setFourthNum] = useState<string>('');
  const [authNumber, setAuthNumber] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [showGuide, setShowGuide] = useState<boolean>(true);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setShowGuide(false);
    }, 2000);

    return () => clearTimeout(timeout);
  }, []);

  function openModal() {
    setIsModalOpen(true);
    setLoading(true);
    setError(null);

    fetchAccountAuthNumber({ accountNum: accountInfo.account })
      .then((response) => {
        setAuthNumber(response.data.authNum);
        setLoading(false);
      })
      .catch((error) => {
        console.error("인증번호 불러오기 오류:", error);
        setError("인증번호를 불러오는 데 실패했습니다.");
        setLoading(false);
      });
  }

  function closeModal() {
    setIsModalOpen(false);
  }

  useEffect(() => {
    firstRef.current?.focus();
    const handleResize = () => {
      if (window.visualViewport) {
        setIsKeyboardVisible(window.visualViewport.height < window.innerHeight);
      }
    };

    if (window.visualViewport) {
      window.visualViewport.addEventListener('resize', handleResize);
    }
    return () => {
      if (window.visualViewport) {
        window.visualViewport.removeEventListener('resize', handleResize);
      }
    };
  }, []);

  useEffect(() => {
    if (firstNum.length >= 1) {
      secondRef.current?.focus()
    }
  }, [firstNum])

  useEffect(() => {
    if (secondNum.length >= 1) {
      thirdRef.current?.focus()
    }
  }, [secondNum])

  useEffect(() => {
    if (thirdNum.length >= 1) {
      fourthRef.current?.focus()
    }
  }, [thirdNum])

  function handleAccountCheck() {
    fetchAccountCheck({
      accountNum: accountInfo?.account,
      authNum: firstNum + secondNum + thirdNum + fourthNum
    })
      .then((data) => {
        console.log(data);
        navigate('/main');
      })
      .catch((error) => {
        console.log(error);
      });
  }

  return (
    <div className="w-full h-[92vh] flex justify-center items-center relative">
      <div className="w-full h-4/5 flex flex-col items-center">
        <h1 className="text-xl font-bold">입력된 계좌로 1원을 보냈습니다.</h1>
        <p className="text-sm mt-4">입금내역에 표시된 숫자 4자리를 입력해 주세요.</p>
        {!isKeyboardVisible && (
          <img src={AccountImg} alt="dummy_img" className="w-11/12 mt-6" />
        )}
        <div className="flex items-center w-5/6 justify-between my-4 text-sm text-gray-600">
          <p>{accountInfo?.name}</p>
          <p className="w-40">{accountInfo?.account}</p>
          <button className="text-sm underline underline-offset-4 font-bold"
          onClick={() => navigate('/account/link')}>변경하기
          </button>
        </div>
        <div className="w-5/6 grid grid-cols-4">
          <input type="text" maxLength={1} inputMode="numeric" ref={firstRef}
          value={firstNum} onChange={(event) => setFirstNum(event.target.value)}
          className="m-2 h-16 ring-1 ring-gray-400 rounded focus:ring-green-600 focus:drop-shadow-[0_0_2px_#86efac] outline-none p-5 text-2xl" />
          <input type="text" maxLength={1} inputMode="numeric" ref={secondRef}
          value={secondNum} onChange={(event) => setSecondNum(event.target.value)}
          className="m-2 h-16 ring-1 ring-gray-400 rounded focus:ring-green-600 focus:drop-shadow-[0_0_2px_#86efac] outline-none p-5 text-2xl" />
          <input type="text" maxLength={1} inputMode="numeric" ref={thirdRef}
          value={thirdNum} onChange={(event) => setThirdNum(event.target.value)}
          className="m-2 h-16 ring-1 ring-gray-400 rounded focus:ring-green-600 focus:drop-shadow-[0_0_2px_#86efac] outline-none p-5 text-2xl" />
          <input type="text" maxLength={1} inputMode="numeric" ref={fourthRef}
          value={fourthNum} onChange={(event) => setFourthNum(event.target.value)}
          className="m-2 h-16 ring-1 ring-gray-400 rounded focus:ring-green-600 focus:drop-shadow-[0_0_2px_#86efac] outline-none p-5 text-2xl" />
        </div>
        <p className="text-[10px] text-gray-600 my-2">입금 내역이 없다면 등록하신 계좌 정보를 다시 확인해 주세요.</p>
      </div>
      <button className="text-xl text-white w-full h-16 fixed bottom-0"
      style={{backgroundColor: firstNum && secondNum && thirdNum && fourthNum ? '#22c55e' : '#a1a1aa'}} disabled={!(firstNum && secondNum && thirdNum && fourthNum)}
      onClick={handleAccountCheck}>
        확인
      </button>

      {/* 가이드라인 메시지 */}
      {showGuide && (
        <div className="fixed bottom-44 right-10 bg-yellow-100 shadow-lg rounded-lg px-4 py-2 border border-yellow-300 transition ease-in-out">
          <p className="text-sm font-bold text-yellow-800">이용을 위해 인증번호를 확인해보세요!</p>
          <div className="absolute w-0 h-0 border-l-8 border-r-8 border-t-8 border-transparent border-t-yellow-100 right-6 -bottom-2"></div>
        </div>
      )}

      {/* 모달 버튼 */}
      <button
        className="fixed bottom-24 right-10 w-16 h-16 bg-green-600 rounded-full text-white shadow-lg shadow-gray-500 flex justify-center items-center"
        onClick={openModal}
      >
        <span className="material-symbols-outlined text-3xl">info</span>
      </button>

      {/* 인증번호 모달 */}
      <ContentModal isOpen={isModalOpen} onClose={closeModal}>
        <div className="flex flex-col justify-center items-center h-full">  {/* 모달 전체를 중앙 정렬 */}
          <h1 className="font-bold text-lg m-2 text-center">계좌 인증번호</h1>
          {loading ? (
            <p className="text-center">인증번호를 불러오는 중...</p>
          ) : error ? (
            <p className="text-center text-red-500">{error}</p>
          ) : (
            <div className="flex flex-col justify-center items-center h-full">
              <p className="text-center text-4xl font-bold text-green-600 mb-4">{authNumber}</p>  {/* 인증번호 크기 확대 */}
              <p className="text-center text-lg mb-4">코드창에 입력해주세요.</p>
            </div>
          )}
        </div>
      </ContentModal>
    </div>
  );
};

export default Certificate;