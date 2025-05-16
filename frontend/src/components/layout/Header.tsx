import { ChevronLeft } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

interface HeaderProps {
  title: string;
  showSaveButton?: boolean;
  onSaveClick?: () => void;
}

const Header = ({ title }: HeaderProps) => {
  const navigate = useNavigate();

  return (
    <header className='fixed top-0 z-10 flex items-center justify-center w-full h-12 px-4 bg-white max-w-mobile'>
      {/* 왼쪽: 뒤로가기 */}
      <button
        onClick={() => navigate(-1)}
        className='absolute text-gray-800 left-2'
        aria-label='뒤로가기'
      >
        <ChevronLeft />
      </button>

      {/* 가운데: 타이틀 */}
      <h1 className='text-base font-semibold text-gray-800 header-title'>
        {title}
      </h1>
    </header>
  );
};

export default Header;
