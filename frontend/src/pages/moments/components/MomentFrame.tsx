import { useNavigate } from 'react-router-dom';

interface MomentFrameProps {
  momentId?: number;
  summaryId?: number;
  momentTime: string;
  images: string[];
  content: string;
  tags: string[];
}

const MomentFrame = ({
  momentId,
  summaryId,
  momentTime,
  images,
  content,
  tags,
}: MomentFrameProps) => {
  const navigate = useNavigate();

  const handleClick = () => {
    if (summaryId) {
      // summaryId가 있을 경우 diary 상세 페이지로 이동
      console.log(`이동: /diary/${summaryId}`);
      navigate(`/diary/${summaryId}`);
    } else if (momentId) {
      // summaryId가 없을 경우 기존 moment 상세 페이지로 이동
      console.log(`이동: /moment/${momentId}`);
      navigate(`/moment/${momentId}`);
    }
  };

  return (
    <div
      className='cursor-pointer rounded border p-4 hover:shadow'
      onClick={handleClick}
    >
      <div className='mb-1 text-xs text-gray-500'>{momentTime}</div>
      <div className='mb-2 font-semibold'>{content}</div>
      <div className='mb-2 flex gap-2'>
        {images.map((img, idx) => (
          <img
            key={idx}
            src={img}
            alt={`moment-${momentId}-img-${idx}`}
            className='h-16 w-16 rounded object-cover'
          />
        ))}
      </div>
      <div className='flex flex-wrap gap-1'>
        {tags.map(tag => (
          <span key={tag} className='rounded bg-gray-200 px-2 py-1 text-xs'>
            {tag}
          </span>
        ))}
      </div>
    </div>
  );
};

export default MomentFrame;
