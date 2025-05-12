import { RouterProvider } from 'react-router-dom';

import LoginProvider from '@/providers/LoginQueryProvider';
import { router } from '@/router';

const App = () => {
  return (
    <LoginProvider>
      <RouterProvider router={router} />
    </LoginProvider>
  );
};

export default App;
