import React, {useState, useRef, useEffect} from "react";
import MainSaving from "./MainSaving";
import ChatBotToast from "../common/ChatBotToast";
import { fetchUserInfo } from "../../api/userApi";
import ChildQuiz from "./ChildQuiz";
import MainData from "../mydata/MainData";
import axios from "axios";

const API_KEY = import.meta.env.VITE_OPENAI_API_KEY

const requestChatGpt = async (allMessages: { role: string, content: string }[]) => {
  const API_URL = "https://api.openai.com/v1/chat/completions"

  try {
    const response = await axios.post(
      API_URL,
      {
        model: "gpt-4o-mini",
        messages: allMessages,
      },
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${API_KEY}`,
        },
      }
    )
    console.log("gpt 사용중")
    return response.data.choices[0].message.content
  } catch(error) {
    console.log("Chat GPT Error: ", error)
  }
}

const ChildMain: React.FC = () => {
  const [yPosition, setY] = useState<number>(600);

  const [messages, setMessages] = useState([
    { sender: 'bot', text: '무엇이든 물어보세요!' },
  ])

  const [input, setInput] = useState('')
  const inputRef = useRef<HTMLTextAreaElement>(null)
  const messagesEndRef = useRef<HTMLDivElement>(null)
  const [isSending, setIsSending] = useState<boolean>(false)
  const [initialHeight, setInitialHeight] = useState(window.innerHeight)
  const [chatHeight, setChatHeight] = useState<number>(600)


  // //화면 크기 변경 감지 (가상 키보드의 출현을 체크)
  // useEffect(() => {
  //   const handleResize = () => {
  
      
  //     // // 가상 키보드가 떠 있을 때 높이가 감소하면 감지
  //     // if (windowHeight < initialHeight) {  // 임의의 값, 일반적인 모바일 뷰포트 크기보다 작을 때
  //     //   setY(400);
  //     // }
  //     if (window.visualViewport) {
  //         // setY(0); // toast 위치 조정
  //          // toast의 높이도 조정 (가상 키보드에 맞게
  //         if (window.visualViewport.height < window.innerHeight) {
  //           setChatHeight(400);
  //         } 
  //     } else {
  //       // setY(0); // 기본 위치로 조정
  //           setChatHeight(600); // 기본 높이로 되돌림
  //     }
  //     // if (window.visualViewport.height < window.innerHeight) {  
  //     //   // setY(0); // toast 위치 조정
  //     //   setChatHeight(400); // toast의 높이도 조정 (가상 키보드에 맞게)
  //     // } else {
  //     //   // setY(0); // 기본 위치로 조정
  //     //   setChatHeight(600); // 기본 높이로 되돌림
  //     // }
  //   };

  //   window.addEventListener('resize', handleResize);

  //   return () => {
  //     window.removeEventListener('resize', handleResize);
  //   };
  // }, [initialHeight]);


  useEffect(() => {
    const initialHeight = window.visualViewport ? window.visualViewport.height : window.innerHeight;
  
    const handleResize = () => {
      if (window.visualViewport) {
        // 가상 키보드가 나타났는지 여부를 체크
        if (window.visualViewport.height < initialHeight) {
          setChatHeight(400);  // 키보드가 나타나면 높이를 줄임
        } else {
          setChatHeight(600);  // 키보드가 사라지면 높이를 원래대로
        }
      } else {
        // visualViewport가 없을 때 기본 동작
        setChatHeight(600); 
      }
    };
  
    window.addEventListener('resize', handleResize);
  
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);


  useEffect(() => {
    fetchUserInfo()
    .then((data) => {
      console.log(data)
    })
    .catch((error) => {
      console.log(error)
    })
  }, [])

  const sendMessage = async() => {
    if (input.trim()) {
      const userMessage = input.trim()
      setIsSending(true)

      const newMessages = [...messages, { sender: 'user', text: userMessage }]
      const loadingMessages = [...newMessages, { sender: 'bot', text: "응답중..." }]
      setMessages(loadingMessages)
      setInput('')
      resetTextareaHeight()

      //  ChatGPT API 호출
      const gptResponse = await requestChatGpt(
        newMessages.map((msg) => ({ role: msg.sender === 'user' ? 'user' : 'system', content: msg.text }))
      )

      const responseMessages  = [...newMessages, { sender: "bot", text: gptResponse }];
      setMessages(responseMessages) 
      setIsSending(false)
    }
  }

  // 키보드 눌렀을 경우
  // const handleKeyDown = (event: React.KeyboardEvent<HTMLTextAreaElement>) => {
  //   if (event.key === 'Enter' && !event.shiftKey) {
  //     event.preventDefault()
  //     sendMessage()
  //   } else if (event.key === 'Enter' && event.shiftKey) {
  //     event.preventDefault()
  //     setInput(input + '\n')
  //   }
  // }

  const handleKeyDown = (event: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (event.key === 'Enter') {
      event.preventDefault()
      setInput(input + '\n')
    }
  }

  // 메세지 보내기 버튼 눌렀을 때
  const clickSend = () => {
    if (!isSending) {
      sendMessage()
    }
    // if (chatHeight === 600) {
    //   setChatHeight(400)
    // } else if(chatHeight === 400) {
    //   setChatHeight(600)
    // }

  }

  const adjustTextareaHeight = () => {
    if (inputRef.current) {
      inputRef.current.style.height = "auto"; // 높이를 초기화
      inputRef.current.style.height = `${inputRef.current.scrollHeight}px`; // 컨텐츠에 맞게 높이 설정
    }
  }

  const resetTextareaHeight = () => {
    if (inputRef.current) {
      inputRef.current.style.height = 'auto'; // textarea 높이를 초기 줄 수 (1줄)로 리셋
    }
  }

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    if (!isSending) {
      setInput(e.target.value)
      adjustTextareaHeight()
    }
  }

  const formatMessage = (message: string | undefined) => {
    if (message) {
      return message.split("\n").map((line, index) => (
        <span key={index}>
          {line}
          <br />
        </span>
      ))
    } else {
      return null
    }
  }

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }

  useEffect(() => {
    scrollToBottom(); // 메시지가 변경될 때마다 스크롤을 아래로 이동
  }, [messages]);

  function openToast() {
    setY(0);
  }
  function closeToast() {
    setY(600);
  }

  useEffect(() => {
    if (yPosition === 0 && inputRef.current) {
      inputRef.current.focus()
    }
  }, [yPosition])

  const chatRender = () => {
    return(
      <div className="bg-[#D8D8D8] w-full h-full overflow-y-auto flex-col justify-center rounded-xl">
        
        {/* Messages Section */}
        <div className="flex-1 flex-col-reverse overflow-y-auto min-h-full pt-4 pb-16 relative">
          {messages.map((message, index) => (
            <div
              key={index}
              className={`flex ${message.sender === 'user' ? 'justify-end mr-4': 'justify-start ml-4'} mb-6`}
            >
              <div
                className={`p-2 rounded-lg max-w-xs ${
                  message.sender === 'user' ? 'bg-green-500 text-white' : 'bg-gray-200'
                }`}
                style={{ wordBreak: 'break-word' }}
              >
                {formatMessage(message.text)}
              </div>
            </div>
          ))}

          {/* <div ref={messagesEndRef} /> */}

          {/* 입력창 */}
          <div className="flex-1 flex justify-center items-center absolute bottom-5 w-full">
              <textarea 
                className="w-4/6 h-auto max-h-40 bg-white rounded-3xl px-4 resize-none overflow-y-auto py-4" 
                placeholder="챗봇에게 물어보기"
                value={input}
                onChange={handleChange}
                onKeyDown={handleKeyDown}
                ref={inputRef}
                rows={1}
                disabled={isSending}
              />

              <button 
                className="w-1/6 h-14 bg-white rounded-3xl border-2 border-black ml-3 flex justify-center items-center" 
                onClick={clickSend}
                disabled={isSending}
              >
                  <span className="material-symbols-outlined text-3xl">
                    send
                  </span>
              </button>
          </div>
          <div ref={messagesEndRef} />
        </div>
      </div>
    )
  }

  return (
    <div className="h-3/4">
      <ChildQuiz openToast={openToast} />
      <MainSaving />
      <MainData />
      <ChatBotToast yPosition={yPosition} chatHeight={chatHeight} onClose={closeToast} >
        <span className='absolute right-8 top-8 z-10 material-symbols-outlined'
        onClick={closeToast}>close</span>
        {chatRender()}
      </ChatBotToast>
    </div>
  )
}

export default ChildMain