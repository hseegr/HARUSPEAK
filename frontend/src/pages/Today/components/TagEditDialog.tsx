import { useState } from 'react';

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';

interface TagEditDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  momentTime: string;
  content: string;
  images: string[];
  tags: string[];
}

const TagEditDialog = ({
  open,
  onOpenChange,
  momentTime,
  content: initialContent,
  images: initialImages,
  tags: initialTags,
}: TagEditDialogProps) => {
  const [content, setContent] = useState(initialContent);
  const [images, setImages] = useState(initialImages);
  const [tags, setTags] = useState(initialTags);
  const [newTag, setNewTag] = useState('');

  const handleDeleteImage = (index: number) => {
    setImages(prev => prev.filter((_, i) => i !== index));
  };

  const handleDeleteTag = (index: number) => {
    setTags(prev => prev.filter((_, i) => i !== index));
  };

  const handleAddTag = () => {
    if (newTag.trim() !== '') {
      setTags(prev => [...prev, newTag.trim()]);
      setNewTag('');
    }
  };

  const handleSave = () => {
    console.log('최종 저장할 데이터:', { momentTime, content, images, tags });
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className='min-h-full max-w-96'>
        <DialogHeader>
          <DialogTitle className='text-center'>오늘의 순간 수정</DialogTitle>
        </DialogHeader>

        <div className='flex flex-col gap-4'>
          {/* 상단 시간 & 저장 */}
          <div className='flex items-center justify-between text-sm text-gray-600'>
            <span>{momentTime}</span>
            <button
              onClick={handleSave}
              className='font-bold text-[#41644A] hover:underline'
            >
              저장
            </button>
          </div>

          {/* 이미지 리스트 */}
          <label className='text-sm font-semibold text-gray-600'>
            순간의 사진들
          </label>
          <div className='grid grid-cols-3 gap-2'>
            {Array.from({ length: 5 }).map((_, idx) => (
              <div
                key={idx}
                className='relative h-24 w-full rounded-lg bg-gray-100'
              >
                {images[idx] ? (
                  <>
                    <img
                      src={images[idx]}
                      alt={`image-${idx}`}
                      className='h-full w-full rounded-lg object-cover'
                    />
                    <button
                      onClick={() => handleDeleteImage(idx)}
                      className='absolute right-1 top-1 rounded-full bg-black bg-opacity-50 p-0.5 text-xs text-white'
                    >
                      ✕
                    </button>
                  </>
                ) : null}
              </div>
            ))}
          </div>

          {/* 순간 기록 */}
          <div className='flex flex-col gap-1'>
            <label className='text-sm font-semibold text-gray-600'>
              순간의 기록
            </label>
            <textarea
              value={content}
              onChange={e => setContent(e.target.value)}
              rows={4}
              className='w-full resize-none rounded-md border border-gray-300 p-2 text-sm focus:outline-[#41644A]'
              placeholder='순간의 기록을 입력하세요'
            />
          </div>

          {/* 태그 추가 */}
          <div className='flex flex-col gap-2'>
            <label className='text-sm font-semibold text-gray-600'>태그</label>
            <div className='flex gap-2'>
              <input
                value={newTag}
                onChange={e => setNewTag(e.target.value)}
                className='flex-1 rounded-md border border-gray-300 p-2 text-sm focus:outline-[#41644A]'
                placeholder='태그 입력'
              />
              <button
                onClick={handleAddTag}
                className='rounded-md bg-[#41644A] px-3 py-1.5 text-sm text-white'
              >
                태그 추가
              </button>
            </div>

            {/* 현재 태그 목록 */}
            <div className='flex flex-wrap gap-2'>
              {tags.map((tag, idx) => (
                <div
                  key={idx}
                  className='flex items-center gap-1 rounded-full bg-gray-200 px-3 py-1 text-xs'
                >
                  {tag}
                  <button
                    onClick={() => handleDeleteTag(idx)}
                    className='text-gray-500 hover:text-gray-700'
                  >
                    ✕
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default TagEditDialog;
