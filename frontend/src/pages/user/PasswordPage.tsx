import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { PasswordInfo } from "./SelectUserType";
import { fetchSimple,fetchSimpleLogin } from "../../api/userApi";


interface PasswordPageProps {}

const PasswordPage: React.FC<PasswordPageProps> = function PasswordPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const security_keys = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0']
  const [password, setPassword] = useState<string>('')
  const [activeIndex, setActiveIndex] = useState<number | null>(null)
  const [highlightedIndex, setHighlightedIndex] = useState<number | null>(null)
  const passwordInfo = location?.state as PasswordInfo

  useEffect(() => {
    console.log(passwordInfo)
    if (passwordInfo?.status === 'test-signin-parent') {
      fetchSimpleLogin({password:'999999'})
      .then((response) => {
        const accessToken = response.headers.authorization
        console.log(response)
        console.log("accessToken:", accessToken)
        if (accessToken) {
          localStorage.setItem('ACCESS_TOKEN', accessToken);
          navigate('/main')
        } else {
          navigate('/user/signup')
        }
      })
      .catch((error) => {
        window.alert('로그인 실패')
        console.log(error)
        setPassword('')
      })
    } else if (passwordInfo?.status === 'test-signin-child') {
      fetchSimpleLogin({password:'888888'})
      .then((response) => {
        const accessToken = response.headers.authorization
        console.log(response)
        console.log("accessToken:", accessToken)
        if (accessToken) {
          localStorage.setItem('ACCESS_TOKEN', accessToken);
          navigate('/main')
        } else {
          navigate('/user/signup')
        }
      })
      .catch((error) => {
        window.alert('로그인 실패')
        console.log(error)
        setPassword('')
      })
    } 
    
    if (password.length === 6) {
      if (passwordInfo?.status === 'signup') {
        const nextPasswordInfo: PasswordInfo = {
          status: 'check',
          ...(passwordInfo.userId !== undefined && { userId: passwordInfo.userId }),
          ...(password !== undefined && { passwordInput: password })
        }
        navigate('/user/password', {state: nextPasswordInfo})
        window.location.reload()
      } else if (passwordInfo?.status === 'check') {
        if (passwordInfo?.userId){
          if (passwordInfo?.passwordInput === password) {
            fetchSimple({
              password: password,
              userId: passwordInfo.userId
            })
            .then((response) => {
              if (response.status === 200) {
                window.alert('환영합니다. 아이 돈 케어의 서비스를 즐겨 보세요!')
                fetchSimpleLogin({password:password})
                navigate('/main')
              } else {
                window.alert('회원가입 실패')
              }
            })
            .catch((error) => {
              window.alert(`비밀번호 설정 실패`)
              console.log(error)
              setPassword('')
            })
          } else {
            window.alert("간편비밀번호가 일치하지 않습니다.")
            setPassword('')
          }
        }
      } else if (passwordInfo?.status === 'login') {
        fetchSimpleLogin({password:password})
        .then((response) => {
          const accessToken = response.headers.authorization
          console.log(response)
          console.log("accessToken:", accessToken)
          if (accessToken) {
            localStorage.setItem('ACCESS_TOKEN', accessToken);
            navigate('/main')
          } else {
            navigate('/user/signup')
          }
        })
        .catch((error) => {
          window.alert('로그인 실패')
          console.log(error)
          setPassword('')
        })
      } else {
        navigate('/user/signup')
      }
    }
  },[password])

  const shuffleArray = (array: string[]): string[] => {
    const shuffledArray = [...array];
    for (let i = shuffledArray.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [shuffledArray[i], shuffledArray[j]] = [shuffledArray[j], shuffledArray[i]];
    }
    return shuffledArray;
  };

  const passwordStatus = () => {
    return Array.from({length:6}, (_, index) => (
      <span key={index} style={{'color': index+1 > password.length ? "#166534" : "#ffffff"}}>●</span>
    ))
  }

  function plusInput (num:string, index: number) {
    const newInput = password + num
    setActiveIndex(index);
    let newHighlightedIndex;
    do {
      newHighlightedIndex = Math.floor(Math.random() * 10)
    } while (newHighlightedIndex === index)

    setHighlightedIndex(newHighlightedIndex)
    if (newInput.length > 6) {
      window.alert('6자리를 초과했습니다.')
    } else {
      setPassword(newInput)
    }
    setTimeout(() =>{
      setActiveIndex(null)
      setHighlightedIndex(null)
    }, 150)
  }

  function deleteInput () {
    setPassword(password.slice(0,-1))
  }
  
  const [random_keys, setRandomKeys] = useState<string[]>(shuffleArray(security_keys));

  return (
    <div className="w-full h-full bg-green-600">
      <header className="w-full h-[10vh] flex justify-center items-center text-2xl font-bold text-white">
        아이 돈 케어
      </header>
      <div className="h-[50vh] w-full flex items-center">
        <div className="h-1/2 w-full flex flex-col justify-evenly items-center">
          <h1 className="text-xl text-white text-center">간편비밀번호 {passwordInfo?.status === 'signup' ? '설정' : passwordInfo?.status === 'check' ? '확인' : ''}
            <hr className="border-none" />6자리를 입력해 주세요.
          </h1>
          <div className="w-2/3 grid grid-cols-6 place-items-center text-2xl text-white">
            {passwordStatus()}
          </div>
        </div>
      </div>
      <div className="h-[40vh] w-full bg-white grid grid-cols-3 place-items-center text-xl">
        {random_keys.slice(0, 9).map((key, index) => (
          <button key={index} onClick={() => plusInput(key, index)}
          className={`w-full h-full border transition ${index === highlightedIndex || index === activeIndex ? 'bg-gray-200' : ''}`}>{key}</button>
        ))}
        <button className="w-full h-full border active:bg-gray-200" onClick={() => setRandomKeys(shuffleArray(security_keys))}>
          <span className="material-symbols-outlined">
          cached
          </span>
        </button>
        <button onClick={() => plusInput(random_keys[9], 9)}
        className={`w-full h-full border active:bg-gray-200 ${highlightedIndex === 9 ? 'bg-gray-200' : ''}`}>{random_keys[9]}</button>
        <button className="w-full h-full border active:bg-gray-200" onClick={deleteInput}>
          <span className="material-symbols-outlined">
          backspace
          </span>
        </button>
      </div>

    </div>
  )
}

export default PasswordPage;