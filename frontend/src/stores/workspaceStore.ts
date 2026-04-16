import { create } from "zustand";
import type { Workspace, Board } from "@/types";

interface WorkspaceState {
  workspaces: Workspace[];
  currentWorkspace: Workspace | null;
  boards: Board[];
  currentBoard: Board | null;
  setWorkspaces: (w: Workspace[]) => void;
  setCurrentWorkspace: (w: Workspace | null) => void;
  setBoards: (b: Board[]) => void;
  setCurrentBoard: (b: Board | null) => void;
  addWorkspace: (w: Workspace) => void;
  addBoard: (b: Board) => void;
}

export const useWorkspaceStore = create<WorkspaceState>((set) => ({
  workspaces: [],
  currentWorkspace: null,
  boards: [],
  currentBoard: null,
  setWorkspaces: (workspaces) => set({ workspaces }),
  setCurrentWorkspace: (currentWorkspace) => set({ currentWorkspace }),
  setBoards: (boards) => set({ boards }),
  setCurrentBoard: (currentBoard) => set({ currentBoard }),
  addWorkspace: (w) => set((s) => ({ workspaces: [...s.workspaces, w] })),
  addBoard: (b) => set((s) => ({ boards: [...s.boards, b] })),
}));
