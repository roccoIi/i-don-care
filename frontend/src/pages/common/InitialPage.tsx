import React, {useEffect, useState} from "react";
import Logo from "../../assets/logo/initial_logo.png"
import { useNavigate } from "react-router-dom";
import { PasswordInfo } from "../user/SelectUserType";
import { refreshAccessToken } from "../../api/axiosInstance";
import { fetchSignupForParentTest, fetchSignupForChildTest } from "../../api/userApi";
import { isErrored } from "stream";

interface InitialPageProps {}

const InitialPage: React.FC<InitialPageProps> = function InitialPage () {
  const navigate = useNavigate();
  const [showButtons, setShowButtons] = useState<boolean>(false);
  useEffect(() => {
    const timer = setTimeout(() => {
      const token = localStorage.getItem("ACCESS_TOKEN");
      console.log(token)
      refreshAccessToken()
      .then((data) => {
        console.log(data)
        const passwordInfo: PasswordInfo = {
          status: 'login'
        }
        navigate('/user/password', { state: passwordInfo });
      })
      .catch((error) => {
        const originalRequest = error.config;
        console.log(error)
        if (error.response.status === 401 && !originalRequest._retry) {
          setShowButtons(true);
        }
      })
    }, 3000);
    return () => clearTimeout(timer);
  }, [navigate]);

  const handleIntegrateLogin = () => {
    navigate("/user/signup");
  };

  const handleParentBrowse = () => {
    fetchSignupForParentTest()
    .then((data) => {
      console.log(data)
      if (data?.data?.isExistUser === true) {
        const passwordInfo: PasswordInfo = {
          status: 'test-signin-parent'
        }
        navigate('/user/password', { state: passwordInfo });
      } else {
        const userId = data?.data?.userId
        navigate('/user/select-type', {state: userId})
      }
    })
    .catch((error) => {
      console.log(error)
    })
  };

  const handleChildBrowse = () => {
    fetchSignupForChildTest()
    .then((data) => {
      console.log(data)
      if (data?.data?.isExistUser === true) {
        const passwordInfo: PasswordInfo = {
          status: 'test-signin-child'
        }
        navigate('/user/password', { state: passwordInfo });
      } else {
        const userId = data?.data?.userId
        navigate('/user/select-type', {state: userId})
      }
    })
  };
  return (
    <div className="w-full h-full flex flex-col justify-center items-center bg-green-600">
      <div className="flex flex-col items-center mb-8">
        <img src={Logo} alt="initial_logo" className="w-28 mb-4" />
        <div className="text-center">
          <p className="text-white text-sm mb-2">내 손 안의 경제 교실</p>
          <h2 className="text-xl text-yellow-400 font-bold">I Dont Care</h2>
          <h1 className="text-white text-3xl font-bold">아이 돈 케어</h1>
        </div>
      </div>
      <div className="flex flex-col items-center">
        {showButtons && (
          <div className="mt-8 w-full flex flex-col items-center space-y-8">
            <div className="flex flex-col space-y-4">
              <a
                onClick={handleParentBrowse}
                className="text-white cursor-pointer text-lg flex items-center justify-center space-x-2 w-64 py-2 bg-transparent border border-white rounded-full transition-all duration-300 ease-in-out hover:bg-white hover:text-black hover:border-transparent hover:shadow-lg">
                <span className="relative z-10">부모로 미리보기</span>
              </a>
              <a
                onClick={handleChildBrowse}
                className="text-white cursor-pointer text-lg flex items-center justify-center space-x-2 w-64 py-2 bg-transparent border border-white rounded-full transition-all duration-300 ease-in-out hover:bg-white hover:text-black hover:border-transparent hover:shadow-lg">
                <span className="relative z-10">자녀로 미리보기</span>
              </a>
            </div>

            <button
              onClick={handleIntegrateLogin}
              className="w-64 h-14 bg-gradient-to-r from-yellow-400 to-orange-400 text-white font-bold text-xl rounded-full shadow-md hover:shadow-lg transition-all duration-300 ease-in-out transform hover:scale-105 relative overflow-hidden">
              <span className="absolute inset-0 bg-yellow-500 opacity-0 hover:opacity-20 transition-opacity duration-300"></span>
              <span className="relative z-10">통합 로그인</span>
            </button>
          </div>

        )}
      </div>
    </div>
  )
}

export default InitialPage;