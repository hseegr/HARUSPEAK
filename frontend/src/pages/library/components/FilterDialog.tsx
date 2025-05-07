import { ChangeEvent, useEffect, useState } from 'react';

import {
  CalendarIcon,
  Cross1Icon,
  MagnifyingGlassIcon,
} from '@radix-ui/react-icons';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';

import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { ScrollArea } from '@/components/ui/scroll-area';
import { useGetUserTags } from '@/hooks/useTagQuery';
import { cn } from '@/lib/utils';
import { UserTag } from '@/types/tag';

interface FilterDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onApply: (filters: {
    startDate?: string;
    endDate?: string;
    userTags?: string[];
  }) => void;
}

const FilterDialog = ({ open, onOpenChange, onApply }: FilterDialogProps) => {
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [searchQuery, setSearchQuery] = useState('');

  const { data: tagsResponse } = useGetUserTags();
  const [filteredTags, setFilteredTags] = useState<UserTag[]>([]);
  const [selectedTagObjects, setSelectedTagObjects] = useState<UserTag[]>([]);

  // 태그 검색 처리
  useEffect(() => {
    if (!tagsResponse?.tags) return;

    const filtered = tagsResponse.tags.filter(tag =>
      tag.name.toLowerCase().includes(searchQuery.toLowerCase()),
    );
    setFilteredTags(filtered);
  }, [searchQuery, tagsResponse?.tags]);

  // 선택된 태그 객체 업데이트
  useEffect(() => {
    if (!tagsResponse?.tags) return;

    const tagObjects = selectedTags
      .map(tagId => {
        return tagsResponse.tags.find(tag => String(tag.userTagId) === tagId);
      })
      .filter(tag => tag !== undefined) as UserTag[];

    setSelectedTagObjects(tagObjects);
  }, [selectedTags, tagsResponse?.tags]);

  const handleApply = () => {
    onApply({
      startDate: startDate ? format(startDate, 'yyyy-MM-dd') : undefined,
      endDate: endDate ? format(endDate, 'yyyy-MM-dd') : undefined,
      userTags: selectedTags.length > 0 ? selectedTags : undefined,
    });
    onOpenChange(false);
  };

  const handleReset = () => {
    setStartDate(null);
    setEndDate(null);
    setSelectedTags([]);
    setSearchQuery('');
  };

  const toggleTag = (tagId: number) => {
    setSelectedTags(prev => {
      const stringId = String(tagId);
      if (prev.includes(stringId)) {
        return prev.filter(id => id !== stringId);
      }
      if (prev.length >= 5) {
        return prev;
      }
      return [...prev, stringId];
    });
  };

  const removeTag = (tagId: string) => {
    setSelectedTags(prev => prev.filter(id => id !== tagId));
  };

  const handleSearchChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(e.target.value);
  };

  const handleDateSelect = (
    date: Date | undefined,
    setDate: (date: Date | null) => void,
  ) => {
    setDate(date ?? null);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className='sm:max-w-md'>
        <DialogHeader>
          <DialogTitle>순간 일기 찾기</DialogTitle>
        </DialogHeader>
        <div className='space-y-4'>
          {/* 날짜 선택 - 시작/종료 날짜를 한 줄에 표시 */}
          <div className='flex gap-2'>
            <div className='flex-1'>
              <label className='mb-2 block text-sm font-medium'>
                시작 날짜
              </label>
              <Popover>
                <PopoverTrigger asChild>
                  <Button
                    variant='outline'
                    className={cn(
                      'w-full justify-start text-left font-normal',
                      !startDate && 'text-muted-foreground',
                    )}
                  >
                    <CalendarIcon className='mr-2 h-4 w-4' />
                    {startDate ? (
                      format(startDate, 'yyyy.MM.dd', { locale: ko })
                    ) : (
                      <span>날짜 선택</span>
                    )}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className='w-auto p-0'>
                  <Calendar
                    mode='single'
                    selected={startDate as Date}
                    onSelect={date => handleDateSelect(date, setStartDate)}
                    initialFocus
                    locale={ko}
                  />
                </PopoverContent>
              </Popover>
            </div>

            <div className='flex-1'>
              <label className='mb-2 block text-sm font-medium'>
                종료 날짜
              </label>
              <Popover>
                <PopoverTrigger asChild>
                  <Button
                    variant='outline'
                    className={cn(
                      'w-full justify-start text-left font-normal',
                      !endDate && 'text-muted-foreground',
                    )}
                  >
                    <CalendarIcon className='mr-2 h-4 w-4' />
                    {endDate ? (
                      format(endDate, 'yyyy.MM.dd', { locale: ko })
                    ) : (
                      <span>날짜 선택</span>
                    )}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className='w-auto p-0'>
                  <Calendar
                    mode='single'
                    selected={endDate as Date}
                    onSelect={date => handleDateSelect(date, setEndDate)}
                    initialFocus
                    locale={ko}
                  />
                </PopoverContent>
              </Popover>
            </div>
          </div>

          {/* 태그 선택 부분 */}
          <div>
            <label className='mb-2 block text-sm font-medium'>
              태그 선택 (최대 5개)
            </label>

            {/* 선택된 태그 표시 */}
            {selectedTagObjects.length > 0 && (
              <div className='mb-2 flex flex-wrap gap-1'>
                {selectedTagObjects.map(tag => (
                  <div
                    key={tag.userTagId}
                    className='flex items-center rounded-full bg-green-100 px-2 py-1 text-xs text-green-800'
                  >
                    <span>{tag.name}</span>
                    <button
                      className='ml-1 rounded-full p-1 hover:bg-green-200'
                      onClick={() => removeTag(String(tag.userTagId))}
                    >
                      <Cross1Icon className='h-3 w-3' />
                    </button>
                  </div>
                ))}
              </div>
            )}

            <div className='relative mb-2'>
              <MagnifyingGlassIcon className='absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-500' />
              <Input
                type='text'
                placeholder='태그 검색...'
                value={searchQuery}
                onChange={handleSearchChange}
                className='pl-9'
              />
            </div>
            <ScrollArea className='h-32 rounded-md border p-2'>
              <div className='flex flex-wrap gap-2'>
                {filteredTags.map(tag => (
                  <Button
                    key={tag.userTagId}
                    variant={
                      selectedTags.includes(String(tag.userTagId))
                        ? 'default'
                        : 'outline'
                    }
                    size='sm'
                    onClick={() => toggleTag(tag.userTagId)}
                    disabled={
                      !selectedTags.includes(String(tag.userTagId)) &&
                      selectedTags.length >= 5
                    }
                  >
                    {tag.name}
                    <span className='ml-1 text-xs'>({tag.count})</span>
                  </Button>
                ))}
              </div>
            </ScrollArea>
          </div>

          <div className='mt-6 flex justify-end space-x-2'>
            <Button variant='outline' onClick={handleReset}>
              초기화
            </Button>
            <Button onClick={handleApply}>필터 적용</Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default FilterDialog;
