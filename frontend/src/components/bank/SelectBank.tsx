import React, { useEffect } from "react";
import bankStore from "../../store/bankStore";

interface SelectBankProps {
  setDepth: (depth: number) => void;
  setAccount: (account: string) => void;
  setBank: (bank: string) => void;
}

const SelectBank: React.FC<SelectBankProps> = function SelectBank ({setDepth, setAccount, setBank}) {
  const banks = bankStore((state) => state.banks)
  useEffect(() => {
    setAccount('')
    setBank('')
  },[])

  const handleBank = (data: string): void => {
    setBank(data)
    setDepth(3)
  }

  return (
    <div>
      <div className="flex items-center">
        <span className="material-symbols-outlined" onClick={(() => setDepth(1))}>
        arrow_back
        </span>
        <h1 className="font-bold text-xl m-2">송금할 은행 선택</h1>
      </div>
      <ul className="grid grid-cols-3">{banks.map((bank, index) => (
        <li key={index}
        className="flex flex-col items-center my-4 text-sm"
        onClick={(() => handleBank(bank?.bankName))}>
          <img src={`/bank_logos/${bank?.bankCode}.png`} alt={bank?.bankName}
          className="w-10 h-10 my-2" />
          {bank?.bankName}
        </li>
      ))}</ul>
    </div>
  )
}

export default SelectBank;