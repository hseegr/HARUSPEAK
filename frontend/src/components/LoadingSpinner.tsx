const LoadingSpinner = () => {
  return (
    <div className='flex min-h-[100vh] w-full items-center justify-center bg-gray-100'>
      <div className='flex min-h-lvh w-full max-w-mobile flex-col items-center justify-center gap-4 bg-white'>
        <style>
          {`
            @keyframes wave {
              0% {
                height: 0px;
              }
              50% {
                height: 64px;
              }
              100% {
                height: 0px;
              }
            }
          `}
        </style>
        <div className='flex items-center gap-1'>
          <p className='text-xl font-bold text-haru-light-green/80 transition-all duration-300 hover:scale-105'>
            로딩중
          </p>
          <div className='min-h-[64px]'>
            {[...Array(5)].map((_, index) => (
              <div
                key={index}
                className='h-0 w-2 rounded-full bg-haru-green'
                style={{
                  animation: `wave 1.5s cubic-bezier(0.4, 0, 0.2, 1) infinite`,
                  transformOrigin: 'center',
                  animationDelay: `${index * 0.15}s`,
                }}
              />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoadingSpinner;
