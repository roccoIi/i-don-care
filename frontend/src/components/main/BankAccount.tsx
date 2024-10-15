import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import bankStore, {bankDetail} from "../../store/bankStore";

const BankAccount: React.FC = () => {
  const banks = bankStore((state) => state.banks)
  const [bankImg, setBankImg] = useState<{ value?: string }>({});
  const { bank } = bankDetail()

  const navigate = useNavigate()

  useEffect(() => {
    const findBank = banks.find((b) => b.bankName === bank?.bankName);
    setBankImg(findBank ? { value: findBank.bankCode } : {})
  }, [])

  const goBankPage = () => {
    if (bank) {
      navigate('/main/bank')
    } else {
      navigate('/account/link')
    }
  }

  return (
    <div className='flex flex-col relative items-center justify-center box-border bg-green-700 rounded-lg w-full h-1/5 p-4'
    onClick={goBankPage}>
      {bank? (
        <>
          <div className='flex w-full h-1/4 items-center'> 
            <img src={"/bank_logos/" + bankImg?.value + ".png"} className='w-5 h-5 mr-2'/>
            <h1 className='text-xs text-white'>{bank?.accountName}</h1>
          </div>
          <div className='w-5/6 text-white mt-3'>
            <p className='text-xs text-gray-300'>{bank?.accountNo}</p>
            <p className='text-lg'>잔액 {bank?.accountBalance?.toLocaleString()}원</p>
            <button className='absolute right-3 bottom-3 w-16 h-6 bg-green-900 rounded text-white text-xs'>
              계좌 상세
            </button>
          </div>
        </>
      ):(
        <div className='text-white/70 text-base text-center'>
          <p>아직 계좌가 등록되지 않았어요.</p>
          <p>계좌를 등록하러 가 볼까요?</p>
        </div>
      )}
    </div>
  )
}

export default BankAccount;