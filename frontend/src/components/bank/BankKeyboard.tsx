import React from "react";

interface BankKeyboardProps {
  amount: string;
  setAmount: (amount: string) => void;
  accountBalance: number;
  children: React.ReactNode;
}

const BankKeyboard: React.FC<BankKeyboardProps> = function BankKeyBoard({amount, setAmount, accountBalance, children}) {
  const input_nums = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '00', '0']

  function plusInputNums (num: number) {
    const newAmount = amount === ''? num : parseInt(amount) + num
    if (newAmount > accountBalance) {
      window.alert('잔액이 부족합니다.')
    } else {
      setAmount(newAmount.toString())
    }
  }

  function addInputNums (num: string) {
    const newAmount = amount + num;
    if (parseInt(newAmount) > accountBalance) {
      window.alert('잔액이 부족합니다.')
    } else {
      setAmount(newAmount);
    }
  };

  function deleteInputNums () {
    setAmount(amount.slice(0, -1))
  }

  return (
    <div className="flex flex-col items-center">
      {children}
      <div className="flex w-full justify-between mt-2">
        <button className="w-14 h-6 border border-green-700/50 text-xs font-bold rounded text-green-600 bg-white active:bg-gray-200"
        onClick={() => plusInputNums(1000)}>
          +1천
        </button>
        <button className="w-14 h-6 border border-green-700/50 text-xs font-bold rounded text-green-600 bg-white active:bg-gray-200"
        onClick={() => plusInputNums(5000)}>
          +5천
        </button>
        <button className="w-14 h-6 border border-green-700/50 text-xs font-bold rounded text-green-600 bg-white active:bg-gray-200"
        onClick={() => plusInputNums(10000)}>
          +1만
        </button>
        <button className="w-14 h-6 border border-green-700/50 text-xs font-bold rounded text-green-600 bg-white active:bg-gray-200"
        onClick={() => plusInputNums(50000)}>
          +5만
        </button>
        <button className="w-14 h-6 border border-green-700/50 text-xs font-bold rounded text-green-600 bg-white active:bg-gray-200"
        onClick={() => setAmount('')}>
          초기화
        </button>
      </div>
      <div className="w-full grid grid-cols-3 text-xl my-2 place-items-center">
        {input_nums.map((num, index) => (
          <button key={index}
          className="w-20 h-8 my-1 bg-white leading-loose rounded active:bg-gray-200"
          onClick={() => addInputNums(num)}>
            {num}
          </button>
        ))}
        <button className="w-20 h-10 my-1 bg-white flex items-center justify-center rounded active:bg-gray-200"
        onClick={deleteInputNums}>
          <span className="material-symbols-outlined">backspace</span>
        </button>
      </div>
    </div>
  )
}

export default BankKeyboard;