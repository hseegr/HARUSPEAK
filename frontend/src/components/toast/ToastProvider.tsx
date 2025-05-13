import { ToastContainer } from 'react-toastify';

const ToastProvider = () => {
  return (
    <ToastContainer
      position='top-center'
      autoClose={3000}
      hideProgressBar
      newestOnTop
      closeOnClick
      pauseOnFocusLoss={false}
      draggable
      pauseOnHover
      theme='light'
    />
  );
};

export default ToastProvider;
