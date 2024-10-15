import React, {useState, useEffect} from "react";
import { fetchSendResent } from "../../api/bankingApi";
import bankStore from "../../store/bankStore";

interface SelectAccountProps {
  setDepth: (depth: number) => void;
  setAccount: (account: string) => void;
  setBank: (bank: string) => void;
}

interface Account {
  userName: string,
  bankName: string,
  accountNum: string,
  bankLogo?: string;
}

const SelectAccount: React.FC<SelectAccountProps> = function SelectAccount({setDepth, setAccount, setBank}) {
  const [accounts, setAccounts] = useState<Account[]>([])
  const banks = bankStore((state) => state.banks)

  useEffect(() => {
    fetchSendResent()
    .then((data) => {
      const AccountsData = data?.data || [];
      const updatedAccounts = AccountsData.map((account:Account) => {
        const bank = banks.find(b => b.bankName === account.bankName);
        return {
          ...account,
          bankLogo: bank?.bankCode
        };
      });
      setAccounts(updatedAccounts)
    })
    .catch((error) => {
      console.log(error)
    })
  },[])

  const handleAccount = (data: Account): void => {
    setAccount(data?.accountNum)
    setBank(data?.bankName)
    setDepth(4)
  }
  return(
    <div>
      <h1 className="font-bold text-xl my-4">최근 송금한 계좌 목록</h1>
      {accounts.length > 0 ? accounts.map((account, index) => (
        <div key={index} className="p-2 my-2 shadow-md bg-sky-700/10 rounded-lg flex items-center"
        onClick={() => handleAccount(account)}
        >
          <img src={"/bank_logos/" + account?.bankLogo + ".png"} alt="bank_logo"
          className="w-10 h-10 rounded-full mr-2" />
          <span>
            <p className="text-md">{account?.userName}</p>
            <p className="text-xs text-gray-600">{account?.bankName} {account?.accountNum}</p>
          </span>
        </div>
      )) : (
        <p className="text-lg text-gray-600 my-4">최근 송금한 계좌가 없어요.</p>
      )}
      <button className="w-full h-12 bg-green-500 my-4 rounded-lg text-white font-bold text-lg"
      onClick={(() => setDepth(2))}>계좌 직접 입력</button>
    </div>
  )
}

export default SelectAccount;