import DeleteBtn from './components/DeleteBtn';
import DiaryFrame from './components/DiaryFrame';
import Filter from './components/FilterBadge';

// 내 서재에서 하루일기 모아보기
const Library = () => {
  return (
    <div>
      <div>
        <Filter />
        <DeleteBtn />
        <DiaryFrame />
      </div>
    </div>
  );
};
export default Library;
