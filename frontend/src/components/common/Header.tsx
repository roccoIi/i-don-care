import React from "react";

interface HeaderProps {
  children: React.ReactNode;
}

const Header: React.FC<HeaderProps> = function Header({children}) {
  return(
    <div className="bg-white w-full h-[8vh] flex justify-between px-4 font-bold items-center drop-shadow-lg">
      {children}
    </div>
  )
}

export default Header;