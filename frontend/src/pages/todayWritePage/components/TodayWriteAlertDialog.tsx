import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';

interface CommonAlertDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  title: string;
  message: string;
  confirmText?: string;
  onConfirm?: () => void;
  confirmColor?: string;
}

const CommonAlertDialog = ({
  open,
  onOpenChange,
  title,
  message,
  confirmText = '확인',
  onConfirm,
  confirmColor = 'bg-haru-green',
}: CommonAlertDialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        showCloseButton={false}
        className='max-w-96 rounded-xl bg-white'
      >
        <DialogHeader>
          <DialogTitle className='text-base font-semibold'>{title}</DialogTitle>
          <DialogDescription className='pt-1 text-sm text-gray-500'>
            {message}
          </DialogDescription>
        </DialogHeader>
        <DialogFooter className='mt-4 flex justify-end gap-2'>
          <DialogClose asChild>
            <button
              onClick={() => {
                onConfirm?.();
                onOpenChange(false);
              }}
              className={`rounded px-4 py-1.5 text-sm text-white ${confirmColor}`}
            >
              {confirmText}
            </button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CommonAlertDialog;
