import React from "react";
import ReactDOM from "react-dom";
import "../../css/main.css"

interface ChatBotToastProps {
  yPosition: number;
  chatHeight: number
  onClose: () => void;
  children: React.ReactNode;
}

const ChatBotToast: React.FC<ChatBotToastProps> = function ChatBotToast({yPosition, chatHeight, onClose, children}) {
  // if (yPosition) return null;
  return ReactDOM.createPortal(
    <>
      {!yPosition && (
      <div className="modal-overlay" onClick={onClose}></div>
      )}
      <div className="toast-chatbot-style overflow-y-auto"
      style={{
        bottom: `-${yPosition}px`, 
        height: `${chatHeight}px`,
      }}>
        <hr className="border-gray-500 border-2 w-10 absolute top-2 inset-x-1/2"
        style={{"transform": "translateX(-50%)"}} />
        {children}
      </div>
    </>,
    document.body
  )
}

export default ChatBotToast;