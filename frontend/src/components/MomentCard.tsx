import { useState } from 'react';

import ImageDialog from '@/components/ImageDialog';
import TagBadge from '@/components/TagBadge';
import { formatMomentTime } from '@/lib/timeUtils';
import { createTags, deleteMoment } from '@/mock/mockTodayApi';
import MomentDeleteDialog from '@/pages/today/components/MomentDeleteDialog';
import MomentEditDialog from '@/pages/today/components/MomentEditDialog';
import { MomentCardProps } from '@/types/common';

const MomentCard = ({ moment, isToday }: MomentCardProps) => {
  const [tags, setTags] = useState<string[]>(moment.tags);
  const [isLoading, setIsLoading] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isImageDialogOpen, setIsImageDialogOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);
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

  const handleDelete = async () => {
    try {
      await deleteMoment(moment.momentTime);
      setIsDeleteDialogOpen(false);
      // TODO: 삭제 후 리스트 갱신 로직 추가
    } catch (error) {
      console.error('삭제 실패:', error);
    }
  };

  return (
    <>
      <article className='flex w-full flex-col gap-2 rounded-xl bg-[#F1F0E9] p-3'>
        {/* 상단 */}
        <section className='flex justify-between'>
          <div className='rounded-full rounded-bl-none bg-[#FCFBF2] px-3 py-1'>
            {formattedTime}
          </div>
          {isToday && (
            <div className='flex gap-2'>
              <button onClick={() => setIsDialogOpen(true)}>수정</button>
              <button onClick={() => setIsDeleteDialogOpen(true)}>삭제</button>
            </div>
          )}
        </section>

        {/* 중심부 */}
        <section className='text-start'>
          <div className='flex justify-between w-full mb-3'>
            {moment.images.slice(0, 3).map((image, idx) => {
              const remainingCount = moment.images.length - 3;

              return (
                <div
                  key={`${image}-${idx}`}
                  className='relative h-[105px] w-[105px] cursor-pointer overflow-hidden rounded-xl'
                  onClick={() => {
                    setSelectedImageIndex(idx);
                    setIsImageDialogOpen(true);
                  }}
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
                    <div className='absolute inset-0 flex items-center justify-center text-lg font-bold text-white bg-black bg-opacity-50'>
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
          <div className='flex flex-wrap items-center flex-1 gap-2'>
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
      <MomentEditDialog
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        moment={moment}
      />
      <ImageDialog
        open={isImageDialogOpen}
        onOpenChange={setIsImageDialogOpen}
        images={moment.images}
        currentIndex={selectedImageIndex}
        momentTime={moment.momentTime}
      />

      <MomentDeleteDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        momentTime={moment.momentTime}
        onDelete={handleDelete}
      />
    </>
  );
};

export default MomentCard;
