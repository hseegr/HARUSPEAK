import { Outlet, useMatches } from 'react-router-dom';

import Header from './Header';
import NavigationBar from './NavigationBar';

const BaseLayout = () => {
  const matches = useMatches();

  const currentTitle = matches
    .map(match => (match.handle as { title?: string })?.title)
    .filter(Boolean)
    .slice(-1)[0];

  const isHome = currentTitle === undefined;

  return (
    <div className='flex flex-col items-center bg-gray-100'>
      <div className='min-h-lvh w-full max-w-mobile bg-white'>
        {currentTitle && <Header title={currentTitle} />}
        <main
          className={`flex w-full overflow-y-auto px-4 pb-[80px] pt-[56px] ${isHome ? 'bg-haru-yellow' : 'bg-white'}`}
        >
          <Outlet />
        </main>
        <NavigationBar />
      </div>
    </div>
  );
};

export default BaseLayout;
