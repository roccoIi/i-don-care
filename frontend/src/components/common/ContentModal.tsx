import React from "react";
import ReactDOM from "react-dom";
import "../../css/main.css"
import "../../index.css"

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactNode;
}

const Modal: React.FC<ModalProps> = function({isOpen, onClose, children}) {
  if (!isOpen) return null;
  return ReactDOM.createPortal(
    <div className="modal-basic-style">
      <div className="modal-overlay" onClick={onClose} />
      <div className="modal-content-style w-3/4 h-1/2">
        {children}
        <button onClick={onClose} className="absolute top-2 right-4">X</button>
      </div>
    </div>,
    document.body
  )
}

export default Modal;