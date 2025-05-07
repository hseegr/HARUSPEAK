import { format } from 'date-fns';
import { ko } from 'date-fns/locale';

interface DateRangeDisplayProps {
  startDate?: string;
  endDate?: string;
}

const DateRangeDisplay = ({ startDate, endDate }: DateRangeDisplayProps) => {
  if (!startDate && !endDate) return null;

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return format(date, 'yyyy.MM.dd', { locale: ko });
  };

  return (
    <div className='mt-2 flex flex-wrap gap-1'>
      <span className='text-sm text-gray-500'>기간 :</span>
      {startDate && (
        <span className='rounded-full bg-blue-100 px-2 py-1 text-xs text-blue-800'>
          {formatDate(startDate)}
        </span>
      )}
      {startDate && endDate && (
        <span className='px-1 text-xs text-gray-500'>~</span>
      )}
      {endDate && (
        <span className='rounded-full bg-blue-100 px-2 py-1 text-xs text-blue-800'>
          {formatDate(endDate)}
        </span>
      )}
    </div>
  );
};

export default DateRangeDisplay;
