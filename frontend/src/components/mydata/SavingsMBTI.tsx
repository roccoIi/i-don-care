import React, { useEffect, useState } from 'react';
import { Radar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, ResponsiveContainer } from 'recharts';
import { bankDetail } from "../../store/bankStore";
import { fetchCategory } from '../../api/MyDataApi';
import { userInfo, selectedChildInfo } from '../../store/userStore';


export interface CategoryInfo {
  subject: string,
  A: number,
  fullMark: number
}

interface SavingsMBTIProps {}

const SavingsMBTI:React.FC<SavingsMBTIProps> = function SavingsMBTI() {
  const [data, setData] = useState<CategoryInfo[]>([])
  const {user} = userInfo()
  const {selectedChild} = selectedChildInfo()
  

  useEffect(() => {
    console.log(user)
    if (user && user?.role === "CHILD" && user?.parent?.relationId) {
      fetchCategory({relation_id: user?.parent?.relationId})
      .then((data) => {
        console.log("카테고리 총합", data)
        setData(data as CategoryInfo[])
      })
      .catch((error) => {
        console.log(error)
      })
    } else if (user && user?.role === "PARENT" && selectedChild) {
      fetchCategory({relation_id: selectedChild?.selectedChildRelationId})
      .then((data) => {
        console.log("카테고리 총합", data)
        setData(data as CategoryInfo[])
      })
      .catch((error) => {
        console.log(error)
      })
    }
  },[])
  if (data)
  return (
    <div className='w-full h-full'>
      <ResponsiveContainer width="100%" height="100%">
        <RadarChart cx="50%" cy="50%" outerRadius="70%" data={data}>
          <PolarGrid />
          <PolarAngleAxis dataKey="subject" tick={{ fontSize: 12 }} />
          <PolarRadiusAxis  tick={{ fontSize: 12 }} />
          <Radar name="강신구" dataKey="A" stroke="#8884d8" fill="#8884d8" fillOpacity={0.6} />
        </RadarChart>
      </ResponsiveContainer>
    </div>
  )
}

export default SavingsMBTI;