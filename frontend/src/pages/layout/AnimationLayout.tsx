import { motion, AnimatePresence } from 'framer-motion';
import { useLocation } from 'react-router-dom';
import { useState, useEffect, ReactNode } from 'react';

interface AnimatedLayoutProps {
  children: ReactNode; // children의 타입을 ReactNode로 정의
}

const pageVariants = {
  initial: {
    opacity: 0,
    x: '100%', // 오른쪽에서 들어오는 애니메이션
  },
  in: {
    opacity: 1,
    x: 0,
  },
  out: {
    opacity: 0,
    x: '-100%', // 왼쪽으로 나가는 애니메이션
  },
};

const pageTransition = {
  type: 'tween',
  ease: 'easeInOut',
  duration: 0.5,
};

const AnimatedLayout: React.FC<AnimatedLayoutProps> = ({ children }) => {
  const location = useLocation();
  const [prevLocation, setPrevLocation] = useState<string>(location.pathname);
  const [direction, setDirection] = useState<number>(0);

  useEffect(() => {
    const currentPath = location.pathname;
    const prevPath = prevLocation;

    if (currentPath !== prevPath) {
      setDirection(currentPath > prevPath ? 1 : -1);
    }
    
    setPrevLocation(currentPath);
  }, [location, prevLocation]);

  return (
    <AnimatePresence initial={true} custom={direction}>
      <motion.div
        key={location.pathname}
        custom={direction}
        variants={pageVariants}
        initial="initial"
        animate="in"
        exit="out"
        transition={pageTransition}
        className="w-full h-full"
      >
        {children}
      </motion.div>
    </AnimatePresence>
  );
};

export default AnimatedLayout;