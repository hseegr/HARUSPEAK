const LoadingSpinner = () => {
  return (
    <div className='flex min-h-[100vh] w-full items-center justify-center bg-gray-100'>
      <div className='flex flex-col items-center justify-center w-full gap-4 bg-white min-h-lvh max-w-96'>
        <style>
          {`
            @keyframes wave {
              0% {
                transform: scaleY(1);
              }
              50% {
                transform: scaleY(0.2);
              }
              100% {
                transform: scaleY(1);
              }
            }
          `}
        </style>
        <div className='flex items-center h-20 gap-1'>
          {[...Array(5)].map((_, index) => (
            <div
              key={index}
              className='w-2 h-16 rounded-full bg-haru-green'
              style={{
                animation: `wave 1.5s cubic-bezier(0.4, 0, 0.2, 1) infinite`,
                transformOrigin: 'center',
                animationDelay: `${index * 0.15}s`,
              }}
            />
          ))}
        </div>
        <p className='text-xl font-bold transition-all duration-300 text-haru-light-green/80 hover:scale-105'>
          로딩중
        </p>
      </div>
    </div>
  );
};

export default LoadingSpinner;
