import React, {useState, useEffect} from "react";
import ChildAllowance from "./ChildAllowance";
import MainSaving from "./MainSaving";
import Tab from "../common/Tab";
import MainData from "../mydata/MainData";
import { userInfo } from "../../store/userStore";
import { selectedChildInfo } from "../../store/userStore";


const ChildRegistered: React.FC = () => {

  return (
    <div className="w-full h-3/5">
      <Tab>
        <ChildAllowance />
        <MainSaving />
        <MainData />
      </Tab>
    </div>
  )
}

export default ChildRegistered