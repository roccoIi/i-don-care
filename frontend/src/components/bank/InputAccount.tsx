import React, { useEffect, useState } from "react";
import bankStore from "../../store/bankStore";

interface InputAccountProps {
  setDepth: (depth: number) => void;
  setAccount: (account: string) => void;
  bankName: string,
  account: string;
}

const InputAccount: React.FC<InputAccountProps> = function InputAccount ({setDepth, setAccount, bankName, account}) {
  const banks = bankStore((state) => state.banks)
  const [bankImg, setBankImg] = useState<{ value?: string }>({});

  useEffect(() => {
    const findBank = banks.find((b) => b.bankName === bankName);
    setBankImg(findBank ? { value: findBank.bankCode } : {})
  }, [])
  return (
    <div>
      <h1 className="font-bold text-xl my-2">받는 계좌 입력</h1>
      <div className="flex items-center my-6 relative"
      onClick={(() => setDepth(2))}>
        <img src={"/bank_logos/" + bankImg?.value + ".png"} alt="bank_logo"
        className="w-10 h-10 rounded-full mx-2 border-2" />
        <span>
          <p className="font-bold">{bankName}</p>
          <p className="text-xs text-gray-500">입금은행</p>
        </span>
        <span className="material-symbols-outlined absolute right-4">
        chevron_right
        </span>
      </div>
      <form action="">
        <input className="w-full bg-gray-100 h-12 rounded-md p-4 focus:ring-green-400 focus:ring-1 outline-none"
        type="text" value={account} placeholder="입금할 계좌번호 입력" inputMode="numeric"
        onChange={(event) => setAccount(event.target.value ? event.target.value : '')} />
      </form>
      <button className="absolute bottom-0 w-full h-14 text-lg font-bold left-1/2 transform -translate-x-1/2 text-white"
      style={{'backgroundColor': account ? '#22c55e' : '#a1a1aa'}}
      onClick={account ? (() => setDepth(4)) : undefined}
      disabled={!account}>
        다음
      </button>
    </div>
  )
}

export default InputAccount;