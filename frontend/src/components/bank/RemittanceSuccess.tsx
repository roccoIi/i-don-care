import React from "react";

interface RemittanceSuccessProps {
  onClose: () => void;
}

const RemittanceSuccess: React.FC<RemittanceSuccessProps> = function RemittanceSuccess ({onClose}) {
  return (
    <div className="w-full h-full flex flex-col justify-center items-center">
      <div className="w-16 h-16 bg-green-500 rounded-xl flex justify-center items-center">
      <span className="material-symbols-outlined text-4xl font-bold text-white">
        check
      </span>
      </div>
      <p className="text-xl font-bold text-gray-700 my-8">송금이 완료되었습니다.</p>
      <button className="w-full h-12 bg-green-600 rounded-lg text-lg font-bold text-white"
      onClick={onClose}
      >확인</button>
    </div>
  )
}

export default RemittanceSuccess;