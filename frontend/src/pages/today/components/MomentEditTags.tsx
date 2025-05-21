import React from 'react';

import AutoTagGenerator from '@/components/AutoTagGenerator';
import { MomentContent } from '@/types/common';

interface MomentEditTagsProps {
  moment: MomentContent;
  localTags: string[];
  setLocalTags: (tags: string[]) => void;
  register: any;
  handleSubmit: any;
  errors: any;
  watch: any;
  handleKeyPress: (e: React.KeyboardEvent<HTMLInputElement>) => void;
  onSubmit: (data: any) => void;
  tagError: string;
}

const MomentEditTags = ({
  moment,
  localTags,
  setLocalTags,
  register,
  handleSubmit,
  errors,
  watch,
  handleKeyPress,
  onSubmit,
  tagError,
}: MomentEditTagsProps) => {
  return (
    <div className='flex flex-col'>
      <form
        onSubmit={handleSubmit(onSubmit)}
        className='mb-2 flex items-center gap-2'
      >
        <input
          {...register('tag')}
          onKeyDown={handleKeyPress}
          className={`w-[calc(100%-80px)] rounded-md border p-2 text-sm focus:outline-haru-green ${
            errors.tag && errors.tag.type !== 'too_small'
              ? 'border-red-500'
              : 'border-gray-300'
          }`}
          placeholder={
            localTags.length >= 10 ? '태그 최대 개수 도달 (10개)' : '태그 입력'
          }
          disabled={localTags.length >= 10}
        />
        <button
          type='submit'
          disabled={localTags.length >= 10 || !watch('tag')}
          className='whitespace-nowrap rounded-full bg-haru-green px-3 py-2 text-xs font-bold text-white disabled:bg-gray-300 disabled:text-gray-500'
        >
          태그 추가
        </button>
      </form>

      <div className='mb-2 flex flex-row-reverse justify-between'>
        <AutoTagGenerator
          moment={moment}
          initialTags={localTags}
          isToday={true}
          hideWhenDisabled={false}
          buttonStyle='simple'
          isEditPage={true}
          onTagsUpdate={newTags => setLocalTags(newTags)}
        />
        <button
          onClick={() => setLocalTags([])}
          className='text-sm font-bold text-haru-light-green hover:text-haru-green disabled:cursor-not-allowed disabled:text-gray-500 disabled:hover:text-gray-500'
          disabled={localTags.length === 0}
        >
          태그 목록 초기화
        </button>
      </div>

      <div className='flex flex-wrap gap-2'>
        {localTags.includes('아무말') && (
          <div className='w-full text-center text-sm text-haru-gray-5'>
            아무말 태그가 있을 경우 태그 자동 생성을 할 수 없습니다
          </div>
        )}
        {localTags.map((tag, idx) => (
          <div
            key={idx}
            className={`flex items-center gap-1 rounded-full px-3 py-1 font-leeseyoon text-sm ${
              tag === '아무말'
                ? 'border border-haru-light-green bg-haru-yellow text-haru-green'
                : 'bg-haru-gray-2'
            }`}
          >
            {tag}
            <button
              onClick={() => {
                setLocalTags(localTags.filter((_, i: number) => i !== idx));
              }}
              className='text-haru-gray-5 hover:text-red-600'
            >
              ✕
            </button>
          </div>
        ))}
      </div>
      {errors.tag && errors.tag.type !== 'too_small' && (
        <div className='mt-1 font-bold text-red-500'>
          {errors.tag?.message || tagError}
        </div>
      )}
    </div>
  );
};

export default MomentEditTags;
