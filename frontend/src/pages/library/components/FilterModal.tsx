import { useState } from 'react';

import { CalendarIcon } from '@radix-ui/react-icons';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';

import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { cn } from '@/lib/utils';

interface FilterModalProps {
  onApply: (filters: {
    startDate?: string;
    endDate?: string;
    userTags?: string;
  }) => void;
  onClose: () => void;
}

const FilterModal = ({ onApply, onClose }: FilterModalProps) => {
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [selectedTags, setSelectedTags] = useState<string[]>([]);

  const handleApply = () => {
    onApply({
      startDate: startDate?.toISOString().split('T')[0],
      endDate: endDate?.toISOString().split('T')[0],
      userTags: selectedTags.join(','),
    });
    onClose();
  };

  const handleReset = () => {
    setStartDate(null);
    setEndDate(null);
    setSelectedTags([]);
  };

  const handleTagSelect = (tagId: string) => {
    if (selectedTags.includes(tagId)) {
      setSelectedTags(selectedTags.filter(id => id !== tagId));
    } else if (selectedTags.length < 5) {
      setSelectedTags([...selectedTags, tagId]);
    }
  };

  return (
    <div className='fixed inset-0 flex items-center justify-center bg-black bg-opacity-50'>
      <div className='w-96 rounded-lg bg-white p-6'>
        <div className='mb-4 flex items-center justify-between'>
          <h2 className='text-xl font-bold'>필터</h2>
          <button onClick={onClose} className='text-gray-500'>
            ✕
          </button>
        </div>

        {/* 날짜 선택 */}
        <div className='mb-4'>
          <h3 className='mb-2 font-semibold'>기간 선택</h3>
          <div className='flex gap-2'>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant='outline'
                  className={cn(
                    'w-[200px] justify-start text-left font-normal',
                    !startDate && 'text-muted-foreground',
                  )}
                >
                  <CalendarIcon className='mr-2 h-4 w-4' />
                  {startDate
                    ? format(startDate, 'PPP', { locale: ko })
                    : '시작일'}
                </Button>
              </PopoverTrigger>
              <PopoverContent className='w-auto p-0'>
                <Calendar
                  mode='single'
                  selected={startDate}
                  onSelect={setStartDate}
                  initialFocus
                  locale={ko}
                />
              </PopoverContent>
            </Popover>

            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant='outline'
                  className={cn(
                    'w-[200px] justify-start text-left font-normal',
                    !endDate && 'text-muted-foreground',
                  )}
                >
                  <CalendarIcon className='mr-2 h-4 w-4' />
                  {endDate ? format(endDate, 'PPP', { locale: ko }) : '종료일'}
                </Button>
              </PopoverTrigger>
              <PopoverContent className='w-auto p-0'>
                <Calendar
                  mode='single'
                  selected={endDate}
                  onSelect={setEndDate}
                  disabled={(date: Date) =>
                    date < (startDate ?? new Date('1900-01-01'))
                  }
                  initialFocus
                  locale={ko}
                />
              </PopoverContent>
            </Popover>
          </div>
        </div>

        {/* 태그 선택 */}
        <div className='mb-4'>
          <h3 className='mb-2 font-semibold'>태그 선택 (최대 5개)</h3>
          <div className='flex flex-wrap gap-2'>
            {selectedTags.map(tag => (
              <span
                key={tag}
                className='flex items-center gap-1 rounded-full bg-blue-100 px-2 py-1 text-sm text-blue-800'
              >
                {tag}
                <button onClick={() => handleTagSelect(tag)}>✕</button>
              </span>
            ))}
          </div>
        </div>

        {/* 버튼 */}
        <div className='flex justify-between'>
          <button
            onClick={handleReset}
            className='rounded border px-4 py-2 text-gray-600 hover:bg-gray-100'
          >
            초기화
          </button>
          <button
            onClick={handleApply}
            className='rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600'
          >
            적용
          </button>
        </div>
      </div>
    </div>
  );
};

export default FilterModal;
