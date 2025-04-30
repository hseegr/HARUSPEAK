export const formatMomentTime = (momentTime: string): string => {
  const timeStr = momentTime.split('T')[1].slice(0, 5);
  const [hoursStr, minutesStr] = timeStr.split(':');
  const hours = parseInt(hoursStr, 10);
  const hours12 = hours % 12 || 12;
  const ampm = hours >= 12 ? 'PM' : 'AM';
  return `${ampm} ${hours12}:${minutesStr}`;
};

export const parseMomentTime = (momentTime: string) => {
  const timeStr = momentTime.split('T')[1].slice(0, 5);
  const [hoursStr, minutesStr] = timeStr.split(':');
  const hours = parseInt(hoursStr, 10);
  const hours12 = hours % 12 || 12;
  const ampm = hours >= 12 ? 'PM' : 'AM';

  return {
    date: momentTime.split('T')[0].replace(/-/g, '.'),
    time: `${ampm} ${hours12}:${minutesStr}`,
  };
};

export const updateMomentTime = (originalTime: string, newTime: string) => {
  const [datePart] = originalTime.split('T');
  const [time, period] = newTime.split(' ');
  const [hours, minutes] = time.split(':').map(Number);

  // 12시간 형식을 24시간 형식으로 변환
  let finalHours = hours;
  if (period === 'PM' && hours !== 12) {
    finalHours += 12;
  } else if (period === 'AM' && hours === 12) {
    finalHours = 0;
  }

  // ISO 형식으로 조합
  return `${datePart}T${finalHours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:00.000Z`;
};

export const get24HourFormat = (timeStr: string) => {
  const [time, period] = timeStr.split(' ');
  const [hours, minutes] = time.split(':').map(Number);

  let finalHours = hours;
  if (period === 'PM' && hours !== 12) {
    finalHours += 12;
  } else if (period === 'AM' && hours === 12) {
    finalHours = 0;
  }

  return `${finalHours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
};

export const convert24To12HourFormat = (inputTime: string) => {
  const [hours, minutes] = inputTime.split(':').map(Number);

  // 24시간 형식을 12시간 형식으로 변환
  let period = 'AM';
  let displayHours = hours;

  if (hours >= 12) {
    period = 'PM';
    if (hours > 12) {
      displayHours = hours - 12;
    }
  } else if (hours === 0) {
    displayHours = 12;
  }

  return `${displayHours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')} ${period}`;
};
