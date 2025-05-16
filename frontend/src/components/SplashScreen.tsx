import { useEffect, useState } from 'react';

interface SplashScreenProps {
  onFinish: () => void;
}

const SplashScreen = ({ onFinish }: SplashScreenProps) => {
  const [isVisible, setIsVisible] = useState(true);
  const [displayText, setDisplayText] = useState('');
  const fullText = "Haru'Speak";
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    const textTimer = setInterval(() => {
      if (currentIndex < fullText.length) {
        setDisplayText(prev => prev + fullText[currentIndex]);
        setCurrentIndex(prev => prev + 1);
      } else {
        clearInterval(textTimer);
      }
    }, 50);

    return () => clearInterval(textTimer);
  }, [currentIndex]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsVisible(false);
      setTimeout(() => {
        onFinish();
      }, 300);
    }, 800);

    return () => clearTimeout(timer);
  }, [onFinish]);

  return (
    <div
      className={`fixed inset-0 z-50 flex items-center justify-center bg-white transition-opacity duration-500 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`}
    >
      <div className='flex items-center justify-center w-full h-full max-w-mobile bg-haru-beige'>
        <h1 className='text-4xl font-bold text-haru-green'>{displayText}</h1>
      </div>
    </div>
  );
};

export default SplashScreen;
