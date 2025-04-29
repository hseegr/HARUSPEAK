import { useState } from 'react';

import TagBadge from '@/components/TagBadge';
import { createTags } from '@/mock/mockTodayApi';
import TagEditDialog from '@/pages/Today/components/TagEditDialog';
import { MomentCardProps } from '@/types/common';

const formatMomentTime = (momentTime: string): string => {
  const timeStr = momentTime.split('T')[1].slice(0, 5);
  const [hoursStr, minutesStr] = timeStr.split(':');
  const hours = parseInt(hoursStr, 10);
  const hours12 = hours % 12 || 12;
  const ampm = hours >= 12 ? 'PM' : 'AM';
  return `${ampm} ${hours12}:${minutesStr}`;
};

const MomentCard = ({ moment, isToday }: MomentCardProps) => {
  const [tags, setTags] = useState<string[]>(moment.tags);
  const [isLoading, setIsLoading] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const formattedTime = formatMomentTime(moment.momentTime);

  const handleGenerateTags = async () => {
    try {
      setIsLoading(true);
      const response = await createTags({
        tags,
        createdAt: moment.momentTime,
        content: moment.content,
      });

      setTags(prevTags => {
        const spaceLeft = 3 - prevTags.length;
        const tagsToAdd = response.recommendTags.slice(0, spaceLeft);
        return [...prevTags, ...tagsToAdd];
      });
    } catch (error) {
      console.error('태그 생성 실패:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <article className='flex w-full flex-col gap-2 rounded-xl bg-[#F1F0E9] p-3 shadow-md'>
        {/* 상단 */}
        <section className='flex justify-between'>
          <div className='rounded-full rounded-bl-none bg-[#FCFBF2] px-3 py-1'>
            {formattedTime}
          </div>
          {isToday && (
            <div className='flex gap-2'>
              <button onClick={() => setIsDialogOpen(true)}>수정</button>
              <button>삭제</button>
            </div>
          )}
        </section>

        {/* 중심부 */}
        <section className='text-start'>
          <div className='mb-3 flex w-full justify-between'>
            {moment.images.slice(0, 3).map((image, idx) => {
              const remainingCount = moment.images.length - 3;

              return (
                <div
                  key={`${image}-${idx}`}
                  className='relative h-[105px] w-[105px] overflow-hidden rounded-xl'
                >
                  <img
                    src={image}
                    alt='moment'
                    onError={e => {
                      e.currentTarget.src = '/fallback-image.png';
                    }}
                    className={`h-full w-full object-cover ${idx === 2 && remainingCount > 0 ? 'blur-sm' : ''}`}
                  />
                  {idx === 2 && remainingCount > 0 && (
                    <div className='absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 text-lg font-bold text-white'>
                      +{remainingCount}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
          <div>{moment.content}</div>
        </section>

        {/* 하단 */}
        <section className='flex items-start'>
          <div className='flex flex-1 flex-wrap items-center gap-2'>
            {tags.map((tag, idx) => (
              <TagBadge key={`${tag}-${idx}`} tag={tag} />
            ))}
          </div>

          {isToday && tags.length < 3 && (
            <div className='flex-shrink-0 pl-1'>
              <button
                onClick={handleGenerateTags}
                className='rounded-full bg-[#41644A] px-4 py-1.5 text-sm font-bold text-white disabled:cursor-not-allowed disabled:opacity-50'
                disabled={isLoading}
              >
                {isLoading ? '생성 중...' : '태그 자동 생성'}
              </button>
            </div>
          )}
        </section>
      </article>
      <TagEditDialog
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        momentTime={moment.momentTime}
        content={moment.content}
        images={moment.images}
        tags={tags}
      />
    </>
  );
};

export default MomentCard;
