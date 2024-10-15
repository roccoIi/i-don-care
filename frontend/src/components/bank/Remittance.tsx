import React, { useEffect } from "react";
import BankKeyboard from "./BankKeyboard";

interface RemmitanceProps {
  setDepth: (depth: number) => void;
  bankName: string,
  account: string;
  amount: string;
  setAmount: (amount: string) => void;
  accountBalance: number;
}

const Remittance: React.FC<RemmitanceProps> = function Remittance ({setDepth, bankName, account, amount, setAmount, accountBalance}) {

  useEffect(() => {
    setAmount('')
  },[])

  return (
    <div>
      <span className="material-symbols-outlined" onClick={(() => setDepth(3))}>
      arrow_back
      </span>
      <BankKeyboard amount={amount} setAmount={setAmount} accountBalance={accountBalance}>
        <p className="mt-2"><span className="font-bold">{bankName} {account} </span>계좌로</p>
        {amount === '' ? (
          <p className="text-2xl font-bold text-zinc-400 m-6">얼마를 보낼까요?</p>
        ):(
          <p className="text-2xl font-bold m-6">{Intl.NumberFormat().format(parseInt(amount))}원</p>
        )}
        <button className="w-full h-8 bg-gray-100 rounded text-gray-600 text-sm"
        onClick={(() => setAmount(accountBalance?.toString()))}>
          출금가능액: <span className="text-black font-bold">{accountBalance?.toLocaleString()}</span>원
        </button>
      </BankKeyboard>
      <button className="absolute bottom-0 w-full h-14 text-lg font-bold left-1/2 transform -translate-x-1/2 text-white"
      style={{'backgroundColor': amount ? '#22c55e' : '#a1a1aa'}}
      onClick={amount ? (() => setDepth(5)) : undefined}
      disabled={!amount}>
        다음
      </button>
    </div>
  )
}

export default Remittance;