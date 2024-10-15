import React, {useState, useRef, useEffect} from "react";
import { fetchIntegrateVerify, fetchIntegrateSend, fetchSignup } from "../../api/userApi";
import { useNavigate } from 'react-router-dom';
import { PasswordInfo } from "./SelectUserType";

interface SignupFormProps {}

export interface UserInfo {
  user_name: string;
  birth: string;
  tel: string;
}

const SignupForm: React.FC<SignupFormProps> = function SignupForm () {
  const navigate = useNavigate();
  const [username, Setusername] = useState<string>('')
  const [firstRRN, setFirstRRN] = useState<string>('')
  const [secondRRN, setSecondRRN] = useState<string>('')
  const [phoneNumber, setPhoneNumber] = useState<string>('')
  const [phoneNumberVerify, setPhoneNumberVerify] = useState<string>('')
  const [showRRN, setShowRRN] = useState<boolean>(false)
  const [showPhoneNumber, setShowPhoneNumber] = useState<boolean>(false)
  const [showPhoneNumberVerify, setShowPhoneNumberVerify] = useState<boolean>(false)
  const [loginActive, setLoginActive] = useState<boolean>(false)
  const usernameRef = useRef<HTMLInputElement>(null)
  const firstRRNRef = useRef<HTMLInputElement>(null)
  const secondRRNRef = useRef<HTMLInputElement>(null)
  const phoneNumberRef = useRef<HTMLInputElement>(null)
  const phoneNumberVerifyRef = useRef<HTMLInputElement>(null)
  const [verifySuccess, setVerifySuccess] = useState<boolean>(false)
  const [timeLeft, setTimeLeft] = useState<number>(180);

  useEffect(() => {
    usernameRef.current?.focus();
  },[])

  useEffect(() => {
    if (showRRN) {
      firstRRNRef.current?.focus();
    }
  }, [showRRN]);

  useEffect(() => {
    if (showPhoneNumber) {
      phoneNumberRef.current?.focus();
    }
  }, [showPhoneNumber]);

  useEffect(() => {
    if (showPhoneNumberVerify) {
      phoneNumberVerifyRef.current?.focus();
    }
  }, [showPhoneNumberVerify]);

  useEffect(() => {
    let timer: ReturnType<typeof setInterval> | undefined;
    if (showPhoneNumberVerify && timeLeft > 0) {
      timer = setInterval(() => {
        setTimeLeft(prevTime => prevTime - 1);
      }, 1000);
    } else if (timeLeft === 0) {
      setShowPhoneNumberVerify(false);
      window.alert('인증 시간이 초과되었습니다. 다시 진행해 주세요.')
    }

    return () => {
      if (timer) {
        clearInterval(timer);
      }
    };
  }, [showPhoneNumberVerify, timeLeft]);

  useEffect(() => {
    if (verifySuccess) {
      fetchSignup({
        tel: phoneNumber,
        birth: firstRRN + secondRRN,
        userName: username
      })
      .then((data) => {
        console.log(data)
        if (data?.data?.isExistUser === true) {
          const passwordInfo: PasswordInfo = {
            status: 'login'
          }
          navigate('/user/password', { state: passwordInfo });
        } else {
          const userId = data?.data?.userId
          navigate('/user/select-type', {state: userId})
        }
      })
    }
  },[verifySuccess])

  function handleShowRRN() {
    if (username.length > 0) {
      setShowRRN(true)
    } else {
      window.alert('이름을 입력해 주세요.')
    }
  }

  function handleFirstRNNInput (data: string) {
    setFirstRRN(data)
    if (data.length === 6) {
      secondRRNRef.current?.focus();
    }
  }

  function handleShowPhoneNumber() {
    if (username.length > 0 && firstRRN.length === 6 && secondRRN.length === 1) {
      setShowPhoneNumber(true)
    } else {
      window.alert('이름/주민등록번호를 정확히 입력해 주세요.')
    }
  }

  function handlePhoneNumber() {
    if (phoneNumber.length === 11) {
      setTimeLeft(180);
      setShowPhoneNumberVerify(true)
      fetchIntegrateSend({
        tel: phoneNumber
      })
    } else {
      window.alert('전화번호를 정확히 입력해 주세요.')
    }
  }

  function handlePhoneNumberVerifyInput(data: string) {
    setPhoneNumberVerify(data)
    if (data.length === 6) {
      setLoginActive(true)
    }
  }

  function handleKeyEnter (event: React.KeyboardEvent<HTMLInputElement>) {
    if (event.key === 'Enter') {
      if (document.activeElement === usernameRef.current) {
        handleShowRRN()
      } else if (document.activeElement === firstRRNRef.current) {
        if (firstRRN.length === 6) {
          secondRRNRef.current?.focus();
        } else {
          window.alert('주민등록번호 6자리를 입력해 주세요.')
        }
      } else if (document.activeElement === secondRRNRef.current) {
        handleShowPhoneNumber()
      } else if (document.activeElement === phoneNumberRef.current) {
        handlePhoneNumber()
      }
    }
  }

  function handleVerify() {
    fetchIntegrateVerify({
      tel: phoneNumber,
      code: phoneNumberVerify
    })
      .then((response) => {
        if (response.status === 200) {
          setVerifySuccess(true);
        } else {
          window.alert('인증번호를 다시 입력해 주세요.');
        }
      })
      .catch((error) => {
        console.log(error);
        // error.response가 있는지 확인
        if (error.response && error.response.status === 400) {
          window.alert('인증코드가 유효하지 않습니다');
        } else {
          window.alert('서버와의 통신 중 오류가 발생했습니다.');
        }
      });
  }
  

  const formatTime = (seconds: number): string => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
  };


  return (
    <div className="w-full h-full flex items-center justify-center">
      <div className="w-5/6 h-2/3 flex flex-col items-center">
        <h1 className="text-3xl font-bold text-green-600">아이 돈 케어</h1>
        <hr className="w-full border-2 border-green-700/30 my-2" />
        <form onSubmit={(event) => event.preventDefault()} className="w-full p-2">
          <p className="text-xl font-bold text-green-600">이름</p>
          <input type="text" className="w-3/4 h-10 my-2 ring-2 ring-gray-300 focus:ring-green-400 outline-none rounded p-2"
          placeholder="이름" value={username} onChange={(event) => Setusername(event.target.value)}
          onKeyDown={handleKeyEnter} ref={usernameRef} />
          <button className="w-1/5 ml-3 h-10 bg-green-600 rounded text-white font-bold text-lg"
          onClick={handleShowRRN}>
            완료
          </button>
          {showRRN && (
            <>
              <p className="text-xl font-bold text-green-600 mt-2">주민등록번호</p>
              <div className="flex items-center">
                <input type="text" maxLength={6} className="w-1/2 h-10 my-2 ring-2 ring-gray-300 focus:ring-green-400 outline-none rounded p-2"
                placeholder="앞 6자리" value={firstRRN} inputMode="numeric" ref={firstRRNRef}
                onChange={(event) => handleFirstRNNInput(event.target.value)} onKeyDown={handleKeyEnter} />
                <span className="text-2xl mx-2 text-gray-500">-</span>
                <input type="text" maxLength={1} className="w-8 h-10 my-2 ring-2 ring-gray-300 focus:ring-green-400 outline-none rounded p-2"
                ref={secondRRNRef} value={secondRRN} inputMode="numeric"
                onChange={(event) => setSecondRRN(event.target.value)} onKeyDown={handleKeyEnter} />
                <span className="text-sm ml-1 text-gray-500">●●●●●●</span>
              </div>
              <button className="w-full h-10 bg-green-600 text-white font-bold text-lg rounded"
              onClick={handleShowPhoneNumber}>
                완료
              </button>
            </>
          )}
          {
            showPhoneNumber && (
              <>
                <p className="text-xl font-bold text-green-600 mt-4">전화번호</p>
                <input type="text" maxLength={11} className="w-3/4 h-10 my-2 ring-2 ring-gray-300 focus:ring-green-400 outline-none rounded p-2"
                placeholder="숫자만 입력" inputMode="numeric" value={phoneNumber} onChange={(event) => setPhoneNumber(event.target.value)}
                ref={phoneNumberRef} onKeyDown={handleKeyEnter}  />
                <button className="w-1/5 ml-3 h-10 bg-green-600 rounded text-white font-bold text-md"
                onClick={handlePhoneNumber}>
                  {showPhoneNumberVerify ? '재전송' : '인증'}
                </button>
              </>
            )
          }
          {
            showPhoneNumberVerify && (
              <>
                <div className="relative flex items-center">
                  <input type="text" maxLength={6} className="w-full h-10 ring-2 ring-gray-300 focus:ring-green-400 outline-none rounded p-2"
                  placeholder="인증번호 6자리 입력" value={phoneNumberVerify} ref={phoneNumberVerifyRef}
                  inputMode="numeric" onChange={(event) => handlePhoneNumberVerifyInput(event.target.value)} />
                  <p className="text-green-600 absolute right-2">{formatTime(timeLeft)}</p>
                </div>
                <button className="w-full h-12 mt-6 rounded text-lg font-bold text-white flex items-center justify-center"
                style={{'backgroundColor': loginActive ? '#16a34a' : '#a1a1aa'}}
                onClick={handleVerify} disabled={!loginActive}>
                  다음
                  <span className="material-symbols-outlined text-3xl">
                  chevron_right
                  </span>
                </button>
              </>
            )
          }
        </form>
      </div>
    </div>
  )
}

export default SignupForm;