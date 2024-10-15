import React, {useEffect} from "react";

interface ErrorToastProps {
  message: string,
  setToast: React.Dispatch<React.SetStateAction<boolean>>,
}

const ErrorToast: React.FC<ErrorToastProps> = function ErrorToast({message, setToast}) {
  useEffect(() => {
    const timer = setTimeout(() =>{
      setToast(false)
    }, 3000)

    return () => {
      clearTimeout(timer)
    }
  }, [setToast])

  return(
    <div className="bg-white shadow-lg border border-green-500 text-center font-bold text-xs rounded-lg py-1">
      <p>! {message}</p>
    </div>
  )
}

export default ErrorToast