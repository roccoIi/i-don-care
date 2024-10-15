import React from "react";
import { Outlet } from "react-router-dom";

interface AccountLayoutProps {}

const AccountLayout: React.FC<AccountLayoutProps> = function AccountLayout () {
  return (
    <div className="w-full h-full">
      <Outlet />
    </div>
  )
}

export default AccountLayout;