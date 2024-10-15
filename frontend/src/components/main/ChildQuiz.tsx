import React from "react";
import { useEffect, useState, useRef } from "react";
import { fetchReadQuiz, fetchReviewQuiz } from "../../api/quizApi";
import { userInfo } from "../../store/userStore";
import Swal from "sweetalert2";
import { bankDetail } from "../../store/bankStore";

interface ChildQuizProps {
  openToast: () => void;
}

interface QuizInfo {
  answer: string,
  description: string,
  isSolved: boolean,
  level: number,
  question: string
}

const ChildQuiz:React.FC<ChildQuizProps> = function ChildQuiz({openToast}) {
  const [chatbotMotion, setChatbotMotion] = useState<boolean>(false)
  const chatbotRef = useRef<HTMLDivElement | null>(null)
  const [answer, setAnswer] = useState<string>('')
  const {user} = userInfo()
  const yesRef = useRef<HTMLButtonElement>(null)
  const noRef = useRef<HTMLButtonElement>(null)
  const [isSolved, setIsSolved] = useState<boolean>(false)
  const {bank} = bankDetail()

  const [quiz, setQuiz] = useState<QuizInfo>()

  const showSuccessModal = () => {
    Swal.fire({
      title: '정답입니다!',
      text: '축하해요! 퀴즈 상금이 적립되었어요.',
      icon: 'success',
      confirmButtonText: '확인'
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.reload()
      }
    });
  };

  const showFailModal = () => {
    Swal.fire({
      title: '오답입니다!',
      text: '오늘 내용을 복습해 볼까요?',
      icon: 'error',
      confirmButtonText: '확인'
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.reload()
      }
    });
  };
  
  useEffect(() => {
    if (user?.parent) {
      fetchReadQuiz({relationId: user?.parent?.relationId})
      .then((data) => {
        setQuiz(data?.data as QuizInfo)
      })
      .catch((error) => {
        console.log(error)
      })
    }
    const timer = setInterval(() => {
      setChatbotMotion(prev => !prev);
    }, 5000);
    return () => {
      clearInterval(timer);
    };
  },[])

  useEffect(() => {
    if (isSolved) {
      if (user?.parent && quiz) {
        if (quiz?.answer === answer) {
          showSuccessModal()
        } else {
          showFailModal()
        }
        fetchReviewQuiz({
          relationId: user?.parent?.relationId,
          realAnswer: quiz?.answer,
          userAnswer: answer
        })
        .then((data) => {
          console.log(data)
        })
        .catch((error) => {
          console.log(error)
        })
      }
    }
  },[answer])

  useEffect(() => {
    if (chatbotRef.current) {
      if (chatbotMotion) {
        chatbotRef.current.style.width = '72px'
      } else {
        chatbotRef.current.style.width = '0px'
      }
    }
  }, [chatbotMotion])

  function handleQuiz(ref: React.RefObject<HTMLButtonElement>) {
    if (ref.current) {
      console.log(ref.current.textContent);
      setAnswer(ref.current.textContent || '')
      setIsSolved(true)
    }
  }
  if (bank)
  return (
    <div className="w-full h-1/3 bg-white mt-8 mb-4 rounded-lg relative p-4 flex flex-col justify-center">
      <div className="w-2/3 h-8 bg-orange-500 rounded flex items-center justify-center text-white absolute -top-4 inset-x-1/2 translate-x-[-50%]">
      {quiz?.isSolved? "오늘의 경제 상식💡" : "오늘의 O/X 퀴즈"}
      </div>
      {quiz?.isSolved? (
        <div>
          <p className="text-sm text-center">{quiz?.question} <span className="font-bold text-red-600">{quiz?.answer}</span></p>
          <hr className="border border-gray-300 my-2" />
          <p className="text-sm text-gray-600">A: {quiz?.description}</p>
        </div>
      ) : (
        <>
          <div className="flex mt-2">
            <div className="text-xs mr-2 bg-green-600 w-[12%] h-5 text-white flex items-center justify-center rounded">Lv.{quiz?.level}</div>
            <p className="text-sm w-[87%]">{quiz?.question}</p>
          </div>
          <div className="flex w-full justify-center mt-3">
            <button className="w-20 h-16 border-gray-400 border mr-4 rounded focus:border-green-600 text-4xl font-bold text-red-600"
            onClick={() => handleQuiz(yesRef)}
            ref={yesRef}>
              O
            </button>
            <button className="w-20 h-16 border-gray-400 border rounded focus:border-green-600 text-4xl font-bold text-blue-600"
            onClick={() => handleQuiz(noRef)}
            ref={noRef}>
              X
            </button>
          </div>
        </>
      )}
      <div className="right-2 absolute bottom-2 transition ease-in duration-300"
      style={{'transform': chatbotMotion? 'translateX(-4rem)': ''}}
      onClick={openToast}>
        <button className="relative z-10 w-10 h-10 bg-green-600 rounded-full flex-col items-center text-white">
          <span className="material-symbols-outlined text-3xl">
          support_agent
          </span>
        </button>
        <div ref={chatbotRef} className="absolute z-0 top-1 left-4 h-8 border border-green-600 text-xs flex items-center justify-end p-1 bg-white rounded-lg shadow-md transition-all duration-300 ease-in text-green-600">
          {chatbotMotion? 'AI챗봇': ''}
        </div>
      </div>
    </div>
  )
  else return null;
}

export default ChildQuiz