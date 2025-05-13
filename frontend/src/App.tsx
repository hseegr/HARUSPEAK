import { RouterProvider } from 'react-router-dom';

import LoginProvider from '@/providers/LoginQueryProvider';
import { router } from '@/router';

import ToastProvider from './components/toast/ToastProvider';

const App = () => {
  return (
    <LoginProvider>
      <RouterProvider router={router} />
      <ToastProvider />
    </LoginProvider>
  );
};

export default App;
