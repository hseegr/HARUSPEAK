import { useEffect, useState } from 'react';

import { CalendarIcon } from '@radix-ui/react-icons';
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
  // 다이얼로그가 마운트될 때마다 초기값으로 상태 초기화
  const [startDate, setStartDate] = useState<Date | null>(
    initialStartDate ? new Date(initialStartDate) : null,
  );
  const [endDate, setEndDate] = useState<Date | null>(
    initialEndDate ? new Date(initialEndDate) : null,
  );

  const [startDateOpen, setStartDateOpen] = useState(false);
  const [endDateOpen, setEndDateOpen] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // 오늘 날짜 설정 (시간은 00:00:00으로 설정)
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  // 오류 메시지 업데이트 (날짜 선택 상태에 따라)
  useEffect(() => {
    if (!startDate && !endDate) {
      setError('날짜를 선택해주세요');
    } else if (!startDate && endDate) {
      setError('시작 날짜를 선택해주세요');
    } else if (startDate && !endDate) {
      setError('종료 날짜를 선택해주세요');
    } else if (startDate && endDate && startDate > endDate) {
      setError('시작 날짜는 종료 날짜보다 이전이어야 합니다');
    } else {
      setError(null);
    }
  }, [startDate, endDate]);

  // 필터 적용 버튼 활성화 여부 확인
  const isButtonDisabled = () => {
    return (
      !startDate || !endDate || (startDate && endDate && startDate > endDate)
    );
  };

  const handleApply = () => {
    // 버튼이 비활성화되어 있으면 함수 실행 중지
    if (isButtonDisabled()) return;

    onApply({
      // 여기서는 API 요청을 위한 'yyyy-MM-dd' 형식을 유지
      startDate: startDate ? format(startDate, 'yyyy-MM-dd') : undefined,
      endDate: endDate ? format(endDate, 'yyyy-MM-dd') : undefined,
    });
    onOpenChange(false);
  };

  const handleReset = () => {
    setStartDate(null);
    setEndDate(null);
    setError(null);
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
          <DialogTitle>일기 기간 설정</DialogTitle>
        </DialogHeader>
        <div className='space-y-4'>
          {/* 날짜 선택 - 시작/종료 날짜를 한 줄에 표시 */}
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

export default DateFilterDialog;
