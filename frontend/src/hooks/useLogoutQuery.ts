import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

import { userLogout } from '@/apis/accountApi';

export const useLogoutQuery = () => {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: userLogout,
    onSuccess: () => {
      toast.success('로그아웃되었습니다.');
      navigate('/login', { replace: true });
    },
    onError: () => {
      toast.error('로그아웃 처리 중 오류가 발생했습니다.');
    },
  });
};
