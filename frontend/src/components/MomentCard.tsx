import { useState } from 'react';

import ImageGrid from '@/components/ImageGrid';
import TagBadge from '@/components/TagBadge';
import { useMomentTagRecommend } from '@/hooks/useMomentTagRecommend';
import { formatMomentTime } from '@/lib/timeUtils';
import MomentDeleteDialog from '@/pages/today/components/MomentDeleteDialog';
import MomentEditDialog from '@/pages/today/components/MomentEditDialog';
import { MomentCardProps } from '@/types/common';

const MomentCard = ({ moment, isToday }: MomentCardProps) => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const formattedTime = formatMomentTime(moment.momentTime);

  const { tags, isLoading, handleGenerateTags } = useMomentTagRecommend({
    moment,
    initialTags: moment.tags,
  });

  return (
    <>
      <article className='flex w-full flex-col gap-2 rounded-xl bg-haru-beige p-3'>
        {/* 상단 */}
        <section className='flex justify-between'>
          <div className='rounded-full rounded-bl-none bg-haru-yellow px-3 py-1 font-mont'>
            {formattedTime}
          </div>
          {isToday && (
            <div className='flex gap-2'>
              <button
                onClick={() => setIsDialogOpen(true)}
                aria-label='순간 기록 수정'
              >
                수정
              </button>
              <button
                onClick={() => setIsDeleteDialogOpen(true)}
                aria-label='순간 기록 삭제'
              >
                삭제
              </button>
            </div>
          )}
        </section>

        {/* 중심부 */}
        <section className='text-start'>
          <ImageGrid
            images={moment.images}
            momentTime={moment.momentTime}
            momentId={moment.momentId}
          />
          <div className='font-leeseyoon' style={{ whiteSpace: 'pre-wrap' }}>
            {moment.content}
          </div>
        </section>

        {/* 하단 */}
        <section className='flex items-start'>
          <div className='flex flex-1 flex-wrap items-center gap-2'>
            {tags.map((tag: string, idx: number) => (
              <TagBadge key={`${tag}-${idx}`} tag={tag} />
            ))}
          </div>

          {isToday &&
            tags.length < 3 &&
            moment.content !== '' &&
            !tags.includes('아무말') && (
              <div className='flex-shrink-0 pl-1'>
                <button
                  onClick={handleGenerateTags}
                  className='rounded-full bg-[#41644A] px-4 py-1.5 text-sm font-bold text-white disabled:cursor-not-allowed disabled:opacity-50'
                  aria-label='AI 태그 자동 생성'
                  disabled={isLoading}
                >
                  {isLoading ? '생성 중...' : '태그 자동 생성'}
                </button>
              </div>
            )}
        </section>
      </article>
      <MomentEditDialog
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        moment={moment}
      />

      {isToday && (
        <MomentDeleteDialog
          open={isDeleteDialogOpen}
          onOpenChange={setIsDeleteDialogOpen}
          momentTime={moment.momentTime}
          createdAt={moment.createdAt}
        />
      )}
    </>
  );
};

export default MomentCard;
