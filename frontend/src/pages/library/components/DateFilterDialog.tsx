// components/DateFilterDialog.tsx
import { useEffect, useState } from 'react';

import { CalendarIcon } from '@radix-ui/react-icons';
import { format } from 'date-fns';
import { ko } from 'date-fns/locale';

// [여기] 기존 유틸리티 추가

import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { formatDate } from '@/lib/timeUtils';
import { cn } from '@/lib/utils';

interface DateFilterDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onApply: (filters: { startDate?: string; endDate?: string }) => void;
  initialStartDate?: string;
  initialEndDate?: string;
}

const DateFilterDialog = ({
  open,
  onOpenChange,
  onApply,
  initialStartDate,
  initialEndDate,
}: DateFilterDialogProps) => {
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);

  // 초기값 설정
  useEffect(() => {
    if (initialStartDate) {
      setStartDate(new Date(initialStartDate));
    }
    if (initialEndDate) {
      setEndDate(new Date(initialEndDate));
    }
  }, [initialStartDate, initialEndDate, open]);

  const handleApply = () => {
    onApply({
      // 여기서는 API 요청을 위한 'yyyy-MM-dd' 형식을 유지
      // formatDate는 'yyyy.MM.dd' 형식으로 변환하므로 UI 표시용으로만 사용
      startDate: startDate ? format(startDate, 'yyyy-MM-dd') : undefined,
      endDate: endDate ? format(endDate, 'yyyy-MM-dd') : undefined,
    });
    onOpenChange(false);
  };

  const handleReset = () => {
    setStartDate(null);
    setEndDate(null);
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
          <DialogTitle>일기 기간 설정</DialogTitle>
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
                      formatDate(startDate.toISOString())
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
                      formatDate(endDate.toISOString())
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

export default DateFilterDialog;
