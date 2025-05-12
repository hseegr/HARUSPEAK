import { useNavigate } from 'react-router-dom';

import ImageGrid from '@/components/ImageGrid';
import TagBadge from '@/components/TagBadge';
import { formatDate, formatMomentTime } from '@/lib/timeUtils';
import { MomentContent } from '@/types/common';

interface MomentFrameProps {
  moment: MomentContent;
  isToday?: boolean;
}

const MomentFrame = ({ moment }: MomentFrameProps) => {
  const navigate = useNavigate();
  const { momentTime, content, images, tags, momentId, orderInDay } = moment;

  const formattedTime = formatMomentTime(momentTime);
  const formattedDate = formatDate(momentTime);

  const handleClick = () => {
    if (moment.summaryId && moment.momentId) {
      navigate(`/diary/${moment.summaryId}?momentId=${moment.momentId}`);
    } else if (moment.summaryId) {
      navigate(`/diary/${moment.summaryId}`);
    } else if (moment.momentId) {
      navigate(`/moment/${moment.momentId}`);
    }
  };

  return (
    <article
      className='flex w-full cursor-pointer flex-col gap-2 rounded-xl bg-haru-beige p-3'
      onClick={handleClick}
    >
      {/* 상단 */}
      <section className='flex items-center justify-between'>
        <div className='rounded-full rounded-bl-none bg-haru-yellow px-3 py-1 font-mont'>
          {formattedDate}의 {orderInDay}번째 순간
        </div>
        {orderInDay && (
          <div className='text-mg text-haru-gray-5'>{formattedTime}</div>
        )}
      </section>

      {/* 중심부 */}
      <section className='text-start'>
        <ImageGrid
          images={images}
          momentId={momentId}
          momentTime={momentTime}
        />

        <div className='font-leeseyoon' style={{ whiteSpace: 'pre-wrap' }}>
          {content}
        </div>
      </section>

      {/* 하단 - 태그 표시 */}
      {tags.length > 0 && (
        <section className='flex items-start'>
          <div className='flex flex-1 flex-wrap items-center gap-2'>
            {tags.map((tag: string, idx: number) => (
              <TagBadge key={`${tag}-${idx}`} tag={tag} />
            ))}
          </div>
        </section>
      )}
    </article>
  );
};

export default MomentFrame;
