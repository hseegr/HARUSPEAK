import { Outlet } from 'react-router-dom';

import Header from './Header';
import NavigationBar from './NavigationBar';

const BaseLayout = () => {
  return (
    <div className='flex flex-col items-center'>
      <div className='flex w-full max-w-96 flex-col'>
        {/* 임시로 타이틀 전달 */}
        <Header title="Haru's Peak" />
        <main className='flex w-full flex-1 overflow-y-auto'>
          <Outlet />
        </main>
        <NavigationBar />
      </div>
    </div>
  );
};

export default BaseLayout;
