import { ChevronLeft, Home } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

interface HeaderProps {
  title: string;
  showSaveButton?: boolean;
  onSaveClick?: () => void;
}

const Header = ({ title }: HeaderProps) => {
  const navigate = useNavigate();

  return (
    <header className='fixed top-0 z-10 flex h-12 w-full max-w-96 items-center justify-between bg-white px-4'>
      {/* 왼쪽: 뒤로가기 */}
      <button onClick={() => navigate(-1)} className='text-gray-800'>
        <ChevronLeft />
      </button>

      {/* 가운데: 타이틀 */}
      <h1 className='header-title text-base font-semibold text-gray-800'>{title}</h1>

      {/* 오른쪽: 홈 아이콘 + (선택) 저장 버튼 */}
      <div className='flex items-center space-x-3'>
        <button onClick={() => navigate('/')} className='text-gray-800'>
          <Home size={20} />
        </button>
      </div>
    </header>
  );
};

export default Header;
