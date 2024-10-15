import React from 'react';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  message: string;
}

const Modal: React.FC<ModalProps> = ({ isOpen, onClose, message }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center">
      <div className="bg-white rounded-lg p-6 max-w-sm w-full relative">
        <button onClick={onClose} className="absolute top-3 right-3 text-gray-500 hover:text-gray-700">
          &times;
        </button>
        <div className="text-center">
          {/* 메시지에서 줄바꿈(\n)을 <br />로 변환하여 출력 */}
          {message.split('\n').map((line, index) => (
            <React.Fragment key={index}>
              {line}
              <br />
            </React.Fragment>
          ))}
          <button onClick={onClose} className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded mt-4">
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default Modal;