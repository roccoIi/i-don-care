import React from 'react';

interface ProgressBarProps {
  currentAmount: number
  targetAmount: number
}

const ProgressBar: React.FC<ProgressBarProps> = ({ currentAmount, targetAmount }) => {
  const percentage = Math.min((currentAmount / targetAmount) * 100, 100)

  return (
    <div>
      <div className="w-full h-3 bg-gray-200 rounded-full">
        <div
          className="h-3 rounded-full"
          style={{
            width: `${percentage}%`,
            backgroundColor: 'green',
          }}
        >
        </div>
      </div>
      <div className="mt-4 text-sm text-gray-500">
        <p>
          {currentAmount}원 / {targetAmount}원 
            <span className="text-[#00712D]"> ({percentage.toFixed(0)}% 달성)</span>
        </p>
      </div>
    </div>
  )
}

export default ProgressBar;