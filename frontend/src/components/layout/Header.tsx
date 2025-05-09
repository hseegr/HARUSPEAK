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
    <header className='fixed top-0 z-10 flex h-12 w-full max-w-96 items-center justify-center bg-white px-4'>
      {/* 왼쪽: 뒤로가기 */}
      <button
        onClick={() => navigate(-1)}
        className='absolute left-2 text-gray-800'
        aria-label='뒤로가기'
      >
        <ChevronLeft />
      </button>

      {/* 가운데: 타이틀 */}
      <h1 className='header-title text-base font-semibold text-gray-800'>
        {title}
      </h1>
    </header>
  );
};

export default Header;
