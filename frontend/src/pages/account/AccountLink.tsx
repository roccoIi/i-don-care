import React, { useState, useEffect } from "react";
import bankStore from "../../store/bankStore";
import InputToast from "../../components/common/InputToast";
import ContentModal from '../../components/common/ContentModal';
import { Bank } from "../../store/bankStore";
import { fetchAccountAuth, fetchCanDoRegisterAccount } from "../../api/bankingApi";
import { useNavigate } from "react-router-dom";

interface AccountLinkProps {}

export interface AccountInfo {
  name: string;
  account: string;
}

export interface Account {
  accountNum: string;
}

const AccountLink: React.FC<AccountLinkProps> = function AccountLink() {
  const navigate = useNavigate();
  const banks = bankStore((state) => state.banks);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [yPosition, setY] = useState<number>(500);
  const [bankName, setBankName] = useState<string>("싸피은행");
  const [bankImg, setBankImg] = useState<string>("999");
  const [account, setAccount] = useState<string>("");
  const [accountList, setAccountList] = useState<Account[]>([]); 
  const [loadingAccounts, setLoadingAccounts] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null); 
  const [showGuide, setShowGuide] = useState<boolean>(true);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setShowGuide(false);
    }, 2000);

    return () => clearTimeout(timeout);
  }, []);


  function openToast() {
    setY(0);
  }
  function closeToast() {
    setY(500);
  }

  function openModal() {
    setIsModalOpen(true);
    setLoadingAccounts(true);
    setError(null);

    fetchCanDoRegisterAccount()
      .then((response) => {
        setAccountList(response.data);
        setLoadingAccounts(false);
      })
      .catch((error) => {
        console.error("계좌 리스트 불러오기 오류:", error);
        setError("계좌 정보를 불러오는 데 실패했습니다.");
        setLoadingAccounts(false);
      });
  }

  function closeModal() {
    setIsModalOpen(false);
  }

  function handleBank(data: Bank) {
    setBankName(data?.bankName);
    setBankImg(data?.bankCode);
    closeToast();
    closeModal();
  }

  function handleAuth() {
    fetchAccountAuth({ accountNum: account })
      .then((data) => {
        console.log(data);
        const accountInfo: AccountInfo = {
          name: bankName,
          account: account,
        };
        navigate("/account/certificate", { state: accountInfo });
      })
      .catch((error) => {
        console.log(error);
        const accountInfo: AccountInfo = {
          name: bankName,
          account: account,
        };
        navigate("/account/certificate", { state: accountInfo });
      });
  }

  function handleAccount(accountNum: string) {
    setAccount(accountNum);  // 계좌 번호 상태 업데이트
    closeModal();
  }

  return (
    <div className="w-full h-[92vh] flex flex-col items-center p-10">
      <h1 className="text-xl text-center">
        본인 명의의 계좌 정보를
        <hr className="border-none" />
        입력해 주세요.
      </h1>
      <div className="w-full mt-8">
        <label className="text-lg text-green-600">은행명</label>
        <button
          className="w-full h-14 mt-2 relative bg-white border-2 rounded-md border-gray-300 focus:border-green-600 flex items-center"
          onClick={openToast}
        >
          <div className="border-r-2 p-2 border-gray-300">
            <img
              src={"/bank_logos/" + bankImg + ".png"}
              alt="bank_logo"
              className="w-10 h-10 rounded-full"
            />
          </div>
          <p className="ml-4 text-gray-600">{bankName}</p>
          <span className="material-symbols-outlined text-gray-500 text-3xl absolute right-2">
            keyboard_arrow_down
          </span>
        </button>
      </div>
      <div className="w-full mt-8">
        <label className="text-lg text-green-600">계좌번호</label>
        <input
          type="number"
          value={account}
          onChange={(event) => setAccount(event.target.value)}
          placeholder="-없이 계좌번호 입력"
          inputMode="numeric"
          className="w-full h-12 mt-2 focus:ring-green-600 ring-1 ring-gray-300 rounded outline-none p-3"
        />
      </div>
      <div className="text-gray-500 text-sm w-full p-4">
        <ul className="list-disc">
          <li>은행 점검 시간에는 인증이 어렵습니다.</li>
          <li>인증 코드는 2시간 동안 유효합니다.</li>
        </ul>
      </div>
      <button
        className="text-xl text-white w-full h-16 fixed bottom-0"
        style={{ backgroundColor: account ? "#22c55e" : "#a1a1aa" }}
        disabled={!account}
        onClick={handleAuth}
      >
        1원 인증 요청하기
      </button>

      {/* Toast for Bank Selection */}
      <InputToast yPosition={yPosition} onClose={closeToast}>
        <span className="absolute right-6 material-symbols-outlined" onClick={closeToast}>
          close
        </span>
        <h1 className="font-bold text-lg m-2 text-center">은행 선택</h1>
        <ul className="grid grid-cols-3">
          {banks.map((bank, index) => (
            <li
              key={index}
              className="flex flex-col items-center my-4 text-sm"
              onClick={() => handleBank(bank)}
            >
              <img
                src={"/bank_logos/" + bank?.bankCode + ".png"}
                alt={bank?.bankName}
                className="w-10 h-10 my-2"
              />
              {bank?.bankName}
            </li>
          ))}
        </ul>
      </InputToast>

      {/* 가이드라인 메시지 */}
      {showGuide && (
        <div className="fixed bottom-44 right-10 bg-yellow-100 shadow-lg rounded-lg px-4 py-2 border border-yellow-300 transition ease-in-out">
          <p className="text-sm font-bold text-yellow-800">이용을 위해 계좌번호를 확인해보세요!</p>
          {/* 말풍선의 화살표를 아래쪽으로 변경 */}
          <div className="absolute w-0 h-0 border-l-8 border-r-8 border-t-8 border-transparent border-t-yellow-100 right-6 -bottom-2"></div>
        </div>
      )}

      {/* 계좌 리스트 모달 열기 버튼 */}
      <button
        className="fixed bottom-24 right-10 w-16 h-16 bg-green-600 rounded-full text-white shadow-lg shadow-gray-500 flex justify-center items-center"
        onClick={openModal}
      >
        <span className="material-symbols-outlined text-3xl">info</span>
      </button>

      {/* 계좌 리스트 모달 */}
      <ContentModal isOpen={isModalOpen} onClose={closeModal}>
        <h1 className="font-bold text-lg m-2 text-center">내 계좌 리스트</h1>
        {loadingAccounts ? (
          <p className="text-center">계좌 정보를 불러오는 중...</p>
        ) : error ? (
          <p className="text-center text-red-500">{error}</p>
        ) : (
          <ul className="flex flex-col space-y-4 overflow-y-auto max-h-[400px] mt-6 px-4">
            {accountList.map((account, index) => (
              <li
                key={index}
                className="flex flex-col items-center bg-white text-gray-700 rounded-lg border border-gray-300 shadow-sm py-4 px-6 transition-transform transform hover:scale-105 active:scale-95 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
                onClick={() => handleAccount(account.accountNum)}
                role="button"  
                tabIndex={0}  
              >
                <p className="w-full text-center text-lg font-medium">{account.accountNum}</p> {/* 계좌번호 출력 */}
              </li>
            ))}
          </ul>
        )}
      </ContentModal>

    </div>
  );
};

export default AccountLink;
