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

// 오늘 날짜 설정 (시간은 00:00:00으로 설정)
const today = new Date();
today.setHours(0, 0, 0, 0);

interface FilterDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onApply: (filters: {
    startDate?: string;
    endDate?: string;
    userTags?: number[];
  }) => void;
}

const FilterDialog = ({ open, onOpenChange, onApply }: FilterDialogProps) => {
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [selectedTags, setSelectedTags] = useState<number[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [startDateOpen, setStartDateOpen] = useState(false);
  const [endDateOpen, setEndDateOpen] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const { data: tagsResponse } = useGetUserTags();
  const [filteredTags, setFilteredTags] = useState<UserTag[]>([]);
  const [selectedTagObjects, setSelectedTagObjects] = useState<UserTag[]>([]);

  // 필터 적용 버튼 활성화 여부 확인
  const isButtonDisabled = () => {
    // 날짜와 태그 모두 선택되지 않은 경우 비활성화
    if (!startDate && !endDate && selectedTags.length === 0) {
      return true;
    }

    // 날짜를 하나만 선택한 경우 비활성화
    if ((startDate && !endDate) || (!startDate && endDate)) {
      return true;
    }

    // 시작일이 종료일보다 나중이면 비활성화
    if (startDate && endDate && startDate > endDate) {
      return true;
    }

    // 그 외의 경우 활성화
    return false;
  };

  // 오류 메시지 업데이트 (날짜 선택 상태에 따라)
  useEffect(() => {
    if (!startDate && !endDate && selectedTags.length === 0) {
      setError('태그 또는 날짜를 선택해주세요');
    } else if (!startDate && endDate) {
      setError('시작 날짜를 선택해주세요');
    } else if (startDate && !endDate) {
      setError('종료 날짜를 선택해주세요');
    } else if (startDate && endDate && startDate > endDate) {
      setError('시작 날짜는 종료 날짜보다 이전이어야 합니다');
    } else {
      setError(null);
    }
  }, [startDate, endDate, selectedTags.length]);

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
        return tagsResponse.tags.find(tag => tag.userTagId === tagId);
      })
      .filter(tag => tag !== undefined) as UserTag[];

    setSelectedTagObjects(tagObjects);
  }, [selectedTags, tagsResponse?.tags]);

  const handleApply = () => {
    // 버튼이 비활성화되어 있으면 함수 실행 중지
    if (isButtonDisabled()) return;
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
    setError(null);
  };

  const toggleTag = (tagId: number) => {
    setSelectedTags(prev => {
      if (prev.includes(tagId)) {
        return prev.filter(id => id !== tagId);
      }
      if (prev.length >= 3) {
        return prev;
      }
      return [...prev, tagId];
    });
  };

  const removeTag = (tagId: number) => {
    setSelectedTags(prev => prev.filter(id => id !== tagId));
  };

  const handleSearchChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(e.target.value);
  };

  const handleStartDateSelect = (date: Date | undefined) => {
    setStartDate(date ?? null);
    setStartDateOpen(false); // 날짜 선택 후 달력 닫기
  };

  const handleEndDateSelect = (date: Date | undefined) => {
    setEndDate(date ?? null);
    setEndDateOpen(false); // 날짜 선택 후 달력 닫기
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className='sm:max-w-md'>
        <DialogHeader>
          <DialogTitle>순간 찾기</DialogTitle>
        </DialogHeader>
        <div className='space-y-4'>
          <div className='flex gap-2'>
            <div className='flex-1'>
              <label className='mb-2 block text-sm font-medium'>
                시작 날짜
              </label>
              <Popover open={startDateOpen} onOpenChange={setStartDateOpen}>
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
                    onSelect={handleStartDateSelect}
                    initialFocus
                    locale={ko}
                    disabled={date => date > today} // 오늘 이후 날짜는 선택 불가
                    fromDate={undefined} // 가장 빠른 날짜 제한 없음
                    toDate={today} // 오늘까지만 선택 가능
                  />
                </PopoverContent>
              </Popover>
            </div>

            <div className='flex-1'>
              <label className='mb-2 block text-sm font-medium'>
                종료 날짜
              </label>
              <Popover open={endDateOpen} onOpenChange={setEndDateOpen}>
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
                    onSelect={handleEndDateSelect}
                    initialFocus
                    locale={ko}
                    disabled={date => date > today} // 오늘 이후 날짜는 선택 불가
                    fromDate={undefined} // 가장 빠른 날짜 제한 없음
                    toDate={today} // 오늘까지만 선택 가능
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
                      onClick={() => removeTag(tag.userTagId)}
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
                      selectedTags.includes(tag.userTagId)
                        ? 'default'
                        : 'outline'
                    }
                    size='sm'
                    onClick={() => toggleTag(tag.userTagId)}
                    disabled={
                      !selectedTags.includes(tag.userTagId) &&
                      selectedTags.length >= 5
                    }
                  >
                    {tag.name}
                    <span className='ml-1 text-xs'>({tag.count})</span>
                  </Button>
                ))}
                {filteredTags.length === 0 && (
                  <div className='w-full p-2 text-center text-gray-500'>
                    검색 결과가 없습니다
                  </div>
                )}
              </div>
            </ScrollArea>
          </div>

          {/* 오류 메시지 표시 */}
          {error && (
            <div className='text-sm font-medium text-red-500'>{error}</div>
          )}

          <div className='mt-6 flex justify-end space-x-2'>
            <Button variant='outline' onClick={handleReset}>
              초기화
            </Button>
            <Button
              onClick={handleApply}
              disabled={isButtonDisabled()}
              className={
                isButtonDisabled()
                  ? 'cursor-not-allowed opacity-50'
                  : 'bg-haru-green'
              }
            >
              필터 적용
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default FilterDialog;
