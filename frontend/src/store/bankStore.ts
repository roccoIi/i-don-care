import {create} from 'zustand';
import { persist } from 'zustand/middleware';

export interface Bank {
  bankCode: string;
  bankName: string;
}

export interface bankDetailInfo {
  accountBalance: number;
  accountCreateDate: string;
  accountExpireDate: string;
  accountName: string;
  accountNo: string;
  accountTypeName: string;
  bankName: string;
  dailyTransferLimit: number;
  lastTransactionDate: string;
  oneTimeTransferLimit: number;
}

interface bankInfo {
  bank: bankDetailInfo | null,
  setBank: (bank:bankDetailInfo) => void;
}

interface BankStore {
  banks: Bank[];
}

const bankStore = create<BankStore>((_) => ({
  banks: [
    {
      "bankCode": "001",
      "bankName": "한국은행"
    },
    {
      "bankCode": "002",
      "bankName": "산업은행"
    },
    {
      "bankCode": "003",
      "bankName": "기업은행"
    },
    {
      "bankCode": "004",
      "bankName": "국민은행"
    },
    {
      "bankCode": "011",
      "bankName": "농협은행"
    },
    {
      "bankCode": "020",
      "bankName": "우리은행"
    },
    {
      "bankCode": "023",
      "bankName": "SC제일은행"
    },
    {
      "bankCode": "027",
      "bankName": "시티은행"
    },
    {
      "bankCode": "032",
      "bankName": "대구은행"
    },
    {
      "bankCode": "034",
      "bankName": "광주은행"
    },
    {
      "bankCode": "035",
      "bankName": "제주은행"
    },
    {
      "bankCode": "037",
      "bankName": "전북은행"
    },
    {
      "bankCode": "039",
      "bankName": "경남은행"
    },
    {
      "bankCode": "045",
      "bankName": "새마을금고" 
    },
    {
      "bankCode": "081",
      "bankName": "KEB하나은행"
    },
    {
      "bankCode": "088",
      "bankName": "신한은행"
    },
    {
      "bankCode": "090",
      "bankName": "카카오뱅크"
    },
    {
      "bankCode": "999",
      "bankName": "싸피은행"
    }
  ]
}))

export const bankDetail = create<bankInfo>()(
  persist(
    (set) => ({
      bank: null,
      setBank: (bank: bankDetailInfo) => set({bank:bank}),
    }),
    {
      name: 'user-storage',
      getStorage: () => localStorage,
    }
  )
)

export default bankStore;