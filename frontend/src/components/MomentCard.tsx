import TagBadge from '@/components/TagBadge';
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
  const formattedTime = formatMomentTime(moment.momentTime);

  return (
    <article className='flex w-full flex-col gap-2 rounded-xl bg-[#F1F0E9] p-3 shadow-md'>
      {/* 상단 : 사용자 지정 시각, 버튼 모음 */}
      <section className='flex justify-between'>
        <div className='rounded-full rounded-bl-none bg-[#FCFBF2] px-3 py-1'>
          {formattedTime}
        </div>
        {isToday && (
          <div className='flex gap-2'>
            <button>수정</button>
            <button>삭제</button>
          </div>
        )}
      </section>
      {/* 중심부 : 이미지, 내용 세로 정렬 */}
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
      {/* 하단 : 태그 뱃지 모음, 태그 생성 버튼 */}
      <section className='flex items-start'>
        <div className='flex flex-1 flex-wrap items-center gap-2'>
          {moment.tags.map((tag, idx) => (
            <TagBadge key={`${tag}-${idx}`} tag={tag} />
          ))}
        </div>
        {isToday && (
          <div className='flex-shrink-0 pl-1'>
            <button className='rounded-full bg-[#41644A] px-4 py-1.5 text-sm font-bold text-white'>
              태그 자동 생성
            </button>
          </div>
        )}
      </section>
    </article>
  );
};

export default MomentCard;
