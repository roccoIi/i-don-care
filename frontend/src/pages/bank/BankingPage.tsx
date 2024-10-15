import React, { useEffect, useState } from 'react';
import AccountHistoryItem from '../../components/bank/AccountHistoryItem';
import Modal from '../../components/common/ContentModal';
import InputToast from '../../components/common/InputToast';
import SelectAccount from '../../components/bank/SelectAccount';
import SelectBank from '../../components/bank/SelectBank';
import InputAccount from '../../components/bank/InputAccount';
import Remittance from '../../components/bank/Remittance';
import RemmitanceConfirm from '../../components/bank/RemittanceConfirm';
import RemittanceSuccess from '../../components/bank/RemittanceSuccess';
import { fetchReadBankStatement } from '../../api/bankingApi';
import { bankDetail } from '../../store/bankStore';

interface BankingPageProps {}

interface Transaction {
  transactionName: string;
  cost: number;
  balance: number;
  createAt: string;
  transactionTypeName: string;
}

const BankingPage: React.FC<BankingPageProps> = function BankingPage() {
  const [isModalOpen, setModalOpen] = useState<boolean>(false);
  const [yPosition, setY] = useState<number>(500);
  const [accountDepth, setAccountDepth] = useState<number>(1)
  const [bankName, setBank] = useState<string>("")
  const [account, setAccount] = useState<string>('')
  const [transaction, setTransaction] = useState<Transaction[]>([]);
  const [total, setTotal] = useState<number>(0)
  const {bank} = bankDetail()
  const [amount, setAmount] = useState<string>('')
  const [balance, setBalance] = useState<number>(0)

  useEffect(() => {
    if (bank) {
      setBalance(bank?.accountBalance)
      fetchReadBankStatement({
        accountNum: bank?.accountNo
      })
      .then((data) => {
        setTransaction(data?.data?.list)
        setTotal(data?.data?.list.reduce((sum: number, transaction: Transaction) => {
          if (transaction?.transactionTypeName?.slice(0,2) === "출금") {
              return sum + transaction?.cost;
          }
          return sum
      }, 0));
      })
      .catch((error) => {
        console.log(error)
      })
    }
  }, [])

  const openModal = () => setModalOpen(true);
  const closeModal = () => setModalOpen(false);

  function openToast() {
    setY(0);
    setAccountDepth(1);
  }
  function closeToast() {
    setY(500);
  }

  function accountRender() {
    switch (accountDepth) {
      case 1:
        return <SelectAccount setDepth={setAccountDepth} setAccount={setAccount} setBank={setBank} />;
      case 2:
        return <SelectBank setDepth={setAccountDepth} setAccount={setAccount} setBank={setBank} />;
      case 3:
        return <InputAccount setDepth={setAccountDepth} setAccount={setAccount} bankName={bankName} account={account} />;
      case 4:
        return <Remittance setDepth={setAccountDepth} bankName={bankName} account={account} amount={amount} setAmount={setAmount} accountBalance={balance} />
      case 5:
        return <RemmitanceConfirm setDepth={setAccountDepth} bankName={bankName} account={account} amount={amount} />
      case 6:
        return <RemittanceSuccess onClose={closeToast} />
    }

  }

  return (
    <div className='w-full flex flex-col items-center' style={{'height': '92vh'}}>
      <div className='w-full h-1/4 bg-green-500 flex flex-col items-center p-4 text-white text-lg relative'>
        <p className='text-sm flex items-center'>{bank?.bankName} {bank?.accountNo}
          <span className='material-symbols-outlined ml-1'
            onClick={openModal}>
            info
          </span>
        </p>
        <p className='text-xl mt-2'>계좌 잔액 {bank?.accountBalance?.toLocaleString()}원</p>
        <Modal isOpen={isModalOpen} onClose={closeModal}>
          <p className='font-bold text-lg my-2'>계좌 상세</p>
          <hr className='w-full border-gray-400 my-2' />
          <div className='my-1 text-sm leading-relaxed'>
            <p className='flex justify-between'><span className='text-gray-600'>계좌명</span><span>{bank?.accountName}</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>은행명</span><span>{bank?.bankName}</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>계좌번호</span><span>{bank?.accountNo}</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>상품구분</span><span>{bank?.accountTypeName}</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>계좌 개설일</span><span>{bank?.accountCreateDate?.substring(0,4)}년 {bank?.accountCreateDate?.substring(4,6)}월 {bank?.accountCreateDate?.substring(6,8)}일</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>계좌 만기일</span><span>{bank?.accountExpireDate?.substring(0,4)}년 {bank?.accountExpireDate?.substring(4,6)}월 {bank?.accountExpireDate?.substring(6,8)}일</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>1일 거래한도</span><span>{bank?.dailyTransferLimit}원</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>1회 거래한도</span><span>{bank?.oneTimeTransferLimit}원</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>계좌잔액</span><span>{bank?.accountBalance}원</span></p>
            <p className='flex justify-between'><span className='text-gray-600'>최종거래일</span><span>{bank?.lastTransactionDate}</span></p>
          </div>
          <hr className='w-full border-gray-400 my-2' />
        </Modal>
      </div>
      <div className='w-5/6 h-36 relative bottom-6 shadow-xl bg-white rounded-xl flex flex-col justify-center items-center'>
        <p>나의 소비금액: <span className='font-bold'>{total?.toLocaleString()}원</span></p>
        <button className='font-bold rounded w-20 h-8 border-2 border-green-500 text-green-500 mt-2'
        onClick={openToast}
        >송금하기</button>
        <InputToast yPosition={yPosition} onClose={closeToast}>
          <span className='absolute right-6 material-symbols-outlined'
          onClick={closeToast}>close</span>
          {accountRender()}
        </InputToast>
      </div>
      <div className='w-full flex justify-between p-4 text-lg'>
        <span className='text-base'>전체조회 ({transaction?.length}건)</span>
        <span className='text-sm text-gray-600'>최근 3개월</span>
      </div>
      <div className='w-full h-full overflow-y-auto'>
        {transaction?.length > 0 ? transaction.map((transaction, index) => (
          <AccountHistoryItem key={index} {...transaction} />
        )) : (
          <p>결제 내역이 존재하지 않습니다.</p>
        )}
        <div className='w-full h-[80px]'></div>
      </div>
    </div>
  );
}

export default BankingPage;