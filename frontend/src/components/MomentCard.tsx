import { useState } from 'react';

import AutoTagGenerator from '@/components/AutoTagGenerator';
import ImageGrid from '@/components/ImageGrid';
import TagBadge from '@/components/TagBadge';
import { formatMomentTime } from '@/lib/timeUtils';
import MomentDeleteDialog from '@/pages/today/components/MomentDeleteDialog';
import MomentEditDialog from '@/pages/today/components/MomentEditDialog';
import { MomentCardProps } from '@/types/common';

const MomentCard = ({ moment, isToday }: MomentCardProps) => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const formattedTime = formatMomentTime(moment.momentTime);

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
                className='text-sm font-bold text-haru-gray-5 hover:text-haru-green'
                onClick={() => setIsDialogOpen(true)}
                aria-label='순간 기록 수정'
              >
                수정
              </button>
              <button
                className='text-sm font-bold text-haru-gray-5 hover:text-haru-green'
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
          <div
            className='font-leeseyoon text-lg'
            style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}
          >
            {moment.content}
          </div>
        </section>

        {/* 하단 */}
        {(moment.tags.length > 0 || moment.content) && (
          <section>
            <div className='mb-2 flex items-center'>
              <div className='flex h-full w-full flex-wrap items-center gap-2'>
                {moment.tags.map((tag: string, idx: number) => (
                  <TagBadge key={`${tag}-${idx}`} tag={tag} />
                ))}
              </div>
              <AutoTagGenerator
                moment={moment}
                initialTags={moment.tags}
                isToday={isToday}
              />
            </div>
            {moment.tags.includes('아무말') && (
              <div className='w-full text-sm text-haru-gray-5'>
                아무말 태그가 있을 경우 태그 자동 생성을 할 수 없습니다
              </div>
            )}
          </section>
        )}
      </article>
      <MomentEditDialog
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        moment={moment}
      />

      {isToday && moment.createdAt && (
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
