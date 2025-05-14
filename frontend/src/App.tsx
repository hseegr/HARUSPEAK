import { RouterProvider } from 'react-router-dom';

import LoginProvider from '@/providers/LoginQueryProvider';
import { router } from '@/router';

import ToastProvider from './components/toast/ToastProvider';

const App = () => {
  return (
    <LoginProvider>
      <ToastProvider />
      <RouterProvider router={router} />
    </LoginProvider>
  );
};

export default App;
