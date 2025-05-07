// 일기(1개) 조회 시 상단의 제목, 날짜
const DiaryHeader = ({ title, date }: { title: string; date: string }) => {
  return (
    <div>
      <div>{title}</div>
      <div>{date}</div>
    </div>
  );
};

export default DiaryHeader;
