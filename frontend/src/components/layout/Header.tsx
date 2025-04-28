import { ChevronLeft, Home } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

interface HeaderProps {
  title: string;
  showSaveButton?: boolean;
  onSaveClick?: () => void;
}

const Header = ({
  title,
  showSaveButton = false,
  onSaveClick,
}: HeaderProps) => {
  const navigate = useNavigate();

  return (
    <header className='flex h-12 items-center justify-center border-gray-200'>
      {/* 왼쪽: 뒤로가기 */}
      <button
        onClick={() => navigate(-1)}
        className='absolute left-4 text-gray-800'
      >
        <ChevronLeft />
      </button>

      {/* 가운데: 타이틀 */}
      <h1 className='text-base font-semibold text-gray-800'>{title}</h1>

      {/* 오른쪽: 홈 아이콘 + (선택) 저장 버튼 */}
      <div className='absolute right-4 flex items-center space-x-3'>
        <button onClick={() => navigate('/')} className='text-gray-800'>
          <Home size={20} />
        </button>
        {showSaveButton && (
          <button
            onClick={onSaveClick}
            className='text-sm font-medium text-green-700'
          >
            저장
          </button>
        )}
      </div>
    </header>
  );
};

export default Header;
