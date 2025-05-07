import TagBadge from '@/components/TagBadge';
import { formatMomentTime } from '@/lib/timeUtils';

// MomentCard를 참고하여 상세 페이지용 MomentDetailCard 컴포넌트 생성
interface MomentDetailProps {
  moment: {
    momentId: number;
    momentTime: string;
    images: string[];
    content?: string;
    tags?: string[];
  };
  onImageClick: (index: number) => void;
}

const MomentDetailCard = ({ moment, onImageClick }: MomentDetailProps) => {
  const formattedTime = formatMomentTime(moment.momentTime);

  return (
    <article className='flex w-full flex-col gap-4 rounded-xl bg-[#F1F0E9] p-5 shadow-md'>
      {/* 상단 - 시간 표시 */}
      <section className='flex justify-between'>
        <div className='rounded-full rounded-bl-none bg-[#FCFBF2] px-4 py-1.5 text-sm font-medium'>
          {formattedTime}
        </div>
      </section>

      {/* 중심부 - 컨텐츠 */}
      <section className='text-start'>
        {/* 이미지 섹션 */}
        {moment.images.length > 0 && (
          <div className='mb-5 grid grid-cols-2 gap-4 md:grid-cols-3 lg:grid-cols-4'>
            {moment.images.map((image, idx) => (
              <div
                key={`${image}-${idx}`}
                className='relative aspect-square cursor-pointer overflow-hidden rounded-xl'
                onClick={() => onImageClick(idx)}
              >
                <img
                  src={image}
                  alt={`순간 이미지 ${idx + 1}`}
                  onError={e => {
                    e.currentTarget.src = '/fallback-image.png';
                  }}
                  className='h-full w-full object-cover transition-transform hover:scale-105'
                />
              </div>
            ))}
          </div>
        )}

        {/* 내용 섹션 */}
        <div className='mb-4 whitespace-pre-wrap text-lg'>{moment.content}</div>
      </section>

      {/* 하단 - 태그 */}
      {moment.tags?.length && moment.tags.length > 0 && (
        <section className='flex flex-wrap items-center gap-2'>
          {moment.tags?.map((tag, idx) => (
            <TagBadge key={`${tag}-${idx}`} tag={tag} />
          ))}
        </section>
      )}
    </article>
  );
};

export default MomentDetailCard;
