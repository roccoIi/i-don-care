import React from "react";

interface AccountHistoryItemProps {
  transactionName: string;
  cost: number;
  balance: number;
  createAt: string;
  transactionTypeName: string;
}

const AccountHistoryItem: React.FC<AccountHistoryItemProps> = function ({ transactionName, cost, balance, createAt, transactionTypeName }) {
  return (
    <div className='w-full flex justify-between p-4 bg-white items-center border-t-2'>
      <span>
        {transactionName ? (
          <h2 className='font-bold text-md'>{transactionName}</h2>
        ) : (
          <h2 className='font-bold text-md'>{transactionTypeName}</h2>
        )}
        <p className="text-sm text-gray-500">{createAt.slice(0,10)}</p>
      </span>
      <span className="text-right">
        <p className='text-lg font-bold'
        style={{'color': transactionTypeName?.slice(0,2) === '입금'? '#2563eb':'#dc2626'}}>
          {transactionTypeName?.slice(0,2) === '입금'? '+' : '-'} {cost?.toLocaleString()} 원
        </p>
        <p className="text-sm text-gray-500">잔액: {balance?.toLocaleString()}원</p>
      </span>
    </div>
  );
};

export default AccountHistoryItem;