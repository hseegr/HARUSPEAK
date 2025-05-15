interface EmptyStateProps {
  title: string;
  description?: string;
  className?: string;
}

const EmptyState = ({
  title,
  description,
  className = '',
}: EmptyStateProps) => {
  return (
    <div
      className={`flex h-[80vh] w-full flex-col items-center justify-center gap-4 ${className}`}
    >
      <div className='text-center'>
        <h2 className='mb-2 text-xl font-semibold text-haru-green'>{title}</h2>
        {description && <p className='text-haru-light-green'>{description}</p>}
      </div>
    </div>
  );
};

export default EmptyState;
