import React from "react";
import { Outlet, useNavigate } from "react-router-dom";
import Header from "../../components/common/Header";

interface AccountLayoutProps {}

const AccountLayout: React.FC<AccountLayoutProps> = function AccountLayout () {
  const navigate = useNavigate()

  return (
    <div className="w-full h-full">
      <Header>
        <span className="material-symbols-outlined text-[30px] text-green-600"
        onClick={() => navigate(-1)}>
        arrow_back
        </span>
        <h1 className="text-lg text-green-600">계좌 등록</h1>
        <span className="material-symbols-outlined text-[30px] text-green-600"
        onClick={() => navigate('/main')}>
        home
        </span>
      </Header>
      <Outlet />
    </div>
  )
}

export default AccountLayout;