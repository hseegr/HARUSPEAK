import { RouterProvider } from 'react-router-dom';

import LoginQueryProvider from '@/providers/LoginQueryProvider';
import { router } from '@/router';

const App = () => {
  return (
    <LoginQueryProvider>
      <RouterProvider router={router} />
    </LoginQueryProvider>
  );
};

export default App;
