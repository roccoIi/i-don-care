import React from "react";
import { PieChart, Pie, Cell } from 'recharts';

interface SavingsGraphProps {
  score: number;
}

const SavingsGraph:React.FC<SavingsGraphProps> = function SavingsGraph({score}) {
  const data = [
    {name: 'score', value: score},
    {name: 'score-remain', value: 100-score}
  ]

  const colors = ["#facc15", "#e5e7eb"]

  return (
    <div className="w-24 h-24 flex justify-center items-center my-2 rounded-full">
      <PieChart width={100} height={100}>
        <Pie
        data={data}
        cx={45}
        cy={45}
        innerRadius={40}
        outerRadius={48}
        fill="#166534"
        paddingAngle={0}
        startAngle={90}
        endAngle={-270}
        dataKey="value">
          {data.map((_, index) => (
            <Cell key={`cell-${index}`} fill={colors[index % colors.length]} />
          ))}
        </Pie>
        <text
          x={52} 
          y={52} 
          textAnchor="middle" 
          dominantBaseline="middle" 
          fontSize={30} 
          fontWeight='bold'
          fill="#facc15">{score}</text>
      </PieChart>
    </div>
  )
}

export default SavingsGraph;