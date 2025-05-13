// import { useGetImage } from '@/hooks/useDiaryQuery';

// interface ThumbnailProps {
//   summaryId: string;
// }

// const Thumbnail = ({ summaryId }: ThumbnailProps) => {
//   const { data, isPending } = useGetImage(summaryId);

//   if (isPending) {
//     return (
//       <div className='aspect-[4/3] w-full animate-pulse rounded-lg bg-gray-200' />
//     );
//   }

//   if (!data?.imageUrl) {
//     return (
//       <div className='flex aspect-[4/3] w-full items-center justify-center rounded-lg bg-gray-100'>
//         <span className='text-gray-400'>하루 요약 이미지 없음</span>
//       </div>
//     );
//   }

//   return (
//     <div className='aspect-[4/3] w-full overflow-hidden rounded-lg'>
//       <img
//         src={data.imageUrl}
//         alt='일기 썸네일'
//         className='h-full w-full object-cover'
//       />
//     </div>
//   );
// };

// export default Thumbnail;
