import React, { useEffect, useState } from "react";
import Header from "../../components/common/Header";
import Navbar from "../../components/common/Navbar";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { userInfo } from "../../store/userStore";
import { fetchNotificationCount } from "../../api/notificationApi"; // API 함수 임포트
import { fetchBankDetail } from "../../api/bankingApi";
import { fetchLogout } from "../../api/userApi";
import Swal from "sweetalert2";

interface MainLayoutProps {}

const MainLayout: React.FC<MainLayoutProps> = function MainLayout() {
  const navigate = useNavigate();
  const { user } = userInfo();
  const [notificationCount, setNotificationCount] = useState<number>(0);

  const showWarningModal = () => {
    Swal.fire({
      title: "계좌 미등록 사용자",
      text: "등록된 계좌가 없어 계좌 연동 페이지로 이동합니다.",
      icon: "warning",
      confirmButtonText: "확인",
    });
  };

  const showConfirmModal = () => {
    return Swal.fire({
      title: "로그아웃",
      text: "정말 로그아웃 하시겠습니까?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "확인",
      cancelButtonText: "취소",
      reverseButtons: true,
    });
  };

  const showLogoutSuccessAlert = () => {
    return Swal.fire({
      title: "로그아웃 되었습니다.",
      text: "다음에 또 만나요!",
      icon: "success",
      confirmButtonText: "확인",
    });
  };
  

  useEffect(() => {
    console.log("사용자 정보", user);

    const getNotificationCount = async () => {
      try {
        const response = await fetchNotificationCount();
        setNotificationCount(response.data);
      } catch (error) {
        console.error("Error fetching notification count:", error);
      }
    };
    getNotificationCount();
  }, []);

  function handleNavigate() {
    if (user?.role === "PARENT") {
      fetchBankDetail()
        .then((data) => {
          navigate("/main/allowance", {
            state: { accountBalance: data?.data?.accountBalance },
          });
        })
        .catch((error) => {
          console.log(error);
          showWarningModal();
          navigate("/account/link");
        });
    } else {
      navigate("/main/mydata");
    }
  }

  async function handleLogout() {
    const result = await showConfirmModal();
    if (result.isConfirmed) {
      fetchLogout();
      localStorage.removeItem("ACCESS_TOKEN");
      await showLogoutSuccessAlert();
      navigate("/", { replace: true })
    }
  }

  const location = useLocation();
  return (
    <div className="w-full h-full relative">
      <Header>
        <div className="flex items-center space-x-4">
          {location.pathname === "/main" ? (
            <span
              className="material-symbols-outlined text-[30px]"
              onClick={handleNavigate}
            >
              account_circle
            </span>
          ) : (
            <span
              className="material-symbols-outlined text-[30px]"
              onClick={() => navigate(-1)}
            >
              arrow_back
            </span>
          )}
        </div>
        <div className="absolute left-1/2 transform -translate-x-1/2">
          <span>아이 돈 케어</span>
        </div>
        <div className="flex items-center space-x-4">
          <span
            className="material-symbols-outlined text-[30px]"
            onClick={handleLogout}
          >
            logout
          </span>
          <span
            className="material-symbols-outlined text-[30px]"
            onClick={() => navigate("/main/notification")}
          >
            notifications
          </span>
          {notificationCount > 0 && (
            <span className="absolute top-8 right-2 bg-red-500 text-white rounded-full text-xs w-5 h-5 flex items-center justify-center">
              {notificationCount}
            </span>
          )}
        </div>
      </Header>
      <Outlet />
      <Navbar />
    </div>
  );
};

export default MainLayout;
