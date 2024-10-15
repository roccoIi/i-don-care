import React from "react";
import { useNavigate, useLocation } from "react-router-dom";

interface NavbarProps {}

const Navbar: React.FC<NavbarProps> = function Navbar () {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <div className="fixed bottom-0 flex w-full h-16 justify-evenly bg-white rounded-t-2xl">
      <div className="flex flex-col items-center">
          <span className="material-symbols-outlined text-4xl"
          style={{'fontVariationSettings': '"FILL" 1', color: location.pathname === "/main/mission" ? '#16a34a' : '#86efac'}}
          onClick={() => navigate('/main/mission')}>
          rocket_launch
        </span>
        미션
      </div>
      <div className="relative bottom-8 rounded-full bg-green-600 w-20 h-20 border-8 border-slate-100 flex justify-center items-center"
      onClick={() => navigate("/main")} >
        <span className="material-symbols-outlined text-4xl text-white"
        style={{'fontVariationSettings': '"FILL" 1'}}>
          home
        </span>
      </div>
      <div className="flex flex-col items-center"
      onClick={() => navigate("/main/savings")}>
        <span className="material-symbols-outlined text-4xl text-green-300"
        style={{'fontVariationSettings': '"FILL" 1', color: location.pathname === "/main/savings" ? '#16a34a' : '#86efac'}}>
          savings
        </span>
        저금통
      </div>
    </div>
  )
}

export default Navbar;