import React, { useEffect, useState } from "react";
import bankStore, {bankDetail} from "../../store/bankStore";
import { fetchSendCommon } from "../../api/bankingApi";

interface RemmitanceConfirmProps {
  setDepth: (depth: number) => void;
  bankName: string,
  account: string;
  amount: string;
}

const RemmitanceConfirm: React.FC<RemmitanceConfirmProps> = function RemittanceConfirm ({setDepth, bankName, account, amount }) {
  const banks = bankStore((state) => state.banks)
  const {bank} = bankDetail()
  const [bankImg, setBankImg] = useState<{ value?: string }>({});

  useEffect(() => {
    const findBank = banks.find((b) => b.bankName === bankName);
    setBankImg(findBank ? { value: findBank.bankCode } : {})
  }, [])

  function sendCommon () {
    if (bank) {
      fetchSendCommon({
        receiveAccountNum: account,
        amount: amount,
        sendAccountNum: bank?.accountNo
      })
      .then((data) => {
        console.log(data)
        if (data?.message === "SUCCESS") {
          setDepth(6)
        }
      })
      .catch((error) => {
        console.log(error)
      })
    }
  }

  return (
    <div>
      <p className="text-lg font-bold text-center">송금 확인</p>
      <div className="w-full h-full flex flex-col justify-center items-center">
        <img src={"/bank_logos/" + bankImg?.value + ".png"} alt="bank_logo"
          className="w-14 h-14 rounded-full my-5" />
        <p><span className="text-lg font-bold">{bankName} {account}</span> 계좌로</p>
        <p className="text-2xl"><span className="text-green-600 font-bold">{amount}</span>원을 보낼까요?</p>
        <hr className="w-full border-gray-300 my-5" />
        <p className="flex justify-between text-sm w-full"><span>보내는 계좌</span><span className="text-green-700">{bank?.bankName} {bank?.accountNo}</span></p>
        <p className="flex justify-between text-sm w-full my-1"><span>보내는 사람</span><span className="font-bold">User</span></p>
        <p className="flex justify-between text-sm w-full"><span>1회 이체한도</span><span>{bank?.oneTimeTransferLimit?.toLocaleString()}원</span></p>
        <hr className="w-full border-gray-300 my-4" />
        <button className="w-full h-12 bg-green-600 font-bold text-lg text-white rounded-lg"
        onClick={sendCommon}
        >송금하기</button>
        <button className="w-full h-12 bg-gray-300 font-bold text-lg rounded-lg my-2"
        onClick={() => setDepth(4)}
        >송금정보 변경</button>
      </div>
    </div>
  )
}

export default RemmitanceConfirm;