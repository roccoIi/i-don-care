import { create } from 'zustand';
import { persist } from 'zustand/middleware';


interface ChildData {
  relationId: number,
  userId: number,
  userName: string,
}

interface ParentData {
  relationId: number,
  userId: number,
  userName: string,
}

interface UserData {
  parent: ParentData | null,
  children: ChildData[] | null,
  role: string,
}

interface User {
  user: UserData | null
  setUser: (user: UserData) => void;
}

interface SelectedChildData {
  selectedChildUserId: number,
  selectedChildRelationId: number,
  selectedChildUserName: string,
}

interface SelectedChild {
  selectedChild: SelectedChildData | null,
  setSelectedChild: (selectedChild: SelectedChildData) => void
}

export const userInfo = create<User>()(
  persist(
    (set) => ({
      user: null,
      setUser: (user: UserData) => set({ user: user }),
    }),
    {
      name: 'user-storage',
      getStorage: () => localStorage,
    }
  )
)

export const selectedChildInfo = create<SelectedChild>()(
  persist(
    (set) => ({
      selectedChild: null,
      setSelectedChild: (selectedChild: SelectedChildData) =>
        set({ selectedChild }),
    }),
    {
      name: 'selected-child-storage',
      getStorage: () => localStorage,
    }
  )
)

// const userStore = create<UserState>((set) => ({
//   role: 'child', 
//   setRole: (newRole: string) => set({ role: newRole }), // 역할 업데이트 함수
// }));
