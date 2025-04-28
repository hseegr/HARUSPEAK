import { useEffect, useState } from 'react';

// useEffect와 useState는 React에서 import 했지만, React 자체는 import하지 않았습니다.
// 최신 React 버전(17+)에서는 JSX 변환이 개선되어 명시적으로 React를 import할 필요가 없지만,
// 프로젝트 설정에 따라 여전히 React import가 필요할 수 있습니다.
interface BeforeInstallPromptEvent extends Event {
  prompt: () => Promise<void>;
  userChoice: Promise<{ outcome: 'accepted' | 'dismissed' }>;
}

const InstallPWA = () => {
  const [supportsPWA, setSupportsPWA] = useState(false);
  const [promptEvent, setPromptEvent] =
    useState<BeforeInstallPromptEvent | null>(null);

  useEffect(() => {
    const handler = (e: Event) => {
      e.preventDefault();
      setSupportsPWA(true);
      setPromptEvent(e as BeforeInstallPromptEvent);
    };

    window.addEventListener('beforeinstallprompt', handler);

    return () => {
      window.removeEventListener('beforeinstallprompt', handler);
    };
  }, []);

  const installApp = async () => {
    if (!promptEvent) {
      return;
    }

    await promptEvent.prompt();

    const { outcome } = await promptEvent.userChoice;

    if (outcome === 'accepted') {
      console.log('App installed');
    } else {
      console.log('App install declined');
    }

    setPromptEvent(null);
  };

  if (!supportsPWA) {
    return null;
  }

  return (
    <button
      onClick={installApp}
      className='fixed bottom-4 right-4 rounded-md bg-primary p-2 text-primary-foreground shadow-lg'
    >
      앱 설치하기
    </button>
  );
};

export default InstallPWA;
