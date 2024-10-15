import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import { useEffect } from 'react';
import BankingPage from './pages/bank/BankingPage.tsx';
import ChildRegisterPage from './pages/main/ChildRegisterPage.tsx';
import SavingsPage from './pages/savings/SavingsPage.tsx';
import MainLayout from './pages/layout/MainLayout.tsx';
import MainPage from './pages/main/MainPage.tsx';
import UserLayout from './pages/layout/UserLayout.tsx';
import SignupForm from './pages/user/SignupForm.tsx';
import SelectUserType from './pages/user/SelectUserType.tsx';
import PasswordPage from './pages/user/PasswordPage.tsx';
import MissionPage from './pages/mission/MissionPage.tsx'
import InitialPage from './pages/common/InitialPage.tsx';
import AnimatedLayout from './pages/layout/AnimationLayout.tsx';
import AccountLayout from './pages/layout/AccountLayout.tsx';
import AccountLink from './pages/account/AccountLink.tsx';
import Certificate from './pages/account/Certificate.tsx';
import NotificationPage from './pages/notification/NotificationPage.tsx';
import MyData from './pages/main/MyData.tsx';
import Allowance from './pages/main/Allowance.tsx';

const router = createBrowserRouter([
  {
    path: "/",
    element: 
    <AnimatedLayout>
      <InitialPage />
    </AnimatedLayout>
  },
  {
    path: "/main",
    element: <MainLayout />,
    children: [
      { path: "", element: <MainPage /> },
      { path: "bank", element: <BankingPage /> },
      { path: "savings", element: <SavingsPage />},
      { path: "child-register", element: <ChildRegisterPage />},
      { path: "notification", element: <NotificationPage /> },
      { path: "mydata", element: <MyData />},
      { path: "mission", element: <MissionPage />},
      { path: "allowance", element: <Allowance />},
    ]
  },
  {
    path: "/user",
    element: <UserLayout />,
    children: [
      { path: "signup", element: <SignupForm />},
      { path: "select-type", element: <SelectUserType />},
      { path: 'password', element: 
        <AnimatedLayout>
          <PasswordPage />
        </AnimatedLayout> }
    ]
  },
  {
    path: '/account',
    element: <AccountLayout />,
    children: [
      { path: "link", element: <AccountLink /> },
      { path: "certificate", element: <Certificate />}
    ]
  },
])

function App() {
  function setScreenSize() {
    let vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty("--vh", `${vh}px`);
  }
  useEffect(() => {
    setScreenSize();
  });
  return (
      <div className='App'>
        <RouterProvider router={router} />
      </div>
  )
}

export default App
